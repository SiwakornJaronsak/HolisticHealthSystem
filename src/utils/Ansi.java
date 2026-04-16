import java.util.*;

public class Ansi {
    static final String RESET          = "\033[0m";
    static final String BOLD           = "\033[1m";
    static final String UNDERLINE      = "\033[4m";
    static final String BLACK          = "\033[30m";
    static final String RED            = "\033[31m";
    static final String GREEN          = "\033[32m";
    static final String YELLOW         = "\033[33m";
    static final String BLUE           = "\033[34m";
    static final String MAGENTA        = "\033[35m";
    static final String CYAN           = "\033[36m";
    static final String WHITE          = "\033[37m";
    static final String BRIGHT_RED     = "\033[91m";
    static final String BRIGHT_GREEN   = "\033[92m";
    static final String BRIGHT_YELLOW  = "\033[93m";
    static final String BRIGHT_BLUE    = "\033[94m";
    static final String BRIGHT_MAGENTA = "\033[95m";
    static final String BRIGHT_CYAN    = "\033[96m";
    static final String BRIGHT_WHITE   = "\033[97m";
    // Background colors
    static final String BG_BLACK       = "\033[40m";
    static final String BG_RED         = "\033[41m";
    static final String BG_GREEN       = "\033[42m";
    static final String BG_YELLOW      = "\033[43m";
    static final String BG_BLUE        = "\033[44m";
    static final String BG_MAGENTA     = "\033[45m";
    static final String BG_CYAN        = "\033[46m";
    static final String BG_WHITE       = "\033[47m";

    static String style(String ansiCode, String text) {
        return ansiCode + text + RESET;
    }

    // ── Flexible box helpers ──────────────────────────────────────────────────

    /** Return the visible length of a string (strips ANSI escape codes). */
    static int visibleLen(String s) {
        return s.replaceAll("\033\\[[;\\d]*m", "").length();
    }

    /**
     * Print a single-row titled box whose width adapts to the title.
     * Minimum inner width = minWidth (default 40).
     */
    static void printFlexBox(String title, String borderColor, int minInner) {
        int inner = Math.max(minInner, visibleLen(title) + 4);
        String top = "  ╔" + "═".repeat(inner) + "╗";
        String bot = "  ╚" + "═".repeat(inner) + "╝";
        int pad = (inner - visibleLen(title)) / 2;
        String padded = " ".repeat(Math.max(0, pad)) + title;
        String mid = "  ║" + padded + " ".repeat(Math.max(0, inner - visibleLen(padded))) + "║";
        System.out.println(BOLD + borderColor + top + RESET);
        System.out.println(BOLD + borderColor + mid + RESET);
        System.out.println(BOLD + borderColor + bot + RESET);
    }

    /**
     * Print a flexible-width box with a top title row + multiple content rows.
     * rows = list of { contentString, colorCode }.
     * Width is determined by the widest visible content.
     */
    static void printFlexTable(String title, String borderColor,
                               List<String[]> rows, int minInner) {
        // Compute inner width
        int inner = Math.max(minInner, visibleLen(title) + 4);
        for (String[] r : rows) inner = Math.max(inner, visibleLen(r[0]) + 4);

        String hBar  = "═".repeat(inner);
        String sep   = "  ╠" + hBar + "╣";
        String top   = "  ╔" + hBar + "╗";
        String bot   = "  ╚" + hBar + "╝";

        // Title row
        int pad = (inner - visibleLen(title)) / 2;
        String padded = " ".repeat(Math.max(0, pad)) + title;
        String titleRow = "  ║" + padded + " ".repeat(Math.max(0, inner - visibleLen(padded))) + "║";

        System.out.println(BOLD + borderColor + top      + RESET);
        System.out.println(BOLD + borderColor + titleRow + RESET);
        System.out.println(BOLD + borderColor + sep      + RESET);

        for (String[] r : rows) {
            String content = r[0];
            String color   = r.length > 1 ? r[1] : WHITE;
            int spaces = inner - 2 - visibleLen(content); // 2 = leading "  "
            String row = "  ║  " + color + content + RESET
                       + " ".repeat(Math.max(0, spaces - 2)) + BOLD + borderColor + "║" + RESET;
            System.out.println(row);
        }

        System.out.println(BOLD + borderColor + bot + RESET);
    }

    /** Print a flexible separator. */
    static void printSep(String color, int width) {
        System.out.println(color + "  " + "─".repeat(width) + RESET);
    }

    /** Print a tag badge. */
    static String badge(String label, String color) {
        return color + BOLD + " ● " + label + " " + RESET;
    }
}
