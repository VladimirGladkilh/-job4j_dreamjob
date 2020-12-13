package servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.junit.Test;

public class PostServletTest  {

    @Test
    public void testServlet() throws IOException,  ServletException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("id")).thenReturn("123");
        when(request.getParameter("name")).thenReturn("new post");
        new PostServlet().doPost(request, response);

        verify(request, atLeast(1)).getParameter("id");
        assertTrue(request.getParameter("name").equals("new post"));

    }
}