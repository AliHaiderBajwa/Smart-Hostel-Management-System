package hostel.ui;

import hostel.controllers.ComplaintController;
import hostel.domain.Complaint;
import hostel.domain.User;
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

public class SubmitComplaintView {

    private final Stage stage;
    private final User user;
    private final Scene scene;
    private TableView<Complaint> complaintTable;
    private final ObservableList<Complaint> complaintData = FXCollections.observableArrayList();

    public SubmitComplaintView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.scene = createScene();
        loadComplaintData();
    }

    public Scene getScene() {
        return this.scene;
    }

    private void loadComplaintData() {
        complaintData.setAll(ComplaintController.getComplaintsByStudent(user.getUsername()));
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
        // This structure is identical to the one in StudentDashboard
        // In a real app, this would be a shared component.
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
                // Already on this view, do nothing or refresh
                System.out.println("Already on Submit Complaint view.");
            } else if (text.contains("Logout")) {
                new Main().start(stage);
            } else {
                System.out.println("Navigation for '" + text + "' is not implemented yet.");
            }
        });
        return button;
    }

    private HBox createTopBar() {
        Button backButton = new Button("← Back to Dashboard");
        backButton.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");
        backButton.setOnAction(e -> stage.setScene(new StudentDashboard(stage, user).getScene()));

        Label title = new Label("Submit a Complaint");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");
        
        HBox topBar = new HBox(15, backButton, title);
        topBar.setPadding(new Insets(20));
        topBar.setPrefHeight(60);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #f5f7fa;");
        return topBar;
    }

    private VBox createCenterContent() {
        VBox formCard = createFormCard();
        
        Label tableTitle = new Label("My Complaints");
        tableTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        complaintTable = createComplaintTable();

        VBox centerLayout = new VBox(20, formCard, tableTitle, complaintTable);
        centerLayout.setPadding(new Insets(30));
        centerLayout.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(complaintTable, Priority.ALWAYS);

        return centerLayout;
    }

    private VBox createFormCard() {
        String labelStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #333333;";
        String fieldStyle = "-fx-background-color: #f4f4f4; -fx-border-width: 0; -fx-background-radius: 8px; -fx-padding: 10px;";

        Label typeLabel = new Label("Complaint Type");
        typeLabel.setStyle(labelStyle);
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Maintenance", "Electricity", "Cleanliness", "Plumbing", "Other");
        typeBox.setPromptText("Select complaint type");
        typeBox.setMaxWidth(Double.MAX_VALUE);
        typeBox.setStyle(fieldStyle);

        Label descLabel = new Label("Description");
        descLabel.setStyle(labelStyle);
        TextArea descArea = new TextArea();
        descArea.setPromptText("Describe your complaint in detail...");
        descArea.setWrapText(true);
        descArea.setPrefRowCount(4);
        descArea.setStyle(fieldStyle);

        Label urgencyLabel = new Label("Urgency Level");
        urgencyLabel.setStyle(labelStyle);
        ComboBox<String> urgencyBox = new ComboBox<>();
        urgencyBox.getItems().addAll("Low", "Medium", "High");
        urgencyBox.setPromptText("Select urgency level");
        urgencyBox.setMaxWidth(Double.MAX_VALUE);
        urgencyBox.setStyle(fieldStyle);

        Label roomLabel = new Label("Room Number");
        roomLabel.setStyle(labelStyle);
        TextField roomField = new TextField("Block A - 204");
        roomField.setPromptText("e.g. Block A - 204");
        roomField.setStyle(fieldStyle);

        Label resultLabel = new Label();
        resultLabel.setWrapText(true);
        resultLabel.setAlignment(Pos.CENTER);

        Button submitBtn = new Button("Submit Complaint");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setPrefHeight(45);
        submitBtn.setStyle("-fx-background-color: #0d1b4b; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8px;");
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle("-fx-background-color: #1a3c8f; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8px;"));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle("-fx-background-color: #0d1b4b; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8px;"));

        submitBtn.setOnAction(e -> {
            String type = typeBox.getValue();
            String description = descArea.getText();
            String urgency = urgencyBox.getValue();
            String roomNumber = roomField.getText();

            if (type == null || description.trim().isEmpty() || urgency == null || roomNumber.trim().isEmpty()) {
                resultLabel.setText("All fields are required. Please fill them out.");
                resultLabel.setStyle("-fx-text-fill: #e74c3c;");
            } else {
                Complaint newComplaint = ComplaintController.submitComplaint(type, description, urgency, roomNumber, user.getUsername());
                resultLabel.setText("Complaint submitted successfully! Your Complaint ID is: " + newComplaint.getComplaintId());
                resultLabel.setStyle("-fx-text-fill: #2e7d32;");
                loadComplaintData(); // Refresh table
                typeBox.setValue(null);
                descArea.clear();
                urgencyBox.setValue(null);
            }
        });

        VBox formFields = new VBox(15,
            new VBox(5, typeLabel, typeBox),
            new VBox(5, descLabel, descArea),
            new VBox(5, urgencyLabel, urgencyBox),
            new VBox(5, roomLabel, roomField),
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

    private TableView<Complaint> createComplaintTable() {
        TableView<Complaint> table = new TableView<>(complaintData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        // Basic row coloring for alternating colors inline
        table.setRowFactory(tv -> new TableRow<Complaint>() {
            @Override
            protected void updateItem(Complaint item, boolean empty) {
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

        TableColumn<Complaint, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("complaintId"));

        TableColumn<Complaint, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Complaint, String> urgencyCol = new TableColumn<>("Urgency");
        urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));

        TableColumn<Complaint, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(column -> new TableCell<Complaint, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Submitted": setTextFill(Color.ORANGE); break;
                        case "Assigned": setTextFill(Color.BLUE); break;
                        case "In Progress": setTextFill(Color.PURPLE); break;
                        case "Resolved": setTextFill(Color.GREEN); break;
                        default: setTextFill(Color.BLACK); break;
                    }
                }
            }
        });

        TableColumn<Complaint, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        table.getColumns().addAll(idCol, typeCol, urgencyCol, statusCol, dateCol);
        
        // Removed the external CSS stylesheet injection
        return table;
    }
}