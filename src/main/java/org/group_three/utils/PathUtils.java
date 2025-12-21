package org.group_three.utils;

import org.group_three.api.SimController;
import org.group_three.constants.Documents;
import org.group_three.debug.exceptions.SumoCfgParsingError;
import org.group_three.debug.exceptions.XMLEmptyAttributeError;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility Class for Pathing
 * @author Luca
 * */
public final class PathUtils {

    private static final Logger log =
            Logger.getLogger(PathUtils.class.getName());

    /**
     * Reads out the relative path / filename of the network file via XML parsing the .sumocfg file.
     * @param scfg The location of the .sumocfg file
     * @return The location of the network file as File, or null if failed.
     * @author Luca
     * */
    public static File getNetFromSCFG(File scfg) throws Exception {

        NodeList nets;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); //this may throw
        Document doc = dBuilder.parse(scfg); //this too

        doc.getDocumentElement().normalize();

        nets = doc.getElementsByTagName("net-file");


        if(nets.getLength() == 0){
            throw new SumoCfgParsingError("No network value found in SumoConfig");
        }

        String parent = scfg.getParent();

        Element netelement = (Element) nets.item(0);

        String netpathstr =  netelement.getAttribute("value");

        if(netpathstr.isEmpty()) throw new XMLEmptyAttributeError("Value of network element is empty");

        return new File(parent, netpathstr);
    }

    /**
     * Checks if the output directory is present, and creates one of not present.
     * @param filename the name of the file
     * @return the Path of the given filename
     * @author Luca
     * */
    public static Path prepareOutputPath(String filename) throws IOException {
        Path outputDir = Path.of(Documents.outputDirName);
        Files.createDirectories(outputDir);
        return outputDir.resolve(filename);
    }



    /**
     * Reads out the relative path / filename of the route file via XML parsing the .sumocfg file.
     * @param scfg The location of the .sumocfg file
     * @return The location of the route file as File
     * @author Luca
     * */
    public static File getRouFromSCFG(File scfg) throws Exception {

        NodeList routes;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); //this may throw
        Document doc = dBuilder.parse(scfg); //this too

        doc.getDocumentElement().normalize();

        routes = doc.getElementsByTagName("route-file");


        if(routes.getLength() == 0){
            throw new SumoCfgParsingError("No network value found in SumoConfig");
        }

        String parent = scfg.getParent();

        Element routeelement = (Element) routes.item(0);

        String routepathstr =  routeelement.getAttribute("value");

        if(routepathstr.isEmpty()) throw new XMLEmptyAttributeError("Value of route element is empty");

        return new File(parent, routepathstr);
    }

    /**
     * A method to convert an absolute path to a relative path based on the SumoConfig path.
     * <br>
     * AI was used for help on the path conversion.
     *
     * @param absolutePath The absolute path which should be converted to a relative path.
     * @return The relative path which was created.
     * @author Joel
     */
    public static String getRelativePath(String absolutePath) {
        // get SumoConfig path
        Path sumoConfigPath = Paths.get(new File(SimController.getProjectLocation(), "SumoConfig").getPath());

        // create and return a relative path based on the SumoConfig path
        return sumoConfigPath.relativize(Paths.get(absolutePath)).toString();

        // Used AI code part explanations
        // Paths.get(String)        <-- converts a string to a Path
        // path0.relativize(path1)  <--returns the relative path of path1 relative to path0
    }

}
