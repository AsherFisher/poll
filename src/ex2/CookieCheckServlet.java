package ex2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CookieCheck servlet only checks if the client as already a cookie
 */
@WebServlet(name = "CookieCheck", urlPatterns = "/CookieCheck")
public class CookieCheckServlet extends HttpServlet {

    /**
     * doPost send to doGet
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    /**
     * doGet checks if the client as already a cookie
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Cookie get the all cookies
        Cookie[] cookies = request.getCookies();
        for (int i = 0 ; cookies!=null && i < cookies.length ; i++) {
            // if cookie is already exist forward to results servlet
            if (cookies[i].getName().equals("username")) {
                request.getRequestDispatcher("results").forward(request, response);
                return;
            }
        }
    }
}
