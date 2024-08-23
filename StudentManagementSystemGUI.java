import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementSystemGUI {
    private List<Student> students;
    private final String fileName = "students.txt";
    private JFrame frame;
    private JTextArea textArea;
    private JTextField nameField, rollNumberField, gradeField;

    public StudentManagementSystemGUI() {
        students = new ArrayList<>();
        loadStudents();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField();
        panel.add(rollNumberField);

        panel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        panel.add(gradeField);

        frame.add(panel, BorderLayout.NORTH);

        JButton addButton = new JButton("Add Student");
        JButton removeButton = new JButton("Remove Student");
        JButton searchButton = new JButton("Search Student");
        JButton displayButton = new JButton("Display All Students");
        JButton exitButton = new JButton("Exit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStudent();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAllStudents();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStudents();
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private void addStudent() {
        String name = nameField.getText();
        String rollNumber = rollNumberField.getText();
        String grade = gradeField.getText();

        if (name.isEmpty() || rollNumber.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        students.add(new Student(name, rollNumber, grade));
        saveStudents();
        clearFields();
        JOptionPane.showMessageDialog(frame, "Student added successfully!");
    }

    private void removeStudent() {
        String rollNumber = rollNumberField.getText();

        if (rollNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Roll number is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean removed = students.removeIf(student -> student.getRollNumber().equals(rollNumber));
        if (removed) {
            saveStudents();
            JOptionPane.showMessageDialog(frame, "Student removed successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        clearFields();
    }

    private void searchStudent() {
        String rollNumber = rollNumberField.getText();

        if (rollNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Roll number is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student student = searchStudentByRollNumber(rollNumber);
        if (student != null) {
            textArea.setText("Student found: " + student);
        } else {
            textArea.setText("Student not found.");
        }
    }

    private Student searchStudentByRollNumber(String rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber().equals(rollNumber)) {
                return student;
            }
        }
        return null;
    }

    private void displayAllStudents() {
        if (students.isEmpty()) {
            textArea.setText("No students available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Student student : students) {
            sb.append(student).append("\n");
        }
        textArea.setText(sb.toString());
    }

    private void clearFields() {
        nameField.setText("");
        rollNumberField.setText("");
        gradeField.setText("");
    }

    private void saveStudents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Student student : students) {
                writer.write(student.getName() + "," + student.getRollNumber() + "," + student.getGrade());
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStudents() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    students.add(new Student(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error loading students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementSystemGUI());
    }
}
