package hostel.ui;

import hostel.controllers.ReportController;
import hostel.controllers.ReportController.FoodReport;
import hostel.controllers.ReportController.FoodReportRow;
import hostel.domain.User;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FoodConsumptionReportView {

    private final Stage stage;
    private final User user;
    private final Scene scene;

    private final Label totalMealsLabel = new Label("0");
    private final Label foodKgLabel = new Label("0.0");
    private final Label wasteKgLabel = new Label("0.0");
    private final Label costLabel = new Label("0.0");

    private final ObservableList<FoodReportRow> rows = FXCollections.observableArrayList();

    public FoodConsumptionReportView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.scene = createScene();
    }

    public Scene getScene() { return scene; }

    private Scene createScene() {
        BorderPane root = new BorderPane();
        root.setLeft(createSidebar());

        BorderPane content = new BorderPane();
        content.setStyle("-fx-background-color: #ffffff;");
        content.setTop(createTopBar());
        content.setCenter(createMainContent());
        root.setCenter(content);

        return new Scene(root, 1100, 680);
    }

    private VBox createSidebar() {
        Label logo = new Label("HMS");
        logo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label subtitle = new Label("Mess Portal");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #a5d6a7;");
        VBox logoBox = new VBox(-5, logo, subtitle);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(20, 0, 20, 20));

        VBox nav = new VBox(5,
                navBtn("Dashboard"),
                navBtn("Mark Attendance"),
                navBtn("Food Report")
        );

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button logout = navBtn("Logout");

        VBox sidebar = new VBox(20, logoBox, nav, spacer, logout);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #0a3b1a;");
        sidebar.setPadding(new Insets(0, 0, 10, 0));
        return sidebar;
    }

    private Button navBtn(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        String base = "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
        String hover = "-fx-background-color: #145227; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        b.setOnAction(e -> {
            if ("Dashboard".equals(text)) stage.setScene(new MessDashboard(stage, user).getScene());
            else if ("Mark Attendance".equals(text)) stage.setScene(new MarkAttendanceView(stage, user).getScene());
            else if ("Food Report".equals(text)) stage.setScene(new FoodConsumptionReportView(stage, user).getScene());
            else if ("Logout".equals(text)) new Main().start(stage);
        });
        return b;
    }

    private HBox createTopBar() {
        Label title = new Label("Food Consumption Report");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0a3b1a;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        Label title = new Label("Food Consumption Report");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        DatePicker start = new DatePicker(LocalDate.now().minusDays(7));
        DatePicker end = new DatePicker(LocalDate.now());
        Button generate = new Button("Generate");
        generate.setStyle("-fx-background-color: #0a3b1a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");

        HBox filter = new HBox(10, new Label("Start"), start, new Label("End"), end, generate);
        filter.setAlignment(Pos.CENTER_LEFT);

        VBox reportCard = new VBox(8,
                line("Total Meals Served", totalMealsLabel),
                line("Estimated Food Consumed (kg)", foodKgLabel),
                line("Estimated Waste (kg)", wasteKgLabel),
                line("Cost Estimate", costLabel)
        );
        reportCard.setPadding(new Insets(14));
        reportCard.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        // TODO: Real data from AttendanceDAO

        TableView<FoodReportRow> table = new TableView<>(rows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().add(col("Date", "date"));
        table.getColumns().add(col("Breakfast Count", "breakfastCount"));
        table.getColumns().add(col("Lunch Count", "lunchCount"));
        table.getColumns().add(col("Dinner Count", "dinnerCount"));
        table.getColumns().add(col("Total", "total"));
        table.setRowFactory(tv -> new TableRow<FoodReportRow>() {
            @Override
            protected void updateItem(FoodReportRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setStyle("");
                else setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
            }
        });

        generate.setOnAction(e -> {
            FoodReport report = ReportController.generateFoodReport(start.getValue().toString(), end.getValue().toString());
            totalMealsLabel.setText(String.valueOf(report.getTotalMealsServed()));
            foodKgLabel.setText(String.format("%.2f", report.getEstimatedFoodKg()));
            wasteKgLabel.setText(String.format("%.2f", report.getEstimatedWasteKg()));
            costLabel.setText("Rs. " + String.format("%.2f", report.getCostEstimate()));
            rows.setAll(report.getRows());
        });

        VBox root = new VBox(14, title, filter, reportCard, table);
        root.setPadding(new Insets(30));
        VBox.setVgrow(table, Priority.ALWAYS);
        return root;
    }

    private HBox line(String label, Label valueLabel) {
        Label l = new Label(label);
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        HBox row = new HBox(10, l, sp, valueLabel);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private TableColumn<FoodReportRow, ?> col(String title, String property) {
        TableColumn<FoodReportRow, Object> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }
}