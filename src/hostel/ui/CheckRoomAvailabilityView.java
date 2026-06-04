package hostel.ui;

import hostel.controllers.RoomController;
import hostel.domain.Room;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CheckRoomAvailabilityView {

    private final Stage stage;
    private final User user;
    private final Scene scene;

    private final ObservableList<Room> roomData = FXCollections.observableArrayList();
    private final Label summaryLabel = new Label("Total: 0  Available: 0  Full: 0  Partial: 0");

    public CheckRoomAvailabilityView(Stage stage, User user) {
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
        Label title = new Label("Check Room Availability");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a0a3b;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        Label title = new Label("Check Room Availability");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        ComboBox<String> blockBox = new ComboBox<>();
        blockBox.getItems().addAll("All", "A", "B", "C", "D");
        blockBox.setValue("All");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("All", "Single", "Double", "Triple", "Quad");
        typeBox.setValue("All");

        ComboBox<String> genderBox = new ComboBox<>();
        genderBox.getItems().addAll("All", "Male", "Female");
        genderBox.setValue("All");

        Button filterBtn = new Button("Filter");
        filterBtn.setStyle("-fx-background-color: #1a0a3b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");

        HBox filters = new HBox(10,
                new Label("Block"), blockBox,
                new Label("Type"), typeBox,
                new Label("Gender"), genderBox,
                filterBtn
        );
        filters.setAlignment(Pos.CENTER_LEFT);

        summaryLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #333;");

        TableView<Room> table = createRoomTable();
        applyFilter(blockBox.getValue(), typeBox.getValue(), genderBox.getValue());

        filterBtn.setOnAction(e -> applyFilter(blockBox.getValue(), typeBox.getValue(), genderBox.getValue()));

        VBox root = new VBox(14, title, filters, summaryLabel, table);
        root.setPadding(new Insets(30));
        VBox.setVgrow(table, Priority.ALWAYS);
        return root;
    }

    private TableView<Room> createRoomTable() {
        TableView<Room> table = new TableView<>(roomData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getColumns().add(col("Room ID", "roomId"));
        table.getColumns().add(col("Block", "block"));
        table.getColumns().add(col("Floor", "floor"));
        table.getColumns().add(col("Type", "type"));
        table.getColumns().add(col("Capacity", "capacity"));
        table.getColumns().add(col("Current Occupancy", "occupied"));
        table.getColumns().add(col("Available Spots", "availableSpots"));
        table.getColumns().add(col("Gender", "gender"));

        TableColumn<Room, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<Room, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item);
                if ("Available".equalsIgnoreCase(item)) setTextFill(Color.GREEN);
                else if ("Full".equalsIgnoreCase(item)) setTextFill(Color.RED);
                else setTextFill(Color.ORANGE);
            }
        });
        table.getColumns().add(statusCol);

        table.setRowFactory(tv -> new TableRow<Room>() {
            @Override
            protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setStyle("");
                else setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
            }
        });

        return table;
    }

    private TableColumn<Room, ?> col(String title, String property) {
        TableColumn<Room, Object> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }

    private void applyFilter(String block, String type, String gender) {
        List<Room> all = RoomController.getAvailableRooms();

        List<Room> filtered = all.stream()
                .filter(r -> "All".equals(block) || r.getBlock().equalsIgnoreCase(block))
                .filter(r -> "All".equals(type) || r.getType().equalsIgnoreCase(type))
                .filter(r -> "All".equals(gender) || r.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());

        roomData.setAll(filtered);

        long available = all.stream().filter(r -> "Available".equalsIgnoreCase(r.getStatus())).count();
        long full = all.stream().filter(r -> "Full".equalsIgnoreCase(r.getStatus())).count();
        long partial = all.stream().filter(r -> "Partially Occupied".equalsIgnoreCase(r.getStatus())).count();
        summaryLabel.setText("Total: " + all.size() + "  Available: " + available + "  Full: " + full + "  Partial: " + partial);
    }
}