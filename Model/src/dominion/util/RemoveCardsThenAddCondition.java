package dominion.util;

import dominion.Deck;
import dominion.Game;
import dominion.Player;

/**
 * Created by Tom Dobbelaere on 23/05/2016.
 */
public class RemoveCardsThenAddCondition extends Condition
{
    private int startHandSize;
    private boolean isDone;
    private transient Game game;

    public RemoveCardsThenAddCondition(Player player, Game game)
    {
        super(player);

        this.startHandSize = player.getHand().size() - 1;
        this.isDone = false;
        this.game = game;
    }

    @Override
    public boolean isFulfilled()
    {
        Deck playerHand = getPlayer().getHand();
        int cardsRemoved = startHandSize - playerHand.size();

        if (isDone)
        {
            game.findCurrentPlayer().addCards(cardsRemoved);

            if (getPlayer().hasActionCards() && getPlayer().getActions() > 0)
            {
                game.setPhase(0);
            }
            else
            {
                game.setPhase(1);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void finish()
    {
        this.isDone = true;
        isFulfilled();
    }
}
