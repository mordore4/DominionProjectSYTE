package dominion.tests;

import dominion.*;
import dominion.persistence.*;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by Digaly on 14/04/2016.
 */
public class DatabaseTest
{
    @Test
    public void testDatabaseConnection()
    {
        Database db = null;

        try
        {
            db = new Database();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex)
        {
            ex.printStackTrace();
            fail("There was a problem initializing the JDBC driver.");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            fail("There was a problem connecting to the database.");
        }
    }

    @Test
    public void testImportCards()
    {
        Database db = null;

        try
        {
            db = new Database();
        }
        catch (Exception ex)
        {

        }

        DatabaseResults result = db.executeQuery("SELECT * FROM Card");

        assert (result.size() > 0);
    }
}