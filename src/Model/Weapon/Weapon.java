package Model.Weapon;

import Model.Coordinate;
import Model.Map.Grid;

import java.util.List;

public interface Weapon {
    /**
     * Generates the list of actual coordinates that will be affected by this weapon.
     * This method is used by the Game class to determine the impact zone.
     * @param coord The primary target coordinate.
     * @param grid The grid being targeted.
     * @return A list of Coordinates that will receive an impact.
     */
    List<Coordinate> generateTargets(Coordinate coord, Grid grid);

    /**
     *
      * @return The name of the Weapon
     */
    String getName();

    /**
     * @return The number of uses remaining for this weapon.
     */
    Integer getUsesLeft();

    /**
     * Decrements the remaining uses of the weapon after it has been fired.
     */
    void use();
}
