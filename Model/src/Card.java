/**
 * Created by Sam on 23/03/2016.
 */
public class Card {
    private String name;
    private int[] abilities; //possibly wrong, Abilities might become class
    private int type;
    private int cost;
    private int points; //Correct place???
    //private int count; Amount still buyable, probably not the right class

    public String getName()
    {
        return name;
    }

    public int getType()
    {
        return type;
    }

    public int getCost() {
        return cost;
    }

    public int getPoints() {
        return points;
    }
}
