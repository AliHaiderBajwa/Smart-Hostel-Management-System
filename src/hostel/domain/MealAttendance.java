package hostel.domain;

public class MealAttendance {

	private String date;
	private String mealType;
	private String studentId;

	public MealAttendance(String date, String mealType, String studentId) {
		this.date = date;
		this.mealType = mealType;
		this.studentId = studentId;
	}

	public String getDate() {
		return date;
	}

	public String getMealType() {
		return mealType;
	}

	public String getStudentId() {
		return studentId;
	}

}
