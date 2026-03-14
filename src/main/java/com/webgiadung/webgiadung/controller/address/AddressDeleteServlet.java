package com.webgiadung.doanweb.controller.address;

import com.webgiadung.doanweb.dao.UserAddressDao;
import com.webgiadung.doanweb.model.User;
import com.webgiadung.doanweb.model.UserAddress;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/address/delete")
public class AddressDeleteServlet extends HttpServlet {

    private static String esc(String s){
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n","\\n").replace("\r","\\r");
    }
    private static String toJsonList(List<UserAddress> list){
        StringBuilder sb = new StringBuilder("[");
        for (int i=0;i<list.size();i++){
            UserAddress a = list.get(i);
            if(i>0) sb.append(",");
            sb.append("{")
                    .append("\"id\":").append(a.getId()).append(",")
                    .append("\"fullName\":\"").append(esc(a.getFullName())).append("\",")
                    .append("\"phone\":\"").append(esc(a.getPhone())).append("\",")
                    .append("\"address\":\"").append(esc(a.getAddress())).append("\",")
                    .append("\"isDefault\":").append(a.getIsDefault())
                    .append("}");
        }
        sb.append("]");
        return sb.toString();
    }
    private static String toJsonOne(UserAddress a){
        if(a==null) return "null";
        return "{"
                + "\"id\":" + a.getId() + ","
                + "\"fullName\":\"" + esc(a.getFullName()) + "\","
                + "\"phone\":\"" + esc(a.getPhone()) + "\","
                + "\"address\":\"" + esc(a.getAddress()) + "\","
                + "\"isDefault\":" + a.getIsDefault()
                + "}";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        User u = (User) req.getSession().getAttribute("user");
        if (u == null) { resp.getWriter().write("{\"ok\":false,\"msg\":\"not_login\"}"); return; }

        int id;
        try { id = Integer.parseInt(req.getParameter("id")); }
        catch (Exception e){ resp.getWriter().write("{\"ok\":false,\"msg\":\"invalid\"}"); return; }

        UserAddressDao dao = new UserAddressDao();
        dao.delete(u.getId(), id);

        List<UserAddress> addresses = dao.listByUser(u.getId());

        // chọn lại selected: ưu tiên default, không có thì lấy cái đầu
        UserAddress selected = dao.findDefault(u.getId()).orElse(null);
        if(selected == null && !addresses.isEmpty()) selected = addresses.get(0);

        if(selected != null) req.getSession().setAttribute("SELECTED_ADDR_ID", selected.getId());
        else req.getSession().removeAttribute("SELECTED_ADDR_ID");

        resp.getWriter().write("{\"ok\":true,\"addresses\":"+toJsonList(addresses)+",\"selected\":"+toJsonOne(selected)+"}");
    }
}
