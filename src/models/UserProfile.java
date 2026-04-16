import java.util.*;

public class UserProfile extends HealthProfile implements HealthPlan {

    public UserProfile(String name, double bmi, MentalStatus mentalStatus, Goal goal) {
        super(name, bmi, mentalStatus, goal);
    }

    @Override
    public String getProfileType() { return "Standard User"; }

    @Override
    public String generateWorkoutPlan() {
        String bmiColor = getBMIColor();
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(Ansi.BOLD + Ansi.BRIGHT_CYAN  + "  ╔══════════════════════════════════════════════╗\n" + Ansi.RESET);
        sb.append(Ansi.BOLD + Ansi.BRIGHT_WHITE  + "  ║        🏋  PERSONALIZED WORKOUT PLAN  🧘     ║\n" + Ansi.RESET);
        sb.append(Ansi.BOLD + Ansi.BRIGHT_CYAN   + "  ╠══════════════════════════════════════════════╣\n" + Ansi.RESET);

        sb.append(Ansi.BOLD + Ansi.CYAN    + "  ║  Name    : " + Ansi.BRIGHT_WHITE  + padRight(name, 33) + "║\n" + Ansi.RESET);
        sb.append(Ansi.BOLD + Ansi.CYAN    + "  ║  BMI     : " + bmiColor
                + padRight(String.format("%.1f", bmi) + " (" + getBMICategory() + ")", 33) + Ansi.RESET
                + Ansi.BOLD + Ansi.CYAN + "║\n" + Ansi.RESET);
        sb.append(Ansi.BOLD + Ansi.CYAN    + "  ║  Mental  : " + getMentalColor()
                + padRight(mentalStatus.toString(), 33) + Ansi.RESET
                + Ansi.BOLD + Ansi.CYAN + "║\n" + Ansi.RESET);
        sb.append(Ansi.BOLD + Ansi.CYAN    + "  ║  Goal    : " + Ansi.BRIGHT_YELLOW
                + padRight(goal.toString(), 33) + Ansi.RESET
                + Ansi.BOLD + Ansi.CYAN + "║\n" + Ansi.RESET);

        sb.append(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╠══════════════════════════════════════════════╣\n" + Ansi.RESET);
        sb.append(Ansi.BOLD + Ansi.BRIGHT_WHITE + "  ║           📅  WEEKLY SCHEDULE                ║\n" + Ansi.RESET);
        sb.append(Ansi.BOLD + Ansi.BRIGHT_CYAN  + "  ╠══════════════════════════════════════════════╣\n" + Ansi.RESET);

        List<String> schedule = buildSchedule();
        String[] days         = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] dayIcons     = {"🌅", "🌤 ", "🌥 ", "⛅", "🌞", "🎉", "💤"};

        for (int i = 0; i < days.length; i++) {
            String activity   = (i < schedule.size()) ? schedule.get(i) : "Rest";
            boolean isWeekend = days[i].equals("Saturday") || days[i].equals("Sunday");
            String dayColor   = isWeekend ? Ansi.BRIGHT_MAGENTA : Ansi.BRIGHT_YELLOW;
            String actColor   = isWeekend ? Ansi.MAGENTA : Ansi.WHITE;
            sb.append("  " + Ansi.BOLD + Ansi.BRIGHT_CYAN + "║  " + Ansi.RESET
                    + dayIcons[i] + " " + Ansi.BOLD + dayColor + padRight(days[i], 11) + Ansi.RESET
                    + Ansi.BLUE + "│ " + Ansi.RESET
                    + actColor + activity + Ansi.RESET + "\n");
        }

        sb.append(Ansi.BOLD + Ansi.BRIGHT_CYAN + "  ╚══════════════════════════════════════════════╝\n" + Ansi.RESET);
        return sb.toString();
    }

    private String padRight(String s, int n) {
        if (s.length() >= n) return s.substring(0, n);
        return s + " ".repeat(n - s.length());
    }

    private String getBMIColor() {
        switch (getBMICategory()) {
            case "Underweight": return Ansi.BRIGHT_CYAN;
            case "Normal":      return Ansi.BRIGHT_GREEN;
            case "Overweight":  return Ansi.BRIGHT_YELLOW;
            case "Obese":       return Ansi.BRIGHT_RED;
            default:            return Ansi.WHITE;
        }
    }

