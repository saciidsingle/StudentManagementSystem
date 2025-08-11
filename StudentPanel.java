import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class StudentPanel extends JPanel {
    private final ArrayList<Student> students;
    private final JTable studentTable;
    private final StudentTableModel tableModel;
    private Runnable updateCallback;
    
    public StudentPanel(ArrayList<Student> students) {
        this.students = students;
        setLayout(new BorderLayout());
        
        tableModel = new StudentTableModel(students);
        studentTable = new JTable(tableModel);
        
        add(new JScrollPane(studentTable), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton viewButton = new JButton("View Details");
        
        addButton.addActionListener(e -> showAddStudentDialog());
        updateButton.addActionListener(e -> showUpdateStudentDialog());
        viewButton.addActionListener(e -> showStudentDetails());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(viewButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void showAddStudentDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New Student");
        dialog.setModal(true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(6, 2));
        
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField majorField = new JTextField();
        
        dialog.add(new JLabel("Student ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Major:"));
        dialog.add(majorField);
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                if (idField.getText().isEmpty() || nameField.getText().isEmpty()) {
                    throw new IllegalArgumentException("ID and Name are required fields");
                }
                
                for (Student s : students) {
                    if (s.getId().equals(idField.getText())) {
                        throw new IllegalArgumentException("Student ID already exists");
                    }
                }
                
                Student newStudent = new Student(
                    idField.getText(),
                    nameField.getText(),
                    emailField.getText(),
                    majorField.getText()
                );
                
                students.add(newStudent);
                tableModel.fireTableDataChanged();
                
                if (updateCallback != null) {
                    updateCallback.run();
                }
                
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Student added successfully!");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.add(saveButton);
        dialog.add(cancelButton);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showUpdateStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a student to update", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Student selectedStudent = students.get(selectedRow);
        JDialog dialog = new JDialog();
        dialog.setTitle("Update Student");
        dialog.setModal(true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(6, 2));
        
        JTextField idField = new JTextField(selectedStudent.getId());
        idField.setEditable(false);
        JTextField nameField = new JTextField(selectedStudent.getName());
        JTextField emailField = new JTextField(selectedStudent.getEmail());
        JTextField majorField = new JTextField(selectedStudent.getMajor());
        
        dialog.add(new JLabel("Student ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Major:"));
        dialog.add(majorField);
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                if (nameField.getText().isEmpty()) {
                    throw new IllegalArgumentException("Name is a required field");
                }
                
                selectedStudent.setName(nameField.getText());
                selectedStudent.setEmail(emailField.getText());
                selectedStudent.setMajor(majorField.getText());
                
                tableModel.fireTableDataChanged();
                
                if (updateCallback != null) {
                    updateCallback.run();
                }
                
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.add(saveButton);
        dialog.add(cancelButton);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showStudentDetails() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a student to view", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Student selectedStudent = students.get(selectedRow);
        JDialog dialog = new JDialog();
        dialog.setTitle("Student Details: " + selectedStudent.getName());
        dialog.setSize(500, 400);
        
        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setText(selectedStudent.getDetailedInfo());
        
        dialog.add(new JScrollPane(detailsArea));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    public void setUpdateCallback(Runnable callback) {
        this.updateCallback = callback;
    }
    
    public void refreshData() {
        tableModel.fireTableDataChanged();
    }
}

class StudentTableModel extends AbstractTableModel {
    private final ArrayList<Student> students;
    private final String[] columnNames = {"ID", "Name", "Email", "Major"};
    
    public StudentTableModel(ArrayList<Student> students) {
        this.students = students;
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
            case 2 -> student.getEmail();
            case 3 -> student.getMajor();
            default -> null;
        };
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

