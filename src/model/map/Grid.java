package model.map;

import model.boat.BoatFactory;
import model.Coordinate;
import model.EntityType;
import model.GridEntity;
import model.boat.Boat;
import model.player.Player;
import model.trap.TrapFactory;
import model.entity.island.IslandItemFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Grid {

    private final Integer m_size;
    private Cell[][] cells;
    private final List<Boat> m_ownBoats;
    private final IslandItemFactory m_islandItemFactory;
    private final BoatFactory m_boatFactory;
    private final TrapFactory m_trapFactory;

    public Grid(Integer gridSize) {
        this.m_size = gridSize;
        this.cells = new Cell[gridSize][gridSize];
        this.m_ownBoats = new ArrayList<>();
        this.m_islandItemFactory = new IslandItemFactory();
        this.m_boatFactory = new BoatFactory();
        this.m_trapFactory = new TrapFactory();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.cells[i][j] = new Cell();
            }
        }
    }

    public void placeEntity(Map<EntityType, List<Coordinate>> entityPosition) {
        for (Map.Entry<EntityType, List<Coordinate>> entry : entityPosition.entrySet()) {
            EntityType type = entry.getKey();
            List<Coordinate> coordinates = entry.getValue();
            GridEntity entity = createEntityFromType(type);
            if (entity != null) {
                placeSingleEntity(entity, coordinates);
            }
        }
    }

    private boolean placeSingleEntity(GridEntity entity, List<Coordinate> coordinates) {
        for (Coordinate coord : coordinates) {
            if (!isInside(coord) || this.cells[coord.getX()][coord.getY()].getEntity() != null) {
                return false;
            }
        }

        int indexInEntity = 0;
        for (Coordinate coord : coordinates) {
            int row = coord.getX();
            int col = coord.getY();
            this.cells[row][col].setEntity(entity);
            this.cells[row][col].setIndexInEntity(indexInEntity);
            indexInEntity++;
        }

        // Ajouter le bateau uniquement après placement réussi
        if (entity instanceof Boat && !m_ownBoats.contains(entity)) {
            m_ownBoats.add((Boat) entity);
        }

        return true;
    }

    public GridEntity createEntityFromType(EntityType type) {
        switch (type) {
            // Items sur l'île
            case NEW_BOMB:
                return m_islandItemFactory.createNewItemBomb();
            case NEW_BLACKHOLE:
                return m_islandItemFactory.createNEwItemBlackHole();
            case NEW_STORM:
                return m_islandItemFactory.createNewItemStorm();
            case NEW_SONAR:
                return m_islandItemFactory.createNewItemSonar();

            // Bateaux
            case AIRCRAFT_CARRIER: {
                Boat boat = m_boatFactory.createAirCraftCarrier();
                return boat;
            }
            case CRUISER: {
                Boat boat = m_boatFactory.createCruiser();
                return boat;
            }
            case DESTROYER: {
                Boat boat = m_boatFactory.createDestroyer();
                return boat;
            }
            case SUBMARINE: {
                Boat boat = m_boatFactory.createSubmarine();
                return boat;
            }
            case TORPEDO: {
                Boat boat = m_boatFactory.createTorpedo();
                return boat;
            }

            // Pièges
            case STORM:
                return m_trapFactory.createStorm();
            case BLACK_HOLE:
                return m_trapFactory.createBlackHole();

            default:
                return null;
        }
    }

    public Cell getCell(int x, int y) {
        return isInside(x, y) ? cells[x][y] : null;
    }

    public boolean isInside(Coordinate coord) {
        return isInside(coord.getX(), coord.getY());
    }

    private boolean isInside(int x, int y) {
        return x >= 0 && x < m_size && y >= 0 && y < m_size;
    }

    public void hit(int x, int y, Player defender, Player attacker) {
        if (!isInside(x, y)) return;
        Cell targetCell = cells[x][y];
        targetCell.setHit(true);
        GridEntity entity = targetCell.getEntity();
        if (entity != null) {
            int segmentIndex = targetCell.getIndexInEntity();
            entity.onHit(attacker, defender, x, y, segmentIndex);
        } else {
            targetCell.setMiss(true);
        }
    }

    public void desactiveTrap(int x, int y) {
        if (!isInside(x, y)) return;
        Cell targetCell = cells[x][y];
        GridEntity entity = targetCell.getEntity();
        // TODO retirer l'entité si c'est un piège
        // targetCell.setEntity(null);
    }

    public void markMiss(int x, int y) {
        if (!isInside(x, y)) return;
        cells[x][y].setMiss(true);
    }
    public void markHitBoat(Coordinate coord){
        this.cells[coord.getX()][coord.getY()].setHitBoat(true);
    }

    public void triggerTornado(Coordinate coord) {
        if (!isInside(coord)) return;
        // TODO appliquer l'effet
    }

    public GridEntity getEntityFromCoord(Coordinate coord) {
        if (!isInside(coord)) return null;
        return getCell(coord.getX(), coord.getY()).getEntity();
    }

    public int getSize() {
        return m_size;
    }

    public boolean isAlreadyHit(int x, int y) {
        return getCell(x, y).isHit();
    }

    public void randomPlacementEntity(EntityType type) {
        List<Coordinate> coord = new ArrayList<>();
        GridEntity entity = createEntityFromType(type);
        int size = entity.getSize();
        int nbCellOk = 0;
        int x, y;

        Random rand = new Random();
        boolean vertical = rand.nextBoolean();
        if (vertical) {
            x = rand.nextInt(this.m_size);
            y = rand.nextInt(this.m_size - size);
        } else {
            x = rand.nextInt(this.m_size - size);
            y = rand.nextInt(this.m_size);
        }

        while (nbCellOk != size) {
            if (isInside(x, y) && !cellAlreadyFilled(x, y)) {
                coord.add(new Coordinate(x, y));
                if (vertical) y++;
                else x++;
                nbCellOk++;
            } else {
                nbCellOk = 0;
                coord.clear();
                if (vertical) {
                    x = rand.nextInt(this.m_size);
                    y = rand.nextInt(this.m_size - size);
                } else {
                    x = rand.nextInt(this.m_size - size);
                    y = rand.nextInt(this.m_size);
                }
            }
        }

        placeSingleEntity(entity, coord);
    }

    public boolean cellAlreadyFilled(int x, int y) {
        return getCell(x, y).isFilled();
    }

    public void displayGrid() {
        for (int r = 0; r < m_size; r++) {
            for (int c = 0; c < m_size; c++) {
                if(this.cells[r][c].isHit()){
                    System.out.print(" -");
                }
                else if(this.cells[r][c].getEntity() != null){
                    System.out.print(" x");
                }
                else {System.out.print(" o");}

            }
            System.out.println();
        }
    }

    public List<Boat> getOwnBoats() {
        return m_ownBoats;
    }


}
