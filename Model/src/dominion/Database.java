package dominion;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Digaly on 25/03/2016.
 */
public class Database
{
    private Connection connection;
    private ArrayList<Card> cardList;

    public Database() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:mysql://digaly.ddns.net/dominion?user=internal&password=ablTDFivUvYJs7VxDscGrIuso32CuQYN");

        this.connection = connection;

        importCards();
    }

    public ArrayList<Card> getCardsOfType(int type)
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
    }

    private void importCards()
    {
        ArrayList<Card> cardList = new ArrayList<Card>();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try
        {
            stmt = connection.prepareStatement("SELECT cardName, type, cost FROM Card");
            rs = stmt.executeQuery();

            while (rs.next())
            {
                Card newCard = new Card(rs.getString("cardName"));
                newCard.setType(rs.getInt("type"));
                newCard.setCost(rs.getInt("cost"));

                cardList.add(newCard);
            }
        } catch (SQLException ex)
        {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally
        {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null)
            {
                try
                {
                    rs.close();
                } catch (SQLException sqlEx)
                {
                } // ignore

                rs = null;
            }

            if (stmt != null)
            {
                try
                {
                    stmt.close();
                } catch (SQLException sqlEx)
                {

                } // ignore

                stmt = null;
            }

        }

        this.cardList = cardList;
    }
}