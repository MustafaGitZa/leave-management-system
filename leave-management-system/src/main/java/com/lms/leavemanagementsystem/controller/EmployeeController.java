package com.lms.leavemanagementsystem.controller;

import com.lms.leavemanagementsystem.model.Employee;
import com.lms.leavemanagementsystem.model.LeaveApplication;
import com.lms.leavemanagementsystem.model.LeaveBalance;
import com.lms.leavemanagementsystem.model.LeaveType;
import com.lms.leavemanagementsystem.service.EmployeeService;
import com.lms.leavemanagementsystem.service.LeaveApplicationService;
import com.lms.leavemanagementsystem.service.LeaveBalanceService;
import com.lms.leavemanagementsystem.service.LeaveTypeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    @Autowired
    private LeaveTypeService leaveTypeService;

    // Employee dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Get logged-in employee from session
        Long employeeId = (Long) session.getAttribute("userId");

        if (employeeId == null) {
            return "redirect:/login";
        }

        Employee employee = employeeService.getEmployeeById(employeeId).orElse(null);

        if (employee == null) {
            return "redirect:/login";
        }

        // Get leave balances
        List<LeaveBalance> balances = leaveBalanceService.getBalancesByEmployee(employee);

        // Get leave applications
        List<LeaveApplication> applications = leaveApplicationService.getApplicationsByEmployee(employee);

        // Get all leave types for dropdown
        List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes();

        model.addAttribute("employee", employee);
        model.addAttribute("balances", balances);
        model.addAttribute("applications", applications);
        model.addAttribute("leaveTypes", leaveTypes);

        return "employee-dashboard";
    }

    // Submit leave application
    @PostMapping("/apply-leave")
    public String applyLeave(@ModelAttribute LeaveApplication application, HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");

        if (employeeId == null) {
            return "redirect:/login";
        }

        Employee employee = employeeService.getEmployeeById(employeeId).orElse(null);

        if (employee != null) {
            application.setEmployee(employee);
            leaveApplicationService.submitApplication(application);
        }

        return "redirect:/employee/dashboard?success=true";
    }

    // Leave status page
    @GetMapping("/leave-status")
    public String leaveStatus(HttpSession session, Model model) {
        Long employeeId = (Long) session.getAttribute("userId");

        if (employeeId == null) {
            return "redirect:/login";
        }

        Employee employee = employeeService.getEmployeeById(employeeId).orElse(null);

        if (employee == null) {
            return "redirect:/login";
        }

        List<LeaveApplication> applications = leaveApplicationService.getApplicationsByEmployee(employee);

        model.addAttribute("employee", employee);
        model.addAttribute("applications", applications);

        return "leave-status";
    }
}