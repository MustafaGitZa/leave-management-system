package com.lms.leavemanagementsystem.service;

import com.lms.leavemanagementsystem.model.Employee;
import com.lms.leavemanagementsystem.model.LeaveBalance;
import com.lms.leavemanagementsystem.model.LeaveType;
import com.lms.leavemanagementsystem.repository.LeaveBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveBalanceService {

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    // Get leave balances for employee
    public List<LeaveBalance> getBalancesByEmployee(Employee employee) {
        return leaveBalanceRepository.findByEmployee(employee);
    }

    // Get specific balance for employee and leave type
    public Optional<LeaveBalance> getBalance(Employee employee, LeaveType leaveType) {
        return leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType);
    }

    // Update leave balance
    public LeaveBalance updateBalance(LeaveBalance balance) {
        return leaveBalanceRepository.save(balance);
    }

    // Deduct days from balance
    public void deductDays(Employee employee, LeaveType leaveType, int days) {
        Optional<LeaveBalance> balanceOpt = getBalance(employee, leaveType);
        if (balanceOpt.isPresent()) {
            LeaveBalance balance = balanceOpt.get();
            balance.setRemainingDays(balance.getRemainingDays() - days);
            updateBalance(balance);
        }
    }
}