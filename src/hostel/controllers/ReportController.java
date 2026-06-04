package hostel.controllers;

import hostel.domain.MealAttendance;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReportController {

    private static int generatedReportsCount = 0;

    public static class FoodReportRow {
        private final String date;
        private final int breakfastCount;
        private final int lunchCount;
        private final int dinnerCount;
        private final int total;

        public FoodReportRow(String date, int breakfastCount, int lunchCount, int dinnerCount) {
            this.date = date;
            this.breakfastCount = breakfastCount;
            this.lunchCount = lunchCount;
            this.dinnerCount = dinnerCount;
            this.total = breakfastCount + lunchCount + dinnerCount;
        }

        public String getDate() { return date; }
        public int getBreakfastCount() { return breakfastCount; }
        public int getLunchCount() { return lunchCount; }
        public int getDinnerCount() { return dinnerCount; }
        public int getTotal() { return total; }
    }

    public static class FoodReport {
        private final int totalMealsServed;
        private final double estimatedFoodKg;
        private final double estimatedWasteKg;
        private final double costEstimate;
        private final List<FoodReportRow> rows;

        public FoodReport(int totalMealsServed, double estimatedFoodKg, double estimatedWasteKg, double costEstimate, List<FoodReportRow> rows) {
            this.totalMealsServed = totalMealsServed;
            this.estimatedFoodKg = estimatedFoodKg;
            this.estimatedWasteKg = estimatedWasteKg;
            this.costEstimate = costEstimate;
            this.rows = rows;
        }

        public int getTotalMealsServed() { return totalMealsServed; }
        public double getEstimatedFoodKg() { return estimatedFoodKg; }
        public double getEstimatedWasteKg() { return estimatedWasteKg; }
        public double getCostEstimate() { return costEstimate; }
        public List<FoodReportRow> getRows() { return rows; }
    }

    public static FoodReport generateFoodReport(String startDate, String endDate) {
        generatedReportsCount++;
        List<MealAttendance> records = AttendanceController.getAttendanceRecords();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        Map<String, int[]> byDate = new TreeMap<>();
        for (MealAttendance record : records) {
            LocalDate current = LocalDate.parse(record.getDate());
            if (current.isBefore(start) || current.isAfter(end)) {
                continue;
            }
            int[] mealCounts = byDate.computeIfAbsent(record.getDate(), d -> new int[] {0, 0, 0});
            String meal = record.getMealType();
            if ("Breakfast".equalsIgnoreCase(meal)) mealCounts[0]++;
            else if ("Lunch".equalsIgnoreCase(meal)) mealCounts[1]++;
            else if ("Dinner".equalsIgnoreCase(meal)) mealCounts[2]++;
        }

        List<FoodReportRow> rows = new ArrayList<>();
        int totalMeals = 0;
        for (Map.Entry<String, int[]> entry : byDate.entrySet()) {
            int[] c = entry.getValue();
            FoodReportRow row = new FoodReportRow(entry.getKey(), c[0], c[1], c[2]);
            rows.add(row);
            totalMeals += row.getTotal();
        }

        // Dummy calculations for MVP.
        // TODO: Real data from AttendanceDAO and inventory tracking.
        double foodKg = totalMeals * 0.45;
        double wasteKg = totalMeals * 0.05;
        double cost = totalMeals * 120.0;

        return new FoodReport(totalMeals, foodKg, wasteKg, cost, rows);
    }

    public static int getGeneratedReportsCount() {
        return generatedReportsCount;
    }
}