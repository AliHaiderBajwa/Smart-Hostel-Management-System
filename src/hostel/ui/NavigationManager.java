package hostel.ui;

import hostel.domain.User;
import javafx.stage.Stage;

public class NavigationManager {
    
    private static Stage mainStage;
    private static User currentUser;
    private static String currentRole;
    
    public static void initialize(Stage stage, String userId, String role) {
        mainStage = stage;
        currentUser = new User(userId, role);
        currentRole = role;
        System.out.println("NavigationManager initialized for: " + role);
    }
    
    public static void navigateTo(String screenName) {
        if (mainStage == null) {
            System.err.println("NavigationManager not initialized!");
            return;
        }
        
        switch (screenName) {
            case "student-dashboard":
                mainStage.setScene(new StudentDashboard(mainStage, currentUser).getScene());
                break;
            case "manager-dashboard":
                mainStage.setScene(new ManagerDashboard(mainStage, currentUser).getScene());
                break;
            case "mess-dashboard":
                mainStage.setScene(new MessDashboard(mainStage, currentUser).getScene());
                break;
            case "staff-dashboard":
                mainStage.setScene(new StaffDashboard(mainStage, currentUser).getScene());
                break;
            case "accounts-dashboard":
                mainStage.setScene(new AccountsDashboard(mainStage, currentUser).getScene());
                break;
            case "login":
                new Main().start(mainStage);
                return;
            default:
                System.out.println("Unknown screen: " + screenName);
                return;
        }
        mainStage.show();
    }
}