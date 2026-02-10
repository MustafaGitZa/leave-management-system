package com.lms.leavemanagementsystem.controller;

import com.lms.leavemanagementsystem.model.Employee;
import com.lms.leavemanagementsystem.model.Manager;
import com.lms.leavemanagementsystem.service.EmployeeService;
import com.lms.leavemanagementsystem.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManagerService managerService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam String role,
                        HttpSession session) {

        if ("employee".equals(role)) {
            Optional<Employee> employee = employeeService.getEmployeeByEmail(email);
            if (employee.isPresent() && employee.get().getPassword().equals(password)) {
                session.setAttribute("userId", employee.get().getUserId());
                session.setAttribute("role", "employee");
                return "redirect:/employee/dashboard";
            }
        } else if ("manager".equals(role)) {
            Optional<Manager> manager = managerService.getManagerByEmail(email);
            if (manager.isPresent() && manager.get().getPassword().equals(password)) {
                session.setAttribute("userId", manager.get().getUserId());
                session.setAttribute("role", "manager");
                return "redirect:/manager/dashboard";
            }
        }

        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}