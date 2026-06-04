package hostel.controllers;

import hostel.domain.Complaint;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComplaintController {

    // TODO: Replace ArrayList with ComplaintDAO for DB persistence
    private static final List<Complaint> complaints = new ArrayList<>();
    private static int complaintCounter = 0;

    /**
     * Creates, stores, and returns a new complaint with a generated ID and timestamp.
     */
    public static Complaint submitComplaint(String type, String description, String urgency, String roomNumber, String studentId) {
        Complaint newComplaint = new Complaint(studentId, type, description, urgency, roomNumber);
        
        // Generate auto-incremented ID
        complaintCounter++;
        String complaintId = "CMP-" + String.format("%03d", complaintCounter);
        newComplaint.setComplaintId(complaintId);

        // Set status and timestamp
        newComplaint.setStatus("Submitted");
        newComplaint.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        
        complaints.add(newComplaint);
        System.out.println("New complaint stored: " + newComplaint.getComplaintId());
        return newComplaint;
    }

    /**
     * Returns a list of all complaints submitted by a specific student.
     */
    public static List<Complaint> getComplaintsByStudent(String studentId) {
        return complaints.stream()
                .filter(c -> c.getStudentId().equalsIgnoreCase(studentId))
                .collect(Collectors.toList());
    }

    /**
     * Returns the count of active (not 'Resolved') complaints for a specific student.
     */
    public static long getActiveComplaintCount(String studentId) {
        return complaints.stream()
                .filter(c -> c.getStudentId().equalsIgnoreCase(studentId) && !c.getStatus().equals("Resolved"))
                .count();
    }

    public static int getAllPendingCount() {
        return (int) complaints.stream()
                .filter(c -> !"Resolved".equalsIgnoreCase(c.getStatus()))
                .count();
    }

    public static List<Complaint> getAllComplaints() {
        return new ArrayList<>(complaints);
    }

    public static boolean assignComplaint(String complaintId, String staffName) {
        for (Complaint complaint : complaints) {
            if (complaint.getComplaintId().equalsIgnoreCase(complaintId)) {
                complaint.setAssignedStaffId(staffName);
                complaint.setStatus("Assigned");
                return true;
            }
        }
        return false;
    }

    public static boolean updateComplaintStatus(String complaintId, String newStatus, String note) {
        for (Complaint complaint : complaints) {
            if (complaint.getComplaintId().equalsIgnoreCase(complaintId)) {
                complaint.setStatus(newStatus);
                complaint.setResolutionNote(note);
                return true;
            }
        }
        return false;
    }

    public static List<Complaint> getAssignedComplaints() {
        return complaints.stream()
                .filter(c -> c.getAssignedStaffId() != null && !c.getAssignedStaffId().trim().isEmpty())
                .collect(Collectors.toList());
    }
}