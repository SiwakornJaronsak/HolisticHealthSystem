import java.io.*;
import java.util.*;

class FileUtil {
    private static final String FILE_PATH = "health_records.txt";
    private static final String RECORD_START = "##RECORD_START##";
    private static final String RECORD_END   = "##RECORD_END##";
    
    public static void saveToFile(UserProfile profile, String workoutPlanRaw) throws IOException {
        String cleanPlan   = workoutPlanRaw.replaceAll("\033\\[[;\\d]*m", "");
        String cleanAdvice = profile.getAdvice().replaceAll("\033\\[[;\\d]*m", "");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(RECORD_START + ":" + profile.getName());
            writer.newLine();
            writer.write("==========================================");
            writer.newLine();
            writer.write("Name    : " + profile.getName());
            writer.newLine();
            writer.write("BMI     : " + String.format("%.1f", profile.getBmi())
                    + " (" + profile.getBMICategory() + ")");
            writer.newLine();
            writer.write("Mental  : " + profile.getMentalStatus());
            writer.newLine();
            writer.write("Goal    : " + profile.getGoal());
            writer.newLine();
            writer.write(cleanPlan);
            writer.write(cleanAdvice);
            writer.write("==========================================");
            writer.newLine();
            writer.write(RECORD_END);
            writer.newLine();
        }
    }

    // ── อ่านทุก record พร้อมตกแต่ง ──────────────────────────────────────────
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

    // ── อ่านเฉพาะ record ของชื่อที่ระบุ (case-insensitive) ──────────────────
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

    // ── ดึงรายชื่อทั้งหมดที่มีในไฟล์ (ไม่ซ้ำ) ─────────────────────────────
    public static List<String> listAllNames() throws IOException {
        List<List<String>> all = loadAllRecords();
        List<String> names = new ArrayList<>();
        for (List<String> rec : all) {
            String n = getRecordName(rec);
            if (!names.contains(n)) names.add(n);
        }
        return names;
    }

    // ── helper: โหลด record ทั้งหมดจากไฟล์ ──────────────────────────────────
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
                    current.add(line); // บรรทัดแรกเก็บชื่อ
                } else if (line.startsWith(RECORD_END)) {
                    if (current != null) {
                        records.add(current);
                        current = null;
                    }
                } else {
                    if (current != null) current.add(line);
                }
            }
            // รองรับไฟล์เก่าที่ไม่มี marker (fallback: แสดงดิบ)
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

    // ── helper: ดึงชื่อออกจาก record ─────────────────────────────────────────
    private static String getRecordName(List<String> rec) {
        // บรรทัดแรกคือ ##RECORD_START##:ชื่อ  หรือ "Name    : ชื่อ"
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

    // ── helper: พิมพ์ record พร้อมตกแต่ง ─────────────────────────────────────
    private static void printDecoratedRecord(List<String> rec) {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ╔══════════════════════════════════════════╗" + Ansi.RESET);
        boolean inHeader = true; // ช่วงก่อนถึง WEEKLY SCHEDULE
        for (String line : rec) {
            if (line.startsWith(RECORD_START)) continue;
            // ข้าม marker

            // ─── หัว/ท้าย separator ─────────────────────────────────────────
            if (line.startsWith("====")) {
                System.out.println(Ansi.BRIGHT_CYAN
                        + "  ╠══════════════════════════════════════════╣" + Ansi.RESET);
                inHeader = true;
                continue;
            }
            // ─── ตกแต่งแต่ละแถวของ header info ──────────────────────────────
            if (line.startsWith("Name    :")) {
                String val = line.substring("Name    :".length()).trim();
                System.out.println(Ansi.BOLD + Ansi.CYAN + "  ║  Name   : "
                        + Ansi.BRIGHT_WHITE + val + Ansi.RESET);
            } else if (line.startsWith("BMI     :")) {
                String val = line.substring("BMI     :".length()).trim();
                String bmiColor = val.contains("Obese")       ?
                Ansi.BRIGHT_RED
                                : val.contains("Overweight")  ?
                Ansi.BRIGHT_YELLOW
                                : val.contains("Underweight") ?
                Ansi.BRIGHT_CYAN
                                :                               Ansi.BRIGHT_GREEN;
                System.out.println(Ansi.BOLD + Ansi.CYAN + "  ║  BMI    : "
                        + bmiColor + val + Ansi.RESET);
            } else if (line.startsWith("Mental  :")) {
                String val = line.substring("Mental  :".length()).trim();
                String mColor = val.contains("DEPRESSED") ? Ansi.BRIGHT_RED
                              : val.contains("STRESSED")  ?
                Ansi.BRIGHT_YELLOW
                              :                             Ansi.BRIGHT_GREEN;
                System.out.println(Ansi.BOLD + Ansi.CYAN + "  ║  Mental : "
                        + mColor + val + Ansi.RESET);
            } else if (line.startsWith("Goal    :")) {
                String val = line.substring("Goal    :".length()).trim();
                System.out.println(Ansi.BOLD + Ansi.CYAN + "  ║  Goal   : "
                        + Ansi.BRIGHT_MAGENTA + val + Ansi.RESET);
            // ─── หัว WEEKLY SCHEDULE ─────────────────────────────────────────
            } else if (line.contains("WEEKLY SCHEDULE")) {
                inHeader = false;
                System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE
                        + "  ║  " + line.trim() + Ansi.RESET);
            // ─── วันในสัปดาห์ ─────────────────────────────────────────────────
            } else if (line.contains("->")) {
                int arrow = line.indexOf("->");
                String day      = line.substring(0, arrow).trim();
                String activity = line.substring(arrow + 2).trim();
                String dayColor = (day.equals("Saturday") || day.equals("Sunday"))
                        ?
                Ansi.MAGENTA : Ansi.YELLOW;
                System.out.println("  " + Ansi.BOLD + dayColor + day + Ansi.RESET
                        + Ansi.BLUE + " -> " + Ansi.RESET
                        + Ansi.WHITE + activity + Ansi.RESET);
            // ─── HEALTH ADVICE labels ─────────────────────────────────────────
            } else if (line.contains("[Mental]")) {
                System.out.println(Ansi.BOLD + Ansi.BRIGHT_MAGENTA
                        + "  ║  " + line.trim() + Ansi.RESET);
            } else if (line.contains("[BMI]")) {
                System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                        + "  ║  " + line.trim() + Ansi.RESET);
            } else if (line.contains("[Goal]")) {
                System.out.println(Ansi.BOLD + Ansi.BRIGHT_YELLOW
                        + "  ║  " + line.trim() + Ansi.RESET);
            // ─── separator เส้น -- ────────────────────────────────────────────
            } else if (line.trim().startsWith("---") || line.trim().startsWith("--")) {
                System.out.println(Ansi.BLUE + "  ║  " + line.trim() + Ansi.RESET);
            // ─── บรรทัดทั่วไป ─────────────────────────────────────────────────
            } else if (!line.trim().isEmpty()) {
                System.out.println(Ansi.WHITE + "  ║  " + line.trim() + Ansi.RESET);
            }
        }
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ╚══════════════════════════════════════════╝" + Ansi.RESET);
    }
}