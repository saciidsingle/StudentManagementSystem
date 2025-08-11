import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class EnrollmentPanel extends JPanel {
    private final ArrayList<Student> students;
    private final ArrayList<Course> courses;
    private final JComboBox<Course> courseComboBox;
    private final JTable eligibleStudentsTable;
    private final EligibleStudentsTableModel tableModel;
    private Runnable updateCallback;
    
    public EnrollmentPanel(ArrayList<Student> students, ArrayList<Course> courses) {
        this.students = students;
        this.courses = courses;
        setLayout(new BorderLayout());
        
        // Top panel for course selection
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Select Course:"));
        
        courseComboBox = new JComboBox<>(courses.toArray(Course[]::new));
        courseComboBox.addActionListener(e -> updateEligibleStudentsList());
        topPanel.add(courseComboBox);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel for eligible students
        tableModel = new EligibleStudentsTableModel(new ArrayList<>());
        eligibleStudentsTable = new JTable(tableModel);
        
        add(new JScrollPane(eligibleStudentsTable), BorderLayout.CENTER);
        
        // Bottom panel for enrollment button
        JPanel bottomPanel = new JPanel();
        JButton enrollButton = new JButton("Enroll Selected Student");
        enrollButton.addActionListener(e -> enrollStudent());
        bottomPanel.add(enrollButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Initialize with first course
        if (!courses.isEmpty()) {
            updateEligibleStudentsList();
        }
    }
    
    private void updateEligibleStudentsList() {
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();
        if (selectedCourse == null) return;
        
        ArrayList<Student> eligibleStudents = new ArrayList<>();
        for (Student student : students) {
            if (!student.isEnrolledIn(selectedCourse.getCode())) {
                eligibleStudents.add(student);
            }
        }
        
        tableModel.setStudents(eligibleStudents);
        tableModel.fireTableDataChanged();
    }
    
    private void enrollStudent() {
        int selectedRow = eligibleStudentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to enroll", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Course selectedCourse = (Course) courseComboBox.getSelectedItem();
        Student selectedStudent = tableModel.getStudentAt(selectedRow);
        
        selectedStudent.enrollInCourse(selectedCourse.getCode());
        JOptionPane.showMessageDialog(this, 
            selectedStudent.getName() + " enrolled in " + selectedCourse.getName(), 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        
        updateEligibleStudentsList();
        
        if (updateCallback != null) {
            updateCallback.run();
        }
    }
    
    public void setUpdateCallback(Runnable callback) {
        this.updateCallback = callback;
    }
    
    public void refreshData() {
        updateEligibleStudentsList();
    }
    
    public void refreshCourseComboBox() {
        courseComboBox.setModel(new DefaultComboBoxModel<>(courses.toArray(Course[]::new)));
        if (!courses.isEmpty()) {
            courseComboBox.setSelectedIndex(0);
            updateEligibleStudentsList();
        }
    }
}

class EligibleStudentsTableModel extends AbstractTableModel {
    private ArrayList<Student> students;
    private final String[] columnNames = {"ID", "Name", "Major"};
    
    public EligibleStudentsTableModel(ArrayList<Student> students) {
        this.students = students;
    }
    
    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
    
    public Student getStudentAt(int row) {
        return students.get(row);
    }
    
    @Override
    public int getRowCount() {
        return students.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> student.getId();
            case 1 -> student.getName();
            case 2 -> student.getMajor();
            default -> null;
        };
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}