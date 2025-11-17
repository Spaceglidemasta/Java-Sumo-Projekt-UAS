package org.group_three.debug;

public class Debug {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String BOLD = "\033[0;1m";



    public static void print(String s){
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        String className = caller.getClassName();

        System.out.println(BOLD + "[" + ANSI_BLUE + "DEBUG" + ANSI_RESET + "](" + className + ") " + s);

    }
}
