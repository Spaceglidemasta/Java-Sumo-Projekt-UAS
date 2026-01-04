package org.group_three.service;



import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.group_three.api.SimController;
import org.group_three.constants.Documents;
import org.group_three.constants.enums.style.CSSdoc;
import org.group_three.model.WEdge;
import org.group_three.utils.Formatting;
import org.group_three.utils.PathUtils;
import com.vladsch.flexmark.ext.tables.TablesExtension;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**<h1>StatCollector</h1>
 * A Collection of Stats. Supports additional features like group-exporting to .tar.gz.
 * @see Statistic
 * @author Luca
 * */
public class StatCollector implements Iterable<Statistic<?>> {

    private static final Logger log =
            Logger.getLogger(StatCollector.class.getName());

    //Style
    private String name;
    private List<String> description;
    private CSSdoc cssStyle = CSSdoc.DEFAULT;
    
    //functionality
    private List<Statistic<?>> statistics;
    private SimController simcon = null;

    // Statistic Variables
    private long vehicleMaxDenseValue = 0;
    
    //TODO add <U> sum(Function<T, U> f)


    public StatCollector(SimController simcon, String name, Statistic<?> ... args) {
        this.simcon = simcon;
        this.name = name;
        //List.of is immutable -> pass it into ArrayList<> init to make it mutable.
        this.statistics = new ArrayList<>(List.of(args));
        this.description = new ArrayList<>();
    }


    /**
     * Simple Iterator for iterating through all Statistics.
     * @author Luca
     * */
    @Override
    public Iterator<Statistic<?>> iterator() {
        return statistics.iterator();
    }

    public List<String> getDescription() {return description;}

    public void setDescription(List<String> paragraphs) {this.description = paragraphs;}

    public SimController getSimcon() {
        return simcon;
    }

    public void setSimcon(SimController simcon) {
        this.simcon = simcon;
    }

    public void setName(String name) {this.name = name;}

    public String getName() {
        return name;
    }

    public int getStatisticsCount(){return statistics.size();}

    public long getVehicleMaxDenseValue() {
        return vehicleMaxDenseValue;
    }

    public void setVehicleMaxDenseValue(long vehicleMaxDenseValue) {
        this.vehicleMaxDenseValue = vehicleMaxDenseValue;
    }

    public CSSdoc getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(CSSdoc cssStyle) {
        this.cssStyle = cssStyle;
    }

    /**
     * Collect the data needed for most Statistics. Need to be called every step.
     * @see SimController#collectTelemetry()
     * @author Luca
     * */
    public void collect(){

        vehicleMaxDenseValue += simcon.getAllVehicles().size();

        for(WEdge edge : simcon.getAllroads().values()){
            edge.addVehDensityCount();
        }

    }

    /**
     * Prints the whole Statistic Collection.
     * @see Statistic#print()
     * @author Luca
     * */
    public void print() throws Exception {
        System.out.println(name + ": ");
        for(Statistic<?> stat : statistics){
            stat.print();
        }
    }

    /**Adds a paragraph to the description.
     * @param paragraph The paragraph to be added.
     * @return the size of the description after insertion.
     * @author Luca
     * @see StatCollector#setDescription(List)
     * */
    public int addDescriptionParagraph(String paragraph) {
        description.add(paragraph);
        return description.size();
    }

    /**
     * Adds a <code>Statistic</code> to the Collector
     * @param stat The Statistic to Add
     * @return the length of the collector-array after insertion
     * @author Luca
     * */
    public int addStatistic(Statistic<?> stat){
        statistics.add(stat);
        return statistics.size();
    }

    /**<h2>exportAsZip</h2>
     * Exports the Statistics contained by this StatCollector to one zipped folder in output.
     * @return <code>true</code> if successful, <code>false</code> if not.
     * @see Statistic#outAsZippedCSV(ZipOutputStream)
     * @author Luca
     * */
    public boolean exportAsZip() {

        String filename = Formatting.uniquegen(name, ".zip");

        try (FileOutputStream fos = new FileOutputStream("output/" + filename);
             ZipOutputStream zip = new ZipOutputStream(fos)) {

            int successful = 0;
            int all = 0;
            for (Statistic<?> stat : statistics) {
                if(stat.outAsZippedCSV(zip)) successful++;
                all++;
            }

            log.info("Exporting (" + successful + " / " + all +  ") Statistics to output/" + filename + " was successful.");
            return true;


        } catch (Exception e) {
            log.warning("Exporting to a zipped folder failed: " + Arrays.toString(e.getStackTrace()));
            return false;
        }


    }

