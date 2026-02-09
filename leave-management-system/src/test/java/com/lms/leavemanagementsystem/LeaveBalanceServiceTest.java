package com.lms.leavemanagementsystem;

import com.lms.leavemanagementsystem.model.Employee;
import com.lms.leavemanagementsystem.model.LeaveBalance;
import com.lms.leavemanagementsystem.model.LeaveType;
import com.lms.leavemanagementsystem.repository.LeaveBalanceRepository;
import com.lms.leavemanagementsystem.service.LeaveBalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveBalanceServiceTest {

    @Mock
    private LeaveBalanceRepository leaveBalanceRepository;

    @InjectMocks
    private LeaveBalanceService leaveBalanceService;

    private Employee employee;
    private LeaveType leaveType;
    private LeaveBalance leaveBalance;

    @BeforeEach
    void setUp() {
        // Setup test employee
        employee = new Employee();
        employee.setUserId(1L);
        employee.setName("Test Employee");
        employee.setEmployeeId("EMP001");

        // Setup test leave type
        leaveType = new LeaveType();
        leaveType.setTypeId(1L);
        leaveType.setTypeName("Annual");
        leaveType.setMaxDays(10);

        // Setup test leave balance
        leaveBalance = new LeaveBalance();
        leaveBalance.setBalanceId(1L);
        leaveBalance.setEmployee(employee);
        leaveBalance.setLeaveType(leaveType);
        leaveBalance.setTotalDays(10);
        leaveBalance.setRemainingDays(10);
        leaveBalance.setYear(2026);
    }

    @Test
    void testGetBalance_Success() {
        // Given
        when(leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType))
                .thenReturn(Optional.of(leaveBalance));

        // When
        Optional<LeaveBalance> result = leaveBalanceService.getBalance(employee, leaveType);

        // Then
        assertTrue(result.isPresent());
        assertEquals(10, result.get().getRemainingDays());
        verify(leaveBalanceRepository, times(1)).findByEmployeeAndLeaveType(employee, leaveType);
    }

    @Test
    void testDeductDays_Success() {
        // Given
        when(leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType))
                .thenReturn(Optional.of(leaveBalance));
        when(leaveBalanceRepository.save(any(LeaveBalance.class)))
                .thenReturn(leaveBalance);

        // When
        leaveBalanceService.deductDays(employee, leaveType, 3);

        // Then
        verify(leaveBalanceRepository, times(1)).findByEmployeeAndLeaveType(employee, leaveType);
        verify(leaveBalanceRepository, times(1)).save(any(LeaveBalance.class));
        assertEquals(7, leaveBalance.getRemainingDays());
    }

    @Test
    void testDeductDays_InsufficientBalance() {
        // Given
        leaveBalance.setRemainingDays(2);
        when(leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType))
                .thenReturn(Optional.of(leaveBalance));
        when(leaveBalanceRepository.save(any(LeaveBalance.class)))
                .thenReturn(leaveBalance);

        // When
        leaveBalanceService.deductDays(employee, leaveType, 5);

        // Then - Balance should become negative (indicating error scenario)
        assertEquals(-3, leaveBalance.getRemainingDays());
    }

    @Test
    void testDeductDays_BalanceNotFound() {
        // Given
        when(leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType))
                .thenReturn(Optional.empty());

        // When
        leaveBalanceService.deductDays(employee, leaveType, 3);

        // Then - Should not throw exception, just not save
        verify(leaveBalanceRepository, times(1)).findByEmployeeAndLeaveType(employee, leaveType);
        verify(leaveBalanceRepository, never()).save(any(LeaveBalance.class));
    }

    @Test
    void testUpdateBalance_Success() {
        // Given
        when(leaveBalanceRepository.save(leaveBalance)).thenReturn(leaveBalance);

        // When
        LeaveBalance result = leaveBalanceService.updateBalance(leaveBalance);

        // Then
        assertNotNull(result);
        assertEquals(leaveBalance, result);
        verify(leaveBalanceRepository, times(1)).save(leaveBalance);
    }
}