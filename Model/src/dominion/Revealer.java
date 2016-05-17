package dominion;

import java.util.ArrayList;

/**
 * Created by Sam on 17/05/2016.
 */
public class Revealer
{
    private ArrayList<Card> cardsToReveal;

    public Revealer()
    {
        cardsToReveal = new ArrayList<>();
    }

    public void addCardToReveal(Card card)
    {
        cardsToReveal.add(card);
    }

    public ArrayList<Card> getCardsToReveal()
    {
        return cardsToReveal;
    }

}
