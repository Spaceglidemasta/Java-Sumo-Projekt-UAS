package org.group_three.utils;

import org.group_three.debug.exceptions.InvalidFilesSelected;
import org.group_three.debug.exceptions.SumoCfgParsingError;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class PathUtils {
    private PathUtils(){}


    public static File getNetfromSConfig(File scfg) throws Exception {

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

        return new File(parent, netpathstr);
    }

}
