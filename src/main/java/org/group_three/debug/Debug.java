package org.group_three.debug;

public class Debug {

    private static final boolean do_debug = true;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String BOLD = "\033[0;1m";



    public static void print(Object value){

        if(do_debug){
            StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
            String className = caller.getClassName().substring(16);

            System.out.println(BOLD + "[" + ANSI_BLUE + "DEBUG" + ANSI_RESET + BOLD + "](" + ANSI_CYAN + className + ANSI_RESET + ") " + ANSI_RESET + String.valueOf(value));
        }

    }
}
