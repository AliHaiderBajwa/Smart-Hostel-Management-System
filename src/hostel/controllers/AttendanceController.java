package hostel.controllers;

import hostel.domain.MealAttendance;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AttendanceController {

    // TODO: Replace with AttendanceDAO persistence.
    private static final List<MealAttendance> attendanceRecords = new ArrayList<>();

    public static void recordAttendance(String date, String mealType, List<String> presentStudentIds) {
        for (String studentId : presentStudentIds) {
            attendanceRecords.add(new MealAttendance(date, mealType, studentId));
        }
    }

    public static List<MealAttendance> getAttendanceRecords() {
        return new ArrayList<>(attendanceRecords);
    }

    public static int getTodayAttendanceCount() {
        String today = LocalDate.now().toString();
        Set<String> uniqueStudents = attendanceRecords.stream()
                .filter(r -> today.equals(r.getDate()))
                .map(MealAttendance::getStudentId)
                .collect(Collectors.toSet());
        return uniqueStudents.size();
    }

    public static int getActiveStudentsCount() {
        Set<String> uniqueStudents = new HashSet<>();
        for (MealAttendance record : attendanceRecords) {
            uniqueStudents.add(record.getStudentId());
        }
        return uniqueStudents.size();
    }

    public static int getAttendanceDaysCountThisMonth() {
        String monthPrefix = LocalDate.now().withDayOfMonth(1).toString().substring(0, 7);
        return (int) attendanceRecords.stream()
                .filter(r -> r.getDate().startsWith(monthPrefix))
                .map(MealAttendance::getDate)
                .distinct()
                .count();
    }
}