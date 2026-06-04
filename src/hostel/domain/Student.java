package hostel.domain;

public class Student {

	private String studentId;
	private String name;
	private String roomNumber;
	private double currentBalance;

	public Student(String studentId, String name, String roomNumber, double currentBalance) {
		this.studentId = studentId;
		this.name = name;
		this.roomNumber = roomNumber;
		this.currentBalance = currentBalance;
	}

	public String getStudentId() {
		return studentId;
	}

	public String getName() {
		return name;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

}
