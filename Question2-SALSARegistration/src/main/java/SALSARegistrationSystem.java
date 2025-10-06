import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.regex.Pattern;

/**
 * SALSA Dance Festival Registration System
 * GUI Application with MS Access Database Integration
 * 
 * Features:
 * - Register new participants
 * - Search by Registration ID
 * - Update participant details
 * - Delete participants
 * - Image upload and display
 * - Input validation
 * 
 * Database: VUE_Exhibition.accdb
 * Required Library: UCanAccess (ucanaccess-5.0.1.jar and dependencies)
 */
public class SALSARegistrationSystem extends JFrame {
    
    // Database connection details
    private static final String DB_PATH = "VUE_Exhibition.accdb";
    private static final String DB_URL = "jdbc:ucanaccess://" + DB_PATH;
    
    // GUI Components
    private JTextField txtRegID, txtName, txtDepartment, txtPartner, txtContact, txtEmail;
    private JLabel lblImagePreview;
    private JButton btnRegister, btnSearch, btnUpdate, btnDelete, btnClear, btnExit, btnUploadImage;
    private String selectedImagePath = "";
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    public SALSARegistrationSystem() {
        initializeDatabase();
        initializeGUI();
    }
    
    /**
     * Initialize database and create table if not exists
     */
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Participants (" +
                "RegistrationID VARCHAR(50) PRIMARY KEY, " +
                "Name VARCHAR(100) NOT NULL, " +
                "Department VARCHAR(100), " +
                "Partner VARCHAR(100), " +
                "Contact VARCHAR(20), " +
                "Email VARCHAR(100), " +
                "ImagePath VARCHAR(255))";
            
            Statement stmt = conn.createStatement();
            stmt.execute(createTableSQL);
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            showError("Database Initialization Error: " + e.getMessage());
        }
    }
    
    /**
     * Initialize GUI components
     */
    private void initializeGUI() {
        setTitle("SALSA Dance Festival - Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 250));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Form Panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Set frame properties
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /**
     * Create header panel with title
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(139, 0, 139)); // Dark Magenta
        panel.setPreferredSize(new Dimension(900, 80));
        
        JLabel title = new JLabel("SALSA DANCE FESTIVAL");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("Exhibition Registration System");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(Color.WHITE);
        
        panel.setLayout(new GridLayout(2, 1));
        panel.add(title);
        panel.add(subtitle);
        
        return panel;
    }
    
    /**
     * Create main form panel with input fields
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Initialize text fields
        txtRegID = new JTextField(20);
        txtName = new JTextField(20);
        txtDepartment = new JTextField(20);
        txtPartner = new JTextField(20);
        txtContact = new JTextField(20);
        txtEmail = new JTextField(20);
        
        // Registration ID
        addFormField(panel, "Registration ID:", txtRegID, gbc, 0);
        
        // Name
        addFormField(panel, "Full Name:", txtName, gbc, 1);
        
        // Department
        addFormField(panel, "Department:", txtDepartment, gbc, 2);
        
        // Partner
        addFormField(panel, "Dance Partner:", txtPartner, gbc, 3);
        
        // Contact
        addFormField(panel, "Contact Number:", txtContact, gbc, 4);
        
        // Email
        addFormField(panel, "Email Address:", txtEmail, gbc, 5);
        
        // Image Upload Section
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel lblImage = new JLabel("ID Photo:");
        lblImage.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblImage, gbc);
        
        gbc.gridx = 1;
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout(5, 5));
        imagePanel.setBackground(Color.WHITE);
        
        lblImagePreview = new JLabel("No image selected");
        lblImagePreview.setPreferredSize(new Dimension(150, 150));
        lblImagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        lblImagePreview.setHorizontalAlignment(SwingConstants.CENTER);
        
        btnUploadImage = new JButton("Choose Image");
        btnUploadImage.setBackground(new Color(70, 130, 180));
        btnUploadImage.setForeground(Color.WHITE);
        btnUploadImage.addActionListener(e -> selectImage());
        
        imagePanel.add(lblImagePreview, BorderLayout.CENTER);
        imagePanel.add(btnUploadImage, BorderLayout.SOUTH);
        panel.add(imagePanel, gbc);
        
        return panel;
    }
    
    /**
     * Helper method to add form fields
     */
    private void addFormField(JPanel panel, String labelText, JTextField textField, 
                              GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(textField, gbc);
    }
    
    /**
     * Create button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(245, 245, 250));
        
        // Register Button
        btnRegister = createStyledButton("Register", new Color(34, 139, 34));
        btnRegister.addActionListener(e -> registerParticipant());
        
        // Search Button
        btnSearch = createStyledButton("Search", new Color(30, 144, 255));
        btnSearch.addActionListener(e -> searchParticipant());
        
        // Update Button
        btnUpdate = createStyledButton("Update", new Color(255, 140, 0));
        btnUpdate.addActionListener(e -> updateParticipant());
        
        // Delete Button
        btnDelete = createStyledButton("Delete", new Color(220, 20, 60));
        btnDelete.addActionListener(e -> deleteParticipant());
        
        // Clear Button
        btnClear = createStyledButton("Clear", new Color(105, 105, 105));
        btnClear.addActionListener(e -> clearFields());
        
        // Exit Button
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
     * Create styled button
     */
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
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
            
            // Display image preview
            ImageIcon imageIcon = new ImageIcon(selectedImagePath);
            Image image = imageIcon.getImage().getScaledInstance(
                150, 150, Image.SCALE_SMOOTH);
            lblImagePreview.setIcon(new ImageIcon(image));
            lblImagePreview.setText("");
        }
    }
    
    /**
     * Validate input fields
     */
    private boolean validateInput() {
        // Check for empty fields
        if (txtRegID.getText().trim().isEmpty() ||
            txtName.getText().trim().isEmpty() ||
            txtDepartment.getText().trim().isEmpty() ||
            txtPartner.getText().trim().isEmpty() ||
            txtContact.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty()) {
            showError("All fields are required!");
            return false;
        }
        
        // Validate email format
        if (!EMAIL_PATTERN.matcher(txtEmail.getText().trim()).matches()) {
            showError("Invalid email format! Please enter a valid email address.");
            return false;
        }
        
        // Validate contact number (should be numeric and reasonable length)
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
        if (!validateInput()) {
            return;
        }
        
        String sql = "INSERT INTO Participants (RegistrationID, Name, Department, " +
                     "Partner, Contact, Email, ImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
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
            }
            
        } catch (SQLException e) {
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
        String regID = txtRegID.getText().trim();
        if (regID.isEmpty()) {
            showError("Please enter Registration ID to search!");
            return;
        }
        
        String sql = "SELECT * FROM Participants WHERE RegistrationID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
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
                            150, 150, Image.SCALE_SMOOTH);
                        lblImagePreview.setIcon(new ImageIcon(image));
                        lblImagePreview.setText("");
                    }
                }
                
                showSuccess("Participant found!");
            } else {
                showError("No participant found with this Registration ID!");
            }
            
        } catch (SQLException e) {
            showError("Search Error: " + e.getMessage());
        }
    }
    
    /**
     * Update participant details
     */
    private void updateParticipant() {
        if (!validateInput()) {
            return;
        }
        
        String sql = "UPDATE Participants SET Name = ?, Department = ?, " +
                     "Partner = ?, Contact = ?, Email = ?, ImagePath = ? " +
                     "WHERE RegistrationID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
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
            } else {
                showError("No participant found with this Registration ID!");
            }
            
        } catch (SQLException e) {
            showError("Update Error: " + e.getMessage());
        }
    }
    
    /**
     * Delete participant
     */
    private void deleteParticipant() {
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
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, regID);
            int rowsDeleted = pstmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                showSuccess("Participant deleted successfully!");
                clearFields();
            } else {
                showError("No participant found with this Registration ID!");
            }
            
        } catch (SQLException e) {
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
            System.exit(0);
        }
    }
    
    /**
     * Show error message
     */
    private void showError(String message) {
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