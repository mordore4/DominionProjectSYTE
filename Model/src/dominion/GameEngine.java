package dominion;

import dominion.exceptions.LobbyNotFoundException;
import dominion.persistence.Database;
import dominion.persistence.DatabaseRecord;
import dominion.persistence.DatabaseResults;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sam on 23/03/2016.
 */
public class GameEngine
{
    private ArrayList<Lobby> lobbies;
    private Database cardDatabase;
    private HashMap<String, Card> cardList;

    public GameEngine() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException
    {
        lobbies = new ArrayList<>();
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

    private HashMap<String, Card> makeCardList()
    {
        HashMap<String, Card> cardList = new HashMap<>();

        DatabaseResults result = cardDatabase.executeQuery("SELECT * FROM Card");
        for (int i = 0; i < result.size(); i++)
        {
            DatabaseRecord cardRecord = result.getRecord(i);

            String cardName = cardRecord.getValue("cardName");
            int type = Integer.parseInt(cardRecord.getValue("type"));
            int cost = Integer.parseInt(cardRecord.getValue("cost"));

            Ability[] abilities = findCardAbilities(cardName);

            Card currentCard = new Card(cardName, type, cost, 0, abilities);
            cardList.put(cardName, currentCard);
        }
        return cardList;
    }

    public void createLobby(String playerName, String lobbyName)
    {
        lobbies.add(new Lobby(playerName, lobbyName, cardList));
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

    public HashMap<String, Card> getCardList()
    {
        return cardList;
    }

    public Card findCard(String name)
    {
        return cardList.get(name);
    }

}
