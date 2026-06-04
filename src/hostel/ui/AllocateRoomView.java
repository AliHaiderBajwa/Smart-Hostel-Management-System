package hostel.ui;

import hostel.controllers.RoomController;
import hostel.domain.Room;
import hostel.domain.RoomAllocation;
import hostel.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AllocateRoomView {

    private final Stage stage;
    private final User user;
    private final Scene scene;

    private final ObservableList<StudentRow> students = FXCollections.observableArrayList();
    private final ObservableList<Room> rooms = FXCollections.observableArrayList();

    private final Label confirmLabel = new Label("Select a student and room.");
    private final Label messageLabel = new Label();

    private TableView<StudentRow> studentTable;
    private TableView<Room> roomTable;

    public AllocateRoomView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.scene = createScene();
        seedStudents();
        loadRooms();
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
            if (text.contains("Dashboard")) stage.setScene(new ManagerDashboard(stage, user).getScene());
            else if (text.contains("Allocate Room")) stage.setScene(new AllocateRoomView(stage, user).getScene());
            else if (text.contains("Complaints")) stage.setScene(new AssignComplaintView(stage, user).getScene());
            else if (text.contains("Impose Penalty")) stage.setScene(new ImposePenaltyView(stage, user).getScene());
            else if (text.contains("Leave Requests")) stage.setScene(new ProcessLeaveView(stage, user).getScene());
            else if (text.contains("Manage Policies")) stage.setScene(new ManagePoliciesView(stage, user).getScene());
            else if (text.contains("Room Availability")) stage.setScene(new CheckRoomAvailabilityView(stage, user).getScene());
            else if (text.contains("Logout")) new Main().start(stage);
        });
        return b;
    }

    private HBox createTopBar() {
        Label title = new Label("Allocate Hostel Room");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a0a3b;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        Label title = new Label("Allocate Hostel Room");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        studentTable = createStudentTable();
        roomTable = createRoomTable();

        VBox leftPane = new VBox(10, new Label("Unallocated Students"), studentTable);
        VBox rightPane = new VBox(10, new Label("Available Rooms"), roomTable);
        HBox split = new HBox(20, leftPane, rightPane);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        VBox.setVgrow(studentTable, Priority.ALWAYS);
        VBox.setVgrow(roomTable, Priority.ALWAYS);

        VBox confirmCard = new VBox(confirmLabel);
        confirmCard.setPadding(new Insets(12));
        confirmCard.setStyle("-fx-background-color: #fafafa; -fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Button allocateBtn = new Button("Allocate Room");
        allocateBtn.setStyle("-fx-background-color: #1a0a3b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");
        allocateBtn.setOnAction(e -> allocateSelected());

        messageLabel.setStyle("-fx-text-fill: #2e7d32;");

        VBox root = new VBox(16, title, split, confirmCard, allocateBtn, messageLabel);
        root.setPadding(new Insets(30));
        VBox.setVgrow(split, Priority.ALWAYS);
        return root;
    }

    private TableView<StudentRow> createStudentTable() {
        TableView<StudentRow> table = new TableView<>(students);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StudentRow, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<StudentRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<StudentRow, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        TableColumn<StudentRow, String> sessionCol = new TableColumn<>("Session");
        sessionCol.setCellValueFactory(new PropertyValueFactory<>("session"));

        table.getColumns().addAll(idCol, nameCol, genderCol, sessionCol);
        table.setRowFactory(tv -> stripedStudentRows());
        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> updateConfirmation());
        return table;
    }

    private TableView<Room> createRoomTable() {
        TableView<Room> table = new TableView<>(rooms);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Room, String> idCol = new TableColumn<>("Room ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        TableColumn<Room, String> blockCol = new TableColumn<>("Block");
        blockCol.setCellValueFactory(new PropertyValueFactory<>("block"));
        TableColumn<Room, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Room, Integer> capCol = new TableColumn<>("Capacity");
        capCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        TableColumn<Room, Integer> occCol = new TableColumn<>("Occupied");
        occCol.setCellValueFactory(new PropertyValueFactory<>("occupied"));
        TableColumn<Room, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        TableColumn<Room, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, blockCol, typeCol, capCol, occCol, genderCol, statusCol);
        table.setRowFactory(tv -> stripedRoomRows());
        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> updateConfirmation());
        return table;
    }

    private TableRow<StudentRow> stripedStudentRows() {
        return new TableRow<StudentRow>() {
            @Override
            protected void updateItem(StudentRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setStyle("");
                else setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
            }
        };
    }

    private TableRow<Room> stripedRoomRows() {
        return new TableRow<Room>() {
            @Override
            protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setStyle("");
                else setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
            }
        };
    }

    private void seedStudents() {
        students.setAll(new ArrayList<>(List.of(
                new StudentRow("S001", "Ahsan Raza", "Male", "Fall-2024"),
                new StudentRow("S002", "Hina Tariq", "Female", "Fall-2024"),
                new StudentRow("S003", "Kashif Ali", "Male", "Spring-2025"),
                new StudentRow("S004", "Sana Waheed", "Female", "Spring-2025"),
                new StudentRow("S005", "Bilal Zafar", "Male", "Fall-2025")
        )));
        // TODO: StudentDAO
    }

    private void loadRooms() {
        rooms.setAll(RoomController.getAvailableRooms().stream()
                .filter(r -> r.getAvailableSpots() > 0)
                .collect(Collectors.toList()));
    }

    private void updateConfirmation() {
        StudentRow s = studentTable.getSelectionModel().getSelectedItem();
        Room r = roomTable.getSelectionModel().getSelectedItem();
        if (s == null || r == null) {
            confirmLabel.setText("Select a student and room.");
            return;
        }
        confirmLabel.setText("Student: " + s.getId() + " - " + s.getName() + " (" + s.getGender() + ")"
                + "\nRoom: " + r.getRoomId() + " | " + r.getType() + " | " + r.getGender()
                + "\nAvailable Spots: " + r.getAvailableSpots());
    }

    private void allocateSelected() {
        StudentRow s = studentTable.getSelectionModel().getSelectedItem();
        Room r = roomTable.getSelectionModel().getSelectedItem();
        if (s == null || r == null) {
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            messageLabel.setText("Please select both student and room.");
            return;
        }
        if (!s.getGender().equalsIgnoreCase(r.getGender())) {
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            messageLabel.setText("Gender mismatch: selected student and room are not compatible.");
            return;
        }

        RoomAllocation allocation = RoomController.allocateRoom(s.getId(), r.getRoomId());
        if (allocation == null) {
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            messageLabel.setText("Unable to allocate room. Room may be full.");
            return;
        }

        students.remove(s);
        loadRooms();
        confirmLabel.setText("Select a student and room.");
        messageLabel.setStyle("-fx-text-fill: #2e7d32;");
        messageLabel.setText("Room allocated successfully. Allocation ID: " + allocation.getAllocationId());
    }

    public static class StudentRow {
        private final String id;
        private final String name;
        private final String gender;
        private final String session;

        public StudentRow(String id, String name, String gender, String session) {
            this.id = id;
            this.name = name;
            this.gender = gender;
            this.session = session;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getGender() {
            return gender;
        }

        public String getSession() {
            return session;
        }
    }
}
