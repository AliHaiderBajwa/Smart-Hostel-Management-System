package hostel.ui;

import hostel.controllers.AttendanceController;
import hostel.controllers.ReportController;
import hostel.domain.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MessDashboard {

    private final Stage stage;
    private final User user;
    private final Scene scene;

    public MessDashboard() {
        this.stage = null;
        this.user = null;
        this.scene = createScene();
    }
    
    public MessDashboard(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.scene = createScene();
    }

    public Scene getScene() {
        return scene;
    }

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
                createNavButton("Dashboard"),
                createNavButton("Mark Attendance"),
                createNavButton("Food Report")
        );

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button logout = createNavButton("Logout");

        VBox sidebar = new VBox(20, logoBox, nav, spacer, logout);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #0a3b1a;");
        sidebar.setPadding(new Insets(0, 0, 10, 0));
        return sidebar;
    }

    private Button createNavButton(String text) {
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
        Label title = new Label("Mess Dashboard");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0a3b1a;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        Label heading = new Label("Mess Supervisor Overview");
        heading.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0a3b1a;");

        int todaysAttendance = AttendanceController.getTodayAttendanceCount();
        int monthReports = ReportController.getGeneratedReportsCount();
        int activeStudents = Math.max(AttendanceController.getActiveStudentsCount(), 5);

        HBox cards = new HBox(20,
                card("Today's Attendance", String.valueOf(todaysAttendance), "#e8f9f0", "#2e7d32"),
                card("This Month Reports", String.valueOf(monthReports), "#e8f4fd", "#2e75b6"),
                card("Active Students", String.valueOf(activeStudents), "#fff8e1", "#f39c12")
        );

        VBox root = new VBox(20, heading, cards);
        root.setPadding(new Insets(30));
        return root;
    }

    private VBox card(String title, String value, String bg, String border) {
        Label t = new Label(title);
        t.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0a3b1a;");
        VBox box = new VBox(6, t, v);
        box.setPadding(new Insets(18));
        box.setMinWidth(220);
        box.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 10px; -fx-border-color: " + border + " transparent transparent transparent; -fx-border-width: 4px; -fx-border-radius: 10px;");
        return box;
    }
}