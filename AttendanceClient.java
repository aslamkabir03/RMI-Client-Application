import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.rmi.Naming;
import java.util.Map;

public class AttendanceClient extends JFrame {
    private AttendanceService service;
    private JTextField studentIdField;
    private JComboBox<String> statusCombo;
    private JTextArea outputArea;
    private JLabel statusLabel;

    public AttendanceClient() {
        connectWithServer();
        createWindow();
    }

    private void connectWithServer() {
        try {
            service = (AttendanceService) Naming.lookup("rmi://localhost:1099/AttendanceService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "RMI server connection failed. Please start AttendanceServer first.\n" + e.getMessage(),
                    "Connection Problem",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
        }
    }

    private void createWindow() {
        setTitle("VU Attendance RMI Client - Faculty Panel");
        setSize(780, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        Color headerColor = new Color(15, 118, 110);
        Color lightTeal = new Color(232, 248, 245);
        Color buttonColor = new Color(213, 245, 238);

        JLabel titleLabel = new JLabel("Distributed Attendance Management System", JLabel.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(headerColor);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 19));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(13, 8, 13, 8));
        add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Faculty Attendance Form"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        inputPanel.add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        studentIdField = new JTextField();
        inputPanel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        inputPanel.add(new JLabel("Attendance Status:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        statusCombo = new JComboBox<>(new String[]{"Present", "Absent"});
        inputPanel.add(statusCombo, gbc);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(lightTeal);
        GridBagConstraints btn = new GridBagConstraints();
        btn.insets = new Insets(5, 5, 5, 5);
        btn.fill = GridBagConstraints.HORIZONTAL;
        btn.weightx = 1.0;

        JButton markButton = new JButton("Mark Attendance");
        JButton reportButton = new JButton("View Report");
        JButton percentageButton = new JButton("Percentage");
        JButton allRecordsButton = new JButton("All Records");
        JButton clearButton = new JButton("Clear");

        JButton[] buttons = {markButton, reportButton, percentageButton, allRecordsButton, clearButton};
        for (JButton button : buttons) {
            button.setBackground(buttonColor);
            button.setFocusPainted(false);
        }

        btn.gridx = 0; btn.gridy = 0; buttonPanel.add(markButton, btn);
        btn.gridx = 1; btn.gridy = 0; buttonPanel.add(reportButton, btn);
        btn.gridx = 2; btn.gridy = 0; buttonPanel.add(percentageButton, btn);
        btn.gridx = 3; btn.gridy = 0; buttonPanel.add(allRecordsButton, btn);
        btn.gridx = 4; btn.gridy = 0; buttonPanel.add(clearButton, btn);

        JPanel topPanel = new JPanel(new BorderLayout(6, 6));
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.CENTER);

        outputArea = new JTextArea(15, 65);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setText("Connected to server. Enter a Student ID and select an operation.");
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Server Response / Output"));

        statusLabel = new JLabel(" Connected: rmi://localhost:1099/AttendanceService");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(lightTeal);
        statusLabel.setForeground(new Color(0, 100, 80));

        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.add(scrollPane, BorderLayout.CENTER);
        outputPanel.add(statusLabel, BorderLayout.SOUTH);
        add(outputPanel, BorderLayout.SOUTH);

        markButton.addActionListener(e -> markAttendance());
        reportButton.addActionListener(e -> viewReport());
        percentageButton.addActionListener(e -> viewPercentage());
        allRecordsButton.addActionListener(e -> viewAllRecords());
        clearButton.addActionListener(e -> outputArea.setText(""));
    }

    private String getStudentId() {
        return studentIdField.getText().trim();
    }

    private void markAttendance() {
        try {
            String status = statusCombo.getSelectedItem().toString();
            outputArea.setText(service.markAttendance(getStudentId(), status));
        } catch (Exception e) {
            outputArea.setText("Client error: " + e.getMessage());
        }
    }

    private void viewReport() {
        try {
            outputArea.setText(service.getAttendanceHistory(getStudentId()));
        } catch (Exception e) {
            outputArea.setText("Client error: " + e.getMessage());
        }
    }

    private void viewPercentage() {
        try {
            double percentage = service.getAttendancePercentage(getStudentId());
            outputArea.setText("Attendance Percentage\n=====================\nStudent ID : "
                    + getStudentId() + "\nPercentage : " + String.format("%.2f", percentage) + "%");
        } catch (Exception e) {
            outputArea.setText("Client error: " + e.getMessage());
        }
    }

    private void viewAllRecords() {
        try {
            Map<String, String> records = service.getAllAttendanceRecords();
            if (records.isEmpty()) {
                outputArea.setText("No attendance record is available yet.");
                return;
            }

            StringBuilder result = new StringBuilder("All Stored Attendance Records\n");
            result.append("=============================\n");
            for (Map.Entry<String, String> entry : records.entrySet()) {
                result.append("Student ID: ").append(entry.getKey()).append("\n");
                result.append(entry.getValue()).append("\n");
            }
            outputArea.setText(result.toString());
        } catch (Exception e) {
            outputArea.setText("Client error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new AttendanceClient().setVisible(true));
    }
}
