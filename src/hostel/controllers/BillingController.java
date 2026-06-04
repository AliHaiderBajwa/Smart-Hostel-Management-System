package hostel.controllers;

import hostel.domain.FinancialStatement;
import hostel.domain.LeaveRequest;
import hostel.domain.MessBill;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BillingController {

    // TODO: Replace with BillingDAO and real attendance data
    private static final List<MessBill> generatedBills = new ArrayList<>();
    private static int billCounter = 0;

    // TODO: Replace with BillingDAO and real attendance data
    public static FinancialStatement generateStatement(String studentId, String month, String year) {
        double baseMessCharge = 8000.0;
        
        // Calculate leave deductions based on approved leaves
        // Use a dummy 300.0 deduction if none to match the initial prompt requirement
        List<LeaveRequest> leaves = LeaveController.getLeavesByStudent(studentId);
        int totalLeaveDays = leaves.stream()
                .filter(l -> "Approved".equals(l.getStatus())) 
                .mapToInt(LeaveRequest::getDuration)
                .sum();
        
        double leaveDeduction = (totalLeaveDays > 0) ? (totalLeaveDays * 100.0) : 300.0; 
        
        double attendanceDeduction = 500.0; // Dummy
        double penalties = 500.0; // Dummy, should read from PenaltyController
        
        String status = "UNPAID"; // Dummy
        
        return new FinancialStatement(studentId, month, year, baseMessCharge, attendanceDeduction, leaveDeduction, penalties, status);
    }
    
    // TODO: Replace with BillingDAO call
    public static double getOutstandingAmount(String studentId) {
        // Return hardcoded 7700 for now as per prompt
        return 7700.0;
    }

    public static List<MessBill> generateAllBills(String month, String year) {
        generatedBills.removeIf(b -> b.getMonth().equalsIgnoreCase(month) && b.getYear().equalsIgnoreCase(year));

        List<String[]> dummyStudents = List.of(
                new String[] {"student1", "Ali Raza"},
                new String[] {"student2", "Hina Tariq"},
                new String[] {"student3", "Usman Khan"},
                new String[] {"student4", "Sara Ahmed"},
                new String[] {"student5", "Bilal Zafar"}
        );

        for (String[] s : dummyStudents) {
            billCounter++;
            String billId = "BILL-" + String.format("%03d", billCounter);
            String studentId = s[0];
            String studentName = s[1];

            double base = 8000.0;
            double leaveDeduction = LeaveController.getLeavesByStudent(studentId).stream()
                    .filter(l -> "Approved".equalsIgnoreCase(l.getStatus()))
                    .mapToInt(LeaveRequest::getDuration)
                    .sum() * 100.0;
            double penalties = 500.0; // Dummy penalty aggregation.
            generatedBills.add(new MessBill(billId, studentId, studentName, month, year, base, leaveDeduction, penalties, "Unpaid"));
        }

        return getBillsForPeriod(month, year);
    }

    public static List<MessBill> getBillsForPeriod(String month, String year) {
        return generatedBills.stream()
                .filter(b -> b.getMonth().equalsIgnoreCase(month) && b.getYear().equalsIgnoreCase(year))
                .collect(Collectors.toList());
    }

    public static boolean markBillPaid(String billId) {
        for (MessBill bill : generatedBills) {
            if (bill.getBillId().equalsIgnoreCase(billId)) {
                bill.setStatus("Paid");
                return true;
            }
        }
        return false;
    }

    public static int getBillsGeneratedCount() {
        return generatedBills.size();
    }

    public static int getPendingPaymentsCount() {
        return (int) generatedBills.stream().filter(b -> "Unpaid".equalsIgnoreCase(b.getStatus())).count();
    }

    public static double getTotalRevenue() {
        return generatedBills.stream().mapToDouble(MessBill::getTotal).sum();
    }
}