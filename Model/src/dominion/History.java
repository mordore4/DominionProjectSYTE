package dominion;

import dominion.persistence.Database;

/**
 * Created by Tom Dobbelaere on 14/05/2016.
 */
public class History
{
    private Database database;

    public History() {
        try
        {
            database = new Database();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



}
