package com.lms.leavemanagementsystem.service;

import com.lms.leavemanagementsystem.model.Employee;
import com.lms.leavemanagementsystem.model.LeaveApplication;
import com.lms.leavemanagementsystem.repository.LeaveApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveApplicationService {

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    // Submit new leave application
    public LeaveApplication submitApplication(LeaveApplication application) {
        application.setStatus("Pending");
        return leaveApplicationRepository.save(application);
    }

    // Get applications by employee
    public List<LeaveApplication> getApplicationsByEmployee(Employee employee) {
        return leaveApplicationRepository.findByEmployee(employee);
    }

    // Get pending applications
    public List<LeaveApplication> getPendingApplications() {
        return leaveApplicationRepository.findByStatus("Pending");
    }

    // Get application by ID
    public Optional<LeaveApplication> getApplicationById(Long id) {
        return leaveApplicationRepository.findById(id);
    }

    // Approve application
    public LeaveApplication approveApplication(Long applicationId) {
        Optional<LeaveApplication> appOpt = getApplicationById(applicationId);
        if (appOpt.isPresent()) {
            LeaveApplication application = appOpt.get();
            application.setStatus("Approved");

            // Calculate days and deduct from balance
            long days = ChronoUnit.DAYS.between(
                    application.getStartDate(),
                    application.getEndDate()
            ) + 1;

            leaveBalanceService.deductDays(
                    application.getEmployee(),
                    application.getLeaveType(),
                    (int) days
            );

            return leaveApplicationRepository.save(application);
        }
        return null;
    }

    // Reject application
    public LeaveApplication rejectApplication(Long applicationId) {
        Optional<LeaveApplication> appOpt = getApplicationById(applicationId);
        if (appOpt.isPresent()) {
            LeaveApplication application = appOpt.get();
            application.setStatus("Rejected");
            return leaveApplicationRepository.save(application);
        }
        return null;
    }
}