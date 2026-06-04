package hostel.ui;

import hostel.controllers.PolicyController;
import hostel.domain.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManagePoliciesView {

    private final Stage stage;
    private final User user;
    private final Scene scene;

    public ManagePoliciesView(Stage stage, User user) {
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
        Label title = new Label("Manage Hostel & Mess Policies");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a0a3b;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        Label title = new Label("Manage Hostel & Mess Policies");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        Label subtitle = new Label("These policies are enforced system-wide across all operations.");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #666;");

        TextField maxLeaveDays = field(PolicyController.getPolicy("maxLeaveDays"));
        TextField maxRoomChanges = field(PolicyController.getPolicy("maxRoomChanges"));
        TextField messCutoffTime = field(PolicyController.getPolicy("messCutoffTime"));
        TextField baseMessCharges = field(PolicyController.getPolicy("baseMessCharges"));
        TextField penaltyRoomChangeExcess = field(PolicyController.getPolicy("penaltyRoomChangeExcess"));
        TextField penaltyPropertyDamage = field(PolicyController.getPolicy("penaltyPropertyDamage"));

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);

        grid.add(card("Max Leave Days", maxLeaveDays), 0, 0);
        grid.add(card("Max Room Changes", maxRoomChanges), 1, 0);
        grid.add(card("Mess Cutoff Time", messCutoffTime), 0, 1);
        grid.add(card("Base Mess Charges", baseMessCharges), 1, 1);
        grid.add(card("Penalty - Room Change Excess", penaltyRoomChangeExcess), 0, 2);
        grid.add(card("Penalty - Property Damage", penaltyPropertyDamage), 1, 2);

        Label msg = new Label();

        Button save = new Button("Save All Policies");
        save.setStyle("-fx-background-color: #1a0a3b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");
        save.setOnAction(e -> {
            PolicyController.updatePolicy("maxLeaveDays", maxLeaveDays.getText());
            PolicyController.updatePolicy("maxRoomChanges", maxRoomChanges.getText());
            PolicyController.updatePolicy("messCutoffTime", messCutoffTime.getText());
            PolicyController.updatePolicy("baseMessCharges", baseMessCharges.getText());
            PolicyController.updatePolicy("penaltyRoomChangeExcess", penaltyRoomChangeExcess.getText());
            PolicyController.updatePolicy("penaltyPropertyDamage", penaltyPropertyDamage.getText());
            msg.setStyle("-fx-text-fill: #2e7d32;");
            msg.setText("Policies updated successfully");
        });

        VBox root = new VBox(14, title, subtitle, grid, save, msg);
        root.setPadding(new Insets(30));
        return root;
    }

    private TextField field(String value) {
        TextField tf = new TextField(value);
        tf.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 8px;");
        return tf;
    }

    private VBox card(String label, TextField field) {
        Label l = new Label(label);
        l.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #333;");
        VBox box = new VBox(8, l, field);
        box.setPadding(new Insets(14));
        box.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        return box;
    }
}