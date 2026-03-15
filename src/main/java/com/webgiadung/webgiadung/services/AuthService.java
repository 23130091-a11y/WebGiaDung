package com.webgiadung.webgiadung.services;

import com.webgiadung.webgiadung.dao.AuthDao;
import com.webgiadung.webgiadung.model.User;

public class AuthService {
    AuthDao authDao = new AuthDao();

    public User checkLogin(String email, String password) {
        User user = authDao.getUserByEmail(email);
        if (user == null) return null;
        if (user.getPassword() != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User loginWithGoogle(String email, String name) {
        // nếu có user trong db thì trả về user còn không thì tạo mới
        User user = authDao.getUserByEmail(email);
        if (user != null) {
            return user;
        } else {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name); // set tên
            newUser.setRole(0); // 0 = user thường
            newUser.setStatus(1); // 1 = active
            authDao.register(newUser); // dùng register() thay vì save()
            return newUser;
        }
    }
}
