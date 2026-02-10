<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manager Dashboard</title>
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>
<!-- Header -->
<div class="header">
    <h1><i class="fas fa-calendar-check"></i> Leave Management System</h1>
    <div class="user-info">
        <span><i class="fas fa-user-tie"></i> <strong>${manager.name}</strong> (Manager)</span>
        <a href="/logout" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Logout</a>
    </div>
</div>

<!-- Navigation -->
<div class="nav-tabs">
    <a href="/manager/dashboard" class="nav-tab active">
        <i class="fas fa-home"></i> Dashboard
    </a>
    <a href="#" class="nav-tab">
        <i class="fas fa-tasks"></i> Requests
    </a>
    <a href="#" class="nav-tab">
        <i class="fas fa-chart-bar"></i> Reports
    </a>
    <a href="#" class="nav-tab">
        <i class="fas fa-user"></i> Profile
    </a>
</div>

<!-- Main Container -->
<div class="container">

    <!-- Stats Cards -->
    <div class="stats-grid">
        <div class="stat-card stat-pending">
            <div class="stat-icon">
                <i class="fas fa-clock"></i>
            </div>
            <div class="stat-info">
                <h3>Pending Requests</h3>
                <p class="stat-number">${pendingRequests.size()}</p>
            </div>
        </div>

        <div class="stat-card stat-approved">
            <div class="stat-icon">
                <i class="fas fa-check-circle"></i>
            </div>
            <div class="stat-info">
                <h3>Approved Today</h3>
                <p class="stat-number">0</p>
            </div>
        </div>

        <div class="stat-card stat-team">
            <div class="stat-icon">
                <i class="fas fa-users"></i>
            </div>
            <div class="stat-info">
                <h3>Team Members</h3>
                <p class="stat-number">${manager.employees.size()}</p>
            </div>
        </div>
    </div>

    <!-- Pending Leave Requests -->
    <div class="card">
        <h2><i class="fas fa-inbox"></i> Pending Leave Requests</h2>

        <c:choose>
            <c:when test="${empty pendingRequests}">
                <div class="empty-state">
                    <i class="fas fa-check-double"></i>
                    <p>All caught up!</p>
                    <small>No pending leave requests at the moment</small>
                </div>
            </c:when>
            <c:otherwise>
                <div class="requests-list">
                    <c:forEach items="${pendingRequests}" var="request">
                        <div class="request-item">
                            <div class="request-employee">
                                <div class="employee-avatar">
                                    <i class="fas fa-user-circle"></i>
                                </div>
                                <div class="employee-info">
                                    <h4>${request.employee.name}</h4>
                                    <p>${request.employee.department} • ${request.employee.position}</p>
                                </div>
                            </div>

                            <div class="request-details">
                                <div class="detail-item">
                                    <i class="fas fa-calendar-alt"></i>
                                    <div>
                                        <label>Leave Type</label>
                                        <p>${request.leaveType.typeName}</p>
                                    </div>
                                </div>
                                <div class="detail-item">
                                    <i class="far fa-calendar"></i>
                                    <div>
                                        <label>Duration</label>
                                        <p>${request.startDate} to ${request.endDate}</p>
                                    </div>
                                </div>
                                <c:if test="${not empty request.reason}">
                                    <div class="detail-item reason-item">
                                        <i class="fas fa-comment-dots"></i>
                                        <div>
                                            <label>Reason</label>
                                            <p>${request.reason}</p>
                                        </div>
                                    </div>
                                </c:if>
                            </div>

                            <div class="request-actions">
                                <form action="/manager/approve/${request.applicationId}" method="post" style="display:inline;">
                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-check"></i> Approve
                                    </button>
                                </form>
                                <form action="/manager/reject/${request.applicationId}" method="post" style="display:inline;">
                                    <button type="submit" class="btn btn-danger">
                                        <i class="fas fa-times"></i> Reject
                                    </button>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Reports Section -->
    <div class="card">
        <h2><i class="fas fa-chart-line"></i> Generate Reports</h2>
        <form class="report-form">
            <div class="form-grid">
                <div class="form-group">
                    <label><i class="far fa-calendar-alt"></i> From Date</label>
                    <input type="date" name="fromDate">
                </div>
                <div class="form-group">
                    <label><i class="far fa-calendar-check"></i> To Date</label>
                    <input type="date" name="toDate">
                </div>
                <div class="form-group">
                    <label><i class="fas fa-filter"></i> Report Type</label>
                    <select>
                        <option>Leave Summary</option>
                        <option>Employee Leave Balance</option>
                        <option>Approved Leaves</option>
                    </select>
                </div>
            </div>
            <button type="submit" class="btn btn-primary">
                <i class="fas fa-file-download"></i> Generate Report
            </button>
        </form>
    </div>
</div>
</body>
</html>