package hostel.ui;

import hostel.controllers.PenaltyController;
import hostel.controllers.StudentController;
import hostel.domain.Penalty;
import hostel.domain.Student;
import hostel.domain.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImposePenaltyView {

    private final Stage stage;
    private final User user;
    private final Scene scene;

    private final Label studentInfo = new Label("Search a student to view details.");
    private final Label messageLabel = new Label();
    private final ObservableList<Penalty> penalties = FXCollections.observableArrayList();
    private final TableView<Penalty> historyTable = new TableView<>(penalties);

    private Student currentStudent;

    public ImposePenaltyView(Stage stage, User user) {
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
        Label subtitle = new Label("Manager Portal");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #ce93d8;");
        VBox logoBox = new VBox(-5, logo, subtitle);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(20, 0, 20, 20));

        VBox nav = new VBox(5,
                createNavButton("🏠 Dashboard"),
                createNavButton("🛏 Allocate Room (UC-04)"),
                createNavButton("📋 Complaints (UC-07)"),
                createNavButton("⚠ Impose Penalty (UC-09)"),
                createNavButton("🚪 Leave Requests (UC-12)"),
                createNavButton("⚙ Manage Policies (UC-14)"),
                createNavButton("🔍 Room Availability (UC-15)")
        );

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button logout = createNavButton("🚪 Logout");

        VBox sidebar = new VBox(20, logoBox, nav, spacer, logout);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #1a0a3b;");
        sidebar.setPadding(new Insets(0, 0, 10, 0));
        return sidebar;
    }

    private Button createNavButton(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        String base = "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
        String hover = "-fx-background-color: #2a1654; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        b.setOnAction(e -> {
            if (text.contains("Dashboard")) {
                stage.setScene(new ManagerDashboard(stage, user).getScene());
            } else if (text.contains("Complaints")) {
                stage.setScene(new AssignComplaintView(stage, user).getScene());
            } else if (text.contains("Impose Penalty")) {
                stage.setScene(new ImposePenaltyView(stage, user).getScene());
            } else if (text.contains("Leave Requests")) {
                stage.setScene(new ProcessLeaveView(stage, user).getScene());
            } else if (text.contains("Logout")) {
                new Main().start(stage);
            } else {
                System.out.println("Navigate to: " + text);
            }
        });
        return b;
    }

    private HBox createTopBar() {
        Label title = new Label("Impose Penalty on Student");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a0a3b;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        Label title = new Label("Impose Penalty on Student");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        TextField studentIdField = new TextField();
        studentIdField.setPromptText("Enter Student ID");
        Button searchBtn = new Button("Search");
        HBox searchBar = new HBox(10, studentIdField, searchBtn);

        VBox studentCard = new VBox(studentInfo);
        studentCard.setPadding(new Insets(12));
        studentCard.setStyle("-fx-background-color: #fafafa; -fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Excessive Room Changes", "Property Damage", "Policy Violation", "Late Payment", "Other");
        typeBox.setPromptText("Violation Type");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount (Rs.)");

        Map<String, String> policyMap = new HashMap<>();
        policyMap.put("Excessive Room Changes", "500");
        policyMap.put("Property Damage", "1000");
        policyMap.put("Policy Violation", "300");
        policyMap.put("Late Payment", "200");

        typeBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (policyMap.containsKey(newVal)) {
                amountField.setText(policyMap.get(newVal));
            }
        });

        TextArea descArea = new TextArea();
        descArea.setPromptText("Description");
        descArea.setPrefRowCount(3);

        Button imposeBtn = new Button("Impose Penalty");
        imposeBtn.setStyle("-fx-background-color: #1a0a3b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");

        VBox formCard = new VBox(10,
                new Label("Violation Type"), typeBox,
                new Label("Amount (Rs.)"), amountField,
                new Label("Description"), descArea,
                imposeBtn,
                messageLabel
        );
        formCard.setPadding(new Insets(15));
        formCard.setMaxWidth(600);
        formCard.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        setupPenaltyHistoryTable();

        searchBtn.setOnAction(e -> {
            currentStudent = StudentController.findStudent(studentIdField.getText());
            if (currentStudent == null) {
                studentInfo.setText("Student not found.");
                penalties.clear();
            } else {
                studentInfo.setText(
                        "Name: " + currentStudent.getName()
                        + "\nRoom: " + currentStudent.getRoomNumber()
                        + "\nCurrent Balance: Rs. " + (int) currentStudent.getCurrentBalance());
                refreshPenaltyHistory(currentStudent.getStudentId());
            }
        });

        imposeBtn.setOnAction(e -> {
            if (currentStudent == null) {
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                messageLabel.setText("Search student first.");
                return;
            }
            if (typeBox.getValue() == null || amountField.getText().trim().isEmpty() || descArea.getText().trim().isEmpty()) {
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                messageLabel.setText("All penalty fields are required.");
                return;
            }
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                PenaltyController.imposePenalty(currentStudent.getStudentId(), typeBox.getValue(), amount, descArea.getText().trim(), user.getUsername());
                messageLabel.setStyle("-fx-text-fill: #2e7d32;");
                messageLabel.setText("Penalty imposed successfully.");
                refreshPenaltyHistory(currentStudent.getStudentId());
                descArea.clear();
            } catch (NumberFormatException ex) {
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                messageLabel.setText("Amount must be numeric.");
            }
        });

        VBox root = new VBox(16, title, searchBar, studentCard, formCard, new Label("Penalty History"), historyTable);
        root.setPadding(new Insets(30));
        VBox.setVgrow(historyTable, Priority.ALWAYS);
        return root;
    }

    private void setupPenaltyHistoryTable() {
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        historyTable.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8px;");

        TableColumn<Penalty, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("penaltyId"));
        TableColumn<Penalty, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Penalty, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Penalty, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Penalty, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        historyTable.getColumns().addAll(idCol, typeCol, amountCol, dateCol, statusCol);
        historyTable.setRowFactory(tv -> new TableRow<Penalty>() {
            @Override
            protected void updateItem(Penalty item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
                }
            }
        });
    }

    private void refreshPenaltyHistory(String studentId) {
        List<Penalty> list = PenaltyController.getPenaltiesByStudent(studentId);
        penalties.setAll(list);
    }
}