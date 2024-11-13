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

public class Player {
    String name;
    List<Card> hand;
    int score; // New attribute to keep track of the score

    Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0; // Initialize score to 0
    }

    void drawCard(Card card) {
        hand.add(card);
    }

    void playCard(Card card) {
        hand.remove(card);
    }

    boolean hasUno() {
        return hand.size() == 1; // Check if the player has only one card left
    }
    
    public boolean hasCard(Card card) {
        for (Card c : hand) {
            if (c.color.equals(card.color) && c.value.equals(card.value)) {
                return true;
            }
        }
        return false;
    }
    
    void calculateScore() {
        this.score = 0;  // Reset score each time it's recalculated
        // Calculate score based on remaining cards
        for (Card card : hand) {
            if (card.value.equals("Wild") || card.value.equals("Wild Draw Four")) {
                score += 50; // Assign points for Wild cards
            } else if (card.value.equals("Draw Two") || card.value.equals("Skip") || card.value.equals("Reverse")) {
                score += 20; // Assign points for special cards
            } else {
                score += Integer.parseInt(card.value); // Assign points for numbered cards
            }
        }
    }

    @Override
    public String toString() {
        return name + "'s hand: " + hand;
    } 
}

