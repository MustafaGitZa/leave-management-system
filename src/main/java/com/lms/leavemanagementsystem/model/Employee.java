package com.lms.leavemanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employees")
public class Employee extends User {

    @Column(nullable = false, unique = true)
    private String employeeId;

    private String department;
    private String position;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;
}