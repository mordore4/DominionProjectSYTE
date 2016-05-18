package dominion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sam on 17/05/2016.
 */
public class Revealer
{
    private Map<String, String> cardsToReveal;

    public Revealer()
    {
        cardsToReveal = new HashMap<>();
    }

    public void addCardToReveal(String playerName, Card card)
    {
        cardsToReveal.put(playerName, card.getName());
    }

    public Map<String, String> getCardsToReveal()
    {
        return cardsToReveal;
    }

}
