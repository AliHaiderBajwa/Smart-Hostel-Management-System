package hostel.ui;

import hostel.controllers.AttendanceController;
import hostel.domain.User;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
//import javafx.scene.control.CheckBoxTableCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableCell;

public class MarkAttendanceView {

    private final Scene scene;
    private final ObservableList<StudentAttendanceRow> rows = FXCollections.observableArrayList();
    private final Label messageLabel = new Label();

    // NO-ARG CONSTRUCTOR
    public MarkAttendanceView() {
        this.scene = createScene();
        seedRows();
    }

    // CONSTRUCTOR WITH Stage AND User
    public MarkAttendanceView(Stage stage, User user) {
        this();  // Calls the no-arg constructor
    }

    public Scene getScene() {
        return scene;
    }

    private Scene createScene() {
        BorderPane root = new BorderPane();
        
        String[][] menuItems = {
            {"Dashboard", "mess-dashboard"},
            {"Mark Attendance", "mark-attendance"},
            {"Food Report", "food-report"},
            {"Logout", "login"}
        };
        
        // CREATE SIDEBAR DIRECTLY (NO buildSidebar method)
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #0a3b1a;");
        sidebar.setPrefWidth(250);
        
        Label title = new Label("HMS - Mess Portal");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        sidebar.getChildren().add(title);
        
        for (String[] item : menuItems) {
            Button btn = new Button(item[0]);
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER_LEFT;");
            btn.setOnAction(e -> NavigationManager.navigateTo(item[1]));
            sidebar.getChildren().add(btn);
        }
        
        root.setLeft(sidebar);

        BorderPane content = new BorderPane();
        content.setStyle("-fx-background-color: #ffffff;");
        content.setTop(createTopBar());
        content.setCenter(createMainContent());
        root.setCenter(content);

        return new Scene(root, 1100, 680);
    }

    private HBox createTopBar() {
        Label title = new Label("Record Mess Attendance");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0a3b1a;");
        HBox top = new HBox(title);
        top.setPadding(new Insets(20));
        top.setPrefHeight(60);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setStyle("-fx-background-color: #f5f7fa;");
        return top;
    }

    private VBox createMainContent() {
        DatePicker datePicker = new DatePicker(LocalDate.now());

        ToggleButton breakfast = new ToggleButton("Breakfast");
        ToggleButton lunch = new ToggleButton("Lunch");
        ToggleButton dinner = new ToggleButton("Dinner");
        ToggleGroup meals = new ToggleGroup();
        breakfast.setToggleGroup(meals);
        lunch.setToggleGroup(meals);
        dinner.setToggleGroup(meals);
        breakfast.setSelected(true);

        HBox mealSelectors = new HBox(8, breakfast, lunch, dinner);
        mealSelectors.setAlignment(Pos.CENTER_LEFT);

        TableView<StudentAttendanceRow> table = new TableView<>(rows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StudentAttendanceRow, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        TableColumn<StudentAttendanceRow, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    //    TableColumn<StudentAttendanceRow, Boolean> presentCol = new TableColumn<>("Present");
   //     presentCol.setCellValueFactory(c -> c.getValue().presentProperty());
   //     presentCol.setCellFactory(tc -> new CheckBoxTableCell<>());
        TableColumn<StudentAttendanceRow, Boolean> presentCol = new TableColumn<>("Present");
        presentCol.setCellValueFactory(c -> c.getValue().presentProperty());
        presentCol.setCellFactory(column -> new TableCell<StudentAttendanceRow, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    javafx.scene.control.CheckBox checkBox = new javafx.scene.control.CheckBox();
                    checkBox.setSelected(item);
                    checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                        StudentAttendanceRow row = getTableView().getItems().get(getIndex());
                        if (row != null) {
                            row.setPresent(newVal);
                        }
                    });
                    setGraphic(checkBox);
                }
            }
        });
        table.getColumns().addAll(idCol, nameCol, presentCol);
        table.setEditable(true);
        table.setRowFactory(tv -> new TableRow<StudentAttendanceRow>() {
            @Override
            protected void updateItem(StudentAttendanceRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setStyle("");
                else setStyle(getIndex() % 2 == 0 ? "-fx-background-color: white;" : "-fx-background-color: #f9f9f9;");
            }
        });

        Button markAll = new Button("Mark All Present");
        markAll.setOnAction(e -> rows.forEach(r -> r.setPresent(true)));

        Button submit = new Button("Submit Attendance");
        submit.setStyle("-fx-background-color: #0a3b1a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8px;");
        submit.setOnAction(e -> {
            ToggleButton selectedMealBtn = (ToggleButton) meals.getSelectedToggle();
            if (selectedMealBtn == null) {
                messageLabel.setText("Please select a meal type.");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                return;
            }
            if (datePicker.getValue() == null) {
                messageLabel.setText("Please select a date.");
                messageLabel.setStyle("-fx-text-fill: #e74c3c;");
                return;
            }
            String mealType = selectedMealBtn.getText();
            String date = datePicker.getValue().toString();
            List<String> presentIds = new ArrayList<>();
            for (StudentAttendanceRow row : rows) {
                if (row.isPresent()) {
                    presentIds.add(row.getStudentId());
                }
            }
            System.out.println("Recorded " + mealType + " attendance for " + date);
            messageLabel.setStyle("-fx-text-fill: #2e7d32;");
            messageLabel.setText("Attendance recorded for " + presentIds.size() + " students");
        });

        VBox root = new VBox(14,
                new Label("Date"), datePicker,
                new Label("Meal Type"), mealSelectors,
                table,
                new HBox(10, markAll, submit),
                messageLabel
        );
        root.setPadding(new Insets(30));
        VBox.setVgrow(table, Priority.ALWAYS);
        return root;
    }

    private void seedRows() {
        rows.setAll(
                new StudentAttendanceRow("S001", "Ahsan Raza"),
                new StudentAttendanceRow("S002", "Hina Tariq"),
                new StudentAttendanceRow("S003", "Usman Khan"),
                new StudentAttendanceRow("S004", "Sana Waheed"),
                new StudentAttendanceRow("S005", "Bilal Zafar")
        );
    }

    public static class StudentAttendanceRow {
        private final String studentId;
        private final String name;
        private final BooleanProperty present = new SimpleBooleanProperty(false);

        public StudentAttendanceRow(String studentId, String name) {
            this.studentId = studentId;
            this.name = name;
        }

        public String getStudentId() { return studentId; }
        public String getName() { return name; }
        public boolean isPresent() { return present.get(); }
        public void setPresent(boolean value) { present.set(value); }
        public BooleanProperty presentProperty() { return present; }
    }
}