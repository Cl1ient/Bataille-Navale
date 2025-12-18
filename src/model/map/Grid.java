package model.map;

import model.boat.BoatFactory;
import model.Coordinate;
import model.EntityType;
import model.GridEntity;
import model.boat.Boat;
import model.player.Player;
import model.trap.Trap;
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
    private boolean m_islandMod;
    private List<Coordinate> m_islandCoordinates = new ArrayList<>();

    public Grid(Integer gridSize, boolean isIsland) {
        this.m_size = gridSize;
        this.cells = new Cell[gridSize][gridSize];
        this.m_ownBoats = new ArrayList<>();
        this.m_islandItemFactory = new IslandItemFactory();
        this.m_boatFactory = new BoatFactory();
        this.m_trapFactory = new TrapFactory();
        this.m_islandMod = isIsland;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.cells[i][j] = new Cell();
            }
        }
        this.getAllCoordsFromIsland();
    }

    public void getAllCoordsFromIsland(){
        List<Integer> indexIsland = getIslandPositions();
        for(Integer x : indexIsland){
            for(Integer y : indexIsland){
                this.m_islandCoordinates.add(new Coordinate(x,y));
            }
        }
    }

    public boolean coordIsinIsland(Coordinate coord){
        for(Coordinate coordinate : this.m_islandCoordinates){
            if(coordinate.getX() == coord.getX() && coordinate.getY() == coord.getY()){
                return true;
            }
        }
        return false;
    }

    public void placeTrap(Trap trap, Coordinate coord){
        cells[coord.getX()][coord.getY()].setEntity( (GridEntity) trap);
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

    public boolean placeSingleEntity(GridEntity entity, List<Coordinate> coordinates) {
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

        if (entity instanceof Boat && !m_ownBoats.contains(entity)) {
            m_ownBoats.add((Boat) entity);
        }
        System.out.println("lentité trouvé a été placé" + entity);
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
                return m_trapFactory.createStorm(true);
            case BLACK_HOLE:
                return m_trapFactory.createBlackHole(true);

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

    public boolean isInside(int x, int y) {
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

    public void markMiss(int x, int y) {
        if (!isInside(x, y)) return;
        cells[x][y].setMiss(true);
    }
    public void markHitBoat(Coordinate coord){
        this.cells[coord.getX()][coord.getY()].setHitBoat(true);
    }

    public GridEntity getEntityFromCoord(Coordinate coord) {
        if (!isInside(coord)) return null;
        return getCell(coord.getX(), coord.getY()).getEntity();
    }

    public EntityType getTypeEntityFromGrid(Coordinate coord){
        return getCell(coord.getX(), coord.getY()).getTypeEntityFromCell(coord);
    }

    public int getSize() {
        return m_size;
    }

    public boolean isAlreadyHit(int x, int y) {
        return getCell(x, y).isHit();
    }

    public void randomPlacementEntity(EntityType type) {
        GridEntity entity = createEntityFromType(type);
        int size = entity.getSize();
        Random rand = new Random();

        int attempts = 0;
        while (attempts < 200) {
            boolean horizontal = rand.nextBoolean();
            int x = rand.nextInt(m_size);
            int y = rand.nextInt(m_size);
            Coordinate start = new Coordinate(x, y);

            if (isValidPosition(start, size, horizontal)) {
                List<Coordinate> coords = new ArrayList<>();
                for(int i=0; i<size; i++) {
                    int r = horizontal ? x : x+i;
                    int c = horizontal ? y+i : y;
                    coords.add(new Coordinate(r, c));
                }
                placeSingleEntity(entity, coords);
                return;
            }
            attempts++;
        }
    }

    private List<Integer> getIslandPositions(){
        Integer islandSize = this.m_size/2 - 1;
        List<Integer> indexIsland = new ArrayList<>(List.of(3));
        switch (islandSize){
            case 4:
                indexIsland.add(4);
                indexIsland.add(5);
                indexIsland.add(6);
                break;
            case 3:
                indexIsland.add(4);
                indexIsland.add(5);
                break;
            case 2:
                indexIsland.add(2);
                break;
        }
        return indexIsland;
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

    public void setIslandMod(boolean isIslandMod) {
        this.m_islandMod = isIslandMod;
    }

    public boolean isValidPosition(Coordinate coord, int size, boolean horizontal) {
        if (coord.getX() < 0 || coord.getX() >= m_size || coord.getY() < 0 || coord.getY() >= m_size) return false;

        for (int i = 0; i < size; i++) {
            int r = horizontal ? coord.getX() : coord.getX() + i;
            int c = horizontal ? coord.getY() + i : coord.getY();

            if (!isInside(r, c)) return false;
            if (coordIsinIsland(new Coordinate(r, c))) return false;
            if (cellAlreadyFilled(r, c)) return false;
        }
        return true;
    }

    public void initIslandItems() {
        if (!m_islandMod) return;

        List<EntityType> itemsToPlace = new ArrayList<>();
        itemsToPlace.add(EntityType.NEW_BOMB);
        itemsToPlace.add(EntityType.NEW_SONAR);
        itemsToPlace.add(EntityType.NEW_STORM);
        itemsToPlace.add(EntityType.NEW_BLACKHOLE);
        List<Coordinate> availableCoords = new ArrayList<>(this.m_islandCoordinates);
        Random rand = new Random();
        for (EntityType type : itemsToPlace) {
            if (availableCoords.isEmpty()) break;
            int index = rand.nextInt(availableCoords.size());
            Coordinate pos = availableCoords.remove(index);
            GridEntity item = createEntityFromType(type);
            if (item != null) {
                this.cells[pos.getX()][pos.getY()].setEntity(item);
            }
        }
    }


    public boolean isPosOnIsland(int x, int y) {
        if (!m_islandMod) return false;
        List<Integer> islandIndices = getIslandPositions();
        return islandIndices.contains(x) && islandIndices.contains(y);
    }

    public GridEntity realTrap(Trap trap) {
        GridEntity realTrapToPlace;
        if (trap.getType() == EntityType.STORM) {
            realTrapToPlace = m_trapFactory.createStorm(m_islandMod);
            return realTrapToPlace;
        }
        else if (trap.getType() == EntityType.BLACK_HOLE) {
            realTrapToPlace = m_trapFactory.createBlackHole(m_islandMod);
            return realTrapToPlace;

        }
        return null;
    }


}
