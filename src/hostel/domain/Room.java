package hostel.domain;

public class Room {

	private String roomId;
	private String block;
	private int floor;
	private String type;
	private int capacity;
	private int occupied;
	private String gender;
	private String status;

	public Room(String roomId, int occupied, String type, String status) {
		this.roomId = roomId;
		this.occupied = occupied;
		this.type = type;
		this.status = status;
		this.block = parseBlock(roomId);
		this.floor = parseFloor(roomId);
		this.capacity = inferCapacity(type);
		this.gender = inferGender(this.block);
	}

	public Room(String roomId, String block, int floor, String type, int capacity, int occupied, String gender, String status) {
		this.roomId = roomId;
		this.block = block;
		this.floor = floor;
		this.type = type;
		this.capacity = capacity;
		this.occupied = occupied;
		this.gender = gender;
		this.status = status;
	}

	private String parseBlock(String id) {
		if (id == null || !id.startsWith("Block ")) return "A";
		String[] parts = id.split(" ");
		return parts.length >= 2 ? parts[1] : "A";
	}

	private int parseFloor(String id) {
		if (id == null || !id.contains("-")) return 1;
		String num = id.substring(id.indexOf('-') + 1).trim();
		return num.isEmpty() ? 1 : Character.getNumericValue(num.charAt(0));
	}

	private int inferCapacity(String roomType) {
		if ("Single".equalsIgnoreCase(roomType)) return 1;
		if ("Double".equalsIgnoreCase(roomType)) return 2;
		if ("Triple".equalsIgnoreCase(roomType)) return 3;
		return 4;
	}

	private String inferGender(String roomBlock) {
		return ("A".equalsIgnoreCase(roomBlock) || "C".equalsIgnoreCase(roomBlock)) ? "Male" : "Female";
	}

	public String getRoomId() {
		return roomId;
	}

	public String getRoomNumber() {
		return roomId;
	}

	public String getBlock() {
		return block;
	}

	public int getFloor() {
		return floor;
	}

	public String getType() {
		return type;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getOccupied() {
		return occupied;
	}

	public int getOccupancy() {
		return occupied;
	}

	public int getAvailableSpots() {
		return Math.max(0, capacity - occupied);
	}

	public String getGender() {
		return gender;
	}

	public String getStatus() {
		return status;
	}

	public void setOccupied(int occupied) {
		this.occupied = occupied;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return roomId + " | " + type + " | " + status;
	}

}
