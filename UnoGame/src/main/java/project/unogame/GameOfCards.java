/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project.unogame;

import java.util.*;

/**
 *
 * @author dhruv
 */

public class GameOfCards {
    List<Card> deck;
    List<Player> players;
    Card topCard;

    GameOfCards() {
        deck = new ArrayList<>();
        players = new ArrayList<>();
        createDeck();
        shuffleDeck();
    }

    void createDeck() {
        String[] colors = {"Red", "Green", "Blue", "Yellow"};
        String[] values = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Skip", "Reverse", "Draw Two", "Wild", "Wild Draw Four"};

        for (String color : colors) {
            for (String value : values) {
                if (value.equals("0")) {
                    deck.add(new Card(color, value));
                } else {
                    deck.add(new Card(color, value));
                    deck.add(new Card(color, value)); // Two of each color for 1-9
                }
            }
        }
        deck.add(new Card("Wild", "Wild"));
        deck.add(new Card("Wild", "Wild Draw Four"));
    }

    void shuffleDeck() {
        Collections.shuffle(deck);
    }

    void dealCards(int numCards) {
        for (Player player : players) {
            for (int i = 0; i < numCards; i++) {
                player.drawCard(deck.remove(deck.size() - 1));
            }
        }
    }

    void addPlayer(Player player) {
        players.add(player);
    }

    boolean isValidPlay(Card card) {
        return card.color.equals(topCard.color) || card.value.equals(topCard.value) || card.color.equals("Wild");
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("How many players are you: ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Insert the name of player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            addPlayer(new Player(name));
        }

        dealCards(7); // Deal 7 cards to each player

        // Draw a valid starting card (not skip, reverse, draw two, wild, or wild draw four)
        do {
            topCard = deck.remove(deck.size() - 1);
        } while (topCard.value.equals("Skip") || topCard.value.equals("Reverse") || 
                 topCard.value.equals("Draw Two") || topCard.color.equals("Wild"));

        System.out.println("Starting card: " + topCard);

        int currentPlayerIndex = 0;
        boolean gameOn = true;

        while (gameOn) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println(currentPlayer.name + "'s turn:");
            System.out.println(currentPlayer);
            System.out.println("Top card: " + topCard);
            Card playedCard = null;
            boolean validPlay = false;

            while (!validPlay) {
                System.out.print("Ask for a card to play (e.g., 'Red 5') or type 'draw' to pick a card: ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("draw")) {
                    if (!deck.isEmpty()) {
                        Card drawnCard = deck.remove(deck.size() - 1);
                        currentPlayer.drawCard(drawnCard);
                        System.out.println(currentPlayer.name + " drew a card: " + drawnCard);
                        // Check if the drawn card can be played
                        if (isValidPlay(drawnCard)) {
                            System.out.print("You can play the drawn card! Play it? (yes/no): ");
                            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                                playedCard = drawnCard;
                                validPlay = true; // Exit the loop on valid play
                            }
                        }
                    } else {
                        System.out.println("Deck is empty! You cannot draw.");
                        validPlay = true; // End turn
                    }
                } else {
                    String[] cardInput = input.split(" ");
                    if (cardInput.length == 2) {
                        playedCard = new Card(cardInput[0], cardInput[1]);
                        if (isValidPlay(playedCard)) {
                            currentPlayer.playCard(playedCard); // Remove played card from hand
                            topCard = playedCard; // Update top card
                            System.out.println("Played: " + playedCard);
                            validPlay = true; // Exit the loop on valid play

                            // Handle special cards
                            if (playedCard.value.equals("Draw Two")) {
                                int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
                                for (int i = 0; i < 2; i++) {
                                    players.get(nextPlayerIndex).drawCard(deck.remove(deck.size() - 1));
                                }
                            } else if (playedCard.value.equals("Wild Draw Four")) {
                                int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
                                for (int i = 0; i < 4; i++) {
                                    players.get(nextPlayerIndex).drawCard(deck.remove(deck.size() - 1));
                                }
                            } else if (playedCard.value.equals("Skip")) {
                                currentPlayerIndex = (currentPlayerIndex + 1) % players.size(); // Skip the next player's turn
                            }
                        } else {
                            System.out.println("Invalid play! Try again.");
                        }
                    } else {
                        System.out.println("Invalid input! Please enter in the format 'Color Value'.");
                    }
                }
            }

            if (currentPlayer.hasUno()) {
                System.out.println(currentPlayer.name + " has UNO!");
            }

            // Check for win
            if (currentPlayer.hand.isEmpty()) {
                System.out.println(currentPlayer.name + " wins!");
                gameOn = false; // End game
            }

            // Move to next player
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }

        scanner.close();
    }
}

