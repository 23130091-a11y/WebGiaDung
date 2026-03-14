package com.webgiadung.doanweb.controller;

import com.webgiadung.doanweb.dao.ProductDao;
import com.webgiadung.doanweb.model.Categories;
import com.webgiadung.doanweb.model.Product;
import com.webgiadung.doanweb.services.CategoriesService;
import com.webgiadung.doanweb.services.ProductService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchServlet", value = "/search-product")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String keyword = request.getParameter("keyword");
        String categoryId = request.getParameter("categoryId");

        String[] brands = request.getParameterValues("brands");
        String[] priceRanges = request.getParameterValues("priceRanges");
        if (brands == null) brands = request.getParameterValues("brands[]");
        if (priceRanges == null) priceRanges = request.getParameterValues("priceRanges[]");

        if (keyword != null) {
            keyword = keyword.trim();
        }

        boolean hasKeyword = (keyword != null && !keyword.isEmpty());
        boolean hasCategory = (categoryId != null && !categoryId.isEmpty());
        boolean hasFilter = (brands != null && brands.length > 0) || (priceRanges != null && priceRanges.length > 0);

        if (!hasKeyword && !hasFilter && !hasCategory) {
            request.setAttribute("message", "Vui lòng nhập từ khóa tìm kiếm hoặc chọn bộ lọc");
            request.getRequestDispatcher("/search.jsp").forward(request, response);
            return;
        }

        if (hasKeyword && keyword.length() < 2 && !hasFilter && !hasCategory) {
            request.setAttribute("message", "Từ khóa tìm kiếm quá ngắn");
            request.getRequestDispatcher("/search.jsp").forward(request, response);
            return;
        }
        if (hasCategory) {
            try {
                int id = Integer.parseInt(categoryId);
                CategoriesService catService = new CategoriesService();
                Categories cat = catService.getCategory(id);

                if (cat != null) {
                    request.setAttribute("category", cat);
                    request.setAttribute("categoryName", cat.getName());
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        if (hasKeyword) {
            HttpSession session = request.getSession();
            List<String> searchHistory = (List<String>) session.getAttribute("searchHistory");
            if (searchHistory == null) searchHistory = new ArrayList<>();
            searchHistory.remove(keyword);
            searchHistory.add(0, keyword);
            if (searchHistory.size() > 5) searchHistory = new ArrayList<>(searchHistory.subList(0, 5));
            session.setAttribute("searchHistory", searchHistory);
        }

        ProductService productsSer = new ProductService();
        List<Product> products = productsSer.searchWithFilters(keyword, brands, priceRanges, categoryId);

        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategoryId", categoryId);
        request.setAttribute("products", products);
        request.setAttribute("selectedBrands", brands);
        request.setAttribute("selectedPrices", priceRanges);

        request.getRequestDispatcher("/search.jsp").forward(request, response);
    }
}