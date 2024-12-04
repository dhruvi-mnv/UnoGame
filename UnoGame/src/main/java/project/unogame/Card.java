/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.unogame;

/**
 *
 * @author dhruv
 */
/**
 * Represents a card in the Uno game.
 * - Adheres to SRP: manages card properties.
 * - DRY: Constructor and toString methods avoid repetitive logic.
 */

public class Card {
    private final String color;
    private final String value;

    public Card(String color, String value) {
        this.color = color;
        this.value = value;
    }
    
    public String getColor() {
        return color;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return color + " " + value;
    }
    
    public int getValueAsNumber() {
        switch (value) {
            case "Skip": case "Reverse": case "Draw Two":
                return 20;
            case "Wild": case "Wild Draw Four":
                return 50;
            default:
                return Integer.parseInt(value);
        }
    }
}
