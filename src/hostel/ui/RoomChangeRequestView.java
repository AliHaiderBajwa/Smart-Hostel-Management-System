package hostel.ui;

import hostel.controllers.RoomController;
import hostel.domain.RoomChangeRequest;
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
import javafx.stage.Stage;

public class RoomChangeRequestView {

    private final Stage stage;
    private final User user;
    private final Scene scene;
    private TableView<RoomChangeRequest> requestTable;
    private final ObservableList<RoomChangeRequest> requestData = FXCollections.observableArrayList();
    private Label limitLabel;

    public RoomChangeRequestView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.scene = createScene();
        loadRequestData();
    }

    public Scene getScene() {
        return this.scene;
    }

    private void loadRequestData() {
        requestData.setAll(RoomController.getRoomChangesByStudent(user.getUsername()));
        if (limitLabel != null) {
            long used = RoomController.getUsedRequestCount(user.getUsername());
            limitLabel.setText("You have used " + used + " of 2 allowed room changes this academic year.");
        }
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

    // Sidebar and TopBar methods reuse
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
                System.out.println("Already on Room Change view.");
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

        Label title = new Label("Room Change Request");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        HBox topBar = new HBox(15, backButton, title);
        topBar.setPadding(new Insets(20));
        topBar.setPrefHeight(60);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #f5f7fa;");
        return topBar;
    }

    private VBox createCenterContent() {
        Label title = new Label("Room Change Request");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");
        Label subtitle = new Label("Submit a request to change your hostel room.");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #888888;");
        VBox titleBox = new VBox(5, title, subtitle);
        titleBox.setAlignment(Pos.CENTER);

        // Info Cards
        VBox currentRoomCard = new VBox(5);
        currentRoomCard.setStyle("-fx-background-color: #e8f4fd; -fx-background-radius: 8px; -fx-padding: 15px;");
        Label roomInfo = new Label("Current Room: Block A - 204");
        roomInfo.setStyle("-fx-font-weight: bold;");
        Label occupancy = new Label("Occupancy: 2/4");
        // TODO: Load from StudentDAO
        currentRoomCard.getChildren().addAll(roomInfo, occupancy);

        VBox policyCard = new VBox();
        policyCard.setStyle("-fx-background-color: #fff8e1; -fx-border-color: transparent transparent transparent orange; -fx-border-width: 0 0 0 4px; -fx-padding: 15px; -fx-background-radius: 0 8px 8px 0;");
        limitLabel = new Label("You have used 0 of 2 allowed room changes this academic year.");
        limitLabel.setStyle("-fx-text-fill: #666666;");
        policyCard.getChildren().add(limitLabel);

        VBox infoContainer = new VBox(10, currentRoomCard, policyCard);
        infoContainer.setMaxWidth(600);

        VBox formCard = createFormCard();
        
        Label tableTitle = new Label("My Requests");
        tableTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        requestTable = createRequestTable();

        VBox centerLayout = new VBox(20, titleBox, infoContainer, formCard, tableTitle, requestTable);
        centerLayout.setPadding(new Insets(30));
        centerLayout.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(requestTable, Priority.ALWAYS);

        return centerLayout;
    }

    private VBox createFormCard() {
        String labelStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #333333;";
        String fieldStyle = "-fx-background-color: #f4f4f4; -fx-border-width: 0; -fx-background-radius: 8px; -fx-padding: 10px;";

        Label blockLabel = new Label("Preferred Block");
        blockLabel.setStyle(labelStyle);
        ComboBox<String> blockBox = new ComboBox<>();
        blockBox.getItems().addAll("Block A", "Block B", "Block C", "Block D", "No Preference");
        blockBox.setMaxWidth(Double.MAX_VALUE);
        blockBox.setStyle(fieldStyle);

        Label typeLabel = new Label("Room Type");
        typeLabel.setStyle(labelStyle);
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Single", "Double", "Triple", "Quad");
        typeBox.setMaxWidth(Double.MAX_VALUE);
        typeBox.setStyle(fieldStyle);

        Label reasonLabel = new Label("Reason for Change");
        reasonLabel.setStyle(labelStyle);
        TextArea reasonArea = new TextArea();
        reasonArea.setPromptText("Enter a valid reason...");
        reasonArea.setPrefRowCount(3);
        reasonArea.setStyle(fieldStyle);

        CheckBox policyCheck = new CheckBox("I understand that room changes are subject to availability and policy approval");

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
            String block = blockBox.getValue();
            String type = typeBox.getValue();
            String reason = reasonArea.getText();

            if (block == null || type == null || reason.trim().isEmpty()) {
                resultLabel.setText("All fields are required.");
                resultLabel.setStyle("-fx-text-fill: #e74c3c;");
            } else if (!policyCheck.isSelected()) {
                resultLabel.setText("You must accept the change policy.");
                resultLabel.setStyle("-fx-text-fill: #e74c3c;");
            } else {
                try {
                    RoomController.submitRoomChangeRequest(user.getUsername(), block, type, reason);
                    resultLabel.setText("Room change requested successfully.");
                    resultLabel.setStyle("-fx-text-fill: #2e7d32;");

                    loadRequestData(); // Refresh UI with current usage limit

                    blockBox.setValue(null);
                    typeBox.setValue(null);
                    reasonArea.clear();
                    policyCheck.setSelected(false);
                } catch (Exception ex) {
                     resultLabel.setText(ex.getMessage());
                     resultLabel.setStyle("-fx-text-fill: #e74c3c;");
                }
            }
        });

        VBox formFields = new VBox(15,
            new VBox(5, blockLabel, blockBox),
            new VBox(5, typeLabel, typeBox),
            new VBox(5, reasonLabel, reasonArea),
            policyCheck,
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

    private TableView<RoomChangeRequest> createRequestTable() {
        TableView<RoomChangeRequest> table = new TableView<>(requestData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        table.setRowFactory(tv -> new TableRow<RoomChangeRequest>() {
            @Override
            protected void updateItem(RoomChangeRequest item, boolean empty) {
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

        TableColumn<RoomChangeRequest, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("requestId"));

        TableColumn<RoomChangeRequest, String> blockCol = new TableColumn<>("Block");
        blockCol.setCellValueFactory(new PropertyValueFactory<>("preferredBlock"));

        TableColumn<RoomChangeRequest, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        TableColumn<RoomChangeRequest, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));

        TableColumn<RoomChangeRequest, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<RoomChangeRequest, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));

        table.getColumns().addAll(idCol, blockCol, typeCol, reasonCol, statusCol, dateCol);
        return table;
    }
}