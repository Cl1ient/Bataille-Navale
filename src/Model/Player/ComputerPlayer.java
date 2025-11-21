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


    public Coordinates chooseShot() {
        ShotResult lastResult = null;
        return this.shotStrategy.getNextShot(this.m_shotsGrid, lastResult);
    }


    public void setShotStrategy(ShotStrategy strategy) {
        this.shotStrategy = strategy;
    }


    @Override
    public boolean isDefeated() {
        return false;
    }


    @Override
    public ShotResult shoot(Coordinates coordinates, Weapon weapon, Player player) {

        // TODO return weapon.apply(targetPlayer, coordinates);
        return null; // Placeholder
    }

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
