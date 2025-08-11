import java.util.*;

public class Student {
    private final String id;
    private String name;
    private String email;
    private String major;
    private final Set<String> enrolledCourses;
    private Map<String, String> grades;
    
    public Student(String id, String name, String email, String major) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.major = major;
        this.enrolledCourses = new HashSet<>();
        this.grades = new HashMap<>();
    }
    
    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    
    public Set<String> getEnrolledCourses() { return enrolledCourses; }
    public Map<String, String> getGrades() { return grades; }
    public void setGrades(Map<String, String> grades) { this.grades = grades; }
    
    public boolean isEnrolledIn(String courseCode) {
        return enrolledCourses.contains(courseCode);
    }
    
    public void enrollInCourse(String courseCode) {
        enrolledCourses.add(courseCode);
    }
    
    public String getDetailedInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student ID: ").append(id).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Major: ").append(major).append("\n\n");
        
        sb.append("Enrolled Courses:\n");
        if (enrolledCourses.isEmpty()) {
            sb.append("  No courses enrolled\n");
        } else {
            for (String courseCode : enrolledCourses) {
                sb.append("  - ").append(courseCode);
                if (grades.containsKey(courseCode)) {
                    sb.append(" (Grade: ").append(grades.get(courseCode)).append(")");
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}