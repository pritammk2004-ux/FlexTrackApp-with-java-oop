public class UserProfile {
    private String name;
    private String username;
    private String password;
    private double weight;
    private String goal;
    private int age;
    private double height;
    private String bodyType;

    public UserProfile(String username, String password, String name,
                       double weight, String goal, int age,
                       double height, String bodyType) {
        this.username = username;
        this.password = password;
        this.name     = name;
        this.weight   = weight;
        this.goal     = goal;
        this.age      = age;
        this.height   = height;
        this.bodyType = bodyType;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName()     { return name; }
    public double getWeight()   { return weight; }
    public String getGoal()     { return goal; }
    public int    getAge()      { return age; }
    public double getHeight()   { return height; }
    public String getBodyType() { return bodyType; }
}