package com.webgiadung.doanweb.services;

import com.webgiadung.doanweb.dao.KeywordsDao;
import com.webgiadung.doanweb.model.Keywords;
import java.util.List;

public class KeywordService {
    private KeywordsDao keywordsDao = new KeywordsDao();

    public List<Keywords> getAllKeywords() {
        return keywordsDao.getAll();
    }

    public int createKeyword(String name, String description) {
        // 1. Kiểm tra rỗng
        if (name == null || name.trim().isEmpty()) {
            return -1;
        }

        // 2. Kiểm tra trùng lặp
        if (keywordsDao.checkExists(name.trim())) {
            return 0; // Đã tồn tại
        }

        // 3. Tạo đối tượng và lưu
        Keywords kw = new Keywords();
        kw.setName(name.trim());
        kw.setDescription(description);

        try {
            return keywordsDao.insert(kw);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public int updateKeyword(int id, String name, String description) {
        // 1. Kiểm tra rỗng
        if (name == null || name.trim().isEmpty()) {
            return -1;
        }

        // 2. Lấy dữ liệu cũ lên để so sánh
        Keywords currentKw = keywordsDao.getById(id);
        if (currentKw == null) {
            return -1; // Không tìm thấy ID này trong DB
        }

        // 3. Kiểm tra trùng lặp (Logic quan trọng)
        // Chỉ kiểm tra trùng nếu Tên Mới KHÁC Tên Cũ
        // (Nếu người dùng chỉ sửa mô tả mà giữ nguyên tên thì không báo lỗi trùng)
        String newName = name.trim();
        if (!currentKw.getName().equalsIgnoreCase(newName)) {
            if (keywordsDao.checkExists(newName)) {
                return 0; // Tên mới đã tồn tại -> Báo lỗi trùng
            }
        }

        // 4. Cập nhật đối tượng
        currentKw.setName(newName);
        currentKw.setDescription(description);

        try {
            boolean success = keywordsDao.update(currentKw);
            return success ? 1 : -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Lỗi hệ thống
        }
    }

    public boolean deleteKeyword(int id) {
        try {
            return keywordsDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}