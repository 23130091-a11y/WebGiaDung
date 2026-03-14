package com.webgiadung.doanweb.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.webgiadung.doanweb.model.User;
import com.webgiadung.doanweb.model.UserGoogleDto;
import com.webgiadung.doanweb.model.Constants;
import com.webgiadung.doanweb.services.AuthService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

@WebServlet(name = "LoginGoogleHandler", value = "/login-google")
public class LoginGoogleHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String code = request.getParameter("code");
            if (code == null || code.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            // Lấy access token từ Google
            String accessToken = getToken(code);

            // Lấy thông tin user từ Google
            UserGoogleDto userGoogle = getUserInfo(accessToken);
            System.out.println("Google User: " + userGoogle);

            // Kiểm tra email trong hệ thống
            AuthService authService = new AuthService();
            User user = authService.loginWithGoogle(userGoogle.getEmail(), userGoogle.getName());

            if (user != null) {
                // Tạo session giống login thường
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);        // key dùng chung với login thường
                session.setAttribute("USER_LOGIN", user);  // giữ nếu chỗ khác dùng
                response.sendRedirect(request.getContextPath() + "/list-product");
            } else {
                request.setAttribute("error", "Không thể đăng nhập bằng Google");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi đăng nhập Google: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    public static String getToken(String code) throws ClientProtocolException, IOException {
        // call api to get token
        String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", Constants.GOOGLE_CLIENT_ID)
                        .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI).add("code", code)
                        .add("grant_type", Constants.GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        return jobj.get("access_token").toString().replaceAll("\"", ""); //nguyenvanthanh@Gan, ELP99LJBQX86QNR1J2S44EDF
    }

    public static UserGoogleDto getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = Constants.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();

        return new Gson().fromJson(response, UserGoogleDto.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}