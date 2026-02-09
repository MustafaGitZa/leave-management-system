package com.lms.leavemanagementsystem;

import com.lms.leavemanagementsystem.model.*;
import com.lms.leavemanagementsystem.repository.*;
import com.lms.leavemanagementsystem.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LeaveManagementIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LeaveTypeService leaveTypeService;

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    private Employee employee;
    private LeaveType annualLeaveType;

    @BeforeEach
    void setUp() {
        // Clear all data
        leaveApplicationRepository.deleteAll();
        leaveBalanceRepository.deleteAll();
        employeeRepository.deleteAll();
        leaveTypeRepository.deleteAll();

        // Setup employee
        employee = new Employee();
        employee.setName("Integration Test Employee");
        employee.setEmail("integration@test.com");
        employee.setPassword("password123");
        employee.setRole("employee");
        employee.setEmployeeId("INT001");
        employee.setDepartment("IT");
        employee.setPosition("Developer");
        employee.setDateJoined(LocalDateTime.now());
        employee = employeeService.saveEmployee(employee);

        // Setup leave type
        annualLeaveType = new LeaveType();
        annualLeaveType.setTypeName("Annual");
        annualLeaveType.setMaxDays(10);
        annualLeaveType.setDescription("Annual leave");
        annualLeaveType = leaveTypeService.saveLeaveType(annualLeaveType);

        // Setup leave balance
        LeaveBalance balance = new LeaveBalance();
        balance.setEmployee(employee);
        balance.setLeaveType(annualLeaveType);
        balance.setTotalDays(10);
        balance.setRemainingDays(10);
        balance.setYear(2026);
        leaveBalanceService.updateBalance(balance);
    }

    @Test
    void testCompleteLeaveApplicationWorkflow() {
        // 1. Employee applies for leave
        LeaveApplication application = new LeaveApplication();
        application.setEmployee(employee);
        application.setLeaveType(annualLeaveType);
        application.setStartDate(LocalDate.of(2026, 6, 10));
        application.setEndDate(LocalDate.of(2026, 6, 12));
        application.setReason("Vacation");

        LeaveApplication savedApplication = leaveApplicationService.submitApplication(application);
        assertNotNull(savedApplication.getApplicationId());
        assertEquals("Pending", savedApplication.getStatus());

        // 2. Check pending applications
        List<LeaveApplication> pendingApps = leaveApplicationService.getPendingApplications();
        assertEquals(1, pendingApps.size());

        // 3. Approve application
        LeaveApplication approvedApp = leaveApplicationService.approveApplication(savedApplication.getApplicationId());
        assertEquals("Approved", approvedApp.getStatus());

        // 4. Verify balance was deducted (3 days: June 10, 11, 12)
        LeaveBalance updatedBalance = leaveBalanceService.getBalance(employee, annualLeaveType).orElse(null);
        assertNotNull(updatedBalance);
        assertEquals(7, updatedBalance.getRemainingDays());
    }

    @Test
    void testRejectLeaveApplication() {
        // 1. Apply for leave
        LeaveApplication application = new LeaveApplication();
        application.setEmployee(employee);
        application.setLeaveType(annualLeaveType);
        application.setStartDate(LocalDate.of(2026, 7, 1));
        application.setEndDate(LocalDate.of(2026, 7, 5));
        application.setReason("Family event");

        LeaveApplication savedApplication = leaveApplicationService.submitApplication(application);

        // 2. Reject application
        LeaveApplication rejectedApp = leaveApplicationService.rejectApplication(savedApplication.getApplicationId());
        assertEquals("Rejected", rejectedApp.getStatus());

        // 3. Verify balance was NOT deducted
        LeaveBalance balance = leaveBalanceService.getBalance(employee, annualLeaveType).orElse(null);
        assertNotNull(balance);
        assertEquals(10, balance.getRemainingDays());
    }

    @Test
    void testMultipleLeaveApplications() {
        // Apply for 3 days
        LeaveApplication app1 = new LeaveApplication();
        app1.setEmployee(employee);
        app1.setLeaveType(annualLeaveType);
        app1.setStartDate(LocalDate.of(2026, 6, 10));
        app1.setEndDate(LocalDate.of(2026, 6, 12));
        app1.setReason("Vacation");
        app1 = leaveApplicationService.submitApplication(app1);

        // Apply for 2 days
        LeaveApplication app2 = new LeaveApplication();
        app2.setEmployee(employee);
        app2.setLeaveType(annualLeaveType);
        app2.setStartDate(LocalDate.of(2026, 7, 1));
        app2.setEndDate(LocalDate.of(2026, 7, 2));
        app2.setReason("Personal");
        app2 = leaveApplicationService.submitApplication(app2);

        // Approve both
        leaveApplicationService.approveApplication(app1.getApplicationId());
        leaveApplicationService.approveApplication(app2.getApplicationId());

        // Verify total deduction (5 days)
        LeaveBalance balance = leaveBalanceService.getBalance(employee, annualLeaveType).orElse(null);
        assertNotNull(balance);
        assertEquals(5, balance.getRemainingDays());
    }
}