
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class StudentInfoManage {

    private static final String DATA_FILE = "Student_data.txt";
    private ArrayList<Student> studentList = new ArrayList<>();

    // UI Components
    private JFrame frame;
    private JTextField nameField, ageField;
    private JRadioButton mRadio, fRadio;
    private ButtonGroup genderGroup;
    private JComboBox<String> courseBox;
    private JTextArea notesArea;
    private JCheckBox enrollBox;
    private JTable studentTable;
private DefaultTableModel tableModel;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentInfoManage().start());
    }

    private void start() {
        loadStudentData();
        setupUI();
    }

    private void loadStudentData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name:")) {
                    String name = line.substring(5).trim();
                    String age = reader.readLine().substring(4).trim();
                    String gender = reader.readLine().substring(7).trim();
                    String course = reader.readLine().substring(8).trim();
                    boolean enrolled = reader.readLine().substring(9).trim().equalsIgnoreCase("Yes");
                    String notes = reader.readLine().substring(6).trim();
                    reader.readLine(); // skip separator line

                    studentList.add(new Student(name, age, gender, course, enrolled, notes));
                }
            }
            refreshTable();
        } catch (Exception ex) {
            // File might not exist initially or read error
            System.err.println("Error loading student data: " + ex.getMessage());
        }
    }

    private void setupUI() {
        frame = new JFrame("Student Information Manager");
        frame.setSize(500, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10)); // Border layout with spacing
frame.getContentPane().setBackground(Color.WHITE); // Optional: light background


        // Title
       JLabel title = new JLabel("Student Information Entry Form", SwingConstants.CENTER);
title.setFont(new Font("Arial", Font.BOLD, 20));
JPanel titlePanel = new JPanel(new BorderLayout());
titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
titlePanel.add(title, BorderLayout.CENTER);
frame.add(titlePanel, BorderLayout.NORTH);

        // Form Panel (inputs)
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        addFormComponents(formPanel);
       formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // adds padding
