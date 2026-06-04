package hostel.domain;

public class LeaveRequest {

    private String leaveId;
    private String studentId;
    private String startDate;
    private String endDate;
    private int duration;
    private String reason;
    private String details;
    private String status;

    public LeaveRequest(String studentId, String startDate, String endDate, int duration, String reason, String details) {
        this.studentId = studentId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.reason = reason;
        this.details = details;
        this.status = "Pending";
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}