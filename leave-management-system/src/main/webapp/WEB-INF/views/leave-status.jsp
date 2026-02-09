<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Leave Status</title>
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>
<!-- Header -->
<div class="header">
    <h1><i class="fas fa-calendar-check"></i> Leave Management System</h1>
    <div class="user-info">
        <span><i class="fas fa-user-circle"></i> <strong>${employee.name}</strong> (${employee.employeeId})</span>
        <a href="/logout" class="logout-btn"><i class="fas fa-sign-out-alt"></i> Logout</a>
    </div>
</div>

<!-- Navigation -->
<div class="nav-tabs">
    <a href="/employee/dashboard" class="nav-tab">
        <i class="fas fa-home"></i> Dashboard
    </a>
    <a href="/employee/leave-status" class="nav-tab active">
        <i class="fas fa-list-alt"></i> Leave Status
    </a>
    <a href="#" class="nav-tab">
        <i class="fas fa-user"></i> Profile
    </a>
</div>

<!-- Main Container -->
<div class="container">

    <!-- Filter Section -->
    <div class="card filter-card">
        <div class="filter-header">
            <h2><i class="fas fa-filter"></i> Filter Applications</h2>
        </div>
        <div class="filter-options">
            <button class="filter-btn active" data-filter="all">
                <i class="fas fa-list"></i> All
            </button>
            <button class="filter-btn" data-filter="pending">
                <i class="fas fa-clock"></i> Pending
            </button>
            <button class="filter-btn" data-filter="approved">
                <i class="fas fa-check-circle"></i> Approved
            </button>
            <button class="filter-btn" data-filter="rejected">
                <i class="fas fa-times-circle"></i> Rejected
            </button>
        </div>
    </div>

    <!-- All Applications -->
    <div class="card">
        <h2><i class="fas fa-history"></i> All Leave Applications</h2>

        <c:choose>
            <c:when test="${empty applications}">
                <div class="empty-state">
                    <i class="fas fa-inbox"></i>
                    <p>No leave applications found</p>
                    <small>Start by applying for leave from the dashboard</small>
                </div>
            </c:when>
            <c:otherwise>
                <div class="applications-list" id="applicationsList">
                    <c:forEach items="${applications}" var="app">
                        <div class="application-item" data-status="${app.status.toLowerCase()}">
                            <div class="app-header">
                                <div class="app-type">
                                    <c:choose>
                                        <c:when test="${app.leaveType.typeName == 'Annual'}">
                                            <i class="fas fa-umbrella-beach"></i>
                                        </c:when>
                                        <c:when test="${app.leaveType.typeName == 'Sick'}">
                                            <i class="fas fa-hospital"></i>
                                        </c:when>
                                        <c:when test="${app.leaveType.typeName == 'Family'}">
                                            <i class="fas fa-home"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-calendar"></i>
                                        </c:otherwise>
                                    </c:choose>
                                    <strong>${app.leaveType.typeName} Leave</strong>
                                </div>
                                <span class="status-badge status-${app.status.toLowerCase()}">
                                        <c:choose>
                                            <c:when test="${app.status == 'Pending'}">
                                                <i class="fas fa-clock"></i>
                                            </c:when>
                                            <c:when test="${app.status == 'Approved'}">
                                                <i class="fas fa-check-circle"></i>
                                            </c:when>
                                            <c:when test="${app.status == 'Rejected'}">
                                                <i class="fas fa-times-circle"></i>
                                            </c:when>
                                        </c:choose>
                                        ${app.status}
                                    </span>
                            </div>
                            <div class="app-details">
                                <div class="app-dates">
                                    <i class="far fa-calendar"></i> ${app.startDate} → ${app.endDate}
                                </div>
                                <c:if test="${not empty app.reason}">
                                    <div class="app-reason">
                                        <i class="fas fa-quote-left"></i> ${app.reason}
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    // Filter functionality
    const filterButtons = document.querySelectorAll('.filter-btn');
    const applications = document.querySelectorAll('.application-item');

    filterButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            // Remove active class from all buttons
            filterButtons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            const filter = btn.getAttribute('data-filter');

            applications.forEach(app => {
                if (filter === 'all' || app.getAttribute('data-status') === filter) {
                    app.style.display = 'block';
                } else {
                    app.style.display = 'none';
                }
            });
        });
    });
</script>
</body>
</html>