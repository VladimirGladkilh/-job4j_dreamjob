package servlet;

import model.User;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        User user = PsqlStore.instOf().findUserByEmail(email);

        if (user == null) {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser = PsqlStore.instOf().save(newUser);
            HttpSession sc = req.getSession();
            sc.setAttribute("user", newUser);
            resp.sendRedirect(req.getContextPath() + "/index.do");
        } else {
            if (user != null && user.getPassword().equals(password)) {
                HttpSession sc = req.getSession();
                sc.setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/index.do");
            } else {
                req.setAttribute("error", "Email занят");
                req.getRequestDispatcher("reg.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }
}
