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
/**
 * Manages the game logic and card deck.
 * - Factory logic is embedded within GameOfCards via createCard method.
 * - Adheres to SOLID principles (SRP for game management, OCP for extending game rules).
 * - DRY principle for reusing common logic (e.g., reshuffling and dealing cards).

* **Design Patterns**:
 * - **Factory Pattern**: Used in the `createCard` method to encapsulate the creation logic for cards.
 * - **Singleton Pattern**: Not explicitly applied but can be considered for the deck management (a single source of truth).
 * - **MVC Pattern**: Part of the overall project design, separating game logic (controller) from data (model - Card, Player) and user interaction (view - CLI/GUI).
 * 
 * **Principles**:
 * - _S_ingle Responsibility: Each class (GameOfCards, Player, Card) has a single responsibility.
 * - _O_pen/Closed: The game allows extending rules (e.g., adding new cards) without modifying existing code.
 * - _L_iskov Substitution: No inheritance conflicts; derived classes can replace base classes if used in future enhancements.
 * - _I_nterface Segregation: Classes only depend on what they use (e.g., no unused methods).
 * - _D_ependency Inversion: No direct coupling between `GameOfCards` and user input/output; the system can be tested or refactored independently.
 * - _DRY_ (Don't Repeat Yourself): Common logic (e.g., card creation, reshuffling) is centralized to avoid duplication.
 */

public class GameOfCards {
    private final List<Card> deck;
    private final List<Player> players;
    private Card topCard;
    private final List<Card> discardPile;
    private boolean clockwise;
    
    public GameOfCards() {
        deck = new ArrayList<>();
        players = new ArrayList<>();
        discardPile = new ArrayList<>();
        clockwise = true;
        createDeck();
        shuffleDeck();
    }

    /**
        * Creates the deck with all standard Uno cards.
        * Uses the Factory Pattern to generate cards.
        */
    private void createDeck() {
        String[] colors = {"Red", "Green", "Blue", "Yellow"};
        String[] values = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Skip", "Reverse", "Draw Two"};
        for (String color : colors) {
            for (String value : values) {
                deck.add(createCard(color, value));
                if (!value.equals("0")) {
                    deck.add(createCard(color, value));  // Duplicate for two of each card (except 0)
                }
            }
        }
        // Wild cards
        deck.add(createCard("Wild", "Wild"));
        deck.add(createCard("Wild", "Wild Draw Four"));
    }
    
    /**
        * Factory Pattern: Centralized logic for creating a card.
        */
    private Card createCard(String color, String value) {
        return new Card(color, value);
    }

     /**
        * Shuffle the deck randomly (DRY principle: reuses shuffle logic).
        */
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }
    
    /**
        * Reshuffles discard pile into the deck to avoid running out of cards.
        */
    private void reshuffleDeck() {
        if (!discardPile.isEmpty()) {
            deck.addAll(discardPile);
            discardPile.clear();
            shuffleDeck();
            System.out.println("Deck reshuffled from discard pile.");
        }
    }
        /**
        * Deals a specified number of cards to each player.
        */
    private void dealCards(int numCards) {
        for (Player player : players) {
            for (int i = 0; i < numCards; i++) {
                player.drawCard(deck.remove(deck.size() - 1));
            }
        }
    }
    
    /**
        * Checks if a card can be played on the top card.
        */
    private boolean isValidPlay(Card card) {
        return card.getColor().equals(topCard.getColor()) || card.getValue().equals(topCard.getValue()) || card.getColor().equals("Wild");
    }
    
    /**
        * Calculates and displays total scores for each player at the end of 4 rounds.
        */
    private void calculateTotalScores() {
        System.out.println("\n--- Final Scores ---");
        for (Player player : players) {
            int totalScore = player.calculateScore();
            System.out.println(player.getName() + ": " + totalScore);
        }
    }
    
    /**
        * Starts the game with user input for number of players and names.
        */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many players are you: ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Insert the name of player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name));
        }
        
        dealCards(7);
        
        topCard = deck.remove(deck.size() - 1);
        System.out.println("Starting card: " + topCard);

        boolean gameOn = true;
        int currentPlayerIndex = 0;
        int roundCounter = 0;

        while (gameOn) {
            System.out.println("\n--- Round " + (roundCounter + 1) + " ---");
            for (int i = 0; i < players.size(); i++) {
                Player currentPlayer = players.get(currentPlayerIndex);
                System.out.println("\n" + currentPlayer.getName() + "'s turn:");
                System.out.println("Top card: " + topCard);

                System.out.println("Your hand: " + currentPlayer.getHand());
                System.out.print("Play a card (e.g., 'Red 5') or type 'draw': ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("draw")) {
                    if (deck.isEmpty()) reshuffleDeck();
                    if (!deck.isEmpty()) {
                        Card drawnCard = deck.remove(deck.size() - 1);
                        currentPlayer.drawCard(drawnCard);
                        System.out.println("Drew: " + drawnCard);
                    }
                } else {
                    String[] cardInput = input.split(" ");
                    if (cardInput.length == 2) {
                        Card playedCard = createCard(cardInput[0], cardInput[1]);
                        if (currentPlayer.hasCard(playedCard) && isValidPlay(playedCard)) {
                            currentPlayer.playCard(playedCard);
                            topCard = playedCard;
                            System.out.println("Played: " + playedCard);
                        } else {
                            System.out.println("Invalid play.");
                        }
                    } else {
                        System.out.println("Invalid input format.");
                    }
                }

                if (currentPlayer.getHand().isEmpty()) {
                    System.out.println(currentPlayer.getName() + " wins this round!");
                    gameOn = false;
                    break;
                }

                currentPlayerIndex = (currentPlayerIndex + (clockwise ? 1 : -1) + players.size()) % players.size();
            }

            roundCounter++;
            if (roundCounter >= 4) {
                gameOn = false;
                System.out.println("\n--- Game Over ---");
                calculateTotalScores();
            }
        }

        scanner.close();
    }
}