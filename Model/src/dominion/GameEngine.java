package dominion;

import dominion.exceptions.LobbyNotFoundException;
import dominion.persistence.Database;
import dominion.persistence.DatabaseRecord;
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
        lobbies = new ArrayList<Lobby>();
        cardDatabase = new Database();
        cardList = makeCardList();
    }


    private Ability[] findCardAbilities(String cardName)
    {
        DatabaseResults cardAbilities = cardDatabase.executeQuery("SELECT abilityId, abilityAmount FROM CardAbility WHERE cardName = ?", cardName);
        Ability[] abilities = new Ability[cardAbilities.size()];

        for (int j = 0; j < abilities.length; j++)
        {
            DatabaseRecord abilityRecord = cardAbilities.getRecord(j);

            int abilityId = Integer.parseInt(abilityRecord.getValue("abilityId"));
            int abilityAmount = Integer.parseInt(abilityRecord.getValue("abilityAmount"));

            Ability currentAbility = new Ability(abilityId, abilityAmount);
            abilities[j] = currentAbility;
        }
        return abilities;
    }

    private ArrayList<Card> makeCardList()
    {

        ArrayList<Card> cardList = new ArrayList<Card>();

        DatabaseResults result = cardDatabase.executeQuery("SELECT * FROM Card");
        for (int i = 0; i < result.size(); i++)
        {
            DatabaseRecord cardRecord = result.getRecord(i);

            String cardName = cardRecord.getValue("cardName");
            int type = Integer.parseInt(cardRecord.getValue("type"));
            int cost = Integer.parseInt(cardRecord.getValue("cost"));

            Ability[] abilities = findCardAbilities(cardName);

            Card currentCard = new Card(cardName, type, cost, 0, abilities);
            cardList.add(currentCard);
        }
        return cardList;
    }

    public void createLobby(Account account, String name, String password)
    {
        lobbies.add(new Lobby(account, name, password, this));
    }

    public Lobby findLobby(String name) throws LobbyNotFoundException
    {
        for (Lobby lobby : lobbies)
        {
            if (lobby.getName().equals(name))
            {
                return lobby;
            }
        }
        throw new LobbyNotFoundException();
    }

    public ArrayList<Card> getCardList()
    {
        return cardList;
    }

    public Card findCard(String name)
    {
        Card foundCard = null;
        for(Card card : cardList)
        {
            if (card.getName().equals(name))
            {
                foundCard = card;
            }
        }
        return foundCard;
    }

}
