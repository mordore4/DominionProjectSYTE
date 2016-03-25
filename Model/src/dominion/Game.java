package dominion;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Game
{
    private Card[] cardSet;
    private Player currentPlayer;
    private Player[] players;
    private int phase;
    private boolean isOver;

    public Game(Account[] accounts)
    {
        players = new Player[accounts.length];

        for (int i = 0; i < accounts.length; i++)
        {
            Player newPlayer = new Player();

            newPlayer.setAccount(accounts[i]);

            players[i] = newPlayer;
        }
    }



}
