import java.awt.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginScreen extends JFrame {

    JTextField     usernameField;
    JPasswordField passwordField;

    static HashMap<String, String> users = new HashMap<>();

    public LoginScreen() {
        setTitle("FlexTrack Login");
        setSize(450, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Gradient background
        JPanel main = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(10, 25, 50),
                        getWidth(), getHeight(), new Color(0, 80, 40));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Card panel
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(20, 40, 70, 200));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 0), 1),
                new EmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets    = new Insets(10, 10, 10, 10);
        gc.fill      = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = 2;
        gc.gridy     = 0;

        // Title
        JLabel title = new JLabel("⚡ FlexTrack", SwingConstants.CENTER);
        title.setForeground(new Color(255, 200, 0));
        title.setFont(new Font("Arial", Font.BOLD, 30));
        card.add(title, gc);

        // Username label  ← FIX: was invisible on dark bg
        gc.gridwidth = 2; gc.gridy = 1;
        JLabel uLabel = new JLabel("Username");
        uLabel.setForeground(Color.WHITE);
        uLabel.setFont(new Font("Arial", Font.BOLD, 13));
        card.add(uLabel, gc);

        gc.gridy = 2;
        usernameField = new JTextField(15);
        styleField(usernameField);
        card.add(usernameField, gc);

        // Password label  ← FIX: was invisible on dark bg
        gc.gridy = 3;
        JLabel pLabel = new JLabel("Password");
        pLabel.setForeground(Color.WHITE);
        pLabel.setFont(new Font("Arial", Font.BOLD, 13));
        card.add(pLabel, gc);

        gc.gridy = 4;
        passwordField = new JPasswordField(15);
        styleField(passwordField);
        card.add(passwordField, gc);

        // Buttons
        gc.gridy = 5; gc.gridwidth = 1;
        JButton login = styledButton("Login", new Color(0, 160, 80));
        login.addActionListener(e -> login());
        card.add(login, gc);

        gc.gridx = 1;
        JButton register = styledButton("Register", new Color(30, 100, 200));
        register.addActionListener(e -> register());
        card.add(register, gc);

        // Reset gridx
        gc.gridx = 0;

        main.add(card);
        setContentPane(main);
        setVisible(true);
    }

    // ── ACTIONS ──────────────────────────────────────────────────
    void login() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());

        if (users.containsKey(u) && users.get(u).equals(p)) {
            dispose();
            new FitnessApp(u);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password!", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    void register() {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty!", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (users.containsKey(u)) {
            JOptionPane.showMessageDialog(this,
                    "Username already exists!", "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        users.put(u, p);
        JOptionPane.showMessageDialog(this,
                "Registered successfully! You can now login.", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ── HELPERS ──────────────────────────────────────────────────
    private void styleField(JTextField f) {
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setBackground(new Color(255, 255, 255, 220));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 0)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}