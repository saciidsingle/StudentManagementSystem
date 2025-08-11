import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class GradePanel extends JPanel {
    private final ArrayList<Student> students;
    private final ArrayList<Course> courses;
    private final JComboBox<Student> studentComboBox;
    private final JTable enrolledCoursesTable;
    private final EnrolledCoursesTableModel tableModel;
    
    public GradePanel(ArrayList<Student> students, ArrayList<Course> courses) {
        this.students = students;
        this.courses = courses;
        setLayout(new BorderLayout());
        
        // Top panel for student selection
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Select Student:"));
        
        studentComboBox = new JComboBox<>(students.toArray(new Student[0]));
        studentComboBox.addActionListener(e -> updateEnrolledCoursesList());
        topPanel.add(studentComboBox);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel for enrolled courses
        tableModel = new EnrolledCoursesTableModel(new ArrayList<>(), new HashMap<>());
        enrolledCoursesTable = new JTable(tableModel);
        
        // Add grade editor
        enrolledCoursesTable.getColumnModel().getColumn(2).setCellEditor(new GradeCellEditor());
        
        add(new JScrollPane(enrolledCoursesTable), BorderLayout.CENTER);
        
        // Bottom panel for save button
        JPanel bottomPanel = new JPanel();
        JButton saveButton = new JButton("Save Grades");
        saveButton.addActionListener(e -> saveGrades());
        bottomPanel.add(saveButton);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Initialize with first student
        if (!students.isEmpty()) {
            updateEnrolledCoursesList();
        }
    }
    
    private void updateEnrolledCoursesList() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent == null) return;
        
        ArrayList<Course> enrolledCourses = new ArrayList<>();
        for (String courseCode : selectedStudent.getEnrolledCourses()) {
            Course course = findCourseByCode(courseCode);
            if (course != null) {
                enrolledCourses.add(course);
            }
        }
        
        tableModel.setCourses(enrolledCourses, selectedStudent.getGrades());
        tableModel.fireTableDataChanged();
    }
    
    private Course findCourseByCode(String code) {
        for (Course course : courses) {
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return null;
    }
    
    private void saveGrades() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent == null) return;
        
        Map<String, String> grades = new HashMap<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String courseCode = tableModel.getCourseAt(i).getCode();
            String grade = (String) tableModel.getValueAt(i, 2);
            grades.put(courseCode, grade);
        }
        
        selectedStudent.setGrades(grades);
        JOptionPane.showMessageDialog(this, 
            "Grades saved successfully!", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void refreshData() {
        updateEnrolledCoursesList();
    }
    
    public void refreshStudentComboBox() {
        studentComboBox.setModel(new DefaultComboBoxModel<>(students.toArray(Student[]::new)));
        if (!students.isEmpty()) {
            studentComboBox.setSelectedIndex(0);
            updateEnrolledCoursesList();
        }
    }
}

class EnrolledCoursesTableModel extends AbstractTableModel {
    private ArrayList<Course> courses;
    private Map<String, String> grades;
    private final String[] columnNames = {"Course Code", "Course Name", "Grade"};
    
    public EnrolledCoursesTableModel(ArrayList<Course> courses, Map<String, String> grades) {
        this.courses = courses;
        this.grades = grades;
    }
    
    public void setCourses(ArrayList<Course> courses, Map<String, String> grades) {
        this.courses = courses;
        this.grades = grades;
    }
    
    public Course getCourseAt(int row) {
        return courses.get(row);
    }
    
    @Override
    public int getRowCount() {
        return courses.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Course course = courses.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> course.getCode();
            case 1 -> course.getName();
            case 2 -> grades.getOrDefault(course.getCode(), "");
            default -> null;
        };
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 2) {
            Course course = courses.get(rowIndex);
            grades.put(course.getCode(), (String) aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

class GradeCellEditor extends DefaultCellEditor {
    public GradeCellEditor() {
        super(new JComboBox<>(new String[]{"", "A", "B", "C", "D", "F"}));
    }
}