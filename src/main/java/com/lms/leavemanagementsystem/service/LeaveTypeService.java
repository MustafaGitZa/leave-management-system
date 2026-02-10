package com.lms.leavemanagementsystem.service;

import com.lms.leavemanagementsystem.model.LeaveType;
import com.lms.leavemanagementsystem.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveTypeService {

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    // Save or update leave type
    public LeaveType saveLeaveType(LeaveType leaveType) {
        return leaveTypeRepository.save(leaveType);
    }

    // Get all leave types
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    // Get leave type by ID
    public Optional<LeaveType> getLeaveTypeById(Long id) {
        return leaveTypeRepository.findById(id);
    }

    // Get leave type by name
    public Optional<LeaveType> getLeaveTypeByName(String typeName) {
        return leaveTypeRepository.findByTypeName(typeName);
    }
}