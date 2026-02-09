# Leave Management System

A web-based Leave Management System built with Java Spring Boot and JSP that enables employees to apply for leave and managers to approve/reject requests.

## 📋 Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Database Configuration](#database-configuration)
- [Running the Application](#running-the-application)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Screenshots](#screenshots)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### Employee Features
- 🔐 Secure login system
- 📊 View leave balance (Annual, Sick, Family)
- 📝 Apply for leave with date selection
- 📋 View leave application history
- 🔔 Real-time status updates (Pending/Approved/Rejected)

### Manager Features
- 👥 View all pending leave requests
- ✅ Approve leave requests
- ❌ Reject leave requests
- 📈 Automatic leave balance updates
- 📊 Dashboard with statistics

## 🛠 Technology Stack

**Backend:**
- Java 17
- Spring Boot 3.2.2
- Spring Data JPA
- Hibernate
- PostgreSQL
- Lombok

**Frontend:**
- JSP (JavaServer Pages)
- HTML5/CSS3
- JavaScript
- Font Awesome 6.5.1

**Build Tool:**
- Maven

## 📦 Prerequisites

Before you begin, ensure you have the following installed:
- Java JDK 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher
- Git
- IDE (IntelliJ IDEA recommended)

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/[your-username]/leave-management-system.git
cd leave-management-system
```

### 2. Configure PostgreSQL Database

Create a new database:
```sql
CREATE DATABASE lms_db;
```

### 3. Update Application Properties

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lms_db
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
```

### 4. Insert Test Data

Run the following SQL script in pgAdmin:
```sql
-- Insert Leave Types
INSERT INTO leave_types (type_id, type_name, max_days, description) VALUES
(1, 'Annual', 10, 'Annual leave for vacations'),
(2, 'Sick', 5, 'Sick leave for medical reasons'),
(3, 'Family', 3, 'Family emergency leave');

-- Insert Manager
INSERT INTO users (user_id, name, email, password, role, date_joined) VALUES
(1, 'Potego Tjiane', 'potego@company.com', 'password123', 'manager', NOW());

INSERT INTO managers (user_id, manager_id) VALUES
(1, 'MGR001');

-- Insert Employees
INSERT INTO users (user_id, name, email, password, role, date_joined) VALUES
(2, 'Mustafa Xaba', 'mustafa@company.com', 'password123', 'employee', NOW()),
(3, 'Lesedi Mangena', 'lesedi@company.com', 'password123', 'employee', NOW());

INSERT INTO employees (user_id, employee_id, department, position, manager_id) VALUES
(2, 'EMP001', 'IT', 'Developer', 1),
(3, 'EMP002', 'HR', 'HR Specialist', 1);

-- Insert Leave Balances
INSERT INTO leave_balances (balance_id, employee_id, type_id, total_days, remaining_days, year) VALUES
(1, 2, 1, 10, 10, 2026), (2, 2, 2, 5, 5, 2026), (3, 2, 3, 3, 3, 2026),
(4, 3, 1, 10, 10, 2026), (5, 3, 2, 5, 5, 2026), (6, 3, 3, 3, 3, 2026);
```

### 5. Build the Project
```bash
mvn clean install
```

## ▶️ Running the Application

### Using Maven
```bash
mvn spring-boot:run
```

### Using IDE
Run `LeaveManagementSystemApplication.java` from your IDE

The application will start on: **http://localhost:8080**

## 👤 Usage

### Test Credentials

**Manager:**
- Email: `potego@company.com`
- Password: `password123`

**Employees:**
- Email: `mustafa@company.com` / Password: `password123`
- Email: `lesedi@company.com` / Password: `password123`

### Workflow

1. **Employee Login** → View leave balance → Apply for leave
2. **Manager Login** → View pending requests → Approve/Reject
3. **Employee** → Check updated status and balance

## 📁 Project Structure
```
leave-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/lms/leavemanagementsystem/
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── EmployeeController.java
│   │   │   │   ├── HomeController.java
│   │   │   │   └── ManagerController.java
│   │   │   ├── model/
│   │   │   │   ├── Employee.java
│   │   │   │   ├── LeaveApplication.java
│   │   │   │   ├── LeaveBalance.java
│   │   │   │   ├── LeaveType.java
│   │   │   │   ├── Manager.java
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   ├── EmployeeRepository.java
│   │   │   │   ├── LeaveApplicationRepository.java
│   │   │   │   ├── LeaveBalanceRepository.java
│   │   │   │   ├── LeaveTypeRepository.java
│   │   │   │   ├── ManagerRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/
│   │   │   │   ├── EmployeeService.java
│   │   │   │   ├── LeaveApplicationService.java
│   │   │   │   ├── LeaveBalanceService.java
│   │   │   │   ├── LeaveTypeService.java
│   │   │   │   └── ManagerService.java
│   │   │   └── LeaveManagementSystemApplication.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── static/
│   │   │       └── css/
│   │   │           └── style.css
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── views/
│   │               ├── employee-dashboard.jsp
│   │               ├── leave-status.jsp
│   │               ├── login.jsp
│   │               └── manager-dashboard.jsp
├── pom.xml
└── README.md
```

## 🔌 API Endpoints

### Authentication
- `GET /login` - Login page
- `POST /login` - Process login
- `GET /logout` - Logout

### Employee
- `GET /employee/dashboard` - Employee dashboard
- `GET /employee/leave-status` - Leave application history
- `POST /employee/apply-leave` - Submit leave application

### Manager
- `GET /manager/dashboard` - Manager dashboard
- `POST /manager/approve/{id}` - Approve leave request
- `POST /manager/reject/{id}` - Reject leave request


## 🎯 Future Enhancements

- [ ] Email notifications
- [ ] PDF report generation
- [ ] Calendar integration
- [ ] Multi-level approval workflow
- [ ] Mobile responsive improvements
- [ ] Advanced analytics dashboard
- [ ] Leave carry-forward functionality
- [ ] Password encryption (BCrypt)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot Documentation
- Font Awesome for icons
- PostgreSQL Documentation

---

**Note:** This is a prototype system for educational purposes. For production use, implement proper security measures including password hashing, CSRF protection, and input sanitization.
