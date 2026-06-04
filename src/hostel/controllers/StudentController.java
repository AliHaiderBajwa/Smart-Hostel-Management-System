package hostel.controllers;

import hostel.domain.Student;

public class StudentController {

    public static Student findStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        // Dummy lookup for MVP.
        return new Student(studentId.trim(), "Demo Student", "Block A - 204", 7700.0);
    }
}
