package Model.Map;
import Model.Coordinate;
import Model.GridEntity;

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
    public boolean placeEntity(GridEntity entity, List<Coordinate> coordinates) {

        for (Coordinate coord : coordinates) {
            int row = coord.getRow();
            int col = coord.getColumn();
            if (this.cells[row][col].isOccuped()) {
                return false;
            }
        }

        int indexInEntity = 0;
        for (Coordinate coord : coordinates) {
            int row = coord.getRow();
            int col = coord.getColumn();

            this.cells[row][col] = new GridCell(entity, indexInEntity);
            indexInEntity++;
        }
        return true;
    }

    /**
     * Retrieves the GridCell at the specified coordinates.
     * @param coordinate The location to check.
     * @return The GridCell object.
     */
    public GridCell getCell(Coordinate coordinate) {
        int row = coordinate.getRow();
        int col = coordinate.getColumn();
        return this.cells[row][col];
    }
}


