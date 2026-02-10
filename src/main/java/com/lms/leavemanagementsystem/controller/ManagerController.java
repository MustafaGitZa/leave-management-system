package com.lms.leavemanagementsystem.controller;

import com.lms.leavemanagementsystem.model.LeaveApplication;
import com.lms.leavemanagementsystem.model.Manager;
import com.lms.leavemanagementsystem.service.LeaveApplicationService;
import com.lms.leavemanagementsystem.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    // Manager dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long managerId = (Long) session.getAttribute("userId");

        if (managerId == null) {
            return "redirect:/login";
        }

        Manager manager = managerService.getManagerById(managerId).orElse(null);
        if (manager == null) {
            return "redirect:/login";
        }

        List<LeaveApplication> pendingRequests =
                leaveApplicationService.getPendingApplications();

        model.addAttribute("manager", manager);
        model.addAttribute("pendingRequests", pendingRequests);

      return "manager/manager-dashboard";
    }


    // Approve leave request
    @PostMapping("/approve/{id}")
    public String approveLeave(@PathVariable Long id) {
        leaveApplicationService.approveApplication(id);
        return "redirect:/manager/dashboard";
    }

    // Reject leave request
    @PostMapping("/reject/{id}")
    public String rejectLeave(@PathVariable Long id) {
        leaveApplicationService.rejectApplication(id);
        return "redirect:/manager/dashboard";
    }
}