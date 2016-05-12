package dominion.tests;

import dominion.*;
import dominion.persistence.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by Digaly on 14/04/2016.
 */
public class DatabaseTest
{
    private Database database;

    @Before
    public void setUp() throws Exception
    {
        try
        {
            database = new Database();
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
        DatabaseResults result = database.executeQuery("SELECT * FROM Card");

        assert (result.size() > 0);
    }
}