package dominion;

import java.util.ArrayList;

/**
 * Created by Digaly on 24/03/2016.
 */
public class Lobby
{
    private ArrayList<Account> playersInLobby;
    private Game game;
    private String name;
    private String password;


    public Lobby(Account account, String name, String password)
    {
        playersInLobby.add(account);
        this.name = name;
        this.password = password;
    }

    public void addPlayer(Account account)
    {
        if (playersInLobby.size() < 4)
        {
            playersInLobby.add(account);
        }
    }



}
