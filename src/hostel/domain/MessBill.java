package hostel.domain;

public class MessBill {

	private String billId;
	private String studentId;
	private String studentName;
	private String month;
	private String year;
	private double baseCharges;
	private double leaveDeductions;
	private double penalties;
	private double total;
	private String status;

	public MessBill(String billId, String studentId, String studentName, String month, String year,
			double baseCharges, double leaveDeductions, double penalties, String status) {
		this.billId = billId;
		this.studentId = studentId;
		this.studentName = studentName;
		this.month = month;
		this.year = year;
		this.baseCharges = baseCharges;
		this.leaveDeductions = leaveDeductions;
		this.penalties = penalties;
		this.total = baseCharges - leaveDeductions + penalties;
		this.status = status;
	}

	public String getBillId() { return billId; }
	public String getStudentId() { return studentId; }
	public String getStudentName() { return studentName; }
	public String getMonth() { return month; }
	public String getYear() { return year; }
	public double getBaseCharges() { return baseCharges; }
	public double getLeaveDeductions() { return leaveDeductions; }
	public double getPenalties() { return penalties; }
	public double getTotal() { return total; }
	public String getStatus() { return status; }

	public void setStatus(String status) { this.status = status; }

}
