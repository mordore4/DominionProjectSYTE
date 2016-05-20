package dominion.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Digaly on 19/05/2016.
 */
@WebServlet(name = "GameManager")
public class GameManager extends HttpServlet
{
    @Override
    public void init()
    {
        ServletContext servletContext = getServletContext();
        HTMLController htmlController = new HTMLController();

        servletContext.setAttribute("HTMLController", htmlController);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        ServletContext servletContext = getServletContext();
        HTMLController htmlController  = (HTMLController) servletContext.getAttribute("HTMLController");
        PrintWriter writer = response.getWriter();

        htmlController.executeCommand(request, writer);
    }
}
