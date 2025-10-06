
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.regex.Pattern;


public class SALSARegistrationSystem extends JFrame {

    // Database connection details
    private static final String DB_PATH = new File("src/main/resources/database/VUE_Exhibition.accdb").getAbsolutePath();
    private static final String DB_URL = "jdbc:ucanaccess://" + DB_PATH;

    // GUI Components
    private JTextField txtRegID, txtName, txtDepartment, txtPartner, txtContact, txtEmail;
    private JLabel lblImagePreview;
    private JButton btnRegister, btnSearch, btnUpdate, btnDelete, btnClear, btnExit, btnUploadImage;
    private String selectedImagePath = "";

    private static final Pattern EMAIL_PATTERN
            = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private boolean isDatabaseInitialized = false;

    public SALSARegistrationSystem() {
        initializeDatabase();
        if (isDatabaseInitialized) {
            seedTestData();
        }
        initializeGUI();
    }

    
    private void initializeDatabase() {
        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            showError("Database file not found at: " + DB_PATH);
            System.err.println("Database file missing: " + DB_PATH);
            return;
        }
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            System.out.println("Connected to database successfully!");
            DatabaseMetaData dbMeta = conn.getMetaData();
            ResultSet tables = dbMeta.getTables(null, null, "Participants", null);

            if (!tables.next()) { 
                String createTableSQL = "CREATE TABLE Participants ("
                        + "RegistrationID TEXT PRIMARY KEY, "
                        + "Name TEXT NOT NULL, "
                        + "Department TEXT, "
                        + "Partner TEXT, "
                        + "Contact TEXT, "
                        + "Email TEXT, "
                        + "ImagePath TEXT)";
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableSQL);
                    System.out.println("Table created successfully!");
                }
            } else {
                System.out.println("Table 'Participants' already exists!");
            }

            isDatabaseInitialized = true;
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Initialization Error: " + e.getMessage());
        }
    }

 
    private void seedTestData() {
        // Check if table is empty
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Participants")) {

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Table already has data, skipping seeding.");
                return;
            }

            System.out.println("Seeding 10 test entries...");

            String insertSQL = "INSERT INTO Participants (RegistrationID, Name, Department, "
                    + "Partner, Contact, Email, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                for (int i = 1; i <= 10; i++) {
                    pstmt.setString(1, "TEST" + String.format("%03d", i));
                    pstmt.setString(2, "Test User " + i);
                    pstmt.setString(3, "Department " + ((i % 3) + 1));
                    pstmt.setString(4, "Partner " + i);
                    pstmt.setString(5, "123456789" + i);
                    pstmt.setString(6, "test" + i + "@example.com");
                    pstmt.setString(7, ""); // Empty image path for test data

                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Seeded entry: TEST" + String.format("%03d", i));
                    }
                }
                System.out.println("Seeding completed successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Seeding Error: " + e.getMessage());
        }
    }

    /**
     * Initialize GUI components
     */
    private void initializeGUI() {
        setTitle("SALSA Dance Festival - Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(240, 240, 245));

        // Main container with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 245));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form and Image Panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(new Color(240, 240, 245));

        // Form Panel
        JPanel formPanel = createFormPanel();
        contentPanel.add(formPanel);

        // Image Panel
        JPanel imagePanel = createImagePanel();
        contentPanel.add(imagePanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    /**
     * Create header panel with title
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(139, 0, 139));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("SALSA Dance Festival");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("Exhibition Registration System");
        subtitle.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitle.setForeground(new Color(220, 220, 220));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(title, BorderLayout.CENTER);
        panel.add(subtitle, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Create main form panel with input fields
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(139, 0, 139), 2),
                "Participant Details",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                new Color(139, 0, 139)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize text fields
        txtRegID = createStyledTextField();
        txtName = createStyledTextField();
        txtDepartment = createStyledTextField();
        txtPartner = createStyledTextField();
        txtContact = createStyledTextField();
        txtEmail = createStyledTextField();

        // Form fields
        String[] labels = {"Registration ID:", "Full Name:", "Department:",
            "Dance Partner:", "Contact Number:", "Email Address:"};
        JTextField[] fields = {txtRegID, txtName, txtDepartment, txtPartner, txtContact, txtEmail};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1.0;
            panel.add(fields[i], gbc);
            gbc.weightx = 0.0;
        }

        return panel;
    }

    /**
     * Create image upload panel
     */
    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(139, 0, 139), 2),
                "ID Photo",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                new Color(139, 0, 139)
        ));

        lblImagePreview = new JLabel("No image selected", SwingConstants.CENTER);
        lblImagePreview.setPreferredSize(new Dimension(200, 200));
        lblImagePreview.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        lblImagePreview.setBackground(Color.WHITE);
        lblImagePreview.setOpaque(true);

        btnUploadImage = createStyledButton("Upload Photo", new Color(70, 130, 180));
        btnUploadImage.addActionListener(e -> selectImage());

        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.add(btnUploadImage);

        panel.add(lblImagePreview, BorderLayout.CENTER);
        panel.add(buttonWrapper, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Create button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(240, 240, 245));

        btnRegister = createStyledButton("Register", new Color(34, 139, 34));
        btnRegister.addActionListener(e -> registerParticipant());

        btnSearch = createStyledButton("Search", new Color(30, 144, 255));
        btnSearch.addActionListener(e -> searchParticipant());

        btnUpdate = createStyledButton("Update", new Color(255, 140, 0));
        btnUpdate.addActionListener(e -> updateParticipant());

        btnDelete = createStyledButton("Delete", new Color(220, 20, 60));
        btnDelete.addActionListener(e -> deleteParticipant());

        btnClear = createStyledButton("Clear", new Color(105, 105, 105));
        btnClear.addActionListener(e -> clearFields());

        btnExit = createStyledButton("Exit", new Color(178, 34, 34));
        btnExit.addActionListener(e -> exitApplication());

        panel.add(btnRegister);
        panel.add(btnSearch);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);
        panel.add(btnExit);

        return panel;
    }

    /**
     * Create styled text field
     */
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return textField;
    }

    /**
     * Create styled button
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 35));
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    /**
     * Image selection using JFileChooser
     */
    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select ID Photo");
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath();

            ImageIcon imageIcon = new ImageIcon(selectedImagePath);
            Image image = imageIcon.getImage().getScaledInstance(
                    190, 190, Image.SCALE_SMOOTH);
            lblImagePreview.setIcon(new ImageIcon(image));
            lblImagePreview.setText("");
            System.out.println("Image selected: " + selectedImagePath);
        }
    }

    /**
     * Validate input fields
     */
    private boolean validateInput() {
        if (txtRegID.getText().trim().isEmpty()
                || txtName.getText().trim().isEmpty()
                || txtDepartment.getText().trim().isEmpty()
                || txtPartner.getText().trim().isEmpty()
                || txtContact.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()) {
            showError("All fields are required!");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(txtEmail.getText().trim()).matches()) {
            showError("Invalid email format! Please enter a valid email address.");
            return false;
        }

        String contact = txtContact.getText().trim();
        if (!contact.matches("\\d{10,15}")) {
            showError("Invalid contact number! Please enter 10-15 digits.");
            return false;
        }

        return true;
    }

    /**
     * Register new participant
     */
    private void registerParticipant() {
        if (!isDatabaseInitialized) {
            showError("Database not initialized. Please check database configuration.");
            return;
        }
        if (!validateInput()) {
            return;
        }

        String sql = "INSERT INTO Participants (RegistrationID, Name, Department, "
                + "Partner, Contact, Email, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtRegID.getText().trim());
            pstmt.setString(2, txtName.getText().trim());
            pstmt.setString(3, txtDepartment.getText().trim());
            pstmt.setString(4, txtPartner.getText().trim());
            pstmt.setString(5, txtContact.getText().trim());
            pstmt.setString(6, txtEmail.getText().trim());
            pstmt.setString(7, selectedImagePath);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                showSuccess("Participant registered successfully!");
                clearFields();
                System.out.println("Registered participant: " + txtRegID.getText().trim());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("duplicate")) {
                showError("Registration ID already exists!");
            } else {
                showError("Registration Error: " + e.getMessage());
            }
        }
    }

    /**
     * Search participant by Registration ID
     */
    private void searchParticipant() {
        if (!isDatabaseInitialized) {
            showError("Database not initialized. Please check database configuration.");
            return;
        }
        String regID = txtRegID.getText().trim();
        if (regID.isEmpty()) {
            showError("Please enter Registration ID to search!");
            return;
        }

        String sql = "SELECT * FROM Participants WHERE RegistrationID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, regID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                txtName.setText(rs.getString("Name"));
                txtDepartment.setText(rs.getString("Department"));
                txtPartner.setText(rs.getString("Partner"));
                txtContact.setText(rs.getString("Contact"));
                txtEmail.setText(rs.getString("Email"));
                selectedImagePath = rs.getString("ImagePath");

                // Display image if available
                if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                    File imgFile = new File(selectedImagePath);
                    if (imgFile.exists()) {
                        ImageIcon imageIcon = new ImageIcon(selectedImagePath);
                        Image image = imageIcon.getImage().getScaledInstance(
                                190, 190, Image.SCALE_SMOOTH);
                        lblImagePreview.setIcon(new ImageIcon(image));
                        lblImagePreview.setText("");
                        System.out.println("Loaded image for: " + regID);
                    } else {
                        System.out.println("Image file not found: " + selectedImagePath);
                    }
                } else {
                    lblImagePreview.setIcon(null);
                    lblImagePreview.setText("No image");
                }

                showSuccess("Participant found!");
                System.out.println("Searched participant: " + regID);
            } else {
                showError("No participant found with this Registration ID!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Search Error: " + e.getMessage());
        }
    }

    /**
     * Update participant details
     */
    private void updateParticipant() {
        if (!isDatabaseInitialized) {
            showError("Database not initialized. Please check database configuration.");
            return;
        }
        if (!validateInput()) {
            return;
        }

        String sql = "UPDATE Participants SET Name = ?, Department = ?, "
                + "Partner = ?, Contact = ?, Email = ?, ImagePath = ? "
                + "WHERE RegistrationID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtName.getText().trim());
            pstmt.setString(2, txtDepartment.getText().trim());
            pstmt.setString(3, txtPartner.getText().trim());
            pstmt.setString(4, txtContact.getText().trim());
            pstmt.setString(5, txtEmail.getText().trim());
            pstmt.setString(6, selectedImagePath);
            pstmt.setString(7, txtRegID.getText().trim());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                showSuccess("Participant updated successfully!");
                System.out.println("Updated participant: " + txtRegID.getText().trim());
            } else {
                showError("No participant found with this Registration ID!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Update Error: " + e.getMessage());
        }
    }

    /**
     * Delete participant
     */
    private void deleteParticipant() {
        if (!isDatabaseInitialized) {
            showError("Database not initialized. Please check database configuration.");
            return;
        }
        String regID = txtRegID.getText().trim();
        if (regID.isEmpty()) {
            showError("Please enter Registration ID to delete!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this participant?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM Participants WHERE RegistrationID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, regID);
            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                showSuccess("Participant deleted successfully!");
                clearFields();
                System.out.println("Deleted participant: " + regID);
            } else {
                showError("No participant found with this Registration ID!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Delete Error: " + e.getMessage());
        }
    }

    /**
     * Clear all input fields
     */
    private void clearFields() {
        txtRegID.setText("");
        txtName.setText("");
        txtDepartment.setText("");
        txtPartner.setText("");
        txtContact.setText("");
        txtEmail.setText("");
        selectedImagePath = "";
        lblImagePreview.setIcon(null);
        lblImagePreview.setText("No image selected");
        txtRegID.requestFocus();
        System.out.println("Fields cleared");
    }

    /**
     * Exit application
     */
    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Application",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("Application exiting");
            System.exit(0);
        }
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        System.err.println("Error: " + message);
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Show success message
     */
    private void showSuccess(String message) {
        System.out.println("Success: " + message);
        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Main method to launch application
     */
    public static void main(String[] args) {
        try {
            // Set look and feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            SALSARegistrationSystem app = new SALSARegistrationSystem();
            app.setVisible(true);
        });
    }
}
