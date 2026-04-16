import java.io.*;
import java.util.*;

public class HolisticHealthSystem {

    static Scanner scanner = new Scanner(System.in);
    static HealthRecordStore<UserProfile> store = new HealthRecordStore<>();

    public static void main(String[] args) {
        printBanner();
        boolean running = true;
        while (running) {
            System.out.println("\n" + Ansi.BOLD + Ansi.BRIGHT_WHITE
                    + "  ========================================" + Ansi.RESET);
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE
                    + "             MAIN MENU" + Ansi.RESET);
            System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE
                    + "  ========================================" + Ansi.RESET);
            System.out.println(Ansi.CYAN + "  1. " + Ansi.WHITE + "Start Health Assessment" + Ansi.RESET);
            System.out.println(Ansi.CYAN + "  2. " + Ansi.WHITE + "View Saved Records (from file)" + Ansi.RESET);
            System.out.println(Ansi.CYAN + "  3. " + Ansi.WHITE + "Exit" + Ansi.RESET);
            System.out.print(Ansi.BOLD + Ansi.YELLOW + "  Enter choice (1-3): " + Ansi.RESET);
            try {
                int choice = readIntInput();
                switch (choice) {
                    case 1:
                        runAssessment();
                        break;
                    case 2:
                        viewSavedRecords();
                        break;
                    case 3:
                        System.out.println("\n" + Ansi.BOLD + Ansi.GREEN
                                + "  Thank you for using Holistic Health System. Stay healthy!"
                                + Ansi.RESET + "\n");
                        running = false;
                        break;
                    default:
                        throw new InvalidMenuChoiceException("Choice must be 1, 2, or 3.");
                }
            } catch (InvalidMenuChoiceException e) {
                System.out.println(Ansi.RED + "  [Error] " + e.getMessage() + Ansi.RESET);
            } catch (Exception e) {
                System.out.println(Ansi.RED + "  [Error] Unexpected input. Please try again." + Ansi.RESET);
            }
        }

        scanner.close();
    }

    static void runAssessment() {
        try {
            System.out.print("\n" + Ansi.BOLD + Ansi.CYAN + "  Enter your name: " + Ansi.RESET);
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = "User";

            MentalStatus mentalStatus = getMentalStatus();
            double bmi = getBMI();
            String bmiCategory = bmi < 18.5 ? "Underweight" : bmi < 25 ? "Normal" : bmi < 30 ?
            "Overweight" : "Obese";
            System.out.printf("\n" + Ansi.BOLD + Ansi.GREEN
                    + "  Your BMI is: %.1f (%s)\n" + Ansi.RESET, bmi, bmiCategory);
            Goal goal = getGoal();

            UserProfile profile = new UserProfile(name, bmi, mentalStatus, goal);
            String workoutPlan = profile.generateWorkoutPlan();
            String advice = profile.getAdvice();
            System.out.println(workoutPlan);
            System.out.println(advice);

            store.add(profile);
            FileUtil.saveToFile(profile, workoutPlan);
            System.out.println(Ansi.BOLD + Ansi.GREEN
                    + "  Plan saved to file: health_records.txt" + Ansi.RESET);
        } catch (InvalidBMIInputException e) {
            System.out.println(Ansi.RED + "  [BMI Error] " + e.getMessage() + Ansi.RESET);
        } catch (InvalidMenuChoiceException e) {
            System.out.println(Ansi.RED + "  [Menu Error] " + e.getMessage() + Ansi.RESET);
        } catch (IOException e) {
            System.out.println(Ansi.RED + "  [File Error] Could not save record: " + e.getMessage() + Ansi.RESET);
        }
    }

    static MentalStatus getMentalStatus() throws InvalidMenuChoiceException {
        System.out.println("\n" + Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  --- Step 1: Mental Health Assessment ---  " + Ansi.RESET);
        System.out.println(Ansi.WHITE + "  Would you like to take the quiz or select manually?" + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  1. " + Ansi.WHITE + "Take the quiz (5 questions)" + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  2. " + Ansi.WHITE + "I already know my mental state (skip)" + Ansi.RESET);
        System.out.print(Ansi.BOLD + Ansi.YELLOW + "  Enter choice (1-2): " + Ansi.RESET);

        int mode = readIntInput();
        if (mode == 2) {
            System.out.println("\n" + Ansi.WHITE + "  Select your current mental state:" + Ansi.RESET);
            System.out.println(Ansi.CYAN + "  1. " + Ansi.RED    + "Depressed" + Ansi.RESET);
            System.out.println(Ansi.CYAN + "  2. " + Ansi.YELLOW + "Stressed"  + Ansi.RESET);
            System.out.println(Ansi.CYAN + "  3. " + Ansi.GREEN  + "Normal"    + Ansi.RESET);
            System.out.print(Ansi.BOLD + Ansi.YELLOW + "  Enter choice (1-3): " + Ansi.RESET);
            int pick = readIntInput();
            switch (pick) {
                case 1: return MentalStatus.DEPRESSED;
                case 2: return MentalStatus.STRESSED;
                case 3: return MentalStatus.NORMAL;
                default: throw new InvalidMenuChoiceException("Please choose 1, 2, or 3.");
            }
        } else if (mode == 1) {
            return runMentalQuiz();
        } else {
            throw new InvalidMenuChoiceException("Please choose 1 or 2.");
        }
    }

    static MentalStatus runMentalQuiz() {
        System.out.println("\n" + Ansi.BOLD + Ansi.WHITE
                + "  Answer each question honestly." + Ansi.RESET);
        System.out.println(Ansi.CYAN
                + "  Options: A / B / C  (A = most severe, C = fine)\n" + Ansi.RESET);
        String[][] questions = {
            {
                "Q1. How often do you feel sad or empty for no clear reason?",
                "A. Almost every day",
                "B. A few times a week",
                "C. Rarely or never"
            },
            {
                "Q2. How is your energy level throughout the day?",
                "A. I feel exhausted even without doing anything",
                "B. I get tired easily and feel drained",
                "C. My energy is mostly fine"
            },
            {
                "Q3. How well are you sleeping?",
                "A. I sleep too much or can barely sleep at all",
                "B. I have trouble falling or staying asleep",
                "C. I sleep well most nights"
            },
            {
                "Q4. How do you handle daily tasks or responsibilities?",
                "A. I can barely do anything, I feel hopeless",
                "B. I feel overwhelmed and struggle to keep up",
                "C. I manage them without major issues"
            },
            {
                "Q5. How often do you feel anxious, tense, or on edge?",
                "A. Constantly, it is hard to calm down",
                "B. Often, especially under pressure",
                "C. Only occasionally and it passes quickly"
            }
        };

        int totalScore = 0;

        for (String[] q : questions) {
            System.out.println(Ansi.BOLD + Ansi.WHITE + "  " + q[0] + Ansi.RESET);
            System.out.println(Ansi.RED    + "    " + q[1] + Ansi.RESET);
            System.out.println(Ansi.YELLOW + "    " + q[2] + Ansi.RESET);
            System.out.println(Ansi.GREEN  + "    " + q[3] + Ansi.RESET);

            String answer = "";
            while (true) {
                System.out.print(Ansi.BOLD + Ansi.CYAN + "  Your answer (A/B/C): " + Ansi.RESET);
                answer = scanner.nextLine().trim().toUpperCase();

                if (answer.equals("A") || answer.equals("B") || answer.equals("C")) {
                    break;
                } else {
                    System.out.println(Ansi.RED
                            + "  [Invalid] Only A, B, or C are accepted. Please try again."
                            + Ansi.RESET);
                }
            }

            switch (answer) {
                case "A": totalScore += 2; break;
                case "B": totalScore += 1; break;
                case "C": totalScore += 0; break;
            }
            System.out.println();
        }

        System.out.println(Ansi.BLUE + "  ------------------------------------------" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.WHITE + String.format("  Quiz Score : %d / 10", totalScore) + Ansi.RESET);

        MentalStatus result;
        if (totalScore >= 7) {
            result = MentalStatus.DEPRESSED;
            System.out.println(Ansi.BOLD + Ansi.RED + "  Result     : DEPRESSED" + Ansi.RESET);
            System.out.println(Ansi.RED + "  Note       : Your score suggests you may be experiencing" + Ansi.RESET);
            System.out.println(Ansi.RED + "               signs of depression. Please be kind to yourself." + Ansi.RESET);
        } else if (totalScore >= 3) {
            result = MentalStatus.STRESSED;
            System.out.println(Ansi.BOLD + Ansi.YELLOW + "  Result     : STRESSED" + Ansi.RESET);
            System.out.println(Ansi.YELLOW + "  Note       : You seem to be under noticeable stress." + Ansi.RESET);
            System.out.println(Ansi.YELLOW + "               Rest and light activity can help." + Ansi.RESET);
        } else {
            result = MentalStatus.NORMAL;
            System.out.println(Ansi.BOLD + Ansi.GREEN + "  Result     : NORMAL" + Ansi.RESET);
            System.out.println(Ansi.GREEN + "  Note       : You appear to be in a good mental state!" + Ansi.RESET);
        }
        System.out.println(Ansi.BLUE + "  ------------------------------------------" + Ansi.RESET);

        return result;
    }

    static double getBMI() throws InvalidBMIInputException {
        System.out.println("\n" + Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  --- Step 2: BMI Calculation ---  " + Ansi.RESET);
        System.out.print(Ansi.CYAN + "  Enter your weight (kg): " + Ansi.RESET);
        double weight = readDoubleInput();
        System.out.print(Ansi.CYAN + "  Enter your height (cm): " + Ansi.RESET);
        double heightCm = readDoubleInput();
        if (weight <= 0 || heightCm <= 0) {
            throw new InvalidBMIInputException("Weight and height must be positive numbers.");
        }
        if (weight > 500 || heightCm > 300) {
            throw new InvalidBMIInputException("Values seem unrealistic. Please check your input.");
        }

        double heightM = heightCm / 100.0;
        return weight / (heightM * heightM);
    }

    static Goal getGoal() throws InvalidMenuChoiceException {
        System.out.println("\n" + Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  --- Step 3: Your Health Goal ---  " + Ansi.RESET);
        System.out.println(Ansi.WHITE + "  What is your main goal?" + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  1. " + Ansi.MAGENTA + "Lose Weight"     + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  2. " + Ansi.MAGENTA + "Build Muscle"    + Ansi.RESET);
        System.out.println(Ansi.CYAN + "  3. " + Ansi.MAGENTA + "Maintain Weight" + Ansi.RESET);
        System.out.print(Ansi.BOLD + Ansi.YELLOW + "  Enter choice (1-3): " + Ansi.RESET);

        int choice = readIntInput();
        switch (choice) {
            case 1: return Goal.LOSE_WEIGHT;
            case 2: return Goal.BUILD_MUSCLE;
            case 3: return Goal.MAINTAIN_WEIGHT;
            default: throw new InvalidMenuChoiceException("Please choose 1, 2, or 3 for goal.");
        }
    }

    static void viewSavedRecords() {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ╔══════════════════════════════════════════╗" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ║        SAVED HEALTH RECORDS MENU        ║" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_CYAN
                + "  ╚══════════════════════════════════════════╝" + Ansi.RESET);
        try {
            List<String> names = FileUtil.listAllNames();
            if (names.isEmpty()) {
                System.out.println(Ansi.YELLOW + "  No saved records found." + Ansi.RESET);
                return;
            }

            // -- แสดงรายชื่อที่มีในไฟล์
            System.out.println(Ansi.BOLD + Ansi.WHITE
                    + "\n  People with saved records:" + Ansi.RESET);
            System.out.println(Ansi.CYAN
                    + "  0. " + Ansi.WHITE + "View ALL records" + Ansi.RESET);
            for (int i = 0; i < names.size(); i++) {
                System.out.println(Ansi.CYAN + "  " + (i + 1) + ". "
                        + Ansi.BRIGHT_WHITE + names.get(i) + Ansi.RESET);
            }

            System.out.println();
            System.out.print(Ansi.BOLD + Ansi.YELLOW
                    + "  Enter choice (0-" + names.size() + "): " + Ansi.RESET);
            int pick = readIntInput();

            if (pick == 0) {
                System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE
                        + "\n  Showing all records:" + Ansi.RESET);
                FileUtil.readFromFile();

            } else if (pick >= 1 && pick <= names.size()) {
                String chosen = names.get(pick - 1);
                System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE
                        + "\n  Showing records for: "
                        + Ansi.BRIGHT_CYAN + chosen + Ansi.RESET);
                FileUtil.readByName(chosen);

            } else {
                System.out.println(Ansi.RED
                        + "  [Error] Invalid choice. Please enter 0 to " + names.size()
                        + Ansi.RESET);
            }

        } catch (IOException e) {
            System.out.println(Ansi.RED + "  [File Error] Could not read records: "
                    + e.getMessage() + Ansi.RESET);
        }
    }

    static int readIntInput() {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static double readDoubleInput() {
        try {
            String line = scanner.nextLine().trim();
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static void printBanner() {
        System.out.println();
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE
                + "  ============================================" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE
                + "         HOLISTIC HEALTH SYSTEM" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.WHITE
                + "     Your Mind + Body Wellness Guide" + Ansi.RESET);
        System.out.println(Ansi.BOLD + Ansi.BRIGHT_WHITE
                + "  ============================================" + Ansi.RESET);
    }
}
