package hostel.ui;

import hostel.controllers.BillingController;
import hostel.controllers.ComplaintController;
import hostel.controllers.LeaveController;
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
import hostel.domain.Complaint;
import java.util.List;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

/**
 * A modern JavaFX dashboard screen for the Student Portal.
 * This view is displayed after a student successfully logs in.
 */
public class StudentDashboard {

    private final Stage stage;
    private final User user;
    private final Scene scene;

    public StudentDashboard() {
        this.stage = null;
        this.user = null;
        this.scene = createScene();
    }
    
    public StudentDashboard(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.scene = createScene();
    }

    public Scene getScene() {
        return this.scene;
    }

    private Scene createScene() {
        BorderPane root = new BorderPane();
        VBox sidebar = createSidebar();
        root.setLeft(sidebar);

        BorderPane mainContent = new BorderPane();
        mainContent.setStyle("-fx-background-color: #ffffff;");
        mainContent.setTop(createTopBar());
        mainContent.setCenter(createCenterContent());
        root.setCenter(mainContent);

        return new Scene(root, 1100, 680);
    }

    private VBox createSidebar() {
        Label logoLabel = new Label("HMS");
        logoLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label subtitleLabel = new Label("Student Portal");
        subtitleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #4fc3f7;");
        VBox logoBox = new VBox(-5, logoLabel, subtitleLabel);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(20, 0, 20, 20));

        VBox navButtons = new VBox(5);
        navButtons.getChildren().addAll(
            createNavButton("🏠 Dashboard"),
            createNavButton("📋 Submit Complaint"),
            createNavButton("🚪 Request Leave"),
            createNavButton("🔄 Room Change"),
            createNavButton("💰 Financial Statement")
        );

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutButton = createNavButton("🚪 Logout");

        VBox sidebar = new VBox(20, logoBox, navButtons, spacer, logoutButton);
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #0d1b4b;");
        sidebar.setPadding(new Insets(0, 0, 10, 0));
        return sidebar;
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
        String hoverStyle = "-fx-background-color: #1a3c8f; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
        button.setStyle(defaultStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(defaultStyle));

        button.setOnAction(e -> {
            if (text.contains("Dashboard")) {
                stage.setScene(new StudentDashboard(stage, user).getScene());
            } else if (text.contains("Submit Complaint")) {
                stage.setScene(new SubmitComplaintView(stage, user).getScene());
            } else if (text.contains("Request Leave")) {
                stage.setScene(new RequestLeaveView(stage, user).getScene());
            } else if (text.contains("Room Change")) {
                stage.setScene(new RoomChangeRequestView(stage, user).getScene());
            } else if (text.contains("Financial Statement")) {
                stage.setScene(new FinancialStatementView(stage, user).getScene());
            } else if (text.contains("Logout")) {
                new Main().start(stage);
            }
        });
        return button;
    }

    private HBox createTopBar() {
        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label welcomeLabel = new Label("Welcome, " + user.getUsername());
        welcomeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555555;");

        HBox topBar = new HBox(20, title, spacer, welcomeLabel);
        topBar.setPadding(new Insets(20));
        topBar.setPrefHeight(60);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #f5f7fa;");
        return topBar;
    }

    private VBox createCenterContent() {
        long activeComplaints = ComplaintController.getActiveComplaintCount(user.getUsername());
        long pendingLeaves = LeaveController.getPendingLeaveCount(user.getUsername());
        // TODO: Replace with BillingDAO-backed query once persistence is integrated.
        double outstandingAmount = BillingController.getOutstandingAmount(user.getUsername());
        
        HBox cardsBox = new HBox(20);
        cardsBox.getChildren().addAll(
            createSummaryCard("📋 Complaints", activeComplaints + " Active", "#e8f4fd", "#2e75b6"),
            createSummaryCard("🚪 Leave Requests", pendingLeaves + " Pending", "#fff8e1", "#f39c12"),
            createSummaryCard("💰 Outstanding Bill", "Rs. " + (int) outstandingAmount, "#fdecea", "#e74c3c"),
            createSummaryCard("🏠 Room", "Block A - 204", "#e8f9f0", "#27ae60")
        );

        Label activityTitle = new Label("Recent Activity");
        activityTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        VBox activityBox = new VBox(10);
        activityBox.getChildren().add(activityTitle);

        List<Complaint> recentComplaints = ComplaintController.getComplaintsByStudent(user.getUsername());
        if (recentComplaints.isEmpty()) {
            Label activityPlaceholder = new Label("No recent activity to show.");
            activityPlaceholder.setStyle("-fx-text-fill: #888888;");
            activityBox.getChildren().add(activityPlaceholder);
        } else {
            // Show last 3
            recentComplaints.stream().limit(3).forEach(c -> activityBox.getChildren().add(createActivityRow(c)));
        }

        VBox centerContent = new VBox(30, cardsBox, activityBox);
        centerContent.setPadding(new Insets(30));
        return centerContent;
    }

    private HBox createActivityRow(Complaint complaint) {
        Circle statusDot = new Circle(5);
        switch (complaint.getStatus()) {
            case "Submitted": statusDot.setFill(Color.ORANGE); break;
            case "Resolved": statusDot.setFill(Color.GREEN); break;
            default: statusDot.setFill(Color.GRAY); break;
        }

        Label infoLabel = new Label(String.format("Complaint %s (%s) was %s.",
                complaint.getComplaintId(), complaint.getType(), complaint.getStatus()));
        infoLabel.setStyle("-fx-text-fill: #333;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label dateLabel = new Label(complaint.getTimestamp());
        dateLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 12px;");

        HBox activityRow = new HBox(10, statusDot, infoLabel, spacer, dateLabel);
        activityRow.setAlignment(Pos.CENTER_LEFT);
        return activityRow;
    }

    private VBox createSummaryCard(String title, String value, String bgColor, String borderColor) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555555;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        VBox cardContent = new VBox(5, titleLabel, valueLabel);
        cardContent.setPadding(new Insets(20));
        cardContent.setMinWidth(200);
        cardContent.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 10px;" +
            "-fx-border-color: " + borderColor + " transparent transparent transparent;" +
            "-fx-border-width: 4px;" +
            "-fx-border-radius: 10px;" // Simplified radius for border
        );
        HBox.setHgrow(cardContent, Priority.ALWAYS);
        return cardContent;
    }
}