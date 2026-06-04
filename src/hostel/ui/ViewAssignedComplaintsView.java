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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ViewAssignedComplaintsView {

    private final Stage stage;
    private final User user;
    private final Scene scene;

    private final ObservableList<Complaint> complaintRows = FXCollections.observableArrayList();
    private final Label detailLabel = new Label("Select a complaint to view details.");
    private final Label messageLabel = new Label();
    private TableView<Complaint> table;

    private String currentFilter = "All";

    public ViewAssignedComplaintsView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.scene = createScene();
        refreshTable();
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
        Label subtitle = new Label("Staff Portal");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #ffcc80;");
        VBox logoBox = new VBox(-5, logo, subtitle);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(20, 0, 20, 20));

        VBox nav = new VBox(5,
                navBtn("Dashboard"),
                navBtn("My Complaints")
        );

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button logout = navBtn("Logout");

        VBox sidebar = new VBox(20, logoBox, nav, spacer, logout);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #3b1a0a;");
        sidebar.setPadding(new Insets(0, 0, 10, 0));
        return sidebar;
    }

    private Button navBtn(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        String base = "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
        String hover = "-fx-background-color: #5a2a14; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        b.setOnAction(e -> {
            if ("Dashboard".equals(text)) stage.setScene(new StaffDashboard(stage, user).getScene());
            else if ("My Complaints".equals(text)) stage.setScene(new ViewAssignedComplaintsView(stage, user).getScene());
            else if ("Logout".equals(text)) new Main().start(stage);
        });
        return b;
    }

    private HBox createTopBar() {
        Label title = new Label("My Assigned Complaints");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #3b1a0a;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        Label title = new Label("My Assigned Complaints");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        HBox filters = new HBox(10,
                filterBtn("All"),
                filterBtn("Assigned"),
                filterBtn("In Progress"),
                filterBtn("Resolved")
        );

        table = createTable();

        detailLabel.setWrapText(true);
        VBox detailCard = new VBox(detailLabel);
        detailCard.setPadding(new Insets(12));
        detailCard.setStyle("-fx-background-color: #fafafa; -fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("In Progress", "Resolved");
        statusBox.setPromptText("Update Status");

        TextArea noteArea = new TextArea();
        noteArea.setPromptText("Resolution Note");
        noteArea.setPrefRowCount(2);

        Button updateBtn = new Button("Update");
        updateBtn.setStyle("-fx-background-color: #3b1a0a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");
        updateBtn.setOnAction(e -> {
            Complaint selected = table.getSelectionModel().getSelectedItem();
            String status = statusBox.getValue();
            if (selected == null || status == null) return;
            boolean ok = ComplaintController.updateComplaintStatus(selected.getComplaintId(), status, noteArea.getText().trim());
            if (ok) {
                messageLabel.setStyle("-fx-text-fill: #2e7d32;");
                messageLabel.setText("Complaint status updated.");
                refreshTable();
            }
        });

        VBox updatePanel = new VBox(8, statusBox, noteArea, updateBtn, messageLabel);

        VBox root = new VBox(14, title, filters, table, detailCard, updatePanel);
        root.setPadding(new Insets(30));
        VBox.setVgrow(table, Priority.ALWAYS);
        return root;
    }

    private Button filterBtn(String filter) {
        Button b = new Button(filter);
        b.setOnAction(e -> {
            currentFilter = filter;
            refreshTable();
        });
        return b;
    }

    private TableView<Complaint> createTable() {
        TableView<Complaint> t = new TableView<>(complaintRows);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        t.getColumns().add(col("ID", "complaintId"));
        t.getColumns().add(col("Type", "type"));
        t.getColumns().add(col("Room", "roomNumber"));
        t.getColumns().add(col("Urgency", "urgency"));
        t.getColumns().add(col("Student", "studentId"));
        t.getColumns().add(col("Date", "timestamp"));

        TableColumn<Complaint, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<Complaint, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if ("Assigned".equalsIgnoreCase(item)) setTextFill(Color.BLUE);
                    else if ("In Progress".equalsIgnoreCase(item)) setTextFill(Color.ORANGE);
                    else if ("Resolved".equalsIgnoreCase(item)) setTextFill(Color.GREEN);
                    else setTextFill(Color.BLACK);
                }
            }
        });
        t.getColumns().add(statusCol);

        t.setRowFactory(tv -> new TableRow<Complaint>() {
            @Override
            protected void updateItem(Complaint item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setStyle("");
                else setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
            }
        });

        t.getSelectionModel().selectedItemProperty().addListener((obs, o, selected) -> {
            if (selected != null) {
                detailLabel.setText(
                        "Complaint ID: " + selected.getComplaintId()
                                + "\nType: " + selected.getType()
                                + "\nRoom: " + selected.getRoomNumber()
                                + "\nStudent: " + selected.getStudentId()
                                + "\nDescription: " + selected.getDescription()
                                + "\nAssigned To: " + selected.getAssignedStaffId()
                                + "\nAssignment Date: " + selected.getTimestamp()
                                + "\nResolution Note: " + (selected.getResolutionNote() == null ? "-" : selected.getResolutionNote())
                );
            }
        });

        return t;
    }

    private TableColumn<Complaint, ?> col(String title, String property) {
        TableColumn<Complaint, Object> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }

    private void refreshTable() {
        List<Complaint> allAssigned = ComplaintController.getAssignedComplaints();
        List<Complaint> filtered = allAssigned;
        if (!"All".equals(currentFilter)) {
            filtered = allAssigned.stream().filter(c -> currentFilter.equalsIgnoreCase(c.getStatus())).collect(Collectors.toList());
        }
        complaintRows.setAll(filtered);
    }
}