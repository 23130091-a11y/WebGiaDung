package com.webgiadung.doanweb.services;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailService {

    private static final String MY_EMAIL = "nguyenthihonghanh5098@gmail.com";
    private static final String MY_PASSWORD = "nyft fikb wdch tybw";

    public void sendVerifyEmail(String toEmail, String verifyLink) {
        // Cấu hình SMTP của Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Tạo Session của javax.mail
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MY_EMAIL, MY_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MY_EMAIL, "WebGiaDung Support"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Xác nhận tài khoản WebGiaDung");

            String htmlContent = "<div style=\"font-family: Arial, sans-serif; line-height: 1.6;\">"
                    + "<h3>Cảm ơn bạn đã đăng ký!</h3>"
                    + "<p>Vui lòng nhấn vào nút bên dưới để kích hoạt tài khoản của bạn:</p>"
                    + "<a href=\"" + verifyLink + "\" style=\"display:inline-block; padding:12px 25px; background-color:#28a745; color:#ffffff; text-decoration:none; border-radius:5px; font-weight:bold;\">KÍCH HOẠT NGAY</a>"
                    + "<br><br>"
                    + "<p>Nếu nút trên không hoạt động, bạn hãy <b>copy và dán</b> liên kết này vào trình duyệt:</p>"
                    + "<p style=\"color: #007bff; word-break: break-all;\">" + verifyLink + "</p>"
                    + "<hr>"
                    + "<p style=\"font-size: 12px; color: #777;\">Đây là email tự động, vui lòng không phản hồi email này.</p>"
                    + "</div>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("--- THÀNH CÔNG: Đã gửi mail kích hoạt đến " + toEmail + " ---");

        } catch (Exception e) {
            System.err.println("LỖI GỬI MAIL!");
            e.printStackTrace();
        }
    }
}