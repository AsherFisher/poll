package ex2;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * poll Servlet Showing the poll
 */
@WebServlet(name = "poll", urlPatterns = "/poll")
public class pollServlet extends HttpServlet {
    // poll ArrayList for holding the poll list
    private ArrayList<String> poll;
    // a lock for synchronized the vote section
    private final String lock = "locked";
    // holding the error massage if throws IOException | ServletException
    private String err;
    // results holding the vote results
    private HashMap<String,Integer> results;

    /**
     * doPost checks the vote of the client synchronized, and add a cookie, and update the results HashMap
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        Cookie[] cookies = request.getCookies();
        for (int i = 0 ; cookies!=null && i < cookies.length ; i++) {
            // if cookie is already exist forward to results servlet
            if (cookies[i].getName().equals("username")) {
                request.setAttribute("errMsg", "You have already voted, you can vote every 20 seconds");
                doGet(request, response);
                return;
            }
        }
        synchronized (lock) {
            String radio = request.getParameter("poll");
            // check if the client no choose one radio answer
            if (radio == null) {
                request.setAttribute("errMsg", "please choose one answer!");
                doGet(request, response);
                return;
            }
            // create new cookie
            Cookie cookie = new Cookie("username", "voted");
            cookie.setMaxAge(20);
            response.addCookie(cookie);
            // update results map
            results.replace(radio, results.get(radio), results.get(radio)+1);
            request.getRequestDispatcher("results").forward(request, response);
        }
    }

    /**
     * doGet Returns the poll to the client as a html file
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        // print the head of the html file
        request.getRequestDispatcher("head.html").include(request, response);
        // check if error massage IOException | ServletException
        if(err != null && !err.isEmpty()){
            out.println("<div class = \"err\">"+ err +"</div>");
            out.println("</body></html>");
            out.close();
            return;
        }
        // print form
        out.println("<form method = \"post\" action=\"poll\">");
        out.println("<h2>"+poll.get(0)+"</h2>");
        for (int i = 1; i < poll.size(); i++){
            out.println("<input type=\"radio\" name=\"poll\" value="+poll.get(i)+">");
            out.println("<label for="+poll.get(i)+">"+poll.get(i)+"</label><br>");
        }
        String errMsg = (String)request.getAttribute("errMsg");
        // check if the client select a radio
        if(errMsg != null){
            out.println("<br><div class = \"err\">"+ errMsg +"</div>");
        }
        // print the end of poll html file
        request.getRequestDispatcher("endPoll.html").include(request, response);
        out.close();
    }

    /**
     * init run only one time before the servlet started, Initializing the global members
     * @param config ServletConfig
     * @throws ServletException ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        poll = (ArrayList<String>) config.getServletContext().getAttribute("poll");
        err = (String) config.getServletContext().getAttribute("err");
        results = (HashMap<String,Integer>) config.getServletContext().getAttribute("results");
    }
}
