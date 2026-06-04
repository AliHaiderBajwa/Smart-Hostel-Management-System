package hostel.ui;

import hostel.controllers.BillingController;
import hostel.domain.FinancialStatement;
import hostel.domain.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FinancialStatementView {

	private final Stage stage;
	private final User user;
	private final Scene scene;

	private final Label periodLabel = new Label("Month-Year: -");
	private final Label leaveDeductionLabel = new Label("-Rs. 300");
	private final Label totalLabel = new Label("Rs. 7700");
	private final Label statusBadge = new Label("UNPAID");
	private final VBox statementCard = new VBox(12);

	public FinancialStatementView(Stage stage, User user) {
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
		Label subtitle = new Label("Student Portal");
		subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #4fc3f7;");
		VBox logoBox = new VBox(-5, logo, subtitle);
		logoBox.setAlignment(Pos.CENTER_LEFT);
		logoBox.setPadding(new Insets(20, 0, 20, 20));

		VBox nav = new VBox(5,
			createNavButton("🏠 Dashboard"),
			createNavButton("📋 Submit Complaint"),
			createNavButton("🚪 Request Leave"),
			createNavButton("🔄 Room Change"),
			createNavButton("💰 Financial Statement")
		);

		VBox spacer = new VBox();
		VBox.setVgrow(spacer, Priority.ALWAYS);
		Button logout = createNavButton("🚪 Logout");

		VBox sidebar = new VBox(20, logoBox, nav, spacer, logout);
		sidebar.setPrefWidth(200);
		sidebar.setStyle("-fx-background-color: #0d1b4b;");
		sidebar.setPadding(new Insets(0, 0, 10, 0));
		return sidebar;
	}

	private Button createNavButton(String text) {
		Button b = new Button(text);
		b.setMaxWidth(Double.MAX_VALUE);
		b.setAlignment(Pos.CENTER_LEFT);
		String base = "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
		String hover = "-fx-background-color: #1a3c8f; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
		b.setStyle(base);
		b.setOnMouseEntered(e -> b.setStyle(hover));
		b.setOnMouseExited(e -> b.setStyle(base));

		b.setOnAction(e -> {
			if (text.contains("Dashboard")) {
				stage.setScene(new StudentDashboard(stage, user).getScene());
			} else if (text.contains("Submit Complaint")) {
				stage.setScene(new SubmitComplaintView(stage, user).getScene());
			} else if (text.contains("Request Leave")) {
				stage.setScene(new RequestLeaveView(stage, user).getScene());
			} else if (text.contains("Room Change")) {
				stage.setScene(new RoomChangeRequestView(stage, user).getScene());
			} else if (text.contains("Financial Statement")) {
				// already here
			} else if (text.contains("Logout")) {
				new Main().start(stage);
			}
		});
		return b;
	}

	private HBox createTopBar() {
		Button back = new Button("← Dashboard");
		back.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");
		back.setOnAction(e -> stage.setScene(new StudentDashboard(stage, user).getScene()));

		Label title = new Label("Financial Statement");
		title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

		HBox top = new HBox(15, back, title);
		top.setPadding(new Insets(20));
		top.setPrefHeight(60);
		top.setAlignment(Pos.CENTER_LEFT);
		top.setStyle("-fx-background-color: #f5f7fa;");
		return top;
	}

	private VBox createMainContent() {
		Label title = new Label("Financial Statement");
		title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

		ComboBox<String> monthBox = new ComboBox<>();
		monthBox.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
		monthBox.setPromptText("Month");
		monthBox.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 8px;");

		ComboBox<String> yearBox = new ComboBox<>();
		yearBox.getItems().addAll("2024", "2025");
		yearBox.setPromptText("Year");
		yearBox.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 8px;");

		Button generate = new Button("Generate Statement");
		generate.setStyle("-fx-background-color: #0d1b4b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");

		HBox selector = new HBox(10, monthBox, yearBox, generate);
		selector.setAlignment(Pos.CENTER_LEFT);

		buildStatementCard();
		statementCard.setVisible(false);
		statementCard.setManaged(false);

		generate.setOnAction(e -> {
			String month = monthBox.getValue();
			String year = yearBox.getValue();
			if (month == null || year == null) {
				return;
			}
			FinancialStatement fs = BillingController.generateStatement(user.getUsername(), month, year);
			periodLabel.setText("Month-Year: " + fs.getMonth() + " " + fs.getYear());
			leaveDeductionLabel.setText("-Rs. " + (int) fs.getLeaveDeduction());
			totalLabel.setText("Rs. " + (int) fs.getTotalAmount());
			if ("PAID".equalsIgnoreCase(fs.getStatus())) {
				statusBadge.setText("PAID");
				statusBadge.setStyle("-fx-background-color: #e8f9f0; -fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-padding: 4 10; -fx-background-radius: 12;");
			} else {
				statusBadge.setText("UNPAID");
				statusBadge.setStyle("-fx-background-color: #fdecea; -fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-padding: 4 10; -fx-background-radius: 12;");
			}
			statementCard.setVisible(true);
			statementCard.setManaged(true);
		});

		VBox wrap = new VBox(18, title, selector, statementCard);
		wrap.setPadding(new Insets(30));
		return wrap;
	}

	private void buildStatementCard() {
		Label studentName = new Label("Student Name: student1");
		Label studentId = new Label("Student ID: " + user.getUsername());
		Label room = new Label("Room: Block A - 204");
		// TODO: Load from StudentDAO
		periodLabel.setStyle("-fx-text-fill: #666;");

		HBox h1 = row("Mess Charges (base)", "Rs. 8000", "#222");
		HBox h2 = row("Attendance Deduction", "-Rs. 500", "#2e7d32");
		HBox h3 = row("Leave Deduction", "-Rs. 300", "#2e7d32");
		leaveDeductionLabel.setStyle("-fx-text-fill: #2e7d32;");
		((Label) h3.getChildren().get(2)).textProperty().bind(leaveDeductionLabel.textProperty());
		HBox h4 = row("Penalties", "+Rs. 500", "#e74c3c");

		totalLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");
		HBox totalRow = new HBox();
		Label t = new Label("Total");
		t.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		Region sp = new Region();
		HBox.setHgrow(sp, Priority.ALWAYS);
		totalRow.getChildren().addAll(t, sp, totalLabel);

		statusBadge.setStyle("-fx-background-color: #fdecea; -fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-padding: 4 10; -fx-background-radius: 12;");
		Button exportBtn = new Button("Print / Export");
		exportBtn.setStyle("-fx-background-color: #0d1b4b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");
		exportBtn.setOnAction(e -> {
			Alert a = new Alert(Alert.AlertType.INFORMATION, "Export feature coming soon", ButtonType.OK);
			a.setHeaderText(null);
			a.showAndWait();
		});

		HBox footer = new HBox(10, statusBadge, exportBtn);
		footer.setAlignment(Pos.CENTER_LEFT);

		statementCard.getChildren().addAll(studentName, studentId, room, periodLabel, h1, h2, h3, h4, totalRow, footer);
		statementCard.setPadding(new Insets(24));
		statementCard.setMaxWidth(700);
		statementCard.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #e0e0e0; -fx-border-width: 1px; -fx-border-radius: 12px;");
	}

	private HBox row(String left, String right, String color) {
		Label l = new Label(left);
		Label r = new Label(right);
		r.setStyle("-fx-text-fill: " + color + ";");
		Region sp = new Region();
		HBox.setHgrow(sp, Priority.ALWAYS);
		HBox row = new HBox(10, l, sp, r);
		row.setAlignment(Pos.CENTER_LEFT);
		return row;
	}

}
