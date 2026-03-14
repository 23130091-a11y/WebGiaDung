<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Slide</title>

    <!-- Reset CSS -->
    <link rel="stylesheet" href="assets/css/reset.css">
    <link rel="stylesheet" href="assets/css/slide.css?v=1">
    <link rel="stylesheet" href="assets/css/grid.css">
    <link rel="stylesheet" href="assets/css/base.css">
    <link rel="stylesheet" href="assets/css/style.css">

    <!-- Font -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link
        href="https://fonts.googleapis.com/css2?family=Open+Sans:ital,wght@0,300..800;1,300..800&family=Poppins:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
        rel="stylesheet">
    <!-- Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"
        integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw=="
        crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>

<body>
    <!-- header -->
    <%@ include file="/common/header.jsp" %>

    <main class="main" style="background-color: #ffff; ">
        <div class="grid wide">
            <div class="image-sale">
                <img class="logo__img"
                     src="${pageContext.request.contextPath}/${slide.avatar}"
                     alt="${slide.name}">
            </div>

            <div class="slide-content">
                <div class="head-content">
                    <p class="ti">${slide.name}</p>

                    <p class="name">${slide.text}</p>

                    <p class="date">Chương trình áp dụng trên toàn hệ thống</p>
                </div>
            </div>
                <div class="slide-wrapper">
                    <div class="slide-list-product row small-gutter">

                        <%-- Kiểm tra nếu danh sách trống --%>
                        <c:if test="${empty productList}">
                            <p style="padding: 20px; text-align: center; width: 100%;">
                                Hiện tại chưa có sản phẩm nào cho chương trình này.
                            </p>
                        </c:if>

                        <%-- Chạy vòng lặp đúng vị trí --%>
                        <c:forEach items="${productList}" var="p">
                            <div class="col l-2-4 m-4 c-6">
                                <div class="product-card">
                                    <a href="product?id=${p.id}">
                                        <img src="${pageContext.request.contextPath}/assets/img/products/${p.image}" alt="${p.name}">
                                    </a>
                                    <a href="product?id=${p.id}">
                                        <p>${p.name}</p>
                                    </a>
                                    <div class="price-discount">
                                        <div class="price-top">
                                <span class="old-price">
                                    <fmt:formatNumber value="${p.firstPrice}" type="number"/>đ
                                </span>
                                            <div class="discount-badge">Giảm ${p.discountPercent}%</div>
                                        </div>
                                        <div class="price-bottom">
                                <span class="new-price">
                                    <fmt:formatNumber value="${p.totalPrice}" type="number"/>đ
                                </span>
                                        </div>
                                    </div>
                                    <div class="bottom">
                                        <div class="star">
                                            <i class="fa-solid fa-star"></i> ${p.ratingAvg != null ? p.ratingAvg : '5.0'}
                                        </div>
                                        <button class="fav-btn"><i class="fa-regular fa-heart"></i> Yêu thích</button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>

                    </div>
                </div>
            </div>
            </div>
        </div>

    </main>
    <script src="assets/js/script.js"></script>
</body>

</html>