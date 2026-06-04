package hostel.ui;

import hostel.controllers.LeaveController;
import hostel.domain.LeaveRequest;
import hostel.domain.User;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ProcessLeaveView {

    private final Stage stage;
    private final User user;
    private final Scene scene;
    private final ObservableList<LeaveRequest> pendingLeaves = FXCollections.observableArrayList();

    private final Label detailsLabel = new Label("Select a leave request to view details.");
    private final Label policyLabel = new Label("Max allowed leave: 10 days. This request: - days.");
    private final Label messageLabel = new Label();
    private TableView<LeaveRequest> table;

    public ProcessLeaveView(Stage stage, User user) {
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
        Label title = new Label("Process Leave Requests");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a0a3b;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        Label title = new Label("Process Leave Requests");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        table = createTable();

        detailsLabel.setWrapText(true);
        VBox detailsCard = new VBox(8, detailsLabel, policyLabel);
        detailsCard.setPadding(new Insets(15));
        detailsCard.setStyle("-fx-background-color: #fafafa; -fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Button approveBtn = new Button("Approve");
        approveBtn.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");
        Button rejectBtn = new Button("Reject");
        rejectBtn.setStyle("-fx-background-color: #c62828; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");

        approveBtn.setOnAction(e -> processSelected("Approved"));
        rejectBtn.setOnAction(e -> processSelected("Rejected"));

        HBox actions = new HBox(10, approveBtn, rejectBtn);
        messageLabel.setStyle("-fx-text-fill: #2e7d32;");

        VBox root = new VBox(16, title, table, detailsCard, actions, messageLabel);
        root.setPadding(new Insets(30));
        VBox.setVgrow(table, Priority.ALWAYS);
        return root;
    }

    private TableView<LeaveRequest> createTable() {
        TableView<LeaveRequest> t = new TableView<>(pendingLeaves);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<LeaveRequest, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("leaveId"));
        TableColumn<LeaveRequest, String> studentCol = new TableColumn<>("Student ID");
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<LeaveRequest, String> startCol = new TableColumn<>("Start");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<LeaveRequest, String> endCol = new TableColumn<>("End");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn<LeaveRequest, Integer> durationCol = new TableColumn<>("Duration");
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        TableColumn<LeaveRequest, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));
        TableColumn<LeaveRequest, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<LeaveRequest, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if ("Pending".equalsIgnoreCase(item)) setTextFill(Color.ORANGE);
                    else if ("Approved".equalsIgnoreCase(item)) setTextFill(Color.GREEN);
                    else if ("Rejected".equalsIgnoreCase(item)) setTextFill(Color.RED);
                    else setTextFill(Color.BLACK);
                }
            }
        });

        t.getColumns().addAll(idCol, studentCol, startCol, endCol, durationCol, reasonCol, statusCol);
        t.setRowFactory(tv -> new TableRow<LeaveRequest>() {
            @Override
            protected void updateItem(LeaveRequest item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setStyle("");
                else setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
            }
        });

        t.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                detailsLabel.setText(
                        "Leave ID: " + selected.getLeaveId()
                                + "\nStudent ID: " + selected.getStudentId()
                                + "\nStart: " + selected.getStartDate()
                                + "\nEnd: " + selected.getEndDate()
                                + "\nDuration: " + selected.getDuration() + " days"
                                + "\nReason: " + selected.getReason()
                                + "\nStatus: " + selected.getStatus()
                );
                policyLabel.setText("Max allowed leave: 10 days. This request: " + selected.getDuration() + " days.");
            }
        });

        return t;
    }

    private void refreshTable() {
        List<LeaveRequest> allPending = LeaveController.getAllPendingLeaves();
        pendingLeaves.setAll(allPending);
    }

    private void processSelected(String decision) {
        LeaveRequest selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        boolean ok = LeaveController.processLeave(selected.getLeaveId(), decision);
        if (ok) {
            messageLabel.setText("Leave request " + selected.getLeaveId() + " marked as " + decision + ".");
            refreshTable();
            detailsLabel.setText("Select a leave request to view details.");
            policyLabel.setText("Max allowed leave: 10 days. This request: - days.");
        }
    }
}