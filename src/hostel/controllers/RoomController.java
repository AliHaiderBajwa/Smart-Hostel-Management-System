package hostel.controllers;

import hostel.domain.Room;
import hostel.domain.RoomAllocation;
import hostel.domain.RoomChangeRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomController {

    // TODO: Replace with RoomDAO
    private static final List<RoomChangeRequest> requests = new ArrayList<>();
    // TODO: Replace with RoomDAO
    private static final List<RoomAllocation> allocations = new ArrayList<>();
    private static final List<Room> rooms = new ArrayList<>();
    private static int requestCounter = 0;
    private static int allocationCounter = 0;

    static {
        // Prepopulate with 24 dummy rooms across 4 blocks
        addRoom("Block A - 101", "A", 1, "Single", 1, 1, "Male");
        addRoom("Block A - 102", "A", 1, "Double", 2, 2, "Male");
        addRoom("Block A - 103", "A", 1, "Triple", 3, 3, "Male");
        addRoom("Block A - 104", "A", 1, "Quad", 4, 1, "Male");
        addRoom("Block A - 201", "A", 2, "Single", 1, 0, "Male");
        addRoom("Block A - 202", "A", 2, "Double", 2, 1, "Male");

        addRoom("Block B - 101", "B", 1, "Single", 1, 1, "Female");
        addRoom("Block B - 102", "B", 1, "Double", 2, 2, "Female");
        addRoom("Block B - 103", "B", 1, "Triple", 3, 2, "Female");
        addRoom("Block B - 104", "B", 1, "Quad", 4, 4, "Female");
        addRoom("Block B - 201", "B", 2, "Single", 1, 0, "Female");
        addRoom("Block B - 202", "B", 2, "Double", 2, 1, "Female");

        addRoom("Block C - 101", "C", 1, "Single", 1, 1, "Male");
        addRoom("Block C - 102", "C", 1, "Double", 2, 2, "Male");
        addRoom("Block C - 103", "C", 1, "Triple", 3, 1, "Male");
        addRoom("Block C - 104", "C", 1, "Quad", 4, 4, "Male");
        addRoom("Block C - 201", "C", 2, "Single", 1, 0, "Male");
        addRoom("Block C - 202", "C", 2, "Double", 2, 0, "Male");

        addRoom("Block D - 101", "D", 1, "Single", 1, 1, "Female");
        addRoom("Block D - 102", "D", 1, "Double", 2, 2, "Female");
        addRoom("Block D - 103", "D", 1, "Triple", 3, 3, "Female");
        addRoom("Block D - 104", "D", 1, "Quad", 4, 2, "Female");
        addRoom("Block D - 201", "D", 2, "Single", 1, 0, "Female");
        addRoom("Block D - 202", "D", 2, "Double", 2, 1, "Female");
    }

    private static void addRoom(String roomId, String block, int floor, String type, int capacity, int occupied, String gender) {
        String status;
        if (occupied == 0) status = "Available";
        else if (occupied >= capacity) status = "Full";
        else status = "Partially Occupied";
        rooms.add(new Room(roomId, block, floor, type, capacity, occupied, gender, status));
    }

    public static RoomChangeRequest submitRoomChangeRequest(String studentId, String preferredBlock, String roomType, String reason) throws Exception {
        long currentRequests = requests.stream()
                .filter(r -> r.getStudentId().equalsIgnoreCase(studentId))
                .count();

        if (currentRequests >= 2) {
            throw new Exception("Room change limit exceeded.");
        }

        RoomChangeRequest newRequest = new RoomChangeRequest(studentId, preferredBlock, roomType, reason);
        requestCounter++;
        newRequest.setRequestId("RC-" + String.format("%03d", requestCounter));
        
        requests.add(newRequest);
        System.out.println("New room change request stored: " + newRequest.getRequestId());
        return newRequest;
    }

    public static List<Room> getAvailableRooms() {
        return new ArrayList<>(rooms);
    }

    public static List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public static RoomAllocation allocateRoom(String studentId, String roomId) {
        Room room = rooms.stream().filter(r -> r.getRoomId().equalsIgnoreCase(roomId)).findFirst().orElse(null);
        if (room == null || room.getAvailableSpots() <= 0) {
            return null;
        }

        room.setOccupied(room.getOccupied() + 1);
        if (room.getOccupied() >= room.getCapacity()) {
            room.setStatus("Full");
        } else {
            room.setStatus("Partially Occupied");
        }

        allocationCounter++;
        String allocationId = "ALC-" + String.format("%03d", allocationCounter);
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        RoomAllocation allocation = new RoomAllocation(allocationId, studentId, roomId, date);
        allocations.add(allocation);
        return allocation;
    }

    public static List<RoomChangeRequest> getRoomChangesByStudent(String studentId) {
        return requests.stream()
                .filter(r -> r.getStudentId().equalsIgnoreCase(studentId))
                .collect(Collectors.toList());
    }

    public static long getUsedRequestCount(String studentId) {
        return requests.stream()
                .filter(r -> r.getStudentId().equalsIgnoreCase(studentId))
                .count();
    }
}