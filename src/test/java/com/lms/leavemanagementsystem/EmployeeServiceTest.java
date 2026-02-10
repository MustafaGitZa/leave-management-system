package com.lms.leavemanagementsystem;

import com.lms.leavemanagementsystem.model.Employee;
import com.lms.leavemanagementsystem.repository.EmployeeRepository;
import com.lms.leavemanagementsystem.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setUserId(1L);
        employee.setName("Test Employee");
        employee.setEmail("test@company.com");
        employee.setEmployeeId("EMP001");
        employee.setDepartment("IT");
    }

    @Test
    void testSaveEmployee_Success() {
        // Given
        when(employeeRepository.save(employee)).thenReturn(employee);

        // When
        Employee result = employeeService.saveEmployee(employee);

        // Then
        assertNotNull(result);
        assertEquals("Test Employee", result.getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testGetEmployeeByEmail_Found() {
        // Given
        when(employeeRepository.findByEmail("test@company.com"))
                .thenReturn(Optional.of(employee));

        // When
        Optional<Employee> result = employeeService.getEmployeeByEmail("test@company.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("test@company.com", result.get().getEmail());
        verify(employeeRepository, times(1)).findByEmail("test@company.com");
    }

    @Test
    void testGetEmployeeByEmail_NotFound() {
        // Given
        when(employeeRepository.findByEmail("notfound@company.com"))
                .thenReturn(Optional.empty());

        // When
        Optional<Employee> result = employeeService.getEmployeeByEmail("notfound@company.com");

        // Then
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findByEmail("notfound@company.com");
    }
}