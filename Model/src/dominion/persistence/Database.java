package dominion.persistence;

import java.sql.*;

/**
 * Created by Digaly on 25/03/2016.
 */
public class Database
{
    private Connection connection;

    public Database() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        this.connection = DriverManager.getConnection("jdbc:mysql://digaly.ddns.net/dominion?user=internal&password=ablTDFivUvYJs7VxDscGrIuso32CuQYN");
    }

    public DatabaseResults executeQuery(String parametrizedSQL, String... parameterValues)
    {
        DatabaseResults results = new DatabaseResults();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try
        {
            stmt = connection.prepareStatement(parametrizedSQL);
            parametrizeStatement(stmt, parameterValues);

            if (stmt.execute()) //If this is true, it means it's a resultset
            {
                rs = stmt.getResultSet();
                populateResults(rs, results);
                return results;
            }
            else //No resultset, so no results are returned
            {
                return null;
            }
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally
        {
            cleanup(stmt, rs);
        }

        return null;
    }

    private void parametrizeStatement(PreparedStatement statement, String[] paremeters) throws SQLException
    {
        for (int i = 1; i <= paremeters.length; i++) //Parameters are NOT zero-based
        {
            String currentParam = paremeters[i - 1]; //While the array IS zero-based

            try
            {
                int num = Integer.parseInt(currentParam); //Naive way to support parametrizing ints
                statement.setInt(i, num);
            }
            catch (NumberFormatException e) //if it's not a number, set a parameter string
            {
                statement.setString(i, currentParam);
            }
        }
    }

    private void populateResults(ResultSet rs, DatabaseResults results) throws SQLException
    {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnsCount = metadata.getColumnCount();

        while (rs.next())
        {
            DatabaseRecord record = new DatabaseRecord();

            for (int i = 1; i <= columnsCount; i++)
            {
                record.setValue(metadata.getColumnName(i), rs.getString(i)); //getString/getColumnName are NOT zero-based
            }

            results.addRecord(record);
        }
    }

    private void cleanup(PreparedStatement stmt, ResultSet rs)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
            }
            catch (SQLException sqlEx)
            {
                //Ignore
            }

            rs = null;
        }

        if (stmt != null)
        {
            try
            {
                stmt.close();
            }
            catch (SQLException sqlEx)
            {
                //Ignore
            }

            stmt = null;
        }
    }

    //This responsibility should be moved to GameEngine, and not Database

    /*public ArrayList<Card> getCardsOfType(int type)
    {
        ArrayList<Card> filteredCards = new ArrayList<Card>();

        for (int i = 0; i < cardList.size(); i++)
        {
            if (cardList.get(i).getType() == type)
            {
                filteredCards.add(cardList.get(i));
            }
        }

        return filteredCards;
    }*/
}