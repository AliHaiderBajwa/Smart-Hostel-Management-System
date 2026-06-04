package hostel.ui;

import hostel.controllers.ComplaintController;
import hostel.controllers.LeaveController;
import hostel.domain.Complaint;
import hostel.domain.User;
import java.util.List;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ManagerDashboard {

	private final Stage stage;
	private final User user;
	private final Scene scene;

	 public ManagerDashboard() {
	        this.stage = null;
	        this.user = null;
	        this.scene = createScene();
	    }
	 
	public ManagerDashboard(Stage stage, User user) {
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
			if (text.contains("Logout")) {
				new Main().start(stage);
			} else if (text.contains("Allocate Room")) {
				stage.setScene(new AllocateRoomView(stage, user).getScene());
			} else if (text.contains("Complaints")) {
				stage.setScene(new AssignComplaintView(stage, user).getScene());
			} else if (text.contains("Impose Penalty")) {
				stage.setScene(new ImposePenaltyView(stage, user).getScene());
			} else if (text.contains("Leave Requests")) {
				stage.setScene(new ProcessLeaveView(stage, user).getScene());
			} else if (text.contains("Manage Policies")) {
				stage.setScene(new ManagePoliciesView(stage, user).getScene());
			} else if (text.contains("Room Availability")) {
				stage.setScene(new CheckRoomAvailabilityView(stage, user).getScene());
			} else if (!text.contains("Dashboard")) {
				System.out.println("Navigate to: " + text);
			} else {
				stage.setScene(new ManagerDashboard(stage, user).getScene());
			}
		});
		return b;
	}

	private HBox createTopBar() {
		Label title = new Label("Manager Dashboard");
		title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a0a3b;");
		HBox top = new HBox(title);
		top.setPadding(new Insets(20));
		top.setPrefHeight(60);
		top.setAlignment(Pos.CENTER_LEFT);
		top.setStyle("-fx-background-color: #f5f7fa;");
		return top;
	}

	private VBox createMainContent() {
		long pendingComplaints = ComplaintController.getAllPendingCount();
		long pendingLeaves = LeaveController.getAllPendingCount();

		HBox cards = new HBox(20,
			card("Pending Complaints", String.valueOf(pendingComplaints), "#fdecea", "#e74c3c"),
			card("Pending Leaves", String.valueOf(pendingLeaves), "#fff8e1", "#f39c12"),
			card("Occupied Rooms", "18/24", "#e8f4fd", "#2e75b6"), // TODO: RoomDAO
			card("Total Students", "24", "#e8f9f0", "#27ae60") // TODO: StudentDAO
		);

		Label tableTitle = new Label("Recent Complaints");
		tableTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a0a3b;");

		TableView<Complaint> table = createRecentComplaintsTable();

		VBox wrap = new VBox(22, cards, tableTitle, table);
		wrap.setPadding(new Insets(30));
		VBox.setVgrow(table, Priority.ALWAYS);
		return wrap;
	}

	private VBox card(String title, String value, String bg, String border) {
		Label t = new Label(title);
		t.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
		Label v = new Label(value);
		v.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1a0a3b;");
		VBox box = new VBox(6, t, v);
		box.setPadding(new Insets(20));
		box.setMinWidth(220);
		box.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 10px; -fx-border-color: " + border + " transparent transparent transparent; -fx-border-width: 4px; -fx-border-radius: 10px;");
		return box;
	}

	private TableView<Complaint> createRecentComplaintsTable() {
		TableView<Complaint> table = new TableView<>();
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px;");

		TableColumn<Complaint, String> idCol = new TableColumn<>("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("complaintId"));

		TableColumn<Complaint, String> studentCol = new TableColumn<>("Student");
		studentCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));

		TableColumn<Complaint, String> typeCol = new TableColumn<>("Type");
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

		TableColumn<Complaint, String> urgencyCol = new TableColumn<>("Urgency");
		urgencyCol.setCellValueFactory(new PropertyValueFactory<>("urgency"));

		TableColumn<Complaint, String> statusCol = new TableColumn<>("Status");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		statusCol.setCellFactory(col -> new TableCell<Complaint, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					return;
				}
				setText(item);
				switch (item) {
					case "Submitted": setTextFill(Color.ORANGE); break;
					case "Assigned": setTextFill(Color.BLUE); break;
					case "In Progress": setTextFill(Color.PURPLE); break;
					case "Resolved": setTextFill(Color.GREEN); break;
					default: setTextFill(Color.BLACK);
				}
			}
		});

		table.getColumns().addAll(idCol, studentCol, typeCol, urgencyCol, statusCol);

		List<Complaint> all = ComplaintController.getAllComplaints();
		int from = Math.max(0, all.size() - 5);
		List<Complaint> recent = all.subList(from, all.size());
		table.setItems(FXCollections.observableArrayList(recent));
		table.setRowFactory(tv -> new TableRow<Complaint>() {
			@Override
			protected void updateItem(Complaint item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) setStyle("");
				else setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
			}
		});

		return table;
	}

}
