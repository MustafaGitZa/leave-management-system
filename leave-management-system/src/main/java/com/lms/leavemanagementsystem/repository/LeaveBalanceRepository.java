package com.lms.leavemanagementsystem.repository;

import com.lms.leavemanagementsystem.model.Employee;
import com.lms.leavemanagementsystem.model.LeaveBalance;
import com.lms.leavemanagementsystem.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    List<LeaveBalance> findByEmployee(Employee employee);
    Optional<LeaveBalance> findByEmployeeAndLeaveType(Employee employee, LeaveType leaveType);
}