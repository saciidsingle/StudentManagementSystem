import java.util.*;
import javax.swing.*;

public class StudentManagementSystem extends JFrame {
    private final ArrayList<Student> students = new ArrayList<>();
    private final ArrayList<Course> courses = new ArrayList<>();
    private final JTabbedPane tabbedPane = new JTabbedPane();
    
    public StudentManagementSystem() {
        initializeSampleData();
        setupGUI();
    }
    
    private void initializeSampleData() {
        courses.add(new Course("CS101", "Introduction to Programming"));
        courses.add(new Course("MATH201", "Calculus I"));
        courses.add(new Course("ENG101", "English Composition"));

        // Automatically generate 5 students
        for (int i = 1; i <= 5; i++) {
            String id = String.format("S%03d", i);
            String name = "Student " + i;
            String email = "student" + i + "@email.com";
            String major = switch (i) {
                case 1 -> "Computer Science";
                case 2 -> "Mathematics";
                case 3 -> "Physics";
                case 4 -> "Chemistry";
                default -> "Biology";
            };
            students.add(new Student(id, name, email, major));
        }
        
        for (Student student : students) {
            // Example: enroll each student in the first two courses
            if (!courses.isEmpty()) {
                student.enrollInCourse(courses.get(0).getCode());
                if (courses.size() > 1) {
                    student.enrollInCourse(courses.get(1).getCode());
                }
            }
        }
    }
    
    private void setupGUI() {
        setTitle("Student Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        StudentPanel studentPanel = new StudentPanel(students);
        EnrollmentPanel enrollmentPanel = new EnrollmentPanel(students, courses);
        GradePanel gradePanel = new GradePanel(students, courses);
        
        tabbedPane.addTab("Student Management", studentPanel);
        tabbedPane.addTab("Course Enrollment", enrollmentPanel);
        tabbedPane.addTab("Grade Management", gradePanel);
        
        add(tabbedPane);
        
        studentPanel.setUpdateCallback(() -> {
            gradePanel.refreshStudentComboBox();
            // Also refresh other panels if needed
        });
        
        enrollmentPanel.setUpdateCallback(gradePanel::refreshData);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem system = new StudentManagementSystem();
            system.setVisible(true);
        });
    }
}