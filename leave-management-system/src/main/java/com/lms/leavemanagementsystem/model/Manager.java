package com.lms.leavemanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "managers")
public class Manager extends User {

    @Column(nullable = false, unique = true)
    private String managerId;

    @OneToMany(mappedBy = "manager")
    private List<Employee> employees;
}