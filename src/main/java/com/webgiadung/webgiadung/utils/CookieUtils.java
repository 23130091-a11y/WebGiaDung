package com.webgiadung.doanweb.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

public class CookieUtils {
    // 1. Hàm thêm ID vào Cookie (Gọi ở trang chi tiết sản phẩm)
    public static void addIdToCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, int id) {
        String idStr = String.valueOf(id);
        String currentValue = "";

        // Lấy giá trị cookie cũ hiện có
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(cookieName)) {
                    currentValue = c.getValue();
                    break;
                }
            }
        }

        String newValue;
        if (currentValue.isEmpty()) {
            newValue = idStr;
        } else {
            // Xử lý để ID mới luôn nằm đầu và không trùng (Ví dụ: đang có "1-2", xem 3 -> "3-1-2")
            String[] items = currentValue.split("-");
            StringBuilder sb = new StringBuilder(idStr);
            int count = 1;
            for (String item : items) {
                if (!item.equals(idStr) && count < 5) { // Chỉ lấy tối đa 5 sản phẩm gần nhất
                    sb.append("-").append(item);
                    count++;
                }
            }
            newValue = sb.toString();
        }

        Cookie cookie = new Cookie(cookieName, newValue);
        cookie.setMaxAge(60 * 60 * 24 * 7); // Lưu 7 ngày
        cookie.setPath("/"); // Quan trọng: Để mọi trang đều đọc được cookie này
        response.addCookie(cookie);
    }

    // 2. Hàm đọc danh sách ID từ Cookie (Gọi ở trang chủ)
    public static List<Integer> getIdsFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        List<Integer> ids = new ArrayList<>();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(cookieName)) {
                    String[] parts = c.getValue().split("-");
                    for (String p : parts) {
                        try {
                            ids.add(Integer.parseInt(p));
                        } catch (NumberFormatException e) {
                            // Bỏ qua nếu không phải số
                        }
                    }
                }
            }
        }
        return ids;
    }
}
