import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Digaly on 2/05/2016.
 */
public class Test extends javax.servlet.http.HttpServlet
{
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException
    {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException
    {
        ArrayList<Dummy> test = new ArrayList<Dummy>();
        test.add(new Dummy("Sam", 100, true));
        test.add(new Dummy("Tom", 80, false));
        test.add(new Dummy("Yannis", 64, false));

        response.getWriter().write(new Gson().toJson(test));
    }
}
