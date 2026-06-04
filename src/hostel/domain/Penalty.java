package hostel.domain;

public class Penalty {

	private String penaltyId;
	private String studentId;
	private String type;
	private double amount;
	private String description;
	private String managerId;
	private String date;
	private String status;

	public Penalty(String penaltyId, String studentId, String type, double amount, String description, String managerId, String date, String status) {
		this.penaltyId = penaltyId;
		this.studentId = studentId;
		this.type = type;
		this.amount = amount;
		this.description = description;
		this.managerId = managerId;
		this.date = date;
		this.status = status;
	}

	public String getPenaltyId() {
		return penaltyId;
	}

	public String getStudentId() {
		return studentId;
	}

	public String getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}

	public String getDescription() {
		return description;
	}

	public String getManagerId() {
		return managerId;
	}

	public String getDate() {
		return date;
	}

	public String getStatus() {
		return status;
	}

}
