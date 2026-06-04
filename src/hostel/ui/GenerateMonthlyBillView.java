package hostel.ui;

import hostel.controllers.BillingController;
import hostel.domain.MessBill;
import hostel.domain.User;
import java.util.List;
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

public class GenerateMonthlyBillView {

    private final Stage stage;
    private final User user;
    private final Scene scene;

    private final ObservableList<MessBill> bills = FXCollections.observableArrayList();
    private final Label detailLabel = new Label("Select a bill to view breakdown.");
    private final Label summaryLabel = new Label("Total Bills Generated: 0 | Total Amount: Rs. 0 | Paid Count: 0 | Unpaid Count: 0");
    private TableView<MessBill> table;

    private ComboBox<String> monthBox;
    private ComboBox<String> yearBox;

    public GenerateMonthlyBillView(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
        this.scene = createScene();
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
        Label subtitle = new Label("Accounts Portal");
        subtitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #80cbc4;");
        VBox logoBox = new VBox(-5, logo, subtitle);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPadding(new Insets(20, 0, 20, 20));

        VBox nav = new VBox(5,
                navBtn("Dashboard"),
                navBtn("Generate Bills")
        );

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button logout = navBtn("Logout");

        VBox sidebar = new VBox(20, logoBox, nav, spacer, logout);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #1a3b2a;");
        sidebar.setPadding(new Insets(0, 0, 10, 0));
        return sidebar;
    }

    private Button navBtn(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        String base = "-fx-background-color: transparent; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
        String hover = "-fx-background-color: #24523d; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-size: 13px;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        b.setOnAction(e -> {
            if ("Dashboard".equals(text)) stage.setScene(new AccountsDashboard(stage, user).getScene());
            else if ("Generate Bills".equals(text)) stage.setScene(new GenerateMonthlyBillView(stage, user).getScene());
            else if ("Logout".equals(text)) new Main().start(stage);
        });
        return b;
    }

    private HBox createTopBar() {
        Label title = new Label("Generate Monthly Mess Bills");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a3b2a;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        Label title = new Label("Generate Monthly Mess Bills");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #0d1b4b;");

        monthBox = new ComboBox<>();
        monthBox.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        monthBox.setValue("January");

        yearBox = new ComboBox<>();
        yearBox.getItems().addAll("2024", "2025", "2026");
        yearBox.setValue("2026");

        Button generate = new Button("Generate All Bills");
        generate.setStyle("-fx-background-color: #1a3b2a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");
        generate.setOnAction(e -> generateBills());

        HBox controls = new HBox(10, new Label("Month"), monthBox, new Label("Year"), yearBox, generate);
        controls.setAlignment(Pos.CENTER_LEFT);

        table = createTable();

        VBox detailCard = new VBox(detailLabel);
        detailCard.setPadding(new Insets(12));
        detailCard.setStyle("-fx-background-color: #fafafa; -fx-border-color: #e0e0e0; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        Button markPaid = new Button("Mark as Paid");
        markPaid.setStyle("-fx-background-color: #1a3b2a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");
        markPaid.setOnAction(e -> markSelectedPaid());

        VBox summaryCard = new VBox(summaryLabel);
        summaryCard.setPadding(new Insets(12));
        summaryCard.setStyle("-fx-background-color: #e8f9f0; -fx-border-color: #27ae60; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        VBox root = new VBox(14, title, controls, table, detailCard, markPaid, summaryCard);
        root.setPadding(new Insets(30));
        VBox.setVgrow(table, Priority.ALWAYS);
        return root;
    }

    private TableView<MessBill> createTable() {
        TableView<MessBill> t = new TableView<>(bills);
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        t.getColumns().add(col("Student ID", "studentId"));
        t.getColumns().add(col("Name", "studentName"));
        t.getColumns().add(col("Base Charges", "baseCharges"));
        t.getColumns().add(col("Leave Deductions", "leaveDeductions"));
        t.getColumns().add(col("Penalties", "penalties"));
        t.getColumns().add(col("Total", "total"));
        t.getColumns().add(col("Status", "status"));

        t.setRowFactory(tv -> new TableRow<MessBill>() {
            @Override
            protected void updateItem(MessBill item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setStyle("");
                else setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
            }
        });

        t.getSelectionModel().selectedItemProperty().addListener((obs, o, selected) -> {
            if (selected != null) {
                detailLabel.setText(
                        "Bill ID: " + selected.getBillId()
                                + "\nStudent: " + selected.getStudentName() + " (" + selected.getStudentId() + ")"
                                + "\nBase Charges: Rs. " + (int) selected.getBaseCharges()
                                + "\nLeave Deductions: -Rs. " + (int) selected.getLeaveDeductions()
                                + "\nPenalties: +Rs. " + (int) selected.getPenalties()
                                + "\nTotal: Rs. " + (int) selected.getTotal()
                                + "\nStatus: " + selected.getStatus()
                );
            }
        });

        return t;
    }

    private TableColumn<MessBill, ?> col(String title, String property) {
        TableColumn<MessBill, Object> c = new TableColumn<>(title);
        c.setCellValueFactory(new PropertyValueFactory<>(property));
        return c;
    }

    private void generateBills() {
        List<MessBill> generated = BillingController.generateAllBills(monthBox.getValue(), yearBox.getValue());
        bills.setAll(generated);
        updateSummary();
    }

    private void markSelectedPaid() {
        MessBill selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        BillingController.markBillPaid(selected.getBillId());
        bills.setAll(BillingController.getBillsForPeriod(monthBox.getValue(), yearBox.getValue()));
        updateSummary();
    }

    private void updateSummary() {
        int totalCount = bills.size();
        double totalAmount = bills.stream().mapToDouble(MessBill::getTotal).sum();
        long paid = bills.stream().filter(b -> "Paid".equalsIgnoreCase(b.getStatus())).count();
        long unpaid = bills.stream().filter(b -> "Unpaid".equalsIgnoreCase(b.getStatus())).count();
        summaryLabel.setText("Total Bills Generated: " + totalCount
                + " | Total Amount: Rs. " + (int) totalAmount
                + " | Paid Count: " + paid
                + " | Unpaid Count: " + unpaid);
    }
}