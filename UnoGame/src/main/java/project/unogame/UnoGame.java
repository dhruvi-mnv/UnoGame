/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package project.unogame;

/**
 *
 * @author dhruv
 */
/**
 * Main class of UnoGame. Acts as the controller in MVC.
 * - Implements the Singleton pattern to ensure only one game instance.
 * - Adheres to SOLID principles: SRP (single responsibility for initializing and starting the game).
 */
public class UnoGame {
    
    private static UnoGame instance; // Singleton instance

    private UnoGame() {}

    public static UnoGame getInstance() {
        if (instance == null) {
            instance = new UnoGame();
        }
        return instance;
    }
    
    public static void main(String[] args) {
        GameOfCards game = new GameOfCards();  // Model
        game.startGame();
    }
}

