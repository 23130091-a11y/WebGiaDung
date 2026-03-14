<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${blog.title}"/></title>

    <!-- CSS giống index.jsp -->
    <link rel="stylesheet" href="assets/css/reset.css">
    <link rel="stylesheet" href="assets/css/grid.css">
    <link rel="stylesheet" href="assets/css/base.css">
    <link rel="stylesheet" href="assets/css/style.css">

    <!-- FontAwesome (nếu index có dùng icon clock) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<%@ include file="/common/header.jsp" %>
<body>

<!-- Nếu bạn có header.jsp thì include -->
<%-- <jsp:include page="/header.jsp" /> --%>

<div class="grid wide">
    <div style="padding: 24px 0;">
        <a href="${pageContext.request.contextPath}/list-product"
           style="display:inline-flex;align-items:center;gap:8px;
                  margin:12px 0 16px;
                  color:#333;font-size:14px;font-weight:600;
                  text-decoration:none;">
            <i class="fa-solid fa-arrow-left"></i> Trang chủ
        </a>

        <h1 style="margin: 8px 0 12px;"><c:out value="${blog.title}"/></h1>

        <div class="blog-item__meta" style="margin-bottom: 14px;">
            <span class="blog-item__time">
                <i class="fa-regular fa-clock"></i>
                <fmt:formatDate value="${blog.createdAtDate}" pattern="dd/MM/yyyy • HH:mm"/>
            </span>
        </div>

        <c:if test="${not empty blog.thumbnail}">
            <img src="${blog.thumbnail}" alt="${blog.title}"
                 style="width:100%;max-height:420px;object-fit:cover;border-radius:10px;margin: 12px 0;">
        </c:if>

        <c:if test="${not empty blog.summary}">
            <p style="font-weight:600; margin: 10px 0 18px; line-height: 1.6;">
                <c:out value="${blog.summary}"/>
            </p>
        </c:if>

        <!-- Nội dung chi tiết (content lưu HTML) -->
        <div class="blog-detail-content" style="line-height:1.8; font-size:16px;">
            <c:out value="${blog.content}" escapeXml="false"/>
        </div>
    </div>
</div>

<!-- Nếu bạn có footer.jsp thì include -->
<jsp:include page="/common/footer.jsp" />
</body>
</html>
