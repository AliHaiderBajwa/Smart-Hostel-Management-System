package hostel.domain;

public class RoomAllocation {

	private String allocationId;
	private String studentId;
	private String roomId;
	private String allocationDate;

	public RoomAllocation(String allocationId, String studentId, String roomId, String allocationDate) {
		this.allocationId = allocationId;
		this.studentId = studentId;
		this.roomId = roomId;
		this.allocationDate = allocationDate;
	}

	public String getAllocationId() {
		return allocationId;
	}

	public String getStudentId() {
		return studentId;
	}

	public String getRoomId() {
		return roomId;
	}

	public String getAllocationDate() {
		return allocationDate;
	}

}
