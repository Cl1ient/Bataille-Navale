package Model.Map;

import Model.Coordinates;
import Model.GridEntity;
import Model.ShotResult;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final Integer size;
    private final GridCell[][] cells;

    /**
     * Constructor for the Grid.
     * Initializes the grid array (cells) and the size.
     * @param size The dimension of the square grid .
     */
    public Grid(Integer size) {
        this.size = size;
        this.cells = new GridCell[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.cells[row][col] = new GridCell(null, 0);
            }
        }
    }

    /**
     * Places a GridEntity (Boat, Trap) in the grid.
     * @param entity The entity to place.
     * @param coordinates The list of coordinates the entity will occupy.
     * @return true if placement was successful .
     */
    public boolean placeEntity(GridEntity entity, List<Coordinates> coordinates) {

        for (Coordinates coord : coordinates) {
            int row = coord.getRow();
            int col = coord.getColumn();
            if (this.cells[row][col].isOccuped()) {
                return false;
            }
        }

        int indexInEntity = 0;
        for (Coordinates coord : coordinates) {
            int row = coord.getRow();
            int col = coord.getColumn();

            this.cells[row][col] = new GridCell(entity, indexInEntity);
            indexInEntity++;
        }
        return true;
    }

    /**
     * Retrieves the GridCell at the specified coordinates.
     * @param coordinates The location to check.
     * @return The GridCell object.
     */
    public GridCell getCell(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getColumn();
        return this.cells[row][col];
    }


    /**
     * Attempts to search an Island Cell for an item.
     * @param coordinates The location to search.
     * @return The ShotResult indicating the outcome of the search.
     */
    public ShotResult searchIsland(Coordinates coordinates) {
        return null;
    }
}


