package org.group_three.service;



import org.group_three.utils.Formatting;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
public class StatCollector {

    private static final Logger log =
            Logger.getLogger(StatCollector.class.getName());


    private String name;
    private List<String> description;
    private List<Statistic<?>> statistics;

    public StatCollector(String name, Statistic<?> ... args) {
        this.name = name;
        //List.of is immutable -> pass it into ArrayList<> init to make it mutable.
        this.statistics = new ArrayList<>(List.of(args));
        this.description = new ArrayList<>();
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

    public List<String> getDescription() {return description;}

    public void setDescription(List<String> paragraphs) {this.description = paragraphs;}


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

    public void setName(String name) {this.name = name;}

    public String getName() {
        return name;
    }

    public int getStatisticsCount(){return statistics.size();}

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
            pddInfo.setAuthor(Documents.pdfAuthorShip);
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
