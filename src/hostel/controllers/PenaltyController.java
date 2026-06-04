package hostel.controllers;

import hostel.domain.Penalty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PenaltyController {

    // TODO: PenaltyDAO
    private static final List<Penalty> penalties = new ArrayList<>();
    private static int penaltyCounter = 0;

    public static Penalty imposePenalty(String studentId, String type, double amount, String description, String managerId) {
        penaltyCounter++;
        String penaltyId = "PEN-" + String.format("%03d", penaltyCounter);
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        Penalty penalty = new Penalty(penaltyId, studentId, type, amount, description, managerId, date, "Unpaid");
        penalties.add(penalty);
        return penalty;
    }

    public static List<Penalty> getPenaltiesByStudent(String studentId) {
        return penalties.stream()
                .filter(p -> p.getStudentId().equalsIgnoreCase(studentId))
                .collect(Collectors.toList());
    }
}