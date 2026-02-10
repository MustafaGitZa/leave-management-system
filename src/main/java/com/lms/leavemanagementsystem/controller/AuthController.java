package com.lms.leavemanagementsystem.controller;

import com.lms.leavemanagementsystem.model.Employee;
import com.lms.leavemanagementsystem.model.Manager;
import com.lms.leavemanagementsystem.model.User;
import com.lms.leavemanagementsystem.service.EmployeeService;
import com.lms.leavemanagementsystem.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManagerService managerService;

    // Handle login form submission
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam String role,
                        HttpSession session,
                        Model model) {

        // Simple authentication - check role and email
        if ("employee".equals(role)) {
            Employee employee = employeeService.getEmployeeByEmail(email).orElse(null);

            if (employee != null && employee.getPassword().equals(password)) {
                session.setAttribute("userId", employee.getUserId());
                session.setAttribute("role", "employee");
                return "redirect:/employee/dashboard";
            }
        } else if ("manager".equals(role)) {
            Manager manager = managerService.getManagerByEmail(email).orElse(null);

            if (manager != null && manager.getPassword().equals(password)) {
                session.setAttribute("userId", manager.getUserId());
                session.setAttribute("role", "manager");
                return "redirect:/manager/dashboard";
            }
        }

        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}