/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.unogame;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author dhruv
 */

/**
 * Represents a player in the Uno game.
 * - SRP: Manages player-specific operations.
 * - DRY: Reuses common methods for drawing and playing cards.
 */

public class Player {
    private final String name;
    private final List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public void drawCard(Card card) {
        hand.add(card);
    }

    public void playCard(Card card) {
        hand.remove(card);
    }

    public boolean hasCard(Card card) {
        return hand.stream().anyMatch(c -> c.getColor().equals(card.getColor()) && c.getValue().equals(card.getValue()));
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "'s hand: " + hand;
    }
    
    public int calculateScore() {
        int totalScore = 0;
        for (Card card : hand) {
            totalScore += card.getValueAsNumber(); // Implement a method in Card to convert values like "5", "Draw Two", etc., to numeric scores.
        }
        return totalScore;
    }
}

