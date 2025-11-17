package org.group_three.debug;

public class Debug {

    private static final boolean do_debug = true;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String BOLD = "\033[0;1m";



    public static void print(String s){

        if(do_debug){
            StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
            String className = caller.getClassName().substring(16);

            System.out.println(BOLD + "[" + ANSI_BLUE + "DEBUG" + ANSI_RESET + BOLD + "](" + className + ") " + ANSI_RESET + s );
        }

    }
}
