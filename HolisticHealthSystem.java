import java.io.*;
import java.util.*;

public class HolisticHealthSystem {

    static Scanner scanner = new Scanner(System.in);
    static HealthRecordStore<UserProfile> store = new HealthRecordStore<>();

    public static void main(String[] args) {
        printBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            try {
                int choice = readMenuInt(1, 3);
                switch (choice) {
                    case 1: runAssessment(); break;
                    case 2: viewSavedRecords(); break;
                    case 3:
                        System.out.println();
                        System.out.println(Ansi.BOLD + Ansi.BRIGHT_GREEN
                                + "  ✔  Thank you for using Holistic Health System. Stay healthy! 💪"
                                + Ansi.RESET + "\n");
                        running = false;
                        break;
                }
            } catch (InvalidMenuChoiceException e) {
                printError(e.getMessage());
            } catch (Exception e) {
                printError("Unexpected input. Please try again.");
            }
        }
        scanner.close();
    }

    static void printMainMenu() {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ╔══════════════════════════════════════════════╗" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE
                + "  ║              🌿  MAIN MENU  🌿               ║" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ╠══════════════════════════════════════════════╣" + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  ║  " + Ansi.BRIGHT_YELLOW + "1." + Ansi.WHITE
                + "  Start Health Assessment                  " + Ansi.CYAN + "║" + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  ║  " + Ansi.BRIGHT_YELLOW + "2." + Ansi.WHITE
                + "  View Saved Records (from file)           " + Ansi.CYAN + "║" + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  ║  " + Ansi.BRIGHT_YELLOW + "3." + Ansi.WHITE
                + "  Exit                                     " + Ansi.CYAN + "║" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ╚══════════════════════════════════════════════╝" + Ansi.RESET);
        System.out.print(Ansi.BOLD + Ansi.YELLOW + "\n  ▶  Enter choice (1-3): " + Ansi.RESET);
    }

    static int readMenuInt(int min, int max) throws InvalidMenuChoiceException {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max)
                    throw new InvalidMenuChoiceException("Please choose between " + min + " and " + max + ".");
                return v;
            } catch (NumberFormatException e) {
                printError("\"" + line + "\" is not a number. Please enter a number between " + min + " and " + max + ".");
                System.out.print(Ansi.BOLD + Ansi.YELLOW + "  ▶  Try again: " + Ansi.RESET);
            }
        }
    }

    static double readPositiveDouble(String prompt) throws InvalidBMIInputException {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double v = Double.parseDouble(line);
                if (v <= 0) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException e) {
                printError("\"" + line + "\" is not valid. Please enter a positive number (e.g. 65.5).");
            }
        }
    }

    static void printError(String msg) {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BG_RED + Ansi.WHITE
                + "  ✘  ERROR  " + Ansi.RESET + " " + Ansi.BRIGHT_RED + msg + Ansi.RESET);
    }

    static void printSuccess(String msg) {
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_GREEN + "  ✔  " + msg + Ansi.RESET);
    }

    static void runAssessment() {
        try {
            System.out.println();
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                    + "  ╔══════════════════════════════════════════════╗\n"
                    + "  ║          🩺  HEALTH ASSESSMENT               ║\n"
                    + "  ╚══════════════════════════════════════════════╝" + Ansi.RESET);

            System.out.print(Ansi.BOLD + Ansi.CYAN + "\n  ▶  Enter your name: " + Ansi.RESET);
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "User";

            MentalStatus mentalStatus = getMentalStatus();
            double bmi = getBMI();
            String bmiCat = bmi < 18.5 ? "Underweight" : bmi < 25 ? "Normal" : bmi < 30 ? "Overweight" : "Obese";

            System.out.println();
            System.out.printf(Ansi.BOLD + Ansi.BRIGHT_GREEN + "  ⚖  Your BMI: %.1f  →  %s\n" + Ansi.RESET, bmi, bmiCat);

            Goal goal = getGoal();

            UserProfile profile = new UserProfile(name, bmi, mentalStatus, goal);
            String workoutPlan = profile.generateWorkoutPlan();
            String advice = profile.getAdvice();

            System.out.println(workoutPlan);
            System.out.println(advice);

            store.add(profile);
            FileUtil.saveToFile(profile, workoutPlan);
            printSuccess("Plan saved to file: health_records.txt");

        } catch (InvalidBMIInputException e) {
            printError("[BMI] " + e.getMessage());
        } catch (InvalidMenuChoiceException e) {
            printError("[Menu] " + e.getMessage());
        } catch (IOException e) {
            printError("[File] Could not save record: " + e.getMessage());
        }
    }

    static MentalStatus getMentalStatus() throws InvalidMenuChoiceException {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ┌──────────────────────────────────────────────┐\n"
                + "  │    Step 1 · Mental Health Assessment 🧠      │\n"
                + "  └──────────────────────────────────────────────┘" + Ansi.RESET);

        System.out.println(Ansi.WHITE + "  How would you like to proceed?" + Ansi.RESET);
        System.out.println(Ansi.CYAN  + "  1. " + Ansi.WHITE + "Take the quiz (9 questions)" + Ansi.RESET);
        System.out.println(Ansi.CYAN  + "  2. " + Ansi.WHITE + "I already know my mental state (skip)" + Ansi.RESET);
        System.out.print(Ansi.BOLD + Ansi.YELLOW + "  ▶  Enter choice (1-2): " + Ansi.RESET);

        int mode = readMenuInt(1, 2);

        if (mode == 2) {
            System.out.println("\n" + Ansi.WHITE + "  Select your current mental state:" + Ansi.RESET);
            System.out.println(Ansi.CYAN + "  1. " + Ansi.BRIGHT_RED + "Depressed" + Ansi.RESET);
            System.out.println(Ansi.CYAN + "  2. " + Ansi.BRIGHT_YELLOW + "Stressed" + Ansi.RESET);
            System.out.println(Ansi.CYAN + "  3. " + Ansi.BRIGHT_GREEN + "Normal" + Ansi.RESET);
            System.out.print(Ansi.BOLD + Ansi.YELLOW + "  ▶  Enter choice (1-3): " + Ansi.RESET);
            int pick = readMenuInt(1, 3);
            switch (pick) {
                case 1: return MentalStatus.DEPRESSED;
                case 2: return MentalStatus.STRESSED;
                case 3: return MentalStatus.NORMAL;
            }
        }
        return runMentalQuiz();
    }

    static MentalStatus runMentalQuiz() {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE + "  Answer each question honestly." + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  Options: A / B / C   (A = most severe  ·  C = fine)\n" + Ansi.RESET);

        String[][] questions = {
            {"Q1. How often do you feel sad or empty for no clear reason?", "A.  Almost every day", "B.  A few times a week", "C.  Rarely or never"},
            {"Q2. How is your energy level throughout the day?", "A.  Exhausted even without doing anything", "B.  I get tired easily and feel drained", "C.  My energy is mostly fine"},
            {"Q3. How well are you sleeping?", "A.  Sleep too much or barely at all", "B.  Trouble falling or staying asleep", "C.  Sleep well most nights"},
            {"Q4. How do you handle daily tasks or responsibilities?", "A.  Can barely do anything — feel hopeless", "B.  Overwhelmed and struggling to keep up", "C.  Manage them without major issues"},
            {"Q5. How often do you feel anxious, tense, or on edge?", "A.  Constantly — hard to calm down", "B.  Often, especially under pressure", "C.  Only occasionally and it passes quickly"},
            {"Q6. How is your appetite lately?", "A.  I have almost no appetite or I overeat compulsively", "B.  My appetite is inconsistent / affected by stress", "C.  I eat normally with no major issues"},
            {"Q7. How often do you feel motivated to do things you used to enjoy?", "A.  Almost never — nothing feels worth doing", "B.  Motivation is low — I push myself to do things", "C.  I still enjoy and look forward to activities"},
            {"Q8. How would you describe your ability to concentrate or focus?", "A.  Very hard to focus — my mind feels foggy", "B.  Distracted often but can manage with effort", "C.  I concentrate well without much difficulty"},
            {"Q9. How do you feel about your social connections?", "A.  I avoid people and feel very isolated", "B.  I feel somewhat disconnected or withdrawn", "C.  I feel connected and supported by others"}
        };

        int totalScore = 0;
        int maxScore = questions.length * 2;

        for (int qi = 0; qi < questions.length; qi++) {
            String[] q = questions[qi];
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ┌─ " + (qi + 1) + "/" + questions.length + " ─────────────────────────────────────┐" + Ansi.RESET);
            System.out.println(Ansi.BOLD + Ansi.WHITE + "  │  " + q[0] + Ansi.RESET);
            System.out.println(Ansi.BRIGHT_CYAN + "  └────────────────────────────────────────────┘" + Ansi.RESET);
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_RED + "     " + q[1] + Ansi.RESET);
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_YELLOW + "     " + q[2] + Ansi.RESET);
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_GREEN + "     " + q[3] + Ansi.RESET);

            while (true) {
                System.out.print(Ansi.BOLD + Ansi.CYAN + "\n  ▶  Your answer (A / B / C): " + Ansi.RESET);
                String answer = scanner.nextLine().trim().toUpperCase();

                if (answer.equals("A") || answer.equals("B") || answer.equals("C")) {
                    switch (answer) {
                        case "A": totalScore += 2; break;
                        case "B": totalScore += 1; break;
                    }
                    String fb = answer.equals("A") ? Ansi.RED + "  ✘  Noted. Take care of yourself."
                            : answer.equals("B") ? Ansi.YELLOW + "  ~  Noted."
                            : Ansi.GREEN + "  ✔  Great!";
                    System.out.println(fb + Ansi.RESET);
                    break;
                } else {
                    System.out.println();
                    System.out.println(Ansi.BOLD + Ansi.BG_RED + Ansi.WHITE + "  ✘  INVALID INPUT  " + Ansi.RESET
                            + " " + Ansi.BRIGHT_RED + "\"" + answer + "\" is not accepted. Please type only A, B, or C." + Ansi.RESET);
                    System.out.println(Ansi.YELLOW + "  ↺  Repeating the same question...\n" + Ansi.RESET);
                }
            }
            System.out.println();
        }

        int pct = (int) Math.round((totalScore * 100.0) / maxScore);
        String bar = buildProgressBar(pct, 30);

        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ╔══════════════════════════════════════════════╗\n"
                + "  ║              📊  QUIZ RESULTS                ║\n"
                + "  ╠══════════════════════════════════════════════╣" + Ansi.RESET);
        System.out.printf(Ansi.BOLD + Ansi.WHITE + "  ║  Score   : %d / %d  (%d%%)\n" + Ansi.RESET, totalScore, maxScore, pct);
        System.out.println(Ansi.WHITE + "  ║  " + bar + Ansi.RESET);

        MentalStatus result;
        if (totalScore >= 11) {
            result = MentalStatus.DEPRESSED;
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_RED + "  ║  Result  : DEPRESSED 😔" + Ansi.RESET);
            System.out.println(Ansi.RED + "  ║  Your responses suggest signs of depression.\n"
                    + "  ║  Please be gentle with yourself and seek support." + Ansi.RESET);
        } else if (totalScore >= 5) {
            result = MentalStatus.STRESSED;
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_YELLOW + "  ║  Result  : STRESSED 😰" + Ansi.RESET);
            System.out.println(Ansi.YELLOW + "  ║  You seem to be under noticeable stress.\n"
                    + "  ║  Rest and light activity can help a great deal." + Ansi.RESET);
        } else {
            result = MentalStatus.NORMAL;
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_GREEN + "  ║  Result  : NORMAL 😊" + Ansi.RESET);
            System.out.println(Ansi.GREEN + "  ║  You appear to be in a good mental state!\n"
                    + "  ║  Keep up the great work." + Ansi.RESET);
        }
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╚══════════════════════════════════════════════╝" + Ansi.RESET);

        return result;
    }

    static String buildProgressBar(int pct, int width) {
        int filled = (int) Math.round(pct / 100.0 * width);
        String bar = "█".repeat(filled) + "░".repeat(width - filled);
        String color = pct >= 60 ? Ansi.BRIGHT_RED : pct >= 28 ? Ansi.BRIGHT_YELLOW : Ansi.BRIGHT_GREEN;
        return color + "[" + bar + "] " + pct + "%" + Ansi.RESET;
    }

    static double getBMI() throws InvalidBMIInputException {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ┌──────────────────────────────────────────────┐\n"
                + "  │    Step 2 · BMI Calculation ⚖               │\n"
                + "  └──────────────────────────────────────────────┘" + Ansi.RESET);

        double weight = readPositiveDouble(Ansi.CYAN + "  ▶  Enter your weight (kg): " + Ansi.RESET);
        double heightCm = readPositiveDouble(Ansi.CYAN + "  ▶  Enter your height (cm): " + Ansi.RESET);

        if (weight > 500 || heightCm > 300)
            throw new InvalidBMIInputException("Values seem unrealistic. Please check your input.");

        double heightM = heightCm / 100.0;
        return weight / (heightM * heightM);
    }

    static Goal getGoal() throws InvalidMenuChoiceException {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ┌──────────────────────────────────────────────┐\n"
                + "  │    Step 3 · Your Health Goal 🎯              │\n"
                + "  └──────────────────────────────────────────────┘" + Ansi.RESET);
        System.out.println(Ansi.WHITE + "  What is your main goal?" + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  1. " + Ansi.MAGENTA + "Lose Weight" + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  2. " + Ansi.MAGENTA + "Build Muscle" + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  3. " + Ansi.MAGENTA + "Maintain Weight" + Ansi.RESET);
        System.out.print(Ansi.BOLD + Ansi.YELLOW + "  ▶  Enter choice (1-3): " + Ansi.RESET);

        int choice = readMenuInt(1, 3);
        switch (choice) {
            case 1: return Goal.LOSE_WEIGHT;
            case 2: return Goal.BUILD_MUSCLE;
            case 3: return Goal.MAINTAIN_WEIGHT;
        }
        throw new InvalidMenuChoiceException("Please choose 1, 2, or 3 for goal.");
    }

    static void viewSavedRecords() {
        boolean inRecordMenu = true;

        while (inRecordMenu) {
            System.out.println();
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╔══════════════════════════════════════════════╗" + Ansi.RESET);
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE + "  ║       📂  SAVED HEALTH RECORDS MENU          ║" + Ansi.RESET);
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╠══════════════════════════════════════════════╣" + Ansi.RESET);

            try {
                List<String> names = FileUtil.listAllNames();

                if (names.isEmpty()) {
                    System.out.println(Ansi.BRIGHT_CYAN + "  ║                                              ║" + Ansi.RESET);
                    System.out.println(Ansi.YELLOW + "  ║   📭  No saved records found yet.            ║" + Ansi.RESET);
                    System.out.println(Ansi.BRIGHT_CYAN + "  ║                                              ║" + Ansi.RESET);
                    System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╠══════════════════════════════════════════════╣" + Ansi.RESET);
                    System.out.println(Ansi.CYAN + "  ║  " + Ansi.BRIGHT_YELLOW + "0." + Ansi.WHITE + "  ← Back to Main Menu                     " + Ansi.CYAN + "║" + Ansi.RESET);
                    System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╚══════════════════════════════════════════════╝" + Ansi.RESET);
                    System.out.print(Ansi.BOLD + Ansi.YELLOW + "\n  ▶  Enter choice (0): " + Ansi.RESET);
                    try { readMenuInt(0, 0); } catch (InvalidMenuChoiceException ignored) {}
                    inRecordMenu = false;
                    continue;
                }

                System.out.println(Ansi.CYAN + "  ║  " + Ansi.BRIGHT_YELLOW + "0."
                        + Ansi.BRIGHT_WHITE + "  ← Back to Main Menu" + Ansi.RESET
                        + " ".repeat(23) + Ansi.CYAN + "║" + Ansi.RESET);
                System.out.println(Ansi.BRIGHT_CYAN + "  ╠══════════════════════════════════════════════╣" + Ansi.RESET);
                System.out.println(Ansi.BOLD + Ansi.CYAN + "  ║  " + Ansi.BRIGHT_WHITE + "A." + Ansi.WHITE
                        + "  View ALL records" + " ".repeat(27) + Ansi.CYAN + "║" + Ansi.RESET);
                System.out.println(Ansi.BRIGHT_CYAN + "  ╠══════════════════════════════════════════════╣" + Ansi.RESET);

                for (int i = 0; i < names.size(); i++) {
                    String nameEntry = names.get(i);
                    String label = (i + 1) + ".  " + nameEntry;
                    int spaces = 44 - 4 - label.length();
                    System.out.println(Ansi.CYAN + "  ║  " + Ansi.BRIGHT_YELLOW
                            + (i + 1) + "." + Ansi.BRIGHT_WHITE + "  " + nameEntry
                            + " ".repeat(Math.max(1, spaces)) + Ansi.CYAN + "║" + Ansi.RESET);
                }

                System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╚══════════════════════════════════════════════╝" + Ansi.RESET);
                System.out.print(Ansi.BOLD + Ansi.YELLOW
                        + "\n  ▶  Enter choice (0 = Back, A = All, 1-" + names.size() + " = Person): " + Ansi.RESET);

                String raw = scanner.nextLine().trim().toUpperCase();

                if (raw.equals("0")) {
                    printSuccess("Returning to Main Menu...");
                    inRecordMenu = false;
                } else if (raw.equals("A")) {
                    System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE + "\n  Showing all records:" + Ansi.RESET);
                    FileUtil.readFromFile();
                    pressEnterToContinue();
                } else {
                    try {
                        int pick = Integer.parseInt(raw);
                        if (pick < 1 || pick > names.size()) {
                            printError("Please enter 0, A, or a number between 1 and " + names.size() + ".");
                        } else {
                            String chosen = names.get(pick - 1);
                            System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE + "\n  Showing records for: " + Ansi.BRIGHT_CYAN + chosen + Ansi.RESET);
                            FileUtil.readByName(chosen);
                            pressEnterToContinue();
                        }
                    } catch (NumberFormatException e) {
                        printError("\"" + raw + "\" is not valid. Enter 0, A, or a number 1–" + names.size() + ".");
                    }
                }

            } catch (IOException e) {
                printError("[File Error] Could not read records: " + e.getMessage());
                inRecordMenu = false;
            }
        }
    }

    static void pressEnterToContinue() {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╔══════════════════════════════════════════════╗" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE + "  ║   ↵  Press ENTER to return to Records Menu   ║" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╚══════════════════════════════════════════════╝" + Ansi.RESET);
        scanner.nextLine();
    }

    static void printBanner() {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╔══════════════════════════════════════════════╗" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE + "  ║   🌿  HOLISTIC HEALTH SYSTEM  🌿             ║" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╠══════════════════════════════════════════════╣" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.WHITE + "  ║        Your Mind + Body Wellness Guide       ║" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╚══════════════════════════════════════════════╝" + Ansi.RESET);
    }
}
