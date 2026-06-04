package hostel.ui;

import hostel.controllers.AuthController;
import hostel.domain.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Redesigned JavaFX login screen for the Smart Hostel Management System.
 * Features a split-panel layout with modern styling, while keeping original auth logic.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // --- LEFT PANEL ---
        Label iconLabel = new Label("🏠");
        iconLabel.setStyle("-fx-font-size: 72px; -fx-text-fill: white;");

        Label titleLabel = new Label("Smart Hostel");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("Management System");
        subtitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4fc3f7;");

        Label descriptionLabel = new Label("FAST NUCES — Islamabad");
        descriptionLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: white; -fx-opacity: 0.7;");

        VBox titleBox = new VBox(10, iconLabel, titleLabel, subtitleLabel, descriptionLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Circle dot1 = new Circle(4, Color.web("white", 0.5));
        Circle dot2 = new Circle(4, Color.WHITE);
        Circle dot3 = new Circle(4, Color.web("white", 0.5));
        HBox dots = new HBox(8, dot1, dot2, dot3);
        dots.setAlignment(Pos.CENTER);

        VBox leftContent = new VBox(20, titleBox, dots);
        leftContent.setAlignment(Pos.CENTER);
        leftContent.setPadding(new Insets(20));

        VBox leftPanel = new VBox(leftContent);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setStyle("-fx-background-color: #0d1b4b;");
        HBox.setHgrow(leftPanel, Priority.NEVER);
        leftPanel.setPrefWidth(380); // 40% of 950

        // --- RIGHT PANEL ---
        Label hmsLabel = new Label("HMS");
        hmsLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");
        Circle accentDot = new Circle(5, Color.web("#4fc3f7"));
        HBox hmsBox = new HBox(8, hmsLabel, accentDot);
        hmsBox.setAlignment(Pos.CENTER_LEFT);

        Label welcomeLabel = new Label("Welcome back! Please sign in.");
        welcomeLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #888888;");

        VBox headerBox = new VBox(-2, hmsBox, welcomeLabel);

        // Form fields
        String labelStyle = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #333333;";
        String fieldStyle = "-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1px; " +
                            "-fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 12px; -fx-font-size: 14px;";
        String fieldFocusStyle = "-fx-border-color: #0d1b4b; -fx-border-width: 1.5px;";

        Label userLabel = new Label("Username");
        userLabel.setStyle(labelStyle);
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle(fieldStyle);
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) ->
            usernameField.setStyle(fieldStyle + (newVal ? fieldFocusStyle : ""))
        );

        Label passLabel = new Label("Password");
        passLabel.setStyle(labelStyle);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle(fieldStyle);
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) ->
            passwordField.setStyle(fieldStyle + (newVal ? fieldFocusStyle : ""))
        );

        Label roleLabel = new Label("Role");
        roleLabel.setStyle(labelStyle);
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Student", "Hostel Manager", "Mess Supervisor", "Maintenance Staff", "Accounts");
        roleBox.setPromptText("Select your role");
        roleBox.setStyle(fieldStyle);
        roleBox.setMaxWidth(Double.MAX_VALUE);
        roleBox.focusedProperty().addListener((obs, oldVal, newVal) ->
            roleBox.setStyle(fieldStyle + (newVal ? fieldFocusStyle : ""))
        );

        Button loginBtn = new Button("Sign In");
        loginBtn.setDefaultButton(true);
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(45);
        loginBtn.setStyle("-fx-background-color: #0d1b4b; -fx-text-fill: white; -fx-font-weight: bold; " +
                          "-fx-font-size: 14px; -fx-background-radius: 8px;");
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: #1a3c8f; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8px;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: #0d1b4b; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 8px;"));

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;");
        errorLabel.setAlignment(Pos.CENTER);

        VBox formBox = new VBox(16,
            headerBox,
            new VBox(5, userLabel, usernameField),
            new VBox(5, passLabel, passwordField),
            new VBox(5, roleLabel, roleBox),
            loginBtn,
            errorLabel
        );
        formBox.setAlignment(Pos.CENTER_LEFT);

        Label footerLabel = new Label("Hostel Management System v1.0");
        footerLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #aaaaaa;");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox rightContent = new VBox(20, formBox, spacer, footerLabel);
        rightContent.setAlignment(Pos.CENTER);
        rightContent.setPadding(new Insets(60));
        rightContent.setMaxWidth(570); // 60% of 950

        VBox rightPanel = new VBox(rightContent);
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setStyle("-fx-background-color: #ffffff;");
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        // --- ROOT LAYOUT ---
        HBox root = new HBox(leftPanel, rightPanel);

        // --- LOGIN ACTION ---
        loginBtn.setOnAction(ev -> {
            errorLabel.setText("");
            String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
            String password = passwordField.getText() == null ? "" : passwordField.getText();
            String role = roleBox.getValue();

            if (username.isEmpty() || password.isEmpty() || role == null || role.isEmpty()) {
                errorLabel.setText("Please fill all fields.");
                return;
            }

            User user = AuthController.authenticate(username, password, role);
            if (user != null) {
                System.out.println("Login successful: " + user.getRole());
                
                // Get current stage from a component in the scene
                Stage currentStage = (Stage) loginBtn.getScene().getWindow();
                
                NavigationManager.initialize(currentStage, user.getUserId(), user.getRole());

                if ("Student".equalsIgnoreCase(user.getRole())) {
                    NavigationManager.navigateTo("student-dashboard");
                } else if ("Hostel Manager".equalsIgnoreCase(user.getRole())) {
                    NavigationManager.navigateTo("manager-dashboard");
                } else if ("Mess Supervisor".equalsIgnoreCase(user.getRole())) {
                    NavigationManager.navigateTo("mess-dashboard");
                } else if ("Maintenance Staff".equalsIgnoreCase(user.getRole())) {
                    NavigationManager.navigateTo("staff-dashboard");
                } else if ("Accounts".equalsIgnoreCase(user.getRole())) {
                    NavigationManager.navigateTo("accounts-dashboard");
                } else {
                    // Placeholder for other roles
                    errorLabel.setStyle("-fx-text-fill: #2e7d32;");
                    errorLabel.setText("Login successful for " + user.getRole() + " (No dashboard available).");
                }
            } else {
                errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 12px;"); // Red for error
                errorLabel.setText("Invalid credentials. Please try again.");
            }
        });

        Scene scene = new Scene(root, 950, 620);
        stage.setScene(scene);
        stage.setTitle("Smart Hostel Management System");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}