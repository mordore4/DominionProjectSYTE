package dominion.server;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * Created by Digaly on 19/05/2016.
 */
public interface Command
{
    void runCommand(HttpServletRequest request, PrintWriter writer);
}
