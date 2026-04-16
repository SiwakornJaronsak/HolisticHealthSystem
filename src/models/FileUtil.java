import java.io.*;
import java.util.*;

class FileUtil {
    private static final String FILE_PATH = "health_records.txt";
    private static final String RECORD_START = "##RECORD_START##";
    private static final String RECORD_END = "##RECORD_END##";

    public static void saveToFile(UserProfile profile, String workoutPlanRaw) throws IOException {
        String cleanPlan = workoutPlanRaw.replaceAll("\033\\[[;\\d]*m", "");
        String cleanAdvice = profile.getAdvice().replaceAll("\033\\[[;\\d]*m", "");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(RECORD_START + ":" + profile.getName());
            writer.newLine();
            writer.write("==========================================");
            writer.newLine();
            writer.write("Name    : " + profile.getName()); writer.newLine();
            writer.write("BMI     : " + String.format("%.1f", profile.getBmi())
                    + " (" + profile.getBMICategory() + ")"); writer.newLine();
            writer.write("Mental  : " + profile.getMentalStatus()); writer.newLine();
            writer.write("Goal    : " + profile.getGoal()); writer.newLine();
            writer.write(cleanPlan);
            writer.write(cleanAdvice);
            writer.write("=========================================="); writer.newLine();
            writer.write(RECORD_END); writer.newLine();
        }
    }

    public static void readFromFile() throws IOException {
        List<List<String>> records = loadAllRecords();
        if (records.isEmpty()) {
            System.out.println(Ansi.YELLOW + "  No saved records found." + Ansi.RESET);
            return;
        }
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "\n  Total records found: " + records.size() + Ansi.RESET);
        for (List<String> rec : records) {
            printDecoratedRecord(rec);
        }
    }

    public static void readByName(String targetName) throws IOException {
        List<List<String>> all = loadAllRecords();
        List<List<String>> matches = new ArrayList<>();

        for (List<String> rec : all) {
            if (getRecordName(rec).equalsIgnoreCase(targetName)) {
                matches.add(rec);
            }
        }

        if (matches.isEmpty()) {
            System.out.println(Ansi.YELLOW + "  No records found for: "
                    + Ansi.BRIGHT_YELLOW + targetName + Ansi.RESET);
            return;
        }

        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "\n  Found " + matches.size() + " record(s) for: "
                + Ansi.BRIGHT_WHITE + targetName + Ansi.RESET);

        for (List<String> rec : matches) {
            printDecoratedRecord(rec);
        }
    }

    public static List<String> listAllNames() throws IOException {
        List<List<String>> all = loadAllRecords();
        List<String> names = new ArrayList<>();

        for (List<String> rec : all) {
            String n = getRecordName(rec);
            if (!names.contains(n)) names.add(n);
        }
        return names;
    }

    private static List<List<String>> loadAllRecords() throws IOException {
        List<List<String>> records = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return records;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            List<String> current = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(RECORD_START)) {
                    current = new ArrayList<>();
                    current.add(line);
                } else if (line.startsWith(RECORD_END)) {
                    if (current != null) {
                        records.add(current);
                        current = null;
                    }
                } else {
                    if (current != null) current.add(line);
                }
            }

            if (records.isEmpty() && file.length() > 0) {
                List<String> legacy = new ArrayList<>();
                try (BufferedReader r2 = new BufferedReader(new FileReader(FILE_PATH))) {
                    String l2;
                    while ((l2 = r2.readLine()) != null) legacy.add(l2);
                }
                records.add(legacy);
            }
        }

        return records;
    }

    private static String getRecordName(List<String> rec) {
        if (!rec.isEmpty()) {
            String first = rec.get(0);
            if (first.startsWith(RECORD_START + ":")) {
                return first.substring((RECORD_START + ":").length()).trim();
            }
            for (String line : rec) {
                if (line.startsWith("Name    :")) {
                    return line.substring("Name    :".length()).trim();
                }
            }
        }
        return "Unknown";
    }

    private static void printDecoratedRecord(List<String> rec) {
        List<String[]> displayLines = new ArrayList<>();

        for (String line : rec) {
            if (line.startsWith(RECORD_START)) continue;

            if (line.startsWith("====")) {
                displayLines.add(new String[]{"__DIVIDER__", ""});

            } else if (line.startsWith("Name    :")) {
                String val = line.substring("Name    :".length()).trim();
                displayLines.add(new String[]{"👤  Name   : " + val, Ansi.BOLD + Ansi.BRIGHT_WHITE});

            } else if (line.startsWith("BMI     :")) {
                String val = line.substring("BMI     :".length()).trim();
                String bmiColor = val.contains("Obese") ? Ansi.BRIGHT_RED
                        : val.contains("Overweight") ? Ansi.BRIGHT_YELLOW
                        : val.contains("Underweight") ? Ansi.BRIGHT_CYAN
                        : Ansi.BRIGHT_GREEN;
                displayLines.add(new String[]{"⚖   BMI    : " + val, Ansi.BOLD + bmiColor});

            } else if (line.startsWith("Mental  :")) {
                String val = line.substring("Mental  :".length()).trim();
                String mColor = val.contains("DEPRESSED") ? Ansi.BRIGHT_RED
                        : val.contains("STRESSED") ? Ansi.BRIGHT_YELLOW
                        : Ansi.BRIGHT_GREEN;
                displayLines.add(new String[]{"🧠  Mental : " + val, Ansi.BOLD + mColor});

            } else if (line.startsWith("Goal    :")) {
                String val = line.substring("Goal    :".length()).trim();
                displayLines.add(new String[]{"🎯  Goal   : " + val, Ansi.BOLD + Ansi.BRIGHT_MAGENTA});

            } else if (line.contains("WEEKLY SCHEDULE")) {
                displayLines.add(new String[]{"__DIVIDER__", ""});
                displayLines.add(new String[]{"📅  WEEKLY SCHEDULE", Ansi.BOLD + Ansi.BRIGHT_WHITE});
                displayLines.add(new String[]{"__DIVIDER__", ""});

            } else if (line.contains("PERSONALIZED WORKOUT PLAN")) {
                // skip

            } else if (line.contains("PERSONALIZED HEALTH ADVICE")) {
                displayLines.add(new String[]{"__DIVIDER__", ""});
                displayLines.add(new String[]{"💡  PERSONALIZED HEALTH ADVICE", Ansi.BOLD + Ansi.BRIGHT_WHITE});
                displayLines.add(new String[]{"__DIVIDER__", ""});

            } else if (line.contains("->")) {
                int arrow = line.indexOf("->");
                String day = line.substring(0, arrow).trim();
                String act = line.substring(arrow + 2).trim();
                String dc = (day.equals("Saturday") || day.equals("Sunday"))
                        ? Ansi.BRIGHT_MAGENTA : Ansi.BRIGHT_YELLOW;
                displayLines.add(new String[]{
                    dc + "  " + padRight(day, 11) + Ansi.RESET + Ansi.BLUE + "│ " + Ansi.WHITE + act,
                    ""
                });

            } else if (line.contains("[Mental]") || line.contains("🧠")) {
                displayLines.add(new String[]{line.trim(), Ansi.BRIGHT_MAGENTA});

            } else if (line.contains("[BMI]") || line.contains("⚖")) {
                displayLines.add(new String[]{line.trim(), Ansi.BRIGHT_CYAN});

            } else if (line.contains("[Goal]") || line.contains("🎯")) {
                displayLines.add(new String[]{line.trim(), Ansi.BRIGHT_YELLOW});

            } else if (line.trim().startsWith("---") || line.trim().startsWith("--")) {
                // skip
            } else if (!line.trim().isEmpty()) {
                displayLines.add(new String[]{line.trim(), Ansi.WHITE});
            }
        }

        int inner = 44;
        for (String[] dl : displayLines) {
            if (dl[0].equals("__DIVIDER__")) continue;
            int vlen = Ansi.visibleLen(dl[0]) + 4;
            if (vlen > inner) inner = vlen;
        }

        String hBar = "═".repeat(inner);
        String top = "  ╔" + hBar + "╗";
        String bot = "  ╚" + hBar + "╝";
        String div = "  ╠" + hBar + "╣";

        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + top + Ansi.RESET);

        for (String[] dl : displayLines) {
            if (dl[0].equals("__DIVIDER__")) {
                System.out.println(Ansi.BRIGHT_CYAN + div + Ansi.RESET);
                continue;
            }

            String text = dl[0];
            String color = dl[1];
            int textVlen = Ansi.visibleLen(text);
            int spaces = inner - 2 - textVlen;

            String row = Ansi.BRIGHT_CYAN + "  ║  " + Ansi.RESET
                    + color + text + Ansi.RESET
                    + " ".repeat(Math.max(0, spaces))
                    + Ansi.BRIGHT_CYAN + "║" + Ansi.RESET;

            System.out.println(row);
        }

        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + bot + Ansi.RESET);
    }

    private static String padRight(String s, int n) {
        if (s.length() >= n) return s.substring(0, n);
        return s + " ".repeat(n - s.length());
    }
}
