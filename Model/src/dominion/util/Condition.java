package dominion.util;

import dominion.Player;

/**
 * Created by Digaly on 19/05/2016.
 */
public abstract class Condition
{
    private transient Player player;

    public Condition(Player player) {
        this.player = player;
    }

    public abstract boolean isFulfilled();

    public Player getPlayer()
    {
        return player;
    }
}