    private String getMentalColor() {
        switch (mentalStatus) {
            case DEPRESSED: return Ansi.BRIGHT_RED;
            case STRESSED:  return Ansi.BRIGHT_YELLOW;
            case NORMAL:    return Ansi.BRIGHT_GREEN;
            default:        return Ansi.WHITE;
        }
    }

    @SafeVarargs
    private final <T> List<T> mergeToList(T... items) {
        List<T> list = new ArrayList<>();
        for (T item : items) list.add(item);
        return list;
    }

    private List<String> buildSchedule() {
        String bmiCat = getBMICategory();

        if (mentalStatus == MentalStatus.DEPRESSED) {
            return mergeToList(
                "Light Walk 20 min + Gentle Yoga",
                "Rest / Meditation 15 min",
                "Light Walk 25 min + Stretching",
                "Rest / Breathing Exercise",
                "Light Walk 20 min + Yoga",
                "Nature Walk 30 min",
                "Full Rest"
            );
        } else if (mentalStatus == MentalStatus.STRESSED) {
            if (goal == Goal.LOSE_WEIGHT) {
                return mergeToList(
                    "Brisk Walk 30 min + Light Cardio",
                    "Yoga + Breathing Exercise",
                    "Cycling 30 min (low intensity)",
                    "Rest / Meditation",
                    "Swimming or Walk 30 min",
                    "Light Aerobics 30 min",
                    "Full Rest"
                );
            } else if (goal == Goal.BUILD_MUSCLE) {
                return mergeToList(
                    "Upper Body (Light) + Meditation",
                    "Rest / Yoga",
                    "Lower Body (Light) + Stretching",
                    "Rest / Breathing Exercise",
                    "Full Body Light Resistance",
                    "Walk 30 min",
                    "Full Rest"
                );
            } else {
                return mergeToList(
                    "Yoga 30 min",
                    "Walk 30 min",
                    "Stretching + Breathing",
                    "Rest",
                    "Pilates 30 min",
                    "Light Cardio 20 min",
                    "Full Rest"
                );
            }
        } else {
            if (goal == Goal.LOSE_WEIGHT) {
                if (bmiCat.equals("Obese")) {
                    return mergeToList(
                        "Walk 40 min + Light Cardio",
                        "Cycling 30 min",
                        "Water Aerobics 40 min",
                        "Rest + Stretching",
                        "Walk 45 min",
                        "Low-Impact Aerobics 30 min",
                        "Full Rest"
                    );
                } else {
                    return mergeToList(
                        "Running 30 min + HIIT 15 min",
                        "Cycling 45 min",
                        "Jump Rope + Core Workout",
                        "Rest + Stretching",
                        "Running 40 min",
                        "HIIT 30 min",
                        "Full Rest"
                    );
                }
            } else if (goal == Goal.BUILD_MUSCLE) {
                return mergeToList(
                    "Chest + Triceps (Weights)",
                    "Back + Biceps (Weights)",
                    "Rest + Stretching",
                    "Legs + Glutes (Weights)",
                    "Shoulders + Core (Weights)",
                    "Full Body Light + Cardio 20 min",
                    "Full Rest"
                );
            } else {
                return mergeToList(
                    "Cardio 30 min",
                    "Strength Training (Light)",
                    "Yoga / Pilates 30 min",
                    "Rest + Stretching",
                    "Cardio 30 min",
                    "Full Body Light Workout",
                    "Full Rest"
                );
            }
        }
    }

    @Override
    public String getAdvice() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(Ansi.BOLD + Ansi.BRIGHT_MAGENTA + "  ╔══════════════════════════════════════════════╗\n" + Ansi.RESET);
        sb.append(Ansi.BOLD + Ansi.BRIGHT_WHITE    + "  ║          💡  PERSONALIZED HEALTH ADVICE      ║\n" + Ansi.RESET);
        sb.append(Ansi.BOLD + Ansi.BRIGHT_MAGENTA  + "  ╠══════════════════════════════════════════════╣\n" + Ansi.RESET);

