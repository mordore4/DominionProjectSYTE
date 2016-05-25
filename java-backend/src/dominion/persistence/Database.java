package dominion.persistence;

import com.mysql.jdbc.ExceptionInterceptor;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import java.io.IOException;
import java.sql.*;

/**
 * Created by Digaly on 25/03/2016.
 */
public class Database
{
    private String connectionString;
    private Connection conn;

    public Database() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        connectionString = "jdbc:mysql://178.117.107.177/dominion?user=internal&password=ablTDFivUvYJs7VxDscGrIuso32CuQYN";

        Connection testConnection = null;

        try
        {
            testConnection = DriverManager.getConnection(connectionString);
        }
        catch (CommunicationsException e)
        {
            System.out.println("[Database] WARNING! Could not connect to global database, starting local fallback!");

            System.out.println(System.getProperty("user.dir"));

            try
            {
                Runtime.getRuntime().exec("cmd /c \"cd ..\\Misc\\Fallback && start mysql_start.bat\"");
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException ex)
            {
                ex.printStackTrace();
                //Turn over and weep silently
            }

            connectionString = "jdbc:mysql://localhost:3311/dominion?user=root&password=root";
        }
        finally
        {
            if (testConnection != null) {
                try
                {
                    testConnection.close();
                }
                catch(SQLException ex)
                {
                    //Do nothing
                }
            }
        }

        conn = DriverManager.getConnection(connectionString);
    }

    public DatabaseResults executeQuery(String parametrizedSQL, String... parameterValues)
    {
        DatabaseResults results = new DatabaseResults();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try
        {
            stmt = conn.prepareStatement(parametrizedSQL);
            parametrizeStatement(stmt, parameterValues);

            if (stmt.execute()) //If this is true, it means it's a resultset
            {
                rs = stmt.getResultSet();
                populateResults(rs, results);
                return results;
            } else //No resultset, so no results are returned
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

    public void close()
    {
        if (conn != null) {
            try
            {
                conn.close();
            }
            catch(SQLException ex)
            {
                //Do nothing
            }
        }
    }

    public void open()
    {
        try
        {
            conn = DriverManager.getConnection(connectionString);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}