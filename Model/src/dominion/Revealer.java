package dominion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sam on 17/05/2016.
 */
public class Revealer
{
    private Map<String, ArrayList<String>> cardsToReveal;

    public Revealer()
    {
        cardsToReveal = new HashMap<>();
    }

    public void addCardToReveal(String playerName, Card card)
    {
        ArrayList<String> thisPlayersCards = new ArrayList<>();
        if (!cardsToReveal.containsKey(playerName))
        {
            thisPlayersCards.add(card.getName());
            cardsToReveal.put(playerName, thisPlayersCards);
        }
        else {
            thisPlayersCards = cardsToReveal.get(playerName);
            thisPlayersCards.add(card.getName());
        }
        cardsToReveal.put(playerName, thisPlayersCards);
    }

    public Map<String, ArrayList<String>> getCardsToReveal()
    {
        return cardsToReveal;
    }

}
