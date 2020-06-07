package ex2;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * init Servlet start only one to open the file and push the input to ArrayList
 */
@WebServlet(name = "init", urlPatterns = "/init",
        initParams = {@WebInitParam(name = "fileName", value = "poll.txt")})
public class initServlet extends HttpServlet {
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
     * doGet after init forward to results Servlet
     * @param request request
     * @param response response
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("results").forward(request, response);
        return;
    }

    /**
     * init run only one time before the servlet started
     * @param config ServletConfig to push to global of servlets
     * @throws ServletException ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //get the file name
        String fileName = config.getInitParameter("fileName");
        //ArrayList for the input of the file
        ArrayList<String> pollList = new ArrayList<String>();
        BufferedReader file = null;

        try {
            URL url = config.getServletContext().getResource(fileName);
            file = new BufferedReader(new InputStreamReader(url.openStream()));

            String s = new String();
            while ((s = file.readLine()) != null) {
                pollList.add(s);
                log(s);
            }
            // Checks whether the file has at least one question and two answers
            if(pollList.size() < 3){
                throw new ServletException();
            }
            //push to the global of servlets
            config.getServletContext().setAttribute("poll", pollList);
            file.close();
        } catch (IOException | NullPointerException | ServletException e){
            String Error = e.getMessage() + " - bad input";
            config.getServletContext().setAttribute("err", Error);
            log(e.getMessage());
            try {
                file.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        // HashMap for counting the votes
        Map results = new HashMap<String,Integer>();
        for(int i = 1; i < pollList.size(); i++){
            results.put(pollList.get(i), 0);
        }
        config.getServletContext().setAttribute("results", results);
    }
}
