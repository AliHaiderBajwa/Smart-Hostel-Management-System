package hostel.domain;

public class RoomChangeRequest {

    private String requestId;
    private String studentId;
    private String preferredBlock;
    private String roomType;
    private String reason;
    private String status;
    private String requestDate;

    public RoomChangeRequest(String studentId, String preferredBlock, String roomType, String reason) {
        this.studentId = studentId;
        this.preferredBlock = preferredBlock;
        this.roomType = roomType;
        this.reason = reason;
        this.status = "Pending";
        this.requestDate = java.time.LocalDate.now().toString();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPreferredBlock() {
        return preferredBlock;
    }

    public void setPreferredBlock(String preferredBlock) {
        this.preferredBlock = preferredBlock;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}