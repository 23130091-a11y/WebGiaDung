package com.webgiadung.doanweb.controller;

import com.webgiadung.doanweb.dao.CategoriesDao;
import com.webgiadung.doanweb.model.Categories;
import com.webgiadung.doanweb.model.Product;
import com.webgiadung.doanweb.model.Slide;
import com.webgiadung.doanweb.services.ProductService;
import com.webgiadung.doanweb.services.SlideService;
import com.webgiadung.doanweb.utils.CookieUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import com.webgiadung.doanweb.services.BlogService;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "listProduct", value = "/list-product")
public class ListProductController extends HttpServlet {
    private CategoriesDao categoriesDao = new CategoriesDao();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductService productService = new ProductService();
        List<Product> list = productService.getListProduct();

        // --- QUAN TRỌNG: Đọc lịch sử xem từ Cookie ---
        List<Integer> viewedIds = CookieUtils.getIdsFromCookie(request, "viewed_products");
        if (!viewedIds.isEmpty()) {
            List<Product> historyProducts = productService.getProductsFromIds(viewedIds);
            request.setAttribute("historyProducts", historyProducts);
        }

        SlideService slideService = new SlideService();
        List<Slide> slides = slideService.getListSlide();

        request.setAttribute("list", list);
        request.setAttribute("slides", slides);

        // LOAD CÁC LOẠI SẢN PHẨM
        request.setAttribute("featuredProducts",
                productService.getFeaturedProducts());

        request.setAttribute("promotionProducts",
                productService.getPromotionProducts());

        request.setAttribute("suggestedProducts",
                productService.getSuggestedProducts());

        request.setAttribute("limitedProducts",
                productService.getLimitedProducts());

        request.setAttribute("newProducts",
                productService.getNewProducts());

        List<Categories> parentCategories =
                categoriesDao.getCategoryTree();

        request.setAttribute("parentCategories", parentCategories);
        BlogService blogService = new BlogService();
        request.setAttribute("latestBlogs", blogService.getHomeBlogs());
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}

