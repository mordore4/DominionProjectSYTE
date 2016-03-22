package model;

/**
 * Created by Sam on 22/03/2016.
 */
public class Card {
    private String name;
    private int cardNumber;
    private int[] effects;

    public Card(String name, int cardNumber, int[] effects)
    {
        this.name = name;
        this.cardNumber = cardNumber;
        this.effects = effects;
    }
}
