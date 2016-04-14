package dominion;

import java.util.ArrayList;

/**
 * Created by Digaly on 24/03/2016.
 */
public class Lobby
{
    private ArrayList<Account> playersInLobby;


    public Lobby()
    {

    }

    public void addPlayer(Account account)
    {
        if (playersInLobby.size() < 4)
        {
            playersInLobby.add(account);
        }
    }



}
