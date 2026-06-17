import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class FitnessApp extends JFrame {

    private JTextField weightField;
    private JComboBox<String> goalBox;
    private JTextArea outputArea;
    private String loggedInUser;

    // Colors – sports/fitness theme
    static final Color BG_TOP   = new Color(10, 25, 50);
    static final Color BG_BOT   = new Color(0, 80, 40);
    static final Color GOLD     = new Color(255, 200, 0);
    static final Color GREEN    = new Color(0, 200, 100);
    static final Color PANEL_BG = new Color(20, 40, 70, 220);
    static final Color TEXT_CLR = Color.WHITE;

    public FitnessApp(String username) {
        this.loggedInUser = username;
        setTitle("⚡ FlexTrack – " + username);
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Gradient background panel
        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, BG_TOP, getWidth(), getHeight(), BG_BOT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillOval(-60, -60, 300, 300);
                g2.fillOval(getWidth() - 150, getHeight() - 150, 300, 300);
            }
        };
        bg.setLayout(new BorderLayout(10, 10));
        bg.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(bg);

        // ── HEADER ──────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("⚡ FlexTrack Fitness", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(GOLD);
        JLabel userLabel = new JLabel("Welcome, " + username + "  ", SwingConstants.RIGHT);
        userLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        userLabel.setForeground(Color.LIGHT_GRAY);
        header.add(title, BorderLayout.CENTER);
        header.add(userLabel, BorderLayout.EAST);
        bg.add(header, BorderLayout.NORTH);

        // ── INPUT PANEL ─────────────────────────────────────────
        // FIX: was GridLayout(3,2) with only 1 button → layout broken
        // Now using BoxLayout (vertical) for clean stacking
        JPanel inputPanel = createCard();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setPreferredSize(new Dimension(200, 0));

        inputPanel.add(styledLabel("Weight (kg):"));
        inputPanel.add(Box.createVerticalStrut(5));
        weightField = styledField();
        weightField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(weightField);

        inputPanel.add(Box.createVerticalStrut(12));
        inputPanel.add(styledLabel("Fitness Goal:"));
        inputPanel.add(Box.createVerticalStrut(5));
        goalBox = new JComboBox<>(new String[]{"Weight Loss", "Muscle Building", "Balanced"});
        styleCombo(goalBox);
        goalBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        inputPanel.add(goalBox);

        inputPanel.add(Box.createVerticalStrut(15));
        JButton generateBtn = sportButton("🏋 Generate Plan", GREEN);
        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateBtn.addActionListener(e -> generatePlan());
        inputPanel.add(generateBtn);

        bg.add(inputPanel, BorderLayout.WEST);

        // ── OUTPUT AREA ─────────────────────────────────────────
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(10, 20, 40));
        outputArea.setForeground(new Color(180, 255, 180));
        outputArea.setCaretColor(GOLD);
        outputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(BorderFactory.createLineBorder(GOLD, 1));
        bg.add(scroll, BorderLayout.CENTER);

        // ── BOTTOM BUTTONS ──────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnPanel.setOpaque(false);

        JButton bmiBtn    = sportButton("📊 BMI Chart",     new Color(30, 100, 200));
        JButton timerBtn  = sportButton("⏱ Exercise Timer", new Color(180, 60, 0));
        JButton logoutBtn = sportButton("🚪 Logout",        new Color(100, 0, 0));

        bmiBtn.addActionListener(e    -> showBMIChart());
        timerBtn.addActionListener(e  -> showExerciseTimer());
        logoutBtn.addActionListener(e -> { dispose(); new LoginScreen(); });

        btnPanel.add(bmiBtn);
        btnPanel.add(timerBtn);
        btnPanel.add(logoutBtn);
        bg.add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── GENERATE PLAN ────────────────────────────────────────────
    private void generatePlan() {
        try {
            double weight = Double.parseDouble(weightField.getText().trim());
            String goal   = (String) goalBox.getSelectedItem();

            String ageStr = JOptionPane.showInputDialog(this, "Enter Age:");
            if (ageStr == null) return;                          // user cancelled
            int age = Integer.parseInt(ageStr.trim());

            String hStr = JOptionPane.showInputDialog(this, "Enter Height (m):");
            if (hStr == null) return;
            double height = Double.parseDouble(hStr.trim());

            String bodyType = (String) JOptionPane.showInputDialog(
                    this, "Select Body Type:", "Body Type",
                    JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Lean", "Balanced", "Overweight"}, "Balanced"
            );
            if (bodyType == null) return;

            UserProfile user = new UserProfile(
                    loggedInUser, "", loggedInUser,
                    weight, goal, age, height, bodyType);
            FitnessPlan plan = FitnessPlan.create(user);

            double bmi    = weight / (height * height);
            String bmiCat = bmi < 18.5 ? "Underweight"
                          : bmi < 25   ? "Normal"
                          : bmi < 30   ? "Overweight"
                          : "Obese";

            int steps = switch (goal) {
                case "Weight Loss"     -> 12000;
                case "Muscle Building" -> 8000;
                default                -> 10000;
            };

            outputArea.setText(plan.generateReport()
                    + "\n── HEALTH METRICS ──"
                    + "\nBMI      : " + String.format("%.2f", bmi) + "  (" + bmiCat + ")"
                    + "\nSteps/day: " + steps
                    + "\nAge      : " + age
                    + "\nBody Type: " + bodyType
                    + "\nUser     : " + loggedInUser
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric inputs!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Something went wrong: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── BMI CHART WINDOW ─────────────────────────────────────────
    private void showBMIChart() {
        JDialog d = new JDialog(this, "📊 BMI Reference Chart", true);
        d.setSize(540, 420);
        d.setLocationRelativeTo(this);

        JPanel chart = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(10, 25, 50));
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(GOLD);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                g2.drawString("BMI Category Chart", 170, 35);

                String[] labels = {"Underweight", "Normal", "Overweight", "Obese"};
                Color[]  colors = {new Color(100,180,255), new Color(0,200,100),
                                   new Color(255,180,0),   new Color(220,50,50)};
                double[] ranges = {18.5, 25.0, 30.0, 40.0};
                double   maxBMI = 45.0;
                int barX = 160, barY = 70, barH = 50, gap = 65;

                for (int i = 0; i < 4; i++) {
                    int y = barY + i * gap;
                    int w = (int)((ranges[i] / maxBMI) * 320);
                    g2.setColor(colors[i]);
                    g2.fillRoundRect(barX, y, w, barH, 10, 10);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 13));
                    g2.drawString(labels[i], 20, y + 30);
                    g2.setColor(GOLD);
                    g2.setFont(new Font("Arial", Font.PLAIN, 12));
                    g2.drawString("< " + ranges[i], barX + w + 8, y + 30);
                }

                g2.setColor(Color.LIGHT_GRAY);
                g2.setFont(new Font("Arial", Font.PLAIN, 11));
                for (int v = 0; v <= 45; v += 5) {
                    int x = barX + (int)((v / maxBMI) * 320);
                    g2.drawString(String.valueOf(v), x - 5, 360);
                    g2.drawLine(x, 68, x, 345);
                }
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.drawString("BMI Value →", 290, 385);
            }
        };

        d.setContentPane(chart);
        d.setVisible(true);
    }

    // ── EXERCISE TIMER WINDOW ────────────────────────────────────
    private void showExerciseTimer() {
        JDialog d = new JDialog(this, "⏱ Exercise Timer", false);
        d.setSize(380, 340);
        d.setLocationRelativeTo(this);
        d.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, BG_TOP, getWidth(), getHeight(), new Color(0, 60, 30));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setOpaque(false);

        JLabel timerLabel = new JLabel("00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 64));
        timerLabel.setForeground(GOLD);

        JComboBox<String> exerciseBox = new JComboBox<>(new String[]{
                "Warm Up (5 min)", "Cardio (30 min)", "HIIT (20 min)",
                "Strength (45 min)", "Cool Down (10 min)"
        });
        styleCombo(exerciseBox);

        JLabel statusLabel = new JLabel("Select exercise and press Start", SwingConstants.CENTER);
        statusLabel.setForeground(Color.LIGHT_GRAY);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        int[] durations = {300, 1800, 1200, 2700, 600};
        int[] remaining = {300};
        javax.swing.Timer[] t = {null};

        JButton startBtn = sportButton("▶ Start", GREEN);
        JButton stopBtn  = sportButton("⏹ Stop",  new Color(180, 60, 0));
        JButton resetBtn = sportButton("↺ Reset", new Color(30, 100, 200));
        stopBtn.setEnabled(false);

        startBtn.addActionListener(e -> {
            remaining[0] = durations[exerciseBox.getSelectedIndex()];
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            statusLabel.setText("🔥 Keep going!");
            t[0] = new javax.swing.Timer(1000, ev -> {
                remaining[0]--;
                int m = remaining[0] / 60, s = remaining[0] % 60;
                timerLabel.setText(String.format("%02d:%02d", m, s));
                if (remaining[0] <= 0) {
                    t[0].stop();
                    timerLabel.setText("00:00");
                    timerLabel.setForeground(GREEN);
                    statusLabel.setText("✅ Exercise complete!");
                    startBtn.setEnabled(true);
                    stopBtn.setEnabled(false);
                }
            });
            t[0].start();
        });

        stopBtn.addActionListener(e -> {
            if (t[0] != null) t[0].stop();
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            statusLabel.setText("⏸ Paused");
        });

        resetBtn.addActionListener(e -> {
            if (t[0] != null) t[0].stop();
            remaining[0] = durations[exerciseBox.getSelectedIndex()];
            timerLabel.setText("00:00");
            timerLabel.setForeground(GOLD);
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            statusLabel.setText("Select exercise and press Start");
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.setOpaque(false);
        btnRow.add(startBtn); btnRow.add(stopBtn); btnRow.add(resetBtn);

        panel.add(exerciseBox, BorderLayout.NORTH);
        panel.add(timerLabel, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setOpaque(false);
        south.add(statusLabel, BorderLayout.NORTH);
        south.add(btnRow, BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);

        d.setContentPane(panel);
        d.setVisible(true);
    }

    // ── HELPERS ──────────────────────────────────────────────────
    private JPanel createCard() {
        JPanel p = new JPanel();
        p.setBackground(PANEL_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        return p;
    }

    private JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_CLR);
        l.setFont(new Font("Arial", Font.BOLD, 13));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBackground(new Color(255, 255, 255, 220));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    private void styleCombo(JComboBox<?> c) {
        c.setFont(new Font("Arial", Font.PLAIN, 13));
        c.setBackground(new Color(255, 255, 255, 220));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton sportButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void main(String[] args) { new LoginScreen(); }
}