package hostel.domain;

public class FinancialStatement {

    private String studentId;
    private String month;
    private String year;
    private double baseMessCharge;
    private double attendanceDeduction;
    private double leaveDeduction;
    private double penalties;
    private double totalAmount;
    private String status;

    public FinancialStatement(String studentId, String month, String year, double baseMessCharge, double attendanceDeduction, double leaveDeduction, double penalties, String status) {
        this.studentId = studentId;
        this.month = month;
        this.year = year;
        this.baseMessCharge = baseMessCharge;
        this.attendanceDeduction = attendanceDeduction;
        this.leaveDeduction = leaveDeduction;
        this.penalties = penalties;
        this.totalAmount = baseMessCharge - attendanceDeduction - leaveDeduction + penalties;
        this.status = status;
    }

    public String getStudentId() { return studentId; }
    public String getMonth() { return month; }
    public String getYear() { return year; }
    public double getBaseMessCharge() { return baseMessCharge; }
    public double getAttendanceDeduction() { return attendanceDeduction; }
    public double getLeaveDeduction() { return leaveDeduction; }
    public double getPenalties() { return penalties; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
}