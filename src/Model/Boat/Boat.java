package Model.Boat;

import Model.GridEntity;

public interface Boat extends GridEntity {
    /**
     *
     * @return true if the boat is sunk
     */
    boolean isSunk();

    /**
     * Marks the specific segment of the ship that was hit.
     * @param index index the position of the ship
     */
    void onHit(Integer index);

    /**
     *
     * @return the size of the boat
     */
    Integer getSize();

    /**
     *
     * @return the name of the boat
     */
    String getName();
}
