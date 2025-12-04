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
    private final Cell[][] cells;
    private final List<Boat> m_ownBoats;
    private final IslandItemFactory m_islandItemFactory;
    private final BoatFactory m_boatFactory;
    private final TrapFactory m_trapFactory;
    /**
     * Constructor for the Grid.
     * Initializes the grid array (cells), size, and factories.
     * @param gridSize The dimension of the square grid.
     */
    public Grid(Integer gridSize) {
        this.m_size = gridSize;
        this.cells = new Cell[gridSize][gridSize];
        this.m_ownBoats = new ArrayList<>();
        this.m_islandItemFactory = new IslandItemFactory();
        this.m_boatFactory = new BoatFactory();
        this.m_trapFactory = new TrapFactory();

        // init all the cell
        for (Integer i = 0; i < gridSize; i++) {
            for (Integer j = 0; j < gridSize; j++) {
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
            if (!isInside(coord)) {
                return false;
            }
            Integer row = coord.getX();
            Integer col = coord.getY();

            if (this.cells[row][col].getEntity() != null) {
                return false; // Cell occuped
            }
        }

        for (Coordinate coord : coordinates) {
            Integer row = coord.getX();
            Integer col = coord.getY();

            this.cells[row][col].setEntity(entity);
            if (entity instanceof Boat && !m_ownBoats.contains(entity)) {
                m_ownBoats.add((Boat) entity);
            }
        }
        return true;
    }

    public GridEntity createEntityFromType(EntityType type) {
        switch (type) {
            case NEW_BOMB:
                return m_islandItemFactory.createNewItemBomb();

            case NEW_BLACKHOLE:
                return m_islandItemFactory.createNEwItemBlackHole();

            case NEW_STORM:
                return m_islandItemFactory.createNewItemStorm();

            case NEW_SONAR:
                return m_islandItemFactory.createNewItemSonar();

            case AIRCRAFT_CARRIER:
                m_ownBoats.add(0,this.m_boatFactory.createAirCraftCarrier());
                return this.m_ownBoats.get(0);

            case CRUISER:
                m_ownBoats.add(0,this.m_boatFactory.createCruiser());
                return this.m_ownBoats.get(0);

            case DESTROYER:
                m_ownBoats.add(0,this.m_boatFactory.createDestroyer());
                return this.m_ownBoats.get(0);

            case SUBMARINE:
                m_ownBoats.add(0,this.m_boatFactory.createSubmarine());
                return this.m_ownBoats.get(0);

            case TORPEDO:
                m_ownBoats.add(0,this.m_boatFactory.createTorpedo());
                return this.m_ownBoats.get(0);

            case STORM:
                return this.m_trapFactory.createStorm();

            case BLACK_HOLE:
                return this.m_trapFactory.createBlackHole();

            default:
                return null;
        }
    }


    public Cell getCell(Integer x, Integer y) {
        if (isInside(x, y)) {
            return cells[x][y];
        }
        return null;
    }

    public boolean isInside(Coordinate coord) {
        return isInside(coord.getX(), coord.getY());
    }

    private boolean isInside(Integer x, Integer y) {
        return x >= 0 && x < m_size && y >= 0 && y < m_size;
    }

    public void hit(Integer x, Integer y, Player defender, Player attacker) {
        if (!isInside(x, y)) return;
        Cell targetCell = cells[x][y];
        targetCell.setHit(true);
        GridEntity entity = targetCell.getEntity();
        if (entity != null) {
            entity.onHit(attacker, defender, x, y);
        } else {
            targetCell.setMiss(true);
        }
    }

    public void desactiveTrap(Integer x, Integer y) {
        if (!isInside(x, y)) return;

        Cell targetCell = cells[x][y];
        GridEntity entity = targetCell.getEntity();

        // TODO logique : retirer l'entité si c'est un piège
        // targetCell.setEntity(null);
    }

    public void markMiss(Integer x, Integer y) {
        if (!isInside(x, y)) return;
        cells[x][y].setMiss(true);
    }

    public void triggerTornado(Coordinate coord) {
        if (!isInside(coord)) return;
        // TODO appliquer l'effet
    }


    public GridEntity getEntityFromCoord(Coordinate coord) {
        if (!isInside(coord)) return null;
        Cell targetCell = getCell(coord.getX(), coord.getY());
        if (targetCell != null) {
            return targetCell.getEntity();
        }
        return null;
    }

    /// /////////////////////////////////////

    public Integer getSize(){return this.m_size;}
    public boolean isAlreadyHit(Integer x, Integer y){
        return getCell(x,y).isHit();
    }

    public void randomPlacementEntity(EntityType type){
        List<Coordinate> coord = new ArrayList<>();
        GridEntity entity = createEntityFromType(type);
        Integer size = entity.getSize();
        Integer nbCellOk = 0;
        int x, y;


        Random rand = new Random();
        boolean vertical = rand.nextBoolean();
        if(vertical){
            x = rand.nextInt(this.m_size);
            y = rand.nextInt(this.m_size - size);
        }
        else{
            x = rand.nextInt(this.m_size - size);
            y = rand.nextInt(this.m_size);
        }

        while(nbCellOk != size){


            if(isInside(x,y) && !cellAlreadyFilled(x,y)){
                coord.add(new Coordinate(x, y));
                if(vertical){y ++;}
                else{x ++;}
                nbCellOk++;

            }
            else{
                nbCellOk = 0;
                coord.clear();
                if(vertical){
                    x = rand.nextInt(this.m_size);
                    y = rand.nextInt(this.m_size - size);
                }
                else{
                    x = rand.nextInt(this.m_size - size);
                    y = rand.nextInt(this.m_size);
                }
            }
        }
        this.placeSingleEntity(entity, coord);
    }

    public boolean cellAlreadyFilled(Integer x, Integer y){
        return this.getCell(x,y).isFilled();
    }
    public void displayGrid() {
        for (Integer r = 0; r < this.m_size; r++) {
            for (Integer c = 0; c < this.m_size; c++) {
                if(this.cells[r][c].getEntity() != null){
                    System.out.print(" x");
                }
                else{
                    System.out.print(" o");
                }
            }
            System.out.println();
        }
    }
}