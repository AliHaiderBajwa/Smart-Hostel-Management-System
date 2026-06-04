<div align="center">

# 🏠 Smart Hostel Management System
### Multi-Role Desktop Application

**Software Design & Architecture (SDA) Course Project · FAST-NUCES Islamabad · Spring 2026**

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-UI-4a90d9?style=for-the-badge)
![Eclipse](https://img.shields.io/badge/Eclipse-IDE-2C2255?style=for-the-badge&logo=eclipse&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-MVC%20%2B%20DAO-brightgreen?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Complete-brightgreen?style=for-the-badge)

*A fully-featured hostel administration desktop app with five distinct role-based portals, built in Java and JavaFX with a clean MVC + DAO layered architecture.*

</div>

---

## 👤👤 Team

| Ali Haider | Taimoor Khalid | Ashar Ahmed |

---

## 📌 Overview

Smart Hostel Management System is a desktop application that digitizes the complete operational workflow of a university hostel. It supports five user roles - each with a dedicated dashboard and restricted access to only their relevant modules.

The system handles everything from room allocation and leave management to mess billing, complaint tracking, penalty enforcement, and financial reporting - all through a modern JavaFX UI.

---

## 👥 User Roles & Portals

| Role | Dashboard | Key Responsibilities |
|---|---|---|
| **Student** | `StudentDashboard` | View room info, submit complaints, request leave, check balance |
| **Hostel Manager** | `ManagerDashboard` | Allocate rooms, process leave requests, assign complaints, manage policies |
| **Mess Supervisor** | `MessDashboard` | Mark meal attendance, generate food consumption reports |
| **Maintenance Staff** | `StaffDashboard` | View and resolve assigned maintenance complaints |
| **Accounts** | `AccountsDashboard` | Generate monthly bills, manage penalties, view financial statements |

Authentication routes each user to their correct dashboard automatically via `NavigationManager`.

---

## ✨ Features

### 🏗 Room Management
- 24 pre-seeded rooms across 4 blocks (A, B, C, D) - Male blocks: A & C, Female blocks: B & D
- Room types: Single, Double, Triple, Quad with capacity tracking
- Real-time availability status: Available / Partially Occupied / Full
- Room allocation by manager with student assignment
- Room change requests submitted by students, processed by manager
- `CheckRoomAvailabilityView` - filterable room availability grid

### 📋 Leave Management
- Students submit leave requests with dates and reason via `RequestLeaveView`
- Manager reviews and approves/rejects via `ProcessLeaveView`
- Approved leave days auto-deducted from monthly mess bill (₨100/day)

### 🔧 Complaint System
- Students submit complaints with type, urgency level, room number, and description
- Auto-generated complaint IDs (e.g. `CMP-001`)
- Manager assigns complaints to maintenance staff
- Staff tracks and resolves assigned complaints via `ViewAssignedComplaintsView`

### 💰 Billing & Finance
- Monthly mess bills generated per student: base charge ₨8,000 minus leave deductions plus penalties
- `GenerateMonthlyBillView` - bulk bill generation for all students in a period
- `FinancialStatementView` - per-student breakdown (base charge, deductions, penalties, outstanding)
- Mark bills as Paid / Unpaid
- `AccountsDashboard` shows total revenue, bills generated, and pending payments

### ⚠️ Penalty Management
- Accounts staff can impose penalties on students with reason and amount
- Penalties feed into the monthly billing calculation via `ImposePenaltyView`

### 🍽 Mess & Attendance
- Mess supervisor marks daily meal attendance per student via `MarkAttendanceView`
- `FoodConsumptionReportView` - aggregate food consumption report by date range
- Attendance data feeds into leave deduction calculations

### 📜 Policy Management
- Manager can create, view, and manage hostel policies via `ManagePoliciesView`

---

## 🏗 Architecture

The project follows a clean **3-layer MVC + DAO** structure:

```
src/hostel/
├── ui/              ← JavaFX Views & Dashboards (presentation layer)
│   ├── Main.java                      Login screen (split-panel layout)
│   ├── NavigationManager.java         Scene router - maps role → dashboard
│   ├── StudentDashboard.java
│   ├── ManagerDashboard.java
│   ├── MessDashboard.java
│   ├── StaffDashboard.java
│   ├── AccountsDashboard.java
│   └── [15 feature views...]
│
├── controllers/     ← Business logic layer
│   ├── AuthController.java            Login credential validation
│   ├── RoomController.java            Room allocation, availability, change requests
│   ├── ComplaintController.java       Submit, assign, resolve complaints
│   ├── LeaveController.java           Request, approve, track leave
│   ├── BillingController.java         Bill generation, payment tracking, financials
│   ├── AttendanceController.java      Meal attendance marking & queries
│   ├── PenaltyController.java         Impose and retrieve penalties
│   ├── ReportController.java          Food consumption report generation
│   └── PolicyController.java          Hostel policy CRUD
│
├── domain/          ← Entity/model classes
│   ├── User.java, Student.java, Room.java
│   ├── Complaint.java, LeaveRequest.java
│   ├── MessBill.java, FinancialStatement.java
│   ├── MealAttendance.java, Penalty.java
│   ├── RoomAllocation.java, RoomChangeRequest.java
│   └── Policy.java
│
├── dao/             ← Data Access Object interfaces (DB layer stubs)
│   └── [9 DAO interfaces - AttendanceDAO, BillingDAO, RoomDAO, etc.]
│
└── util/
    └── DBConnection.java              Database connection utility
```

---

## ⚙️ Setup & Run

### Prerequisites
- **Java 17+** (JDK)
- **JavaFX SDK** (if not bundled with your JDK)
- **Eclipse IDE** (project includes `.classpath` and `.project` config)

### Run in Eclipse
1. Clone the repo
2. Open Eclipse → `File` → `Import` → `Existing Projects into Workspace`
3. Select the project root folder
4. Ensure JavaFX is on the module path (`--module-path <javafx-sdk>/lib --add-modules javafx.controls,javafx.fxml`)
5. Run `hostel.ui.Main`

### Default Credentials
The system uses in-memory authentication. Use any of these to log in:

| Username | Password | Role |
|---|---|---|
| `student1` | `pass` | Student |
| `manager1` | `pass` | Hostel Manager |
| `mess1` | `pass` | Mess Supervisor |
| `staff1` | `pass` | Maintenance Staff |
| `accounts1` | `pass` | Accounts |

> Check `AuthController.java` for the exact credential map.

---

## 📂 Repository Structure

```
Hostel_Management_System/
├── src/hostel/          # All Java source files
├── bin/hostel/          # Compiled .class files
├── .classpath           # Eclipse classpath config
├── .project             # Eclipse project config
└── README.md
```

---

## 🔮 Planned / TODO

- [ ] Replace in-memory `ArrayList` controllers with real `DAO` + MySQL/SQLite persistence
- [ ] Connect `DBConnection.java` to a live database
- [ ] Add search/filter to all table views
- [ ] Export reports to PDF/CSV

---

## 📄 License

Developed as an academic project at FAST-NUCES Islamabad. Free to use or learn from this. Credit appreciated but not required.
