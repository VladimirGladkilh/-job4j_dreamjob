package servlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DownloadServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(DownloadServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("path");
        if (!name.isBlank()) {
            resp.setContentType("name=" + name);
            resp.setContentType("image/png");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
            File file = new File("images" + File.separator + name);
            try (FileInputStream in = new FileInputStream(file)) {
                resp.getOutputStream().write(in.readAllBytes());
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

    }
}