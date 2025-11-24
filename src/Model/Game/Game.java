package Model.Game;

import Model.Player.HumanPlayer;
import Model.Player.ComputerPlayer;
import Model.Player.Player;
import Model.ShotResult;
import Model.Coordinate;
import Model.Weapon.Weapon;

public class Game {

    private final HumanPlayer m_humanPlayer;
    private final ComputerPlayer m_computerPlayer;
    private final GameConfiguration m_config;
    // TODO private final List<Observer> observers;
    private int turnNumber;
    private boolean gameOver;
    private Player currentPlayer;

    /**
     * Constructor for the Game class.
     * @param config The configuration settings for the game (size, modes, levels).
     * @param hp The HumanPlayer instance.
     * @param cp The ComputerPlayer instance.
     */
    public Game(GameConfiguration config, HumanPlayer hp, ComputerPlayer cp) {
        this.m_config = config;
        this.m_humanPlayer = hp;
        this.m_computerPlayer = cp;
        // TODO this.observers = new ArrayList<>();
        this.turnNumber = 0;
        this.gameOver = false;
        this.currentPlayer = hp;
    }

    // TODO public void attachObserver(Observer observer) {}
    // TODO public void notifyObservers() {}

    public void startGame() {
        this.turnNumber = 1;
        this.gameOver = false;
        this.currentPlayer = this.m_humanPlayer;
        // TODO this.notifyObservers();
    }

    /**
     * Processes the turn initiated by the human player.
     * Method is called by the GameController.
     * @param coordinate The target coordinates chosen by the human.
     * @param weapon The weapon selected for the attack.
     * @return The ShotResult of the action.
     */
    public ShotResult processHumanTurn(Coordinate coordinate, Weapon weapon) {
        if (gameOver) return null;
        ShotResult result = this.m_humanPlayer.shoot(coordinate, weapon, this.m_computerPlayer);
        // TODO Traitement du résultat (Tornade, check victoire, etc.)
        // TODO Si  piège touché, la logique est dans le ShotResult ou le Piège.
        this.checkGameOver();
        this.nextTurn();
        return result;
    }


    public ShotResult processComputerTurn() {
        if (gameOver) return null;
        Coordinate targetCoord = this.m_computerPlayer.chooseShot();
        Weapon defaultWeapon = null;
        ShotResult result = this.m_computerPlayer.shoot(targetCoord, defaultWeapon, this.m_humanPlayer);
        this.checkGameOver();
        this.nextTurn();
        return result;
    }

    private void checkGameOver() {
        if (this.m_humanPlayer.isDefeated() || this.m_computerPlayer.isDefeated()) {
            this.gameOver = true;
            // TODO this.notifyObservers();
        }
    }

    public void nextTurn() {
        if (this.currentPlayer == this.m_humanPlayer) {
            this.currentPlayer = this.m_computerPlayer;
        } else {
            this.currentPlayer = this.m_humanPlayer;
            this.turnNumber++;
        }
        // TODO this.notifyObservers();
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public void restartGame(GameConfiguration newConfig) {
    }
}