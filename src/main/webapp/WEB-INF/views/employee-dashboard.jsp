<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Employee Dashboard</title>
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
    <a href="/employee/dashboard" class="nav-tab active">
        <i class="fas fa-home"></i> Dashboard
    </a>
    <a href="/employee/leave-status" class="nav-tab">
        <i class="fas fa-list-alt"></i> Leave Status
    </a>
    <a href="#" class="nav-tab">
        <i class="fas fa-user"></i> Profile
    </a>
</div>

<!-- Main Container -->
<div class="container">

    <!-- Success Message -->
    <div id="successMessage" class="success-message" style="display:none;">
        <i class="fas fa-check-circle"></i> Leave request submitted successfully! Waiting for manager approval.
    </div>

    <!-- Leave Balance Summary -->
    <div class="card balance-card">
        <h2><i class="fas fa-chart-pie"></i> Your Leave Balance</h2>
        <div class="balance-grid">
            <c:forEach items="${balances}" var="balance">
                <div class="balance-box balance-${balance.leaveType.typeName.toLowerCase()}">
                    <div class="balance-icon">
                        <c:choose>
                            <c:when test="${balance.leaveType.typeName == 'Annual'}">
                                <i class="fas fa-umbrella-beach"></i>
                            </c:when>
                            <c:when test="${balance.leaveType.typeName == 'Sick'}">
                                <i class="fas fa-hospital"></i>
                            </c:when>
                            <c:when test="${balance.leaveType.typeName == 'Family'}">
                                <i class="fas fa-home"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-calendar"></i>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="balance-info">
                        <h3>${balance.leaveType.typeName} Leave</h3>
                        <p class="balance-days">${balance.remainingDays} <span>of ${balance.totalDays} days</span></p>
                        <div class="balance-bar">
                            <div class="balance-progress" style="width: ${(balance.remainingDays * 100) / balance.totalDays}%"></div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Apply for Leave -->
    <div class="card apply-card">
        <h2><i class="fas fa-plus-circle"></i> Apply for Leave</h2>
        <form id="leaveForm" action="/employee/apply-leave" method="post">
            <div class="form-grid">
                <div class="form-group">
                    <label for="leaveType">
                        <i class="fas fa-list"></i> Leave Type
                    </label>
                    <select id="leaveType" name="leaveType.typeId" required>
                        <option value="">Select Leave Type</option>
                        <c:forEach items="${leaveTypes}" var="type">
                            <option value="${type.typeId}">${type.typeName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="startDate">
                        <i class="far fa-calendar-alt"></i> Start Date
                    </label>
                    <input type="date" id="startDate" name="startDate" required>
                </div>

                <div class="form-group">
                    <label for="endDate">
                        <i class="far fa-calendar-check"></i> End Date
                    </label>
                    <input type="date" id="endDate" name="endDate" required>
                </div>
            </div>

            <div class="form-group">
                <label for="reason">
                    <i class="fas fa-comment-dots"></i> Reason for Leave
                </label>
                <textarea id="reason" name="reason" placeholder="Please provide a reason for your leave request..." rows="4"></textarea>
            </div>

            <button type="submit" class="btn btn-primary btn-submit" id="submitBtn">
                <span class="btn-text"><i class="fas fa-paper-plane"></i> Submit Leave Request</span>
                <span class="spinner" style="display:none;">
                        <i class="fas fa-spinner fa-spin"></i> Submitting...
                    </span>
            </button>
        </form>
    </div>

    <!-- Leave Application Status -->
    <div class="card status-card">
        <h2><i class="fas fa-history"></i> Recent Applications</h2>

        <c:choose>
            <c:when test="${empty applications}">
                <div class="empty-state">
                    <i class="fas fa-inbox"></i>
                    <p>No leave applications yet</p>
                    <small>Submit your first leave request above</small>
                </div>
            </c:when>
            <c:otherwise>
                <div class="applications-list">
                    <c:forEach items="${applications}" var="app" varStatus="status">
                        <c:if test="${status.index < 3}">
                            <div class="application-item">
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
                        </c:if>
                    </c:forEach>
                </div>
                <a href="/employee/leave-status" class="view-all-link">
                    <i class="fas fa-arrow-right"></i> View All Applications
                </a>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    // Form submission with spinner
    document.getElementById('leaveForm').addEventListener('submit', function(e) {
        const submitBtn = document.getElementById('submitBtn');
        const btnText = submitBtn.querySelector('.btn-text');
        const spinner = submitBtn.querySelector('.spinner');

        btnText.style.display = 'none';
        spinner.style.display = 'flex';
        submitBtn.disabled = true;
    });

    // Show success message
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('success') === 'true') {
        document.getElementById('successMessage').style.display = 'flex';
        setTimeout(() => {
            document.getElementById('successMessage').style.display = 'none';
        }, 5000);
    }

    // Set minimum date to today
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('startDate').setAttribute('min', today);
    document.getElementById('endDate').setAttribute('min', today);

    // Update end date minimum
    document.getElementById('startDate').addEventListener('change', function() {
        document.getElementById('endDate').setAttribute('min', this.value);
    });
</script>
</body>
</html>