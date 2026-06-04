package hostel.ui;

import hostel.controllers.LeaveController;
import hostel.domain.LeaveRequest;
import hostel.domain.User;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RequestLeaveView {

    private final Stage stage;
    private final User user;
    private final Scene scene;
    private TableView<LeaveRequest> leaveTable;
    private final ObservableList<LeaveRequest> leaveData = FXCollections.observableArrayList();

    public RequestLeaveView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.scene = createScene();
        loadLeaveData();
    }

    public Scene getScene() {
        return this.scene;
    }

    private void loadLeaveData() {
        leaveData.setAll(LeaveController.getLeavesByStudent(user.getUsername()));
    }

    private Scene createScene() {
        BorderPane root = new BorderPane();
        root.setLeft(createSidebar());

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
                System.out.println("Already on Request Leave view.");
            } else if (text.contains("Room Change")) {
                 System.out.println("Navigation to Room Change not implemented yet.");
            } else if (text.contains("Financial Statement")) {
                 System.out.println("Navigation to Financial Statement not implemented yet.");
            } else if (text.contains("Logout")) {
                new Main().start(stage);
            }
        });
        return button;
    }

    private HBox createTopBar() {
        Button backButton = new Button("← Dashboard");
        backButton.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");
        backButton.setOnAction(e -> stage.setScene(new StudentDashboard(stage, user).getScene()));

        Label title = new Label("Request Hostel Leave");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        HBox topBar = new HBox(15, backButton, title);
        topBar.setPadding(new Insets(20));
        topBar.setPrefHeight(60);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #f5f7fa;");
        return topBar;
    }

    private VBox createCenterContent() {
        Label title = new Label("Request Hostel Leave");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");
        Label subtitle = new Label("Submit a leave request for manager approval.");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #888888;");
        VBox titleBox = new VBox(5, title, subtitle);
        titleBox.setAlignment(Pos.CENTER);

        VBox formCard = createFormCard();
        
        Label tableTitle = new Label("My Leave Requests");
        tableTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        leaveTable = createLeaveTable();

        VBox centerLayout = new VBox(20, titleBox, formCard, tableTitle, leaveTable);
        centerLayout.setPadding(new Insets(30));
        centerLayout.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(leaveTable, Priority.ALWAYS);

        return centerLayout;
    }

    private VBox createFormCard() {
        String labelStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #333333;";
        String fieldStyle = "-fx-background-color: #f4f4f4; -fx-border-width: 0; -fx-background-radius: 8px; -fx-padding: 10px;";

        Label startLabel = new Label("Start Date");
        startLabel.setStyle(labelStyle);
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setMaxWidth(Double.MAX_VALUE);
        startDatePicker.setStyle(fieldStyle);

        Label endLabel = new Label("End Date");
        endLabel.setStyle(labelStyle);
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setMaxWidth(Double.MAX_VALUE);
        endDatePicker.setStyle(fieldStyle);

        Label reasonLabel = new Label("Reason");
        reasonLabel.setStyle(labelStyle);
        ComboBox<String> reasonBox = new ComboBox<>();
        reasonBox.getItems().addAll("Medical", "Family Emergency", "Personal", "Travel", "Other");
        reasonBox.setPromptText("Select reason");
        reasonBox.setMaxWidth(Double.MAX_VALUE);
        reasonBox.setStyle(fieldStyle);

        Label detailsLabel = new Label("Additional Details (Optional)");
        detailsLabel.setStyle(labelStyle);
        TextArea detailsArea = new TextArea();
        detailsArea.setPromptText("Any additional information...");
        detailsArea.setWrapText(true);
        detailsArea.setPrefRowCount(3);
        detailsArea.setStyle(fieldStyle);

        Label policyLabel = new Label("ℹ Maximum 10 leave days allowed per semester. Approved leaves reduce your mess bill.");
        policyLabel.setStyle("-fx-text-fill: #0d47a1; -fx-font-size: 12px;");
        VBox policyBox = new VBox(policyLabel);
        policyBox.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #90caf9; -fx-border-width: 1px; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-padding: 10px;");

        Label resultLabel = new Label();
        resultLabel.setWrapText(true);
        resultLabel.setAlignment(Pos.CENTER);

        Button submitBtn = new Button("Submit Request");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setPrefHeight(45);
        submitBtn.setStyle("-fx-background-color: #0d1b4b; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8px;");
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle("-fx-background-color: #1a3c8f; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8px;"));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle("-fx-background-color: #0d1b4b; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8px;"));

        submitBtn.setOnAction(e -> {
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();
            String reason = reasonBox.getValue();
            String details = detailsArea.getText();

            if (start == null || end == null || reason == null) {
                resultLabel.setText("Start Date, End Date, and Reason are mandatory fields.");
                resultLabel.setStyle("-fx-text-fill: #e74c3c;");
            } else if (end.isBefore(start)) {
                resultLabel.setText("End Date cannot be before Start Date.");
                resultLabel.setStyle("-fx-text-fill: #e74c3c;");
            } else {
                long daysBetween = ChronoUnit.DAYS.between(start, end) + 1; // Inclusive duration
                LeaveRequest newLeave = LeaveController.submitLeaveRequest(user.getUsername(), start.toString(), end.toString(), reason, details, (int)daysBetween);
                resultLabel.setText("Leave request submitted for " + daysBetween + " days. Pending approval.");
                resultLabel.setStyle("-fx-text-fill: #2e7d32;");

                loadLeaveData(); // Refresh table

                startDatePicker.setValue(null);
                endDatePicker.setValue(null);
                reasonBox.setValue(null);
                detailsArea.clear();
            }
        });

        VBox formFields = new VBox(15,
            new VBox(5, startLabel, startDatePicker),
            new VBox(5, endLabel, endDatePicker),
            new VBox(5, reasonLabel, reasonBox),
            new VBox(5, detailsLabel, detailsArea),
            policyBox,
            submitBtn,
            resultLabel
        );
        formFields.setAlignment(Pos.CENTER_LEFT);

        VBox formCard = new VBox(formFields);
        formCard.setPadding(new Insets(30));
        formCard.setMaxWidth(600);
        formCard.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-border-radius: 12px;");

        return formCard;
    }

    private TableView<LeaveRequest> createLeaveTable() {
        TableView<LeaveRequest> table = new TableView<>(leaveData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        table.setRowFactory(tv -> new TableRow<LeaveRequest>() {
            @Override
            protected void updateItem(LeaveRequest item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (getIndex() % 2 == 1) {
                    setStyle("-fx-background-color: #f9f9f9;");
                } else {
                    setStyle("-fx-background-color: white;");
                }
            }
        });

        TableColumn<LeaveRequest, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("leaveId"));

        TableColumn<LeaveRequest, String> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<LeaveRequest, String> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        TableColumn<LeaveRequest, Integer> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));

        TableColumn<LeaveRequest, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));

        TableColumn<LeaveRequest, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(column -> new TableCell<LeaveRequest, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Pending": setTextFill(Color.ORANGE); break;
                        case "Approved": setTextFill(Color.GREEN); break;
                        case "Rejected": setTextFill(Color.RED); break;
                        default: setTextFill(Color.BLACK); break;
                    }
                }
            }
        });

        table.getColumns().addAll(idCol, startCol, endCol, durationCol, reasonCol, statusCol);
        return table;
    }
}