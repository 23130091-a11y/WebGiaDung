package com.webgiadung.doanweb.services;

import com.webgiadung.doanweb.dao.AuthDao;
import com.webgiadung.doanweb.model.User;

public class AuthService {
    AuthDao authDao = new AuthDao();

    public User checkLogin(String email, String password) {
        User user = authDao.getUserByEmail(email);

        // 1. Kiểm tra tồn tại
        if (user == null) return null;

        // 2. Check password (Dùng equals để so sánh chuỗi MD5)
        // password truyền vào đây phải là chuỗi đã băm MD5 từ Controller
        if (user.getPassword() != null && user.getPassword().equals(password)) {
            return user;
        }

        return null; // Sai mật khẩu
    }

    public User loginWithGoogle(String email, String name) {
        // Check user trong DB bằng phương thức AuthDao có sẵn
        User user = authDao.getUserByEmail(email);
        if (user != null) {
            return user; // User đã có → đăng nhập bình thường
        } else {
            // User chưa có → tạo mới
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name); // set tên
            newUser.setRole(0);    // 0 = user thường
            newUser.setStatus(1);  // 1 = active
            authDao.register(newUser); // dùng register() thay vì save()
            return newUser;
        }
    }
}
