package Model.Player;

import Model.Coordinates;
import Model.Map.Grid;
import Model.ShotResult;
import Model.Weapon.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class ComputerPlayer extends Player{
    private String name = "Computer";
    private Grid m_myGrid;
    private Grid m_shotsGrid;
    private ShotStrategy shotStrategy;
    private List<Observer> observers;

    /**
     * Constructor for the ComputerPlayer.
     * @param myGrid The computer's grid for its own boats/traps.
     * @param shotsGrid The computer's grid to track its shots against the opponent.
     */
    public ComputerPlayer(Grid myGrid, Grid shotsGrid) {
        this.m_myGrid = myGrid;
        this.m_shotsGrid = shotsGrid;
        this.observers = new ArrayList<>();

        this.shotStrategy = new RandomShotStrategy();
    }

    public void attachObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void notifyObservers() {

    }

    /**
     * Delegates the choice of the next shot coordinates to the current ShotStrategy.
     * @return The coordinates where the computer intends to shoot.
     */
    public Coordinates chooseShot() {
        ShotResult lastResult = null;
        return this.shotStrategy.getNextShot(this.m_shotsGrid, lastResult);
    }

    /**
     * Sets a new strategy for the computer's shooting behavior.
     * @param strategy The new ShotStrategy to use.
     */
    public void setShotStrategy(ShotStrategy strategy) {
        this.shotStrategy = strategy;
    }

    /**
     * Checks if the computer has been defeated.
     * @return true if the computer has lost the game.
     */
    @Override
    public boolean isDefeated() {
        return false;
    }

    /**
     * Initiates the attack sequence against the opponent .
     * @param coordinates The target coordinates on the opponent's grid.
     * @param weapon The weapon used .
     * @return ShotResult.
     */
    @Override
    public ShotResult shoot(Coordinates coordinates, Weapon weapon) {

        // TODO return weapon.apply(targetPlayer, coordinates);
        return null; // Placeholder
    }

    /**
     * Receives an incoming shot from the opponent and processes the impact on grid.
     * @param coordinates The location of the incoming shot.
     * @param weapon The weapon used by the opponent.
     * @return ShotResult .
     */
    @Override
    public ShotResult receiveSHot(Coordinates coordinates, Weapon weapon) {
        // TODO GridCell cell = this.myGrid.getCell(coordinates);
        // TODO return cell.hit();
        return null;
    }

    @Override
    public void update() {
    }
}