frame.add(formPanel, BorderLayout.CENTER);


        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        addButtons(buttonPanel);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null); // Center on screen
        
        
        addMouseListener();  // <- Add this at the end of setupUI()

        frame.setVisible(true);
        
    }
    private void addMouseListener(){
    studentTable.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && studentTable.getSelectedRow() != -1) {
            int row = studentTable.getSelectedRow();

            // Fetch data from the selected row
            String name = (String) tableModel.getValueAt(row, 0);
            String age = (String) tableModel.getValueAt(row, 1);
            String gender = (String) tableModel.getValueAt(row, 2);
            String course = (String) tableModel.getValueAt(row, 3);
            String enrolled = (String) tableModel.getValueAt(row, 4);
            String notes = (String) tableModel.getValueAt(row, 5);

            // Set form fields with the selected data
            nameField.setText(name);
            ageField.setText(age);

            if (gender.equalsIgnoreCase("Male")) {
                mRadio.setSelected(true);
            } else if (gender.equalsIgnoreCase("Female")) {
                fRadio.setSelected(true);
            } else {
                genderGroup.clearSelection();
            }

            courseBox.setSelectedItem(course);
            enrollBox.setSelected(enrolled.equalsIgnoreCase("Yes"));
            notesArea.setText(notes);
        }
    }
    
});

    }
    private void addFormComponents(JPanel panel) {
        nameField = new JTextField(20);
        ageField = new JTextField(5);

        mRadio = new JRadioButton("Male");
        fRadio = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(mRadio);
        genderGroup.add(fRadio);

        String[] courses = {"Computer Application", "Business Management", "Arts", "Engineering"};
        courseBox = new JComboBox<>(courses);

        notesArea = new JTextArea(4, 25);
        JScrollPane notesScroll = new JScrollPane(notesArea);

        enrollBox = new JCheckBox("Currently enrolled");

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        panel.add(ageField);

        panel.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(mRadio);
        genderPanel.add(fRadio);
        panel.add(genderPanel);

        panel.add(new JLabel("Course:"));
        panel.add(courseBox);

        panel.add(new JLabel("Notes:"));
        panel.add(notesScroll);

        panel.add(new JLabel("")); // Empty label for spacing
        panel.add(enrollBox);

        
    
// Create table model with columns
String[] columnNames = {"Name", "Age", "Gender", "Course", "Enrolled", "Notes"};
tableModel = new DefaultTableModel(columnNames, 0);
studentTable = new JTable(tableModel);
studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

// Put table inside a scroll pane
JScrollPane tableScrollPane = new JScrollPane(studentTable);
tableScrollPane.setPreferredSize(new Dimension(480, 150));

// Add tableScrollPane below formPanel
frame.add(tableScrollPane, BorderLayout.SOUTH);
    }

    private void addButtons(JPanel panel) {
        JButton submitButton = new JButton("Submit");
        JButton clearButton = new JButton("Clear");
        JButton viewButton = new JButton("View All Records");
        JButton delButton = new JButton("Delete");
        JButton searchButton = new JButton("Search");

        panel.add(submitButton);
        panel.add(clearButton);
        panel.add(viewButton);
        panel.add(delButton);
        panel.add(searchButton);

        submitButton.addActionListener(e -> handleSubmit());
        clearButton.addActionListener(e -> handleClear());
        viewButton.addActionListener(e -> handleView());
        delButton.addActionListener(e -> handleDelete());
        searchButton.addActionListener(e -> handleSearch());


    }

    private void handleSubmit() {
        String name = nameField.getText().trim();
        String age = ageField.getText().trim();
        String gender = mRadio.isSelected() ? "Male" : fRadio.isSelected() ? "Female" : "";
        String course = (String) courseBox.getSelectedItem();
        String notes = notesArea.getText().trim();
        boolean enrolled = enrollBox.isSelected();

        // Validation
        if (name.isEmpty() || age.isEmpty()) {
            showError("Please enter both Name and Age");
            return;
        }
        if (gender.isEmpty()) {
            showError("Please select a gender.");
            return;
        }
        try {
            int ageNum = Integer.parseInt(age);
            if (ageNum <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showError("Please enter a valid positive number for Age.");
            return;
        }

        Student student = new Student(name, age, gender, course, enrolled, notes);
        studentList.add(student);
        saveStudentData();

        JOptionPane.showMessageDialog(frame, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        handleClear();
    }
    //to load the data from student list to table formate
private void refreshTable() {
    tableModel.setRowCount(0); // Clear current rows
    for (Student s : studentList) {
        Object[] row = {
            s.name,
            s.age,
            s.gender,
            s.course,
            s.enrollled ? "Yes" : "No",
            s.notes
        };
        tableModel.addRow(row);
    }
}


    private void handleClear() {
        nameField.setText("");
        ageField.setText("");
        genderGroup.clearSelection();
        courseBox.setSelectedIndex(0);
        notesArea.setText("");
        enrollBox.setSelected(false);
    }

    private void handleView() {
        if (studentList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No record available.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder allData = new StringBuilder();
        for (Student s : studentList) {
            allData.append(s).append("\n");
        }
        JTextArea textArea = new JTextArea(15, 40);
        textArea.setText(allData.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(frame, scrollPane, "All Student Records", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleDelete() {
        if (studentList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No record to delete.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] studentNames = studentList.stream().map(s -> s.name).toArray(String[]::new);
        String selectedName = (String) JOptionPane.showInputDialog(
                frame,
                "Select a student to delete:",
                "Delete Student",
                JOptionPane.QUESTION_MESSAGE,
                null,
                studentNames,
                studentNames[0]);

        if (selectedName != null) {
            Student toDelete = null;
            for (Student s : studentList) {
                if (s.name.equals(selectedName)) {
                    toDelete = s;
                    break;
                }
            }
            if (toDelete != null) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to delete " + toDelete.name + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    studentList.remove(toDelete);
                    saveStudentData();
                    JOptionPane.showMessageDialog(frame, "Deleted successfully.");
                }
            }
        }
    }

    private void handleSearch() {
        String inputName = JOptionPane.showInputDialog(frame, "Enter Name:");
        if (inputName == null || inputName.trim().isEmpty()) {
            showError("Name cannot be empty.");
            return;
        }

        Student foundStudent = null;
        for (Student s : studentList) {
            if (s.name.equalsIgnoreCase(inputName.trim())) {
                foundStudent = s;
                break;
            }
        }

        if (foundStudent != null) {
            String info = "Student Information:\n"
                    + "Name: " + foundStudent.name + "\n"
                    + "Age: " + foundStudent.age + "\n"
                    + "Gender: " + foundStudent.gender + "\n"
                    + "Course: " + foundStudent.course + "\n"
                    + "Enrolled: " + (foundStudent.enrollled ? "Yes" : "No") + "\n"
                    + "Notes: " + foundStudent.notes;
            JOptionPane.showMessageDialog(frame, info, "Student Found", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "No student found with that name.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void saveStudentData() {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            for (Student s : studentList) {
                writer.write(s.toFileFormat());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error writing to file.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
