package hostel.ui;

import hostel.controllers.ComplaintController;
import hostel.domain.Complaint;
import hostel.domain.User;
import java.util.List;
import java.util.stream.Collectors;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AssignComplaintView {

    private final Stage stage;
    private final User user;
    private final Scene scene;
    private final ObservableList<Complaint> submittedComplaints = FXCollections.observableArrayList();

    private final Label detailsLabel = new Label("Select a complaint to view details.");
    private final Label messageLabel = new Label();
    private final ComboBox<String> staffBox = new ComboBox<>();
    private TableView<Complaint> table;

    public AssignComplaintView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.scene = createScene();
        refreshTable();
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
        Label title = new Label("Assign Complaints to Staff");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a0a3b;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        Label title = new Label("Assign Complaints to Staff");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        table = createTable();

        detailsLabel.setWrapText(true);
        VBox detailsCard = new VBox(detailsLabel);
        detailsCard.setPadding(new Insets(15));
        detailsCard.setStyle("-fx-background-color: #fafafa; -fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        staffBox.getItems().addAll(
                "Ali Hassan - Plumbing",
                "Bilal Ahmed - Electrical",
                "Usman Khan - Cleaning",
                "Tariq Mehmood - General"
        );
        staffBox.setPromptText("Assign To");
        staffBox.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 8px;");
        // TODO: Load from StaffDAO

        Button assignBtn = new Button("Assign");
        assignBtn.setStyle("-fx-background-color: #1a0a3b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");
        assignBtn.setOnAction(e -> assignSelected());

        HBox assignPanel = new HBox(10, new Label("Assign To"), staffBox, assignBtn);
        assignPanel.setAlignment(Pos.CENTER_LEFT);

        messageLabel.setStyle("-fx-text-fill: #2e7d32;");

        VBox root = new VBox(16, title, table, detailsCard, assignPanel, messageLabel);
        root.setPadding(new Insets(30));
        VBox.setVgrow(table, Priority.ALWAYS);
        return root;
    }

    private TableView<Complaint> createTable() {
        TableView<Complaint> t = new TableView<>(submittedComplaints);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Complaint, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("complaintId"));
        TableColumn<Complaint, String> studentCol = new TableColumn<>("Student ID");
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<Complaint, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Complaint, String> urgencyCol = new TableColumn<>("Urgency");
        urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));
        TableColumn<Complaint, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Complaint, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        t.getColumns().addAll(idCol, studentCol, typeCol, urgencyCol, descCol, dateCol);
        t.setRowFactory(tv -> new TableRow<Complaint>() {
            @Override
            protected void updateItem(Complaint item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else {
                    setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
                }
            }
        });

        t.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                detailsLabel.setText(
                        "Complaint ID: " + selected.getComplaintId()
                        + "\nStudent ID: " + selected.getStudentId()
                        + "\nType: " + selected.getType()
                        + "\nUrgency: " + selected.getUrgency()
                        + "\nDescription: " + selected.getDescription()
                        + "\nDate: " + selected.getTimestamp()
                        + "\nStatus: " + selected.getStatus()
                );
            }
        });

        return t;
    }

    private void refreshTable() {
        List<Complaint> all = ComplaintController.getAllComplaints();
        submittedComplaints.setAll(all.stream().filter(c -> "Submitted".equalsIgnoreCase(c.getStatus())).collect(Collectors.toList()));
    }

    private void assignSelected() {
        Complaint selected = table.getSelectionModel().getSelectedItem();
        String staff = staffBox.getValue();
        if (selected == null || staff == null) {
            return;
        }
        boolean ok = ComplaintController.assignComplaint(selected.getComplaintId(), staff);
        if (ok) {
            messageLabel.setText("Complaint assigned successfully.");
            refreshTable();
            detailsLabel.setText("Select a complaint to view details.");
        }
    }
}