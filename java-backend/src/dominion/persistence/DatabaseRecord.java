package dominion.persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom Dobbelaere ox²n 26/03/2016.
 */
public class DatabaseRecord
{
    private Map<String, String> rowValues;

    public DatabaseRecord()
    {
        rowValues = new HashMap<>();
    }

    public void setValue(String columnName, String value)
    {
        rowValues.put(columnName, value);
    }

    public String getValue(String columnName)
    {
        return rowValues.get(columnName);
    }

}