    // ******************************************************
    // **                   PDF SECTION                    **
    // ******************************************************


    /**
     * Exports the bundle of statistics into a single PDF document. <br>
     * Select the CSS Style of the document via {@link StatCollector#setCssStyle(CSSdoc)}
     * @return <code>true</code> if successful, <code>false</code> if not.
     * @author Luca
     * */
    //TODO Add up duplicates
    //TODO normalize for missing vehicles
    public boolean exportAsPDF() {

        try {

            String templatemd = Files.readString(
                    Path.of(Documents.DOC_LOCATION + "template.md")
            );
            //very important. Your table syntax can be perfect, but you need this renderer.
            MutableDataSet options = new MutableDataSet();
            options.set(Parser.EXTENSIONS, List.of(TablesExtension.create()));

            Parser parser = Parser.builder(options).build();
            HtmlRenderer hrenderer = HtmlRenderer.builder(options).build();

            //modify the main body

            String withFrontpage = buildFrontPage(templatemd);

            String finished = buildMainBody(withFrontpage);

            //finish the build
            Node document = parser.parse(finished);

            String body = hrenderer.render(document);

            String html = Documents.wrapHTMLbody(
                    body.replaceAll("&quot;", ""), //remove quotes from data
                    cssStyle
            );

            String filename =  Formatting.uniquegen(name, ".pdf");

            Path out = PathUtils.prepareOutputPath(
                    filename
            );

            if (buildPDF(html, out)) log.info("Statistics of " + filename + " were exported as a PDF successfully.");


        } catch (IOException e) {
            log.log(Level.WARNING, "Exporting as PDF failed.", e);
            return false;
        }

        return true;
    }

