public class Course {
    private final String code;
    private final String name;
    
    public Course(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    public String getCode() { return code; }
    public String getName() { return name; }
    
    @Override
    public String toString() {
        return code + " - " + name;
    }
}