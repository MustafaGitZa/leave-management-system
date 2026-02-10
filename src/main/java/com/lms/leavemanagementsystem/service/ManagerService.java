package com.lms.leavemanagementsystem.service;

import com.lms.leavemanagementsystem.model.Manager;
import com.lms.leavemanagementsystem.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    // Save or update manager
    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }

    // Get manager by ID
    public Optional<Manager> getManagerById(Long id) {
        return managerRepository.findById(id);
    }

    // Get manager by email
    public Optional<Manager> getManagerByEmail(String email) {
        return managerRepository.findByEmail(email);
    }
}