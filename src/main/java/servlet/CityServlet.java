package servlet;

import model.City;
import store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

public class CityServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        PsqlStore.instOf().save(
                new City(
                        Integer.parseInt(req.getParameter("id")),
                        req.getParameter("name")
                )
        );
        resp.sendRedirect(req.getContextPath() + "/city.do");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("city", PsqlStore.instOf().findAllCities());
        req.setAttribute("user", req.getSession().getAttribute("user"));
        if (req.getParameter("list") == null) {
            req.getRequestDispatcher("city.jsp").forward(req, resp);
        } else {
            List<City> list = (List<City>) PsqlStore.instOf().findAllCities();
            StringJoiner sj = new StringJoiner(System.lineSeparator());
            sj.add("<option value=\"0\"></option>");
            list.forEach(city -> {
                if (city.getId() == Integer.parseInt(req.getParameter("list"))) {
                    sj.add(String.format("<option value=\"%s\" selected>%s</option>", city.getId(), city.getName()));
                } else {
                    sj.add(String.format("<option value=\"%s\">%s</option>", city.getId(), city.getName()));
                }
            });
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/plain");
            resp.getWriter().write(sj.toString());
        }
    }
}
