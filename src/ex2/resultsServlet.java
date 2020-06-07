package ex2;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * results Servlet Showing the results of the poll
 */
@WebServlet(name = "results", urlPatterns = "/results")
public class resultsServlet extends HttpServlet {
    // poll ArrayList for holding the poll list
    private ArrayList<String> poll;
    // results holding the vote results
    private HashMap<String,Integer> results;
    // holding the error massage if throws IOException | ServletException
    private String err;

    /**
     * doPost send the results of the poll to the client as a html file
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        // sum for total votes
        int sum = 0;
        // temp to calculate the percentage
        int temp = 0;
        for(int i = 0; i < results.size(); i++){
            sum += results.get(poll.get(i+1));
        }
        PrintWriter out = response.getWriter();
        // print the head of the html file
        request.getRequestDispatcher("head.html").include(request, response);
        // print the results
        out.println("<h2>results of <span style=\"color:#8C8C69;\">"+poll.get(0)+"</span> poll</h2>");
        for(int i = 1; i < poll.size(); i++) {
            temp = (int) (((double) results.get(poll.get(i)) / (double) sum) * 100);
            out.println(poll.get(i) + " " + temp + "% "+results.get(poll.get(i))+" Votes<br>");
        }
        out.println("<br><b>Total Votes "+sum+"</b>");
        // print the end of results html file
        request.getRequestDispatcher("endResults.html").include(request, response);
        out.close();
    }

    /**
     * doGet send to doPost
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // check if error massage IOException | ServletException
        if(err != null && !err.isEmpty()){
            PrintWriter out = response.getWriter();
            request.getRequestDispatcher("head.html").include(request, response);
            out.println("<div class = \"err\">"+ err +"</div>");
            out.println("</body></html>");
            out.close();
            return;
        }

        doPost(request, response);
    }

    /**
     * init run only one time before the servlet started, Initializing the global members
     * @param config ServletConfig
     * @throws ServletException ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        poll = (ArrayList<String>) config.getServletContext().getAttribute("poll");
        results = (HashMap<String,Integer>) config.getServletContext().getAttribute("results");
        err = (String) config.getServletContext().getAttribute("err");

    }
}