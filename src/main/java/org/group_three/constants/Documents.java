package org.group_three.constants;

import org.group_three.constants.enums.style.CSSStyle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public final class Documents {

    private static final Logger log =
            Logger.getLogger(Documents.class.getName());

    private Documents() {};

    public final static String OUTPUT_DIR_NAME = "output";
    public final static String PDF_AUTHOR_SHIP = "group_three";
    public final static String DOC_LOCATION = "src/main/resources/org/group_three/documents/";
    public final static String STYLE_LOCATION = "src/main/resources/org/group_three/documents/style";

    public static final class Vars {
        public final static String HEADER = "{{H1}}";
        public final static String DESCRIPTION = "{{DSCPT}}";
        public final static String TABLEOFCONTENT = "{{TC}}";
        public final static String STARTOFTEXT = "{{STX}}";
        public final static String ENDOFFILE = "{{EOF}}";
    }

    public static final String HTMLNEWPAGE = "<div class=\"page-break\"></div>";

    /**
     * Wraps the body around a header containing a given Style sheet.
     * @param body the body as String
     * @param style The stylesheet to be embedded.
     * @return the whole html document as String
     * @author Luca
     * */
    public static String wrapHTMLbody(String body, CSSStyle style) throws IOException {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8" />
                    <style>
                       \s""" + getCSS(style) + """
                    </style>
                </head>
                <body>
                """ + body + """
                </body>
                </html>
                """;
    }

    /** Reads out the requested css style sheet and returns it as String parsing into an HTML style.
     * @param select a CSSStyle Enum representing the requested Style sheet
     * @return the Style sheet as String
     * @see Documents#wrapHTMLbody(String, CSSStyle)
     * @author Luca
     * */
    public static String getCSS(CSSStyle select) throws IOException {

        Path style = Path.of(STYLE_LOCATION);

        return switch (select) {
            case YOUNG -> Files.readString(
                    style.resolve("young.css")
            );
            case SERIOUS -> Files.readString(
                    style.resolve("serious.css")
            );
            case MINIMALISTIC -> Files.readString(
                    style.resolve("minimalistic.css")
            );
            case NO_STYLE -> "";
            default -> defaultCSS;
        };

    }

    /**
     * Default CSS String
     * @see Documents#wrapHTMLbody(String, CSSStyle)
     * */
    private final static String defaultCSS =
                """
                @page {
                    @bottom-center {
                    content:  counter(page) " / " counter(pages);
                    }
                }
                .page-break {
                    page-break-before: always;
                    page-break-after: always;
                }
                body {
                    font-family: Arial, sans-serif;
                    margin: 40px;
                }
                h1 { color: #2c3e50; }
                h2 { color: #34495e; }
                
                
                th {
                    background-color: #f2f2f2;
                    padding: 10px;
                    text-align: left;
                    border-bottom: 2px solid #ddd;
                }
                
                table tr td {
                    padding: 10px;
                    border-bottom: 1px solid #ddd;
                
                }
                """;


    /**
     * Converts a List / Array of Strings into one String, were every element of the List is
     * wrapped by the pre- and suffix.
     * @param list The iterable to be converted.
     * @return A string containing all elements of <code>list</code>, wrapped in HTML paragraphs.
     * @author Luca
     * */
    public static String wrapStrings(Iterable<String> list, String prefix, String suffix){

        StringBuilder sb = new StringBuilder();

        for(String parabody : list){
            sb.append(prefix);
            sb.append(parabody);
            sb.append(suffix);
        }

        return sb.toString();
    }
}
