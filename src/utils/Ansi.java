class Ansi {
    static final String RESET       = "\033[0m";
    static final String BOLD        = "\033[1m";
    static final String UNDERLINE   = "\033[4m";
    static final String BLACK       = "\033[30m";
    static final String RED         = "\033[31m";
    static final String GREEN       = "\033[32m";
    static final String YELLOW      = "\033[33m";
    static final String BLUE        = "\033[34m";
    static final String MAGENTA     = "\033[35m";
    static final String CYAN        = "\033[36m";
    static final String WHITE       = "\033[37m";
    static final String BRIGHT_RED     = "\033[91m";
    static final String BRIGHT_GREEN   = "\033[92m";
    static final String BRIGHT_YELLOW  = "\033[93m";
    static final String BRIGHT_BLUE    = "\033[94m";
    static final String BRIGHT_MAGENTA = "\033[95m";
    static final String BRIGHT_CYAN    = "\033[96m";
    static final String BRIGHT_WHITE   = "\033[97m";

    static String style(String ansiCode, String text) {
        return ansiCode + text + RESET;
    }
}
