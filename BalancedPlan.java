public class BalancedPlan extends FitnessPlan {

    public BalancedPlan(UserProfile user) {
        super(user);
    }

    @Override
    public int calories() {
        return (int)(10 * user.getWeight() + 1500);
    }

    @Override
    public String workout() {
        return """
        Mon: Full Body
        Tue: Cardio
        Wed: Yoga
        Thu: Strength
        Fri: Walking
        Sat: Sports
        Sun: Rest
        """;
    }

    @Override
    public String dietPlan() {
        return """
        Rice, Vegetables, Fruit, Chicken, Yogurt
        Balanced nutrition
        """;
    }

    @Override
    public String tips() {
        return """
        Stay consistent
        Eat balanced diet
        Exercise daily
        Manage stress
        """;
    }
}