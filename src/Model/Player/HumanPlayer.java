package Model.Player;

import Model.Coordinates;
import Model.Map.Grid;
import Model.ShotResult;
import Model.Weapon.Weapon;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;


public class HumanPlayer extends Player {
    private String m_name;
    private Grid m_myGrid;
    private Grid m_shotsGrid;
    private List<Observer> observers;
    private List<Weapon> availableWeapons;
    // private  List<Trap> traps; TODO

    /**
     * Constructor for the HumanPlayer.
     * @param name The player name.
     * @param myGrid The player grid.
     * @param shotsGrid The opponent grid.
     */
    public HumanPlayer(String name, Grid myGrid, Grid shotsGrid) {
        this.m_name = name;
        this.m_myGrid = myGrid;
        this.m_shotsGrid = shotsGrid;
        this.observers = new ArrayList<>();
        this.availableWeapons = new ArrayList<>();
        // this.traps = new ArrayList<>(); TODO
    }

    public void attachObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Checks if the player has been defeated .
     * @return true if the player has lost the game.
     */
    @Override
    public boolean isDefeated() {
        return false;
    }

    @Override
    public ShotResult shoot(Coordinates coordinates, Weapon weapon) {
        // TODO Trouver le targetPlayer via le Game.
        // TODO return weapon.apply(targetPlayer, coordinates);
        return null;
    }

    @Override
    public ShotResult receiveSHot(Coordinates coordinates, Weapon weapon) {
        // TODO GridCell cell = this.myGrid.getCell(coordinates);
        // TODO return cell.hit();
        return null; // Placeholder
    }

    @Override
    public void update() {

    }
}
