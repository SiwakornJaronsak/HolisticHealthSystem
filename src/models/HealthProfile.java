public abstract class HealthProfile {
    protected String name;
    protected double bmi;
    protected MentalStatus mentalStatus;
    protected Goal goal;

    public HealthProfile(String name, double bmi, MentalStatus mentalStatus, Goal goal) {
        this.name         = name;
        this.bmi          = bmi;
        this.mentalStatus = mentalStatus;
        this.goal         = goal;
    }

    public String getName()               { return name; }
    public double getBmi()                { return bmi; }
    public MentalStatus getMentalStatus() { return mentalStatus; }
    public Goal getGoal()                 { return goal; }

    public String getBMICategory() {
        if (bmi < 18.5)      return "Underweight";
        else if (bmi < 25.0) return "Normal";
        else if (bmi < 30.0) return "Overweight";
        else                 return "Obese";
    }

    public abstract String getProfileType();
}
