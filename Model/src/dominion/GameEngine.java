package dominion;

import dominion.persistence.Database;
import dominion.persistence.DatabaseResults;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Sam on 23/03/2016.
 */
public class GameEngine
{
    private ArrayList<Lobby> lobbies;
    private Database cardDatabase;
    private ArrayList<Card> cardList;


    public GameEngine() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException
    {
        cardList = new ArrayList<Card>();
        cardDatabase = new Database();
        DatabaseResults result = cardDatabase.executeQuery("SELECT * FROM Card");
        for (int i = 0; i < result.size(); i++)
        {
            String cardName = result.getRecord(i).getValue("cardName");
            int type = Integer.parseInt(result.getRecord(i).getValue("type"));
            int cost = Integer.parseInt(result.getRecord(i).getValue("cost"));
            Card currentCard = new Card(cardName, type, cost);
            cardList.add(currentCard);
        }
    }

    public ArrayList<Card> getCardList()
    {
        return cardList;
    }
}
