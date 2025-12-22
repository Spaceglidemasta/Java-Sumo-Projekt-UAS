package org.group_three.constants;

public final class Documents {
    private Documents() {};

    public final static String OUTPUT_DIR_NAME = "output";
    public final static String PDF_AUTHOR_SHIP = "group_three";
    public final static String DOC_LOCATION = "src/main/resources/org/group_three/documents/";

    public static final class Vars {
        public final static String HEADER = "{{H1}}";
        public final static String DESCRIPTION = "{{DSCPT}}";
        public final static String TABLEOFCONTENT = "{{TC}}";
        public final static String STARTOFTEXT = "{{STX}}";
        public final static String ENDOFFILE = "{{EOF}}";
    }

    public static final String HTMLNEWPAGE = "<div class=\"page-break\"></div>";

    /**
     * Wraps the body around a header containing the Style sheet.
     * @param body the body as String
     * @return the whole html document as String
     * @author Luca
     * */
    public static String wrapHTMLbody(String body) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8" />
                    <style>
                       \s""" + defaultCSS + """
                    </style>
                </head>
                <body>
                """ + body + """
                </body>
                </html>
                """;
    }

    /**
     * Default CSS String
     * @see Documents#wrapHTMLbody(String)
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
                    border-bottom: 1px solid #ddd;\s
                
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
