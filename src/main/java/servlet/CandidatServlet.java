package servlet;

import model.Candidate;
import model.Photo;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.PsqlStore;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@MultipartConfig
public class CandidatServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(CandidatServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            System.out.println("SC_BAD_REQUEST");
            return;
        }

        prepareFileStore();

        if (req.getParameter("deleteid") != null ){
            Candidate delCandidate = PsqlStore.instOf().findCandidateById(Integer.parseInt(req.getParameter("deleteid")));
            PsqlStore.instOf().delete(delCandidate);
            Photo delPhoto = delCandidate.getPhoto();
            if (delPhoto != null && delPhoto.getId() > 0) {
                PsqlStore.instOf().delete(delPhoto);
                clearStorrage(delPhoto.getPath());
            }
        } else {
            Candidate candidate = PsqlStore.instOf().findCandidateById(Integer.parseInt(req.getParameter("id")));
            createCandidate(req, candidate);
        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }

    private void clearStorrage(String path) {
        File folder = getRootFolder();
        File file = new File(folder + File.separator + path);
        file.delete();
    }

    private void createCandidate(HttpServletRequest req, Candidate candidate) throws IOException, ServletException {
        Photo photo = createPhoto(req, candidate);
        PsqlStore.instOf().save(
                new Candidate(
                        Integer.parseInt(req.getParameter("id")),
                        req.getParameter("name"),
                        photo
                )
        );
    }


    private Photo createPhoto(HttpServletRequest req, Candidate candidate) throws IOException, ServletException {
        Photo photo = new Photo(0, "");
        if (candidate != null && candidate.getPhoto() != null) {
            photo = candidate.getPhoto();
        }
        File folder = getRootFolder();
        for (Part part : req.getParts()) {
            String fileName = extractFileName(part);
            File file = new File(folder + File.separator + fileName);
            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(part.getInputStream().readAllBytes());
                photo = PsqlStore.instOf().save(new Photo(photo.getId(), fileName));
            } catch (Exception e) {
                LOG.error(e.getMessage());
            }
        }
        return photo;
    }

    private void prepareFileStore() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024);
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(1024 * 1024 * 10);
    }
    private File getRootFolder() {

        File folder = new File("images");
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }



    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

}