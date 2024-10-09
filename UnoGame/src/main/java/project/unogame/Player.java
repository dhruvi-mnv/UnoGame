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

    Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
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

    @Override
    public String toString() {
        return name + "'s hand: " + hand;
    }
    
    
}