        // Mental advice
        switch (mentalStatus) {
            case DEPRESSED:
                sb.append(Ansi.BOLD + Ansi.BRIGHT_RED    + "  ║  🧠 MENTAL  " + Ansi.RESET
                        + Ansi.RED + "You are not alone. Start gentle.\n" + Ansi.RESET);
                sb.append(Ansi.RED + "  ║            Consider speaking with a professional.\n" + Ansi.RESET);
                break;
            case STRESSED:
                sb.append(Ansi.BOLD + Ansi.BRIGHT_YELLOW + "  ║  🧠 MENTAL  " + Ansi.RESET
                        + Ansi.YELLOW + "Take deep breaths. Pace yourself.\n" + Ansi.RESET);
                sb.append(Ansi.YELLOW + "  ║            Prioritize sleep and reduce caffeine.\n" + Ansi.RESET);
                break;
            case NORMAL:
                sb.append(Ansi.BOLD + Ansi.BRIGHT_GREEN  + "  ║  🧠 MENTAL  " + Ansi.RESET
                        + Ansi.GREEN + "Great mental state! Stay consistent.\n" + Ansi.RESET);
                break;
        }

        sb.append(Ansi.BRIGHT_MAGENTA + "  ║" + Ansi.RESET + "\n");

        // BMI advice
        switch (getBMICategory()) {
            case "Underweight":
                sb.append(Ansi.BOLD + Ansi.BRIGHT_CYAN   + "  ║  ⚖  BMI    " + Ansi.RESET
                        + Ansi.CYAN + "Eat nutritious, calorie-dense foods.\n" + Ansi.RESET);
                sb.append(Ansi.CYAN + "  ║            Avoid skipping meals.\n" + Ansi.RESET);
                break;
            case "Normal":
                sb.append(Ansi.BOLD + Ansi.BRIGHT_GREEN  + "  ║  ⚖  BMI    " + Ansi.RESET
                        + Ansi.GREEN + "BMI is healthy! Maintain balance.\n" + Ansi.RESET);
                sb.append(Ansi.GREEN + "  ║            Keep up your diet and exercise.\n" + Ansi.RESET);
                break;
            case "Overweight":
                sb.append(Ansi.BOLD + Ansi.BRIGHT_YELLOW + "  ║  ⚖  BMI    " + Ansi.RESET
                        + Ansi.YELLOW + "Reduce processed foods.\n" + Ansi.RESET);
                sb.append(Ansi.YELLOW + "  ║            Aim for 7,000+ steps daily.\n" + Ansi.RESET);
                break;
            case "Obese":
                sb.append(Ansi.BOLD + Ansi.BRIGHT_RED    + "  ║  ⚖  BMI    " + Ansi.RESET
                        + Ansi.RED + "Consult a doctor before starting.\n" + Ansi.RESET);
                sb.append(Ansi.RED + "  ║            Low-impact exercise is best for now.\n" + Ansi.RESET);
                break;
        }

        sb.append(Ansi.BRIGHT_MAGENTA + "  ║" + Ansi.RESET + "\n");

        // Goal advice
        switch (goal) {
            case LOSE_WEIGHT:
                sb.append(Ansi.BOLD + Ansi.BRIGHT_MAGENTA + "  ║  🎯 GOAL   " + Ansi.RESET
                        + Ansi.MAGENTA + "Create a modest calorie deficit.\n" + Ansi.RESET);
                sb.append(Ansi.MAGENTA + "  ║            Stay hydrated (8+ glasses/day).\n" + Ansi.RESET);
                break;
            case BUILD_MUSCLE:
                sb.append(Ansi.BOLD + Ansi.BRIGHT_MAGENTA + "  ║  🎯 GOAL   " + Ansi.RESET
                        + Ansi.MAGENTA + "Protein intake: 1.6–2.2g per kg body weight.\n" + Ansi.RESET);
                sb.append(Ansi.MAGENTA + "  ║            Progressive overload is key.\n" + Ansi.RESET);
                break;
            case MAINTAIN_WEIGHT:
                sb.append(Ansi.BOLD + Ansi.BRIGHT_MAGENTA + "  ║  🎯 GOAL   " + Ansi.RESET
                        + Ansi.MAGENTA + "Track intake and output to stay balanced.\n" + Ansi.RESET);
                sb.append(Ansi.MAGENTA + "  ║            Consistency is your superpower.\n" + Ansi.RESET);
                break;
        }

        sb.append(Ansi.BOLD + Ansi.BRIGHT_MAGENTA + "  ╚══════════════════════════════════════════════╝\n" + Ansi.RESET);
        return sb.toString();
    }
}