    /* Reasons why I extracted this from exportAsPDF():
    * 1. It would really clutter the rest of the function, with what is logically only one step.
    * 2. It can have use later, as creating a PDF document out of an HTML String and a Path is not uncommon.
    * 3. It makes clear which part of exportAsPDF() throws the IOException, making it simpler / cleaner to handle.
    * */
    /**
     * Builds a PDF out of a given Html String and the output Path
     * @param html the Html Document as String
     * @param out the path to the output, including the filename
     * @author Luca
     * @see StatCollector#exportAsPDF()
     * */
    private boolean buildPDF(String html, Path out) {

        try (
                OutputStream os = Files.newOutputStream(out)
        ) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(html, null);

            builder.toStream(os);

            builder.run(); //this may throw

            return true;

        } catch (IOException e) {
            log.log(Level.WARNING, "Building PDF failed: ", e);
            return false;
        }

    }

    /**
     * Private method to build the Front page of the Statistics PDF
     * @param doc the read template as String
     * @return the transformed template as String
     * @author Luca
     * */
    private String buildFrontPage(String doc){

        String out = doc.replace(Documents.Vars.HEADER, name);

        out = out.replace(
                Documents.Vars.DESCRIPTION,
                String.join("<br>",
                        Documents.wrapStrings(description,"<p>", "</p>")
                )
        );

        //this is so clean
        out = out.replace(
                Documents.Vars.TABLEOFCONTENT,
                Documents.wrapStrings(
                        statistics.stream()
                                .map(Statistic::getName) //IntelliJ bullied my lambda :(
                                .toList(),
                        "- ",
                        "\n"
                )
        );

        return out;
    }

    /**
     * Builds the main body of the HTML by calling {@link StatCollector#buildStatistic(Statistic)}
     * and adding a new page for every statistic.
     * @param doc the Markdown document as String
     * @return the main body as Markdown
     * */
    private String buildMainBody(String doc) {

        StringBuilder sb = new StringBuilder(doc);

        for(Statistic<?> stat : statistics) {
            sb.append(buildStatistic(stat));
            sb.append(Documents.HTMLNEWPAGE);
        }

        return sb.toString();
    }

    /**
     * Builds one statistic in Markdown Syntax.
     * <p>
     *     Follows the Syntax: <br>
     *     stat.name <br>
     *     stat.content as table
     * </p>
     * @param stat The statistic to build
     * @return the finished head & table as String
     * @author Luca
     * */
    private String buildStatistic(Statistic<?> stat) {

        StringBuilder sb = new StringBuilder()

        .append("\n\n## " + stat.getName() + "\n\n");

        List<? extends Record> content = stat.getContent();
        List<String> csvList = new ArrayList<>();

        //Headers
        csvList.add(String.join(",", stat.getAttributeNames()));

        //content
        for(int i = 0; i < content.size(); i++){
            csvList.add(
                    Formatting.toCSVformat(
                        stat.rowToStringList(i)
                    )
            );
        }

        sb.append(
                csvToMarkdownTable(csvList)
        );

        log.info("Building Statistic " + stat.getName() + " was successful.");

        return sb.toString();
    }

    /**
     * Converts a csv Table to a Markdown table
     * @param csv The csv Table where each row is one element, and the header is the first
     * @return the table in Markdown format
     * @author Luca
     * @see Formatting#toCSVformat(List)
     * */
    public static String csvToMarkdownTable(List<String> csv) {

        if (csv.isEmpty()) return "";

        StringBuilder md = new StringBuilder();

        // Header
        String[] headers = csv.getFirst().split(",");
        md.append("| ");
        md.append(String.join(" | ", headers));
        md.append(" |");
        md.append("\n");

        // Separator
        md.append("| ");
        md.append("--- | ".repeat(headers.length - 1));
        md.append("--- |");
        md.append("\n");

        // Stats
        for (int i = 1; i < csv.size(); i++) {
            String[] cells = csv.get(i).split(",");
            md.append("| ");
            md.append(Arrays.stream(cells)
                    .map(String::trim)
                    .collect(Collectors.joining(" | ")));
            md.append(" |");
            md.append("\n");
        }

        return md.toString();
    }







    /*This was an old attempt at outputting pdfs using PDFbox. I kept this is here for artistic purposes.
    /**
     * <p> Exports all Statistics contained by this StatCollector as a single .pdf file. </p>
     * <p> The output directory is always ./output/ </p>
     * <p> WARNING: CREATES FILES</p>
     * @see StatCollector#writeIntroPDF(PDPageContentStream)
     * @see StatCollector#writeStatisticPDF(PDPageContentStream, Statistic)
     * @see PathUtils#prepareOutputPath(String)
     * @return <code>true</code> if successful, <code>false</code> if not.
     * @author Luca
     *
    public boolean exportAsPDF() {

        //like "with" from python
        try(
                PDDocument pdd = new PDDocument();
        ) {

            PDPage introPage = new PDPage();
            pdd.addPage(introPage);

            //super safety!!!
            try(
                    PDPageContentStream introStream = new PDPageContentStream(pdd, introPage);
            ) {

                writeIntroPDF(introStream);

            } catch (IOException e) {
                log.log(Level.WARNING, "Adding intro Page to " + name + " failed:", e);
                return false;
            }

            //Document Information
            PDDocumentInformation pddInfo = pdd.getDocumentInformation();
            pddInfo.setAuthor(Documents.PDF_AUTHOR_SHIP);
            pddInfo.setTitle(name);
            pddInfo.setSubject(subject);
            //sets the names of the statistics as the keywords.
            //setKeywords accepts a String containing the keywords separated by comma.
            pddInfo.setKeywords(
                    statistics.stream()
                            .map(Statistic::getName)
                            .collect(Collectors.joining(", "))
            );

            //I got told this vvv is unclean. All other alternatives seemed worse.
            int successfulWrites = 0;
            int writeCount = 0;

            for(Statistic<?> stat : statistics) {

                PDPage statPage = new PDPage();
                pdd.addPage(statPage);

                try(
                        PDPageContentStream conStream = new PDPageContentStream(pdd, statPage)
                ) {

                   writeStatisticPDF(conStream, stat);

                   successfulWrites++;

                } catch (IOException e) {

                    log.log(Level.WARNING,
                            "Writing page " + (writeCount + 1) + " with Statistic " + stat.getName() + " failed: ", e);

                } finally {

                    writeCount++;
                }
            }

            Path out = PathUtils.prepareOutputPath(
                    Formatting.uniquegen(name, ".pdf")
            ); //may throw

            pdd.save(out.toFile());

            log.info("StatisticCollection with " +  successfulWrites + " of " + writeCount +
                    " successful written Statistics saved as PDF to \"" + out + "\".");


        } catch (IOException e) {
            log.log(Level.WARNING, "Exporting to PDF failed: " , e);
            return false;
        }

        return true;
    }


    private void writeIntroPDF(PDPageContentStream conStream) throws IOException{

    }

    private void writeStatisticPDF(PDPageContentStream conStream, Statistic<?> stat) throws IOException {

    }
    */



}
