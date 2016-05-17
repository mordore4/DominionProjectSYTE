package dominion.tests;

import dominion.*;
import dominion.persistence.*;
import org.junit.*;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by Digaly on 14/04/2016.
 */
public class DatabaseTest
{
    private static Database database;

    @BeforeClass
    public static void beforeClass() throws Exception
    {
        database = new Database();
    }

    @Test
    public void testImportCards()
    {
        DatabaseResults result = database.executeQuery("SELECT * FROM Card");

        assert (result.size() > 0);
    }

    @AfterClass
    public static void afterClass()
    {
        database.close();
    }
}