package com.lms.leavemanagementsystem;

import com.lms.leavemanagementsystem.model.Employee;
import com.lms.leavemanagementsystem.model.LeaveApplication;
import com.lms.leavemanagementsystem.model.LeaveType;
import com.lms.leavemanagementsystem.repository.LeaveApplicationRepository;
import com.lms.leavemanagementsystem.service.LeaveApplicationService;
import com.lms.leavemanagementsystem.service.LeaveBalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaveApplicationServiceTest {

    @Mock
    private LeaveApplicationRepository leaveApplicationRepository;

    @Mock
    private LeaveBalanceService leaveBalanceService;

    @InjectMocks
    private LeaveApplicationService leaveApplicationService;

    private Employee employee;
    private LeaveType leaveType;
    private LeaveApplication leaveApplication;

    @BeforeEach
    void setUp() {
        // Setup test data
        employee = new Employee();
        employee.setUserId(1L);
        employee.setName("Test Employee");

        leaveType = new LeaveType();
        leaveType.setTypeId(1L);
        leaveType.setTypeName("Annual");

        leaveApplication = new LeaveApplication();
        leaveApplication.setApplicationId(1L);
        leaveApplication.setEmployee(employee);
        leaveApplication.setLeaveType(leaveType);
        leaveApplication.setStartDate(LocalDate.of(2026, 6, 10));
        leaveApplication.setEndDate(LocalDate.of(2026, 6, 12));
        leaveApplication.setReason("Vacation");
        leaveApplication.setStatus("Pending");
    }

    @Test
    void testSubmitApplication_Success() {
        // Given
        when(leaveApplicationRepository.save(any(LeaveApplication.class)))
                .thenReturn(leaveApplication);

        // When
        LeaveApplication result = leaveApplicationService.submitApplication(leaveApplication);

        // Then
        assertNotNull(result);
        assertEquals("Pending", result.getStatus());
        verify(leaveApplicationRepository, times(1)).save(any(LeaveApplication.class));
    }

    @Test
    void testGetPendingApplications_Success() {
        // Given
        List<LeaveApplication> pendingApps = Arrays.asList(leaveApplication);
        when(leaveApplicationRepository.findByStatus("Pending"))
                .thenReturn(pendingApps);

        // When
        List<LeaveApplication> result = leaveApplicationService.getPendingApplications();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pending", result.get(0).getStatus());
        verify(leaveApplicationRepository, times(1)).findByStatus("Pending");
    }

    @Test
    void testApproveApplication_Success() {
        // Given
        when(leaveApplicationRepository.findById(1L))
                .thenReturn(Optional.of(leaveApplication));
        when(leaveApplicationRepository.save(any(LeaveApplication.class)))
                .thenReturn(leaveApplication);

        // When
        LeaveApplication result = leaveApplicationService.approveApplication(1L);

        // Then
        assertNotNull(result);
        assertEquals("Approved", result.getStatus());
        verify(leaveBalanceService, times(1)).deductDays(employee, leaveType, 3);
        verify(leaveApplicationRepository, times(1)).save(any(LeaveApplication.class));
    }

    @Test
    void testApproveApplication_NotFound() {
        // Given
        when(leaveApplicationRepository.findById(999L))
                .thenReturn(Optional.empty());

        // When
        LeaveApplication result = leaveApplicationService.approveApplication(999L);

        // Then
        assertNull(result);
        verify(leaveBalanceService, never()).deductDays(any(), any(), anyInt());
    }

    @Test
    void testRejectApplication_Success() {
        // Given
        when(leaveApplicationRepository.findById(1L))
                .thenReturn(Optional.of(leaveApplication));
        when(leaveApplicationRepository.save(any(LeaveApplication.class)))
                .thenReturn(leaveApplication);

        // When
        LeaveApplication result = leaveApplicationService.rejectApplication(1L);

        // Then
        assertNotNull(result);
        assertEquals("Rejected", result.getStatus());
        verify(leaveBalanceService, never()).deductDays(any(), any(), anyInt());
        verify(leaveApplicationRepository, times(1)).save(any(LeaveApplication.class));
    }

    @Test
    void testCalculateDays_Correctly() {
        // Given
        when(leaveApplicationRepository.findById(1L))
                .thenReturn(Optional.of(leaveApplication));
        when(leaveApplicationRepository.save(any(LeaveApplication.class)))
                .thenReturn(leaveApplication);

        // When - 3 days: June 10, 11, 12
        leaveApplicationService.approveApplication(1L);

        // Then - Verify 3 days were deducted
        verify(leaveBalanceService, times(1)).deductDays(employee, leaveType, 3);
    }
}