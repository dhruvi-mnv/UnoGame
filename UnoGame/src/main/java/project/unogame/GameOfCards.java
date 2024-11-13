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
    List<Card> discardPile;
    boolean clockwise;
    
    GameOfCards() {
        deck = new ArrayList<>();
        players = new ArrayList<>();
        discardPile = new ArrayList<>();
        clockwise = true;
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
    
    void reshuffleDeck() {
        if (!discardPile.isEmpty()) {
            deck.addAll(discardPile);
            discardPile.clear();
            shuffleDeck();
            System.out.println("Deck reshuffled from discard pile.");
        }
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
    
    private void applySpecialCardEffect(Card playedCard, int currentPlayerIndex) {
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
    } else if (playedCard.color.equals("Wild")) {
        Scanner scanner = new Scanner(System.in);  // Initialize the scanner here
        System.out.print("Choose a color (Red, Green, Blue, Yellow): ");
        String chosenColor = scanner.nextLine();
        topCard = new Card(chosenColor, playedCard.value);
        }
    }
    
    private void displayScores() {
        System.out.println("\nFinal Scores:");
        for (Player player : players) {
            player.calculateScore(); // Make sure to calculate score before displaying it
            System.out.println(player.name + "'s score: " + player.score);
        }
    }
    
    public void startGame() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many players are you: ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Insert the name of player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            addPlayer(new Player(name));
        }
        
        dealCards(7);

        int currentPlayerIndex = 0;
        int roundCount = 0;

        // Draw a valid starting card (not skip, reverse, etc.)
        do {
            topCard = deck.remove(deck.size() - 1);
        } while (topCard.value.equals("Skip") || topCard.value.equals("Reverse") || 
                 topCard.value.equals("Draw Two") || topCard.color.equals("Wild"));

        System.out.println("Starting card: " + topCard);

        boolean gameOn = true;
        while (gameOn) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("\n" + currentPlayer.name + "'s turn:");
            System.out.println("Top card: " + topCard);
            
            // Display the player's hand
            System.out.println("Your hand: " + currentPlayer.hand);

            boolean validPlay = false;

            while (!validPlay) {
                System.out.print("Play a card (e.g., 'Red 5') or type 'draw' to pick a card: ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("draw")) {
                    
                    
                    if (deck.isEmpty()) {
                        reshuffleDeck(); // Reshuffle if deck is empty
                    }
                    if (!deck.isEmpty()) {
                        Card drawnCard = deck.remove(deck.size() - 1);
                        currentPlayer.drawCard(drawnCard);
                        System.out.println(currentPlayer.name + " drew: " + drawnCard);

                        // Check if drawn card is valid to play immediately
                        if (isValidPlay(drawnCard)) {
                            System.out.print("Play the drawn card? (yes/no): ");
                            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                                currentPlayer.playCard(drawnCard);
                                topCard = drawnCard;
                                validPlay = true;
                                System.out.println("Played: " + drawnCard);

                                applySpecialCardEffect(drawnCard, currentPlayerIndex);
                            }
                        }
                    } 
                    validPlay = true; // Turn ends after drawing
                } else {
                    // Try to play a specific card from hand
                    String[] cardInput = input.split(" ");
                    if (cardInput.length == 2) {
                        String color = cardInput[0];
                        String value = cardInput[1];
                        Card playedCard = new Card(color, value);

                        if (currentPlayer.hasCard(playedCard) && isValidPlay(playedCard)) {
                            currentPlayer.playCard(playedCard);
                            topCard = playedCard;
                            validPlay = true;
                            System.out.println("Played: " + playedCard);

                            applySpecialCardEffect(playedCard, currentPlayerIndex);
                        } else {
                            System.out.println("Invalid play. Try again.");
                        }
                    } else {
                        System.out.println("Invalid input format! Enter 'Color Value' or 'draw'.");
                    }
                }
            }
            
          
            // Check if current player has won
            if (currentPlayer.hand.isEmpty()) {
                System.out.println(currentPlayer.name + " wins the game!");
                gameOn = false;
                break;
            }
            
            // Increment the round counter and check if 4 rounds have been completed
            roundCount++;
            if (roundCount >= numPlayers * 4) {
                System.out.println("\nGame over! 4 rounds have been completed.");
                gameOn = false;
                break;
            }

            // Advance to next player
            currentPlayerIndex = (currentPlayerIndex + (clockwise ? 1 : -1) + players.size()) % players.size();
        }
        
        // Display final scores after the game ends
        displayScores();

        scanner.close();
    }

 }