package com.lms.leavemanagementsystem.repository;

import com.lms.leavemanagementsystem.model.Employee;
import com.lms.leavemanagementsystem.model.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Long> {
    List<LeaveApplication> findByEmployee(Employee employee);
    List<LeaveApplication> findByStatus(String status);
    List<LeaveApplication> findByEmployeeManagerAndStatus(Employee manager, String status);
}