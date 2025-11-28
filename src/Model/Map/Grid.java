package Model.Map;

import Model.Coordinates;
import Model.EntityType;
import Model.GridEntity;
import Model.Boat.Boat;
import Model.entity.island.IslandItemFactory;

import java.util.List;
import java.util.Map;

public class Grid {

    private final Integer m_size;
    private final Cell[][] cells;
    // TODO private final TrapFactory m_trapFactory;
    // TODO private final IslandItemFactory m_islandItemFactory;
    private final List<Boat> m_ownBoats;
    private final IslandItemFactory m_islandItemFactory;
    /**
     * Constructor for the Grid.
     * Initializes the grid array (cells), size, and factories.
     * @param entityPosition Map of EntityType to a List of Coordinates for initial placement.
     * @param gridSize The dimension of the square grid.
     */
    public Grid(Map<EntityType, List<Coordinates>> entityPosition, Integer gridSize) {
        this.m_size = gridSize;
        this.cells = new Cell[gridSize][gridSize];
        this.m_ownBoats = new ArrayList<>();
        this.m_islandItemFactory = new IslandItemFactory();
        // TODO this.m_trapFactory = new TrapFactory();
        // TODO this.m_islandItemFactory = new IslandItemFactory();

        // init all the cell
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.cells[i][j] = new Cell();
            }
        }
    }

    public void placeEntity(Map<EntityType, List<Coordinates>> entityPosition) {

        for (Map.Entry<EntityType, List<Coordinates>> entry : entityPosition.entrySet()) {
            EntityType type = entry.getKey();
            List<Coordinates> coordinates = entry.getValue();
            GridEntity entity = createEntityFromType(type, coordinates.size());
            if (entity != null) {
                placeSingleEntity(entity, coordinates);
            }
        }
    }

    private boolean placeSingleEntity(GridEntity entity, List<Coordinates> coordinates) {
        for (Coordinates coord : coordinates) {
            if (!isInside(coord)) {
                return false;
            }
            int row = coord.getX();
            int col = coord.getY();

            if (this.cells[row][col].getEntity() != null) {
                return false; // Cell occuped
            }
        }

        for (Coordinates coord : coordinates) {
            int row = coord.getX();
            int col = coord.getY();

            this.cells[row][col].setEntity(entity);
            if (entity instanceof Boat && !m_ownBoats.contains(entity)) {
                m_ownBoats.add((Boat) entity);
            }
        }
        return true;
    }

    private GridEntity createEntityFromType(EntityType type, int size) {
        switch (type){
            case NEW_BOMB-> m_islandItemFactory.createNewItemBomb();
            case NEW_BLACKHOLE -> m_islandItemFactory.createNEwItemBlackHole();
            case NEW_STORM -> m_islandItemFactory.createNewItemStorm();
            case NEW_SONAR -> m_islandItemFactory.createNewItemSonar();
        }
        return null;
    }

    public Cell getCell(int x, int y) {
        if (isInside(x, y)) {
            return cells[x][y];
        }
        return null;
    }

    public boolean isInside(Coordinates coord) {
        return isInside(coord.getX(), coord.getY());
    }

    private boolean isInside(int x, int y) {
        return x >= 0 && x < m_size && y >= 0 && y < m_size;
    }

    public void hit(int x, int y) {
        if (!isInside(x, y)) return;
        Cell targetCell = cells[x][y];
        targetCell.setHit(true);
        GridEntity entity = targetCell.getEntity();
        if (entity != null) {
            // TODO entity.onHit(attacker, defender, x, y)
        } else {
            targetCell.setMiss(true);
        }
    }

    public void desactiveTrap(int x, int y) {
        if (!isInside(x, y)) return;

        Cell targetCell = cells[x][y];
        GridEntity entity = targetCell.getEntity();

        // TODO logique : retirer l'entité si c'est un piège
        // targetCell.setEntity(null);
    }

    public void markMiss(int x, int y) {
        if (!isInside(x, y)) return;
        cells[x][y].setMiss(true);
    }

    public void triggerTornado(Coordinates coord) {
        if (!isInside(coord)) return;
        // TODO appliquer l'effet
    }


    public GridEntity getEntityFromCoord(Coordinates coord) {
        if (!isInside(coord)) return null;
        Cell targetCell = getCell(coord.getX(), coord.getY());
        if (targetCell != null) {
            return targetCell.getEntity();
        }
        return null;
    }
}