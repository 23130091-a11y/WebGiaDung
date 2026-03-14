package com.webgiadung.doanweb.dao;

import com.webgiadung.doanweb.model.*;
import org.jdbi.v3.core.Jdbi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDao extends BaseDao {

    // Lấy danh sách sản phẩm cơ bản (không load phụ để nhanh)
    public List<Product> getListProduct() {
        return get().withHandle(h ->
                h.createQuery(BASE_SELECT + " LIMIT 50")
                        .mapToBean(Product.class)
                        .list()
        );
    }

    // Lấy chi tiết 1 sản phẩm kèm các bảng phụ
    public Product getProduct(int id) {
        return get().withHandle(h -> {
            Product product = h.createQuery("""
                    SELECT
                        id,
                        name,
                        image,
                        price_first AS firstPrice,
                        price_total AS totalPrice,
                        discounts_id AS discountsId,
                        categories_id AS categoriesId,
                        brands_id AS brandsId,
                        keywords_id AS keywordsId,
                        post,
                        quantity,
                        quantity_saled AS quantitySaled,
                        created_at AS createdAt,
                        updated_at AS updatedAt
                    FROM products
                    WHERE id = :id
                """)
                    .bind("id", id)
                    .mapToBean(Product.class)
                    .findOne()
                    .orElse(null);

            if (product != null) {
                // Load attributes (thông số kỹ thuật)
                List<ProductAttribute> attributes = h.createQuery("""
                        SELECT *
                        FROM product_attributes
                        WHERE product_id = :id
                    """)
                        .bind("id", id)
                        .mapToBean(ProductAttribute.class)
                        .list();
                product.setAttributes(attributes);

                // Load options (tùy chọn sản phẩm)
                List<ProductOption> options = h.createQuery("""
                        SELECT *
                        FROM product_options
                        WHERE product_id = :id
                    """)
                        .bind("id", id)
                        .mapToBean(ProductOption.class)
                        .list();
                product.setOptions(options);

                // Load images phụ
                List<ProductImage> images = h.createQuery("""
                        SELECT *
                        FROM product_images
                        WHERE product_id = :id
                    """)
                        .bind("id", id)
                        .mapToBean(ProductImage.class)
                        .list();
                product.setImages(images);

                // Load đánh giá (reviews)
                List<ProductReview> reviews = h.createQuery("""
                        SELECT *
                        FROM product_reviews
                        WHERE product_id = :id
                    """)
                        .bind("id", id)
                        .mapToBean(ProductReview.class)
                        .list();
                product.setReviews(reviews);

                // Bổ sung load Description cho trang chi tiết
                List<ProductDescriptions> descriptions = h.createQuery("""
                        SELECT id, title, description, products_id AS productId
                        FROM products_description
                        WHERE products_id = :id
                    """)
                        .bind("id", id)
                        .mapToBean(ProductDescriptions.class)
                        .list();
                product.setDescriptionsList(descriptions); // Đảm bảo tên setter đúng với class Product
            }

            return product;
        });
    }
    public int insert(Product p) {
        return get().withHandle(h -> {
            return h.createUpdate(
                            "INSERT INTO products (name, image, price_first, price_total, " +
                                    "brands_id, keywords_id, categories_id, post, quantity, created_at, updated_at) " +
                                    "VALUES (:name, :image, :totalPrice,:totalPrice, " +
                                    ":brandsId, :keywordsId, :categoriesId, :post, :quantity, NOW(), NOW())"
                    )
                    .bindBean(p)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Integer.class)
                    .one();
        });
    }

    public List<Product> searchByName(String keyword) {
        return get().withHandle(h ->
                h.createQuery("""
            SELECT
                id,
                name,
                image,
                price_first AS firstPrice,
                price_total AS totalPrice,
                discounts_id AS discountsId,
                categories_id AS categoriesId,
                brands_id AS brandsId,
                keywords_id AS keywordsId,
                post,
                quantity,
                quantity_saled AS quantitySaled,
                created_at AS createdAt,
                updated_at AS updatedAt
            FROM products
            WHERE name LIKE :keyword
        """)
                        .bind("keyword", "%" + keyword + "%")
                        .mapToBean(Product.class)
                        .list()
        );
    }

    private static final String BASE_SELECT = """
    SELECT
        p.id, p.name, p.image,
        -- Nếu price_first NULL thì lấy price_total làm giá gốc
        IFNULL(p.price_first, p.price_total) AS firstPrice,
        p.price_total AS totalPrice,
        p.discounts_id AS discountsId,
        p.categories_id AS categoriesId,
        p.brands_id AS brandsId,
        p.keywords_id AS keywordsId,
        p.post, p.quantity,
        p.quantity_saled AS quantitySaled,
        p.created_at AS createdAt,
        p.updated_at AS updatedAt,
        IFNULL(d.discount, 0) AS discountPercent, -- Đảm bảo không bị NULL
        d.type_discount AS discountType,
        IFNULL(ROUND(AVG(pr.rating), 1), 0.0) AS ratingAvg
    FROM products p
    LEFT JOIN discounts d ON p.discounts_id = d.id
    LEFT JOIN product_reviews pr ON p.id = pr.product_id
    WHERE p.post = 1
    GROUP BY
        p.id, p.name, p.image, p.price_first, p.price_total,
        p.discounts_id, p.categories_id, p.brands_id,
        p.keywords_id, p.post, p.quantity,
        p.quantity_saled, p.created_at, p.updated_at,
        d.discount, d.type_discount
""";

    private static final String PROMOTION_SELECT = """
    SELECT
        p.id, p.name, p.image,
        p.price_first AS firstPrice,
        CASE
            WHEN d.type_discount = 'percentage' THEN ROUND(p.price_first * (1 - d.discount / 100), 0)
            WHEN d.type_discount = 'fixed' THEN GREATEST(p.price_first - d.discount, 0)
            ELSE p.price_first
        END AS totalPrice,
        p.discounts_id AS discountsId,
        d.discount AS discountPercent,
        d.type_discount AS discountType,
        IFNULL(ROUND(AVG(pr.rating), 1), 0.0) AS ratingAvg
    FROM products p
    JOIN discounts d ON p.discounts_id = d.id
    LEFT JOIN product_reviews pr ON p.id = pr.product_id
    WHERE p.post = 1 AND NOW() BETWEEN d.start_date AND d.end_date
    GROUP BY p.id, p.name, p.image, p.price_first, p.discounts_id, d.discount, d.type_discount
""";

    private static final String SUGGESTED_SELECT = """
    SELECT
        p.id, p.name, p.image,
        p.price_first AS firstPrice,
        p.price_total AS totalPrice,
        p.discounts_id AS discountsId,
        p.quantity_saled AS quantitySaled,
        d.discount AS discountPercent,
        ROUND(AVG(pr.rating), 1) AS ratingAvg
    FROM products p
    LEFT JOIN discounts d ON p.discounts_id = d.id
    JOIN product_reviews pr ON p.id = pr.product_id
    WHERE p.post = 1 AND p.quantity_saled > 0
    GROUP BY p.id, p.name, p.image, p.price_first, p.price_total, p.discounts_id, p.quantity_saled, d.discount
    HAVING AVG(pr.rating) >= 4.0
    ORDER BY p.quantity_saled DESC, ratingAvg DESC
    LIMIT 8
""";

    private static final String LIMITED_DISCOUNT_SELECT = """
    SELECT
        p.id,
        p.name,
        p.image,
        p.price_first AS firstPrice,

        -- Giá sau giảm
        CASE
            WHEN d.type_discount = 'percentage'
                THEN ROUND(p.price_first * (1 - d.discount / 100), 0)
            WHEN d.type_discount = 'fixed'
                THEN GREATEST(p.price_first - d.discount, 0)
            ELSE
                p.price_first
        END AS totalPrice,

        p.quantity,
        p.quantity_saled AS quantitySaled,

        d.discount AS discountPercent,
        d.type_discount AS discountType,

        IFNULL(ROUND(AVG(pr.rating), 1), 0.0) AS ratingAvg

    FROM products p
    JOIN discounts d ON p.discounts_id = d.id
    LEFT JOIN product_reviews pr ON p.id = pr.product_id

    WHERE p.post = 1
      AND p.quantity > 0
      AND p.quantity <= 5
      AND NOW() BETWEEN d.start_date AND d.end_date

    GROUP BY
        p.id,
        p.name,
        p.image,
        p.price_first,
        p.quantity,
        p.quantity_saled,
        d.discount,
        d.type_discount

    ORDER BY p.quantity ASC, p.quantity_saled DESC

    LIMIT 8
""";

    public List<Product> getFeaturedProducts() {
        return get().withHandle(h ->
                h.createQuery(BASE_SELECT + """
            ORDER BY p.quantity_saled DESC
            LIMIT 8
        """)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    // Sửa lại getPromotionProducts (Nếu không có khuyến mãi, lấy sản phẩm mới nhất)
    public List<Product> getPromotionProducts() {
        return get().withHandle(h -> {
            List<Product> list = h.createQuery(PROMOTION_SELECT + " ORDER BY p.created_at DESC LIMIT 8")
                    .mapToBean(Product.class).list();

            if (list.isEmpty()) {
                return getNewProducts(); // Fallback về sản phẩm mới
            }
            return list;
        });
    }

    public List<Product> getSuggestedProducts() {
        return get().withHandle(h -> {
            // Bước 1: Thử lấy sản phẩm theo đúng tiêu chí (Rating cao, đã bán được hàng)
            List<Product> list = h.createQuery(SUGGESTED_SELECT)
                    .mapToBean(Product.class)
                    .list();

            // Bước 2: Nếu không đủ 8 sản phẩm, lấy thêm sản phẩm mới nhất để bù vào
            if (list.size() < 8) {
                List<Integer> idsToExclude = list.stream().map(Product::getId).collect(Collectors.toList());
                if (idsToExclude.isEmpty()) idsToExclude.add(-1); // Tránh lỗi SQL IN empty

                List<Product> fallback = h.createQuery(BASE_SELECT + """
                AND p.id NOT IN (<ids>)
                ORDER BY p.created_at DESC
                LIMIT :limit
            """)
                        .bindList("ids", idsToExclude)
                        .bind("limit", 8 - list.size())
                        .mapToBean(Product.class)
                        .list();

                list.addAll(fallback);
            }
            return list;
        });
    }

    // Sửa lại getLimitedProducts (Nếu không có hàng sắp hết, lấy hàng ngẫu nhiên)
    public List<Product> getLimitedProducts() {
        return get().withHandle(h -> {
            List<Product> list = h.createQuery(LIMITED_DISCOUNT_SELECT).mapToBean(Product.class).list();
            if (list.size() < 4) {
                return getFeaturedProducts(); // Fallback về hàng nổi bật
            }
            return list;
        });
    }

    public List<Product> getNewProducts() {
        return get().withHandle(h ->
                h.createQuery(BASE_SELECT + """
            ORDER BY p.created_at DESC
            LIMIT 8
        """)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    // THÊM MỚI: Lấy sản phẩm dựa trên danh sách ID từ Cookie
    public List<Product> getProductsFromIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();

        String idList = ids.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(","));

        return get().withHandle(h ->
                h.createQuery("""
                SELECT 
                    p.id, p.name, p.image, 
                    IFNULL(p.price_first, p.price_total) AS firstPrice,
                    p.price_total AS totalPrice,
                    IFNULL(d.discount, 0) AS discountPercent, 
                    d.type_discount AS discountType,
                    IFNULL(ROUND(AVG(pr.rating), 1), 0.0) AS ratingAvg -- Thêm dòng này
                FROM products p 
                LEFT JOIN discounts d ON p.discounts_id = d.id 
                LEFT JOIN product_reviews pr ON p.id = pr.product_id -- Thêm JOIN này
                WHERE p.id IN (<ids>) 
                GROUP BY p.id, d.id -- Thêm GROUP BY để hàm AVG() hoạt động
                ORDER BY FIELD(p.id, """ + idList + ")")
                        .bindList("ids", ids)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public List<Product> getByCategoryId(int categoryId) {
        return get().withHandle(h ->
                h.createQuery("""
                    SELECT
                        id,
                        name,
                        image,
                        price_first AS firstPrice,
                        price_total AS totalPrice,
                        discounts_id AS discountsId,
                        categories_id AS categoriesId,
                        brands_id AS brandsId,
                        keywords_id AS keywordsId,
                        post,
                        quantity,
                        quantity_saled AS quantitySaled,
                        created_at AS createdAt,
                        updated_at AS updatedAt
                    FROM products
                    WHERE categories_id = :categoryId
                """)
                        .bind("categoryId", categoryId)
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public Product getProductFullInfo(int id) {
        return get().withHandle(handle -> {
            Product product = handle.createQuery("""
            SELECT 
                p.id, p.name, p.image, 
                p.price_first AS firstPrice, 
                p.price_total AS totalPrice, 
                p.discounts_id AS discountsId, 
                p.categories_id AS categoriesId, 
                p.brands_id AS brandsId, 
                p.keywords_id AS keywordsId, 
                p.post, p.quantity, 
                p.quantity_saled AS quantitySaled, 
                p.created_at AS createdAt, 
                p.updated_at AS updatedAt,
                
                -- Lấy thêm tên từ bảng phụ
                b.name AS brandName,
                k.name AS keywordName

            FROM products p
            LEFT JOIN brands b ON p.brands_id = b.id
            LEFT JOIN keywords k ON p.keywords_id = k.id
            
            WHERE p.id = :id
        """)
                    .bind("id", id)
                    .mapToBean(Product.class)
                    .findOne()
                    .orElse(null);
            // Các phần logic lấy list con bên dưới giữ nguyên
            if (product != null) {

                List<ProductDescriptions> descriptions = handle.createQuery("""
                SELECT 
                    id, title, description, 
                    products_id AS productId, 
                    created_at AS createdAt, 
                    updated_at AS updatedAt
                FROM products_description 
                WHERE products_id = :pid
            """)
                        .bind("pid", id)
                        .mapToBean(ProductDescriptions.class)
                        .list();

                List<ProductDetails> details = handle.createQuery("""
                SELECT 
                    id, image, title, description, 
                    products_id AS productId, 
                    created_at AS createdAt, 
                    updated_at AS updatedAt
                FROM products_detail 
                WHERE products_id = :pid
            """)
                        .bind("pid", id)
                        .mapToBean(ProductDetails.class)
                        .list();

                product.setDescriptionsList(descriptions);
                product.setDetailsList(details);
            }

            return product;
        });
    }
    public boolean updateProduct(Product product) {
        return get().inTransaction(handle -> {
            // 1. CẬP NHẬT BẢNG CHÍNH (products)
            // Lưu ý: Không cập nhật cột 'discount' theo yêu cầu
            int rowsUpdated = handle.createUpdate("""
            UPDATE products SET 
                name = :name,
                image = :image,
                price_first = :firstPrice,
                price_total = :totalPrice, 
                categories_id = :categoriesId,
                brands_id = :brandsId,
                keywords_id = :keywordsId,
                post = :post,
                quantity = :quantity,
                quantity_saled = :quantitySaled,
                updated_at = NOW()             
            WHERE id = :id
        """)
                    .bindBean(product)
                    .execute();

            if (rowsUpdated == 0) return false;

            // --- 2. XỬ LÝ DESCRIPTIONS (Thông minh) ---
            if (product.getDescriptionsList() != null) {
                // Bước A: Lấy danh sách ID hiện có trong DB
                List<Integer> existingIds = handle.createQuery("SELECT id FROM products_description WHERE products_id = :pid")
                        .bind("pid", product.getId())
                        .mapTo(Integer.class)
                        .list();

                // Bước B: Lấy danh sách ID từ form gửi lên (chỉ lấy các ID > 0)
                List<Integer> incomingIds = product.getDescriptionsList().stream()
                        .map(ProductDescriptions::getId)
                        .filter(id -> id > 0)
                        .collect(Collectors.toList());

                // Bước C: Xóa các dòng có trong DB nhưng KHÔNG có trong form gửi lên (User đã xóa dòng đó)
                List<Integer> idsToDelete = existingIds.stream()
                        .filter(id -> !incomingIds.contains(id))
                        .collect(Collectors.toList());

                if (!idsToDelete.isEmpty()) {
                    handle.createUpdate("DELETE FROM products_description WHERE id IN (<ids>)")
                            .bindList("ids", idsToDelete)
                            .execute();
                }

                // Bước D: Loop để Update hoặc Insert
                for (ProductDescriptions desc : product.getDescriptionsList()) {
                    if (desc.getId() > 0 && existingIds.contains(desc.getId())) {
                        // Cập nhật dòng cũ
                        handle.createUpdate("""
                        UPDATE products_description 
                        SET title = :title, description = :desc, updated_at = NOW() 
                        WHERE id = :id
                    """)
                                .bind("title", desc.getTitle())
                                .bind("desc", desc.getDescription())
                                .bind("id", desc.getId())
                                .execute();
                    } else {
                        // Insert dòng mới (ID = 0 hoặc rỗng)
                        handle.createUpdate("""
                        INSERT INTO products_description (title, description, products_id, created_at, updated_at)
                        VALUES (:title, :desc, :pid, NOW(), NOW())
                    """)
                                .bind("title", desc.getTitle())
                                .bind("desc", desc.getDescription())
                                .bind("pid", product.getId())
                                .execute();
                    }
                }
            }

            // --- 3. XỬ LÝ DETAILS (Tương tự Description) ---
            if (product.getDetailsList() != null) {
                List<Integer> existingDetailIds = handle.createQuery("SELECT id FROM products_detail WHERE products_id = :pid")
                        .bind("pid", product.getId())
                        .mapTo(Integer.class)
                        .list();

                List<Integer> incomingDetailIds = product.getDetailsList().stream()
                        .map(ProductDetails::getId)
                        .filter(id -> id > 0)
                        .collect(Collectors.toList());

                List<Integer> detailIdsToDelete = existingDetailIds.stream()
                        .filter(id -> !incomingDetailIds.contains(id))
                        .collect(Collectors.toList());

                if (!detailIdsToDelete.isEmpty()) {
                    handle.createUpdate("DELETE FROM products_detail WHERE id IN (<ids>)")
                            .bindList("ids", detailIdsToDelete)
                            .execute();
                }

                for (ProductDetails detail : product.getDetailsList()) {
                    if (detail.getId() > 0 && existingDetailIds.contains(detail.getId())) {
                        // Update
                        handle.createUpdate("""
                        UPDATE products_detail 
                        SET image = :img, title = :title, description = :desc, updated_at = NOW() 
                        WHERE id = :id
                    """)
                                .bind("img", detail.getImage())
                                .bind("title", detail.getTitle())
                                .bind("desc", detail.getDescription())
                                .bind("id", detail.getId())
                                .execute();
                    } else {
                        // Insert
                        handle.createUpdate("""
                        INSERT INTO products_detail (image, title, description, products_id, created_at, updated_at)
                        VALUES (:img, :title, :desc, :pid, NOW(), NOW())
                    """)
                                .bind("img", detail.getImage())
                                .bind("title", detail.getTitle())
                                .bind("desc", detail.getDescription())
                                .bind("pid", product.getId())
                                .execute();
                    }
                }
            }

            return true;
        });
    }
    public boolean deleteProduct(int id) {
        return get().inTransaction(handle -> {

            handle.createUpdate("DELETE FROM products_description WHERE products_id = :pid")
                    .bind("pid", id)
                    .execute();

            // 1.2 Xóa Detail (products_detail)
            handle.createUpdate("DELETE FROM products_detail WHERE products_id = :pid")
                    .bind("pid", id)
                    .execute();

            int rowsDeleted = handle.createUpdate("DELETE FROM products WHERE id = :id")
                    .bind("id", id)
                    .execute();

            // Trả về true nếu xóa thành công ít nhất 1 dòng
            return rowsDeleted > 0;
        });
    }

    // Lấy sản phẩm theo category_id (menu cấp lá)
    public List<Product> getProductsByCategoryId(int categoryId) {
        return get().withHandle(h ->
                h.createQuery("""
                SELECT
                    id,
                    name,
                    image,
                    price_first AS firstPrice,
                    price_total AS totalPrice,
                    discounts_id AS discountsId,
                    categories_id AS categoriesId,
                    brands_id AS brandsId,
                    keywords_id AS keywordsId,
                    post,
                    quantity,
                    quantity_saled AS quantitySaled,
                    created_at AS createdAt,
                    updated_at AS updatedAt
                FROM products
                WHERE categories_id = :cid
            """)
                        .bind("cid", categoryId)
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public int applyDiscountToAll(int newDiscountId) {
        return get().withHandle(handle -> {
            int rows = handle.createUpdate("""
            UPDATE products p
            JOIN discounts d ON d.id = :discountId
            SET p.discounts_id = d.id,
                p.price_total = ROUND(COALESCE(p.price_first, 0) * (1 - COALESCE(d.discount, 0) / 100.0), 0),
                p.updated_at = NOW()
        """)
                    .bind("discountId", newDiscountId)
                    .execute();
            System.out.println("DEBUG: Apply All - Rows affected: " + rows);
            return rows;
        });
    }

    public int applyDiscountToCategory(int categoryId, int newDiscountId) {
        return get().withHandle(handle -> {
            int rows = handle.createUpdate("""
            UPDATE products p
            JOIN discounts d ON d.id = :discountId
            SET p.discounts_id = d.id,
                p.price_total = ROUND(COALESCE(p.price_first, 0) * (1 - COALESCE(d.discount, 0) / 100.0), 0),
                p.updated_at = NOW()
            WHERE p.categories_id = :categoryId
        """)
                    .bind("categoryId", categoryId)
                    .bind("discountId", newDiscountId)
                    .execute();
            System.out.println("DEBUG: Apply Category " + categoryId + " - Rows affected: " + rows);
            return rows;
        });
    }
    public List<Product> searchWithFilters(String keyword, String[] brands, String[] priceRanges, String categoryId) {
        return get().withHandle(h -> {
            StringBuilder sql = new StringBuilder("""
        SELECT 
            p.id, p.name, p.image, p.post, p.quantity,
            p.price_first AS firstPrice, 
            p.price_total AS totalPrice, 
            p.discounts_id AS discountsId, 
           (COALESCE(d.discount, 0) * 1.0) AS discountPercent,
            p.categories_id AS categoriesId, 
            p.brands_id AS brandsId, 
            p.keywords_id AS keywordsId, 
            p.quantity_saled AS quantitySaled, 
            p.created_at AS createdAt, 
            p.updated_at AS updatedAt
        FROM products p
        LEFT JOIN discounts d ON p.discounts_id = d.id
        WHERE 1=1 
    """);

            if (categoryId != null && !categoryId.trim().isEmpty()) {
                sql.append(" AND p.categories_id = :categoryId ");
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND LOWER(p.name) LIKE LOWER(:keyword) ");
            }

            if (brands != null && brands.length > 0) {
                sql.append(" AND p.brands_id IN (SELECT id FROM brands WHERE name IN (<brandNames>)) ");
            }

            if (priceRanges != null && priceRanges.length > 0) {
                sql.append(" AND ( ");
                for (int i = 0; i < priceRanges.length; i++) {
                    sql.append(" (p.price_total BETWEEN :min").append(i).append(" AND :max").append(i).append(") ");
                    if (i < priceRanges.length - 1) {
                        sql.append(" OR ");
                    }
                }
                sql.append(" ) ");
            }

            var query = h.createQuery(sql.toString());

            if (categoryId != null && !categoryId.trim().isEmpty()) {
                query.bind("categoryId", categoryId);
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                query.bind("keyword", "%" + keyword.trim() + "%");
            }
            if (brands != null && brands.length > 0) {
                query.bindList("brandNames", java.util.Arrays.asList(brands));
            }
            if (priceRanges != null && priceRanges.length > 0) {
                for (int i = 0; i < priceRanges.length; i++) {
                    String[] parts = priceRanges[i].split("-");
                    if (parts.length == 2) {
                        try {
                            query.bind("min" + i, Double.parseDouble(parts[0]));
                            query.bind("max" + i, Double.parseDouble(parts[1]));
                        } catch (NumberFormatException e) {
                            query.bind("min" + i, -1.0);
                            query.bind("max" + i, -1.0);
                        }
                    }
                }
            }
            return query.mapToBean(Product.class).list();
        });
    }
    public Product getProductFull(int id) {
        return get().withHandle(handle -> {
            // 1. Lấy thông tin sản phẩm + Brand + Keyword + Discount
            Product product = handle.createQuery("""
            SELECT 
                p.id, p.name, p.image, 
                p.price_first AS firstPrice, 
                p.price_total AS totalPrice, 
                p.discounts_id AS discountsId, 
                p.categories_id AS categoriesId, 
                p.brands_id AS brandsId, 
                p.keywords_id AS keywordsId, 
                p.post, p.quantity, 
                p.quantity_saled AS quantitySaled, 
                p.created_at AS createdAt, 
                p.updated_at AS updatedAt,
                b.name AS brandName,
                k.name AS keywordName,
                d.discount AS discountPercent
            FROM products p
            LEFT JOIN brands b ON p.brands_id = b.id
            LEFT JOIN keywords k ON p.keywords_id = k.id
            LEFT JOIN discounts d ON p.discounts_id = d.id 
            WHERE p.id = :id
        """)
                    .bind("id", id)
                    .mapToBean(Product.class)
                    .findOne()
                    .orElse(null);

            if (product != null) {
                // 2. Lấy Descriptions
                List<ProductDescriptions> descriptions = handle.createQuery("""
                SELECT id, title, description, products_id AS productId, created_at AS createdAt, updated_at AS updatedAt
                FROM products_description WHERE products_id = :pid
            """).bind("pid", id).mapToBean(ProductDescriptions.class).list();

                // 3. Lấy Details
                List<ProductDetails> details = handle.createQuery("""
                SELECT id, image, title, description, products_id AS productId, created_at AS createdAt, updated_at AS updatedAt
                FROM products_detail WHERE products_id = :pid
            """).bind("pid", id).mapToBean(ProductDetails.class).list();

                List<ProductReview> reviews = handle.createQuery("""
                SELECT * FROM product_reviews 
                WHERE product_id = :pid
                ORDER BY created_at DESC
            """)
                        .bind("pid", id)
                        .mapToBean(ProductReview.class)
                        .list();

                product.setDescriptionsList(descriptions);
                product.setDetailsList(details);
                product.setReviews(reviews);
            }

            return product;
        });
    }
    public List<Product> findByName(String name) {
        return get().withHandle(h ->
                h.createQuery("""
                SELECT
                    id, name, image,
                    price_first AS firstPrice,
                    price_total AS totalPrice,
                    discounts_id AS discountsId,
                    categories_id AS categoriesId,
                    brands_id AS brandsId,
                    keywords_id AS keywordsId,
                    post,
                    quantity,
                    quantity_saled AS quantitySaled,
                    created_at AS createdAt,
                    updated_at AS updatedAt
                FROM products
                WHERE post = 1
                  AND name LIKE :name
            """)
                        .bind("name", "%" + name + "%")
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public List<Product> searchByDiscountName(String discountName) {
        return get().withHandle(h ->
                h.createQuery("""
            SELECT 
                p.id, p.name, p.image, 
                p.price_first AS firstPrice, 
                p.price_total AS totalPrice, 
                p.discounts_id AS discountsId, 
                p.categories_id AS categoriesId, 
                p.brands_id AS brandsId, 
                p.keywords_id AS keywordsId, 
                p.post, p.quantity, 
                p.quantity_saled AS quantitySaled, 
                p.created_at AS createdAt, 
                p.updated_at AS updatedAt,
                d.name AS discountName, -- Giả định bảng discount có cột name
                d.discount AS discountPercent,
                d.type_discount AS discountType
            FROM products p
            INNER JOIN discounts d ON p.discounts_id = d.id
            WHERE d.name LIKE :discountName 
              AND p.post = 1 -- Chỉ lấy sản phẩm đang hiển thị
        """)
                        .bind("discountName", "%" + discountName + "%")
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public int removeDiscount(int discountId) {
        return get().withHandle(handle -> {
            return handle.createUpdate("""
            UPDATE products 
            SET 
                discounts_id = NULL,
                price_total = price_first, 
                updated_at = NOW()
            WHERE discounts_id = :discountId
        """)
                    .bind("discountId", discountId)
                    .execute();
        });
    }
    public int updateProductPricesByDiscountId(int discountId) {
        return get().withHandle(handle -> {
            return handle.createUpdate("""
            UPDATE products p
            CROSS JOIN discounts d ON d.id = :discountId
            SET p.price_total = CASE 
                    WHEN d.type_discount = 'percentage' THEN p.price_first * (1 - d.discount / 100)
                    WHEN d.type_discount = 'fixed' THEN GREATEST(p.price_first - d.discount, 0)
                    ELSE p.price_first 
                END,
                p.updated_at = NOW()
            WHERE p.discounts_id = :discountId
        """)
                    .bind("discountId", discountId)
                    .execute();
        });
    }
}



