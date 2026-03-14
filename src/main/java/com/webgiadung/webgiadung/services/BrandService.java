package com.webgiadung.doanweb.services;

import com.webgiadung.doanweb.dao.BrandsDao;
import com.webgiadung.doanweb.model.Brands;
import java.util.List;

public class BrandService {
    private BrandsDao brandsDao = new BrandsDao();

    public List<Brands> getAllBrands() {
        return brandsDao.getAll();
    }

    /**
     * @param brand Đối tượng Brand chứa thông tin cần thêm
     * @return id > 0 nếu insert thành công
     * @return 0 nếu tên thương hiệu đã tồn tại
     * @return -1 nếu dữ liệu không hợp lệ hoặc lỗi hệ thống
     */
    public int createBrand(Brands brand) {
        // 1. Kiểm tra tính hợp lệ (Validate)
        if (brand == null || brand.getName() == null || brand.getName().trim().isEmpty()) {
            return -1;
        }

        String cleanName = brand.getName().trim();

        // 2. Kiểm tra tồn tại (Tránh trùng lặp tên thương hiệu)
        if (brandsDao.checkExists(cleanName)) {
            return 0;
        }

        try {
            // 3. Thực hiện insert qua DAO và nhận về ID kiểu int
            brand.setName(cleanName);
            // Giả sử brandsDao.insert(brand) trả về ID được tạo tự động (Generated Key)
            return brandsDao.insert(brand);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public int updateBrand(Brands brand) {
        // 1. Validate dữ liệu đầu vào
        if (brand == null || brand.getName() == null || brand.getName().trim().isEmpty()) {
            return -1;
        }

        Brands currentBrand = brandsDao.findById(brand.getId());
        if (currentBrand == null) {
            return -1;
        }

        String newName = brand.getName().trim();

        if (!currentBrand.getName().equalsIgnoreCase(newName)) {
            if (brandsDao.checkExists(newName)) {
                return 0;
            }
        }


        brand.setName(newName);

        try {
            boolean success = brandsDao.update(brand);
            return success ? 1 : -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public boolean deleteBrand(int id) {


        try {
            return brandsDao.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}