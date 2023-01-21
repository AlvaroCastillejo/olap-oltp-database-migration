package Utils;

public class Output {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static void print(String s, String color){
        switch (color){
            case "black":
                System.out.println(ANSI_BLACK + s + ANSI_RESET);
                break;
            case "red":
                System.out.println(ANSI_RED + s + ANSI_RESET);
                break;
            case "green":
                System.out.println(ANSI_GREEN + s + ANSI_RESET);
                break;
            case "yellow":
                System.out.println(ANSI_YELLOW + s + ANSI_RESET);
                break;
            case "blue":
                System.out.println(ANSI_BLUE + s + ANSI_RESET);
                break;
            case "purple":
                System.out.println(ANSI_PURPLE + s + ANSI_RESET);
                break;
            case "cyan":
                System.out.println(ANSI_CYAN + s + ANSI_RESET);
                break;
            case "white":
                System.out.println(ANSI_WHITE + s + ANSI_RESET);
                break;
        }
    }
}
