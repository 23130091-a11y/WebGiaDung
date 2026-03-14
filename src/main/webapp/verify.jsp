<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Xác nhận email</title>
    <link rel="stylesheet" href="assets/css/verify.css">
</head>
<body>

<div class="verify-container">
    <h3>Xác nhận đăng ký</h3>

    <form action="verify-otp" method="post">
        <input type="text" name="otp" placeholder="Nhập mã xác nhận" required>
        <button type="submit">XÁC NHẬN</button>
    </form>

    <div class="error">${error}</div>

    <div class="resend">
        Không nhận được mã?
        <a href="send-otp">Gửi lại</a>
    </div>
</div>

</body>
</html>
