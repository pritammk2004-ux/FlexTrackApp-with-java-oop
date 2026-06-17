public class MusclePlan extends FitnessPlan {

    public MusclePlan(UserProfile user) {
        super(user);
    }

    @Override
    public int calories() {
        return (int)(10 * user.getWeight() + 2000);
    }

    @Override
    public String workout() {
        return """
        Mon: Push (Chest, Shoulders, Triceps)
        Tue: Pull (Back, Biceps)
        Wed: Rest
        Thu: Legs
        Fri: Upper Body
        Sat: Abs
        Sun: Rest
        """;
    }

    @Override
    public String dietPlan() {
        return """
        Eggs, Chicken, Rice, Protein Shake, Beef
        High protein diet (1.6–2.2g/kg)
        """;
    }

    @Override
    public String tips() {
        return """
        Eat protein
        Progressive overload
        Rest properly
        Stay hydrated
        """;
    }
}