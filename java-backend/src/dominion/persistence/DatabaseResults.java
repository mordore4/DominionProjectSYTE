package dominion.persistence;

import java.util.ArrayList;

/**
 * Created by Tom Dobbelaere on 26/03/2016.
 */
public class DatabaseResults
{
    private ArrayList<DatabaseRecord> results;

    public DatabaseResults()
    {
        results = new ArrayList<>();
    }

    public void addRecord(DatabaseRecord record)
    {
        results.add(record);
    }

    public DatabaseRecord getRecord(int row)
    {
        return results.get(row);
    }

    public int size()
    {
        return results.size();
    }
}
