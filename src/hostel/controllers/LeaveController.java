package hostel.controllers;

import hostel.domain.LeaveRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LeaveController {

    // TODO: Replace with LeaveDAO for DB persistence
    private static final List<LeaveRequest> leaveRequests = new ArrayList<>();
    private static int leaveCounter = 0;

    public static LeaveRequest submitLeaveRequest(String studentId, String startDate, String endDate, String reason, String details, int duration) {
        LeaveRequest newLeave = new LeaveRequest(studentId, startDate, endDate, duration, reason, details);
        
        leaveCounter++;
        newLeave.setLeaveId("LR-" + String.format("%03d", leaveCounter));
        
        leaveRequests.add(newLeave);
        System.out.println("New leave request stored: " + newLeave.getLeaveId());
        return newLeave;
    }

    public static List<LeaveRequest> getLeavesByStudent(String studentId) {
        return leaveRequests.stream()
                .filter(l -> l.getStudentId().equalsIgnoreCase(studentId))
                .collect(Collectors.toList());
    }

    public static long getPendingLeaveCount(String studentId) {
        return leaveRequests.stream()
                .filter(l -> l.getStudentId().equalsIgnoreCase(studentId) && "Pending".equals(l.getStatus()))
                .count();
    }

    public static int getAllPendingCount() {
        return (int) leaveRequests.stream()
                .filter(l -> "Pending".equalsIgnoreCase(l.getStatus()))
                .count();
    }

    public static List<LeaveRequest> getAllPendingLeaves() {
        return leaveRequests.stream()
                .filter(l -> "Pending".equalsIgnoreCase(l.getStatus()))
                .collect(Collectors.toList());
    }

    public static boolean processLeave(String leaveId, String decision) {
        for (LeaveRequest leaveRequest : leaveRequests) {
            if (leaveRequest.getLeaveId().equalsIgnoreCase(leaveId)) {
                leaveRequest.setStatus(decision);
                return true;
            }
        }
        return false;
    }
}