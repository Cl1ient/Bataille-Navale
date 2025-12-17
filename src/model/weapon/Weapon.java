package model.weapon;

import model.Coordinate;

import java.util.List;

public interface Weapon {
    /**
     * Generates the list of actual coordinates that will be affected by this weapon.
     * This method is used by the Game class to determine the impact zone.
     * @param coord The primary target coordinate.
     * @param gridSize the size of the grid.
     * @return A list of Coordinates that will receive an impact.
     */
    List<Coordinate> generateTargets(Coordinate coord, int gridSize);

    /**
     *
      * @return The name of the Weapon
     */
    String getName();

    /**
     * @return The number of uses remaining for this weapon.
     */
    Integer getUsesLeft();

    void setUsesLeft();
    /**
     * Decrements the remaining uses of the weapon after it has been fired.
     */
    void use();

    public boolean isOffensive();
}
