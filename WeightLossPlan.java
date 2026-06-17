public class WeightLossPlan extends FitnessPlan {

    public WeightLossPlan(UserProfile user) {
        super(user);
    }

    @Override
    public int calories() {
        return (int)(user.getWeight() * 22);
    }

    @Override
    public String workout() {
        return "- 30 min cardio\n- 15 min HIIT\n- 10k steps daily";
    }

    @Override
    public String tips() {
        return "Drink water, sleep well, stay consistent.";
    }

    @Override
    public String dietPlan() {
        return "- High protein\n- Low carbs\n- Avoid junk food";
    }
}