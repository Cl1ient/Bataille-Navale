package model.player;

import model.Coordinate;
import model.EntityType;
import model.IslandListener;
import model.boat.Boat;
import model.trap.*;
import model.game.Game;
import model.game.GameConfiguration;
import model.GridEntity;
import model.game.GameMediator;
import model.map.Cell;
import model.map.Grid;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Player {

    protected String m_name;
    protected Grid m_ownGrid;
    protected Grid m_shotGrid;
    private Integer m_nbBoatRemaning;
    private GameMediator m_mediator;
    private IslandListener m_islandListener;
    protected List<Weapon> m_availableWeapons;
    private List<Trap> m_traps;
    private Coordinate m_lastMove = null;
    private int m_totalShipSegments = 0;
    private TrapFactory m_trapFacto = new TrapFactory();
    private WeaponFactory m_weaponFacto = new WeaponFactory();
    private List<IslandListener> m_listeners;

    private int mTornadoTurnsRemaining = 0;

    public Player(GameConfiguration config) {
        this.m_name = config.getNickName();
        this.m_ownGrid = new Grid(config.getGridSize(), config.isIslandMode());
        this.m_shotGrid = new Grid(config.getGridSize(), config.isIslandMode());
        this.m_availableWeapons = new ArrayList<>();
        this.m_nbBoatRemaning = 1;
        this.m_traps = new ArrayList<>();
        this.placeEntity(config.getGridEntityPlacement());
    }

    public void placeEntity(Map<EntityType, List<Coordinate>> entityPlacement) {
        this.m_ownGrid.placeEntity(entityPlacement);
    }


    public void decrementTornadoEffect() {
        if (this.mTornadoTurnsRemaining > 0) {
            this.mTornadoTurnsRemaining--;
        }
    }

    public Grid getOwnGrid(){return this.m_ownGrid;}
    public Grid getShotGrid(){return this.m_shotGrid;}
    public Integer getGridSize(){return this.m_ownGrid.getSize();}
    public String getNickName() {return this.m_name;}
    public void loseOneBoat(){this.m_nbBoatRemaning --;}
    private int calculateTotalShipSegments() {
        return m_ownGrid.getOwnBoats().stream().mapToInt(Boat::getSize).sum();
    }
    public void updateTotalShipSegments() {
        this.m_totalShipSegments = calculateTotalShipSegments();
    }
    public void triggerTornadoEffect() {
        this.mTornadoTurnsRemaining = 3;
    }
    public boolean isUnderTornadoInfluence() {
        return this.mTornadoTurnsRemaining > 0;
    }
    public Cell getTargetCell(int x, int y) {return this.m_ownGrid.getCell(x, y);}
    public int getTotalShipSegments() {
        return m_totalShipSegments;
    }
    public void setLastMove(Coordinate coord) {
        this.m_lastMove = coord;
    }


    public boolean hasLost(){
        List<Boat> ownBoats = this.m_ownGrid.getOwnBoats();
        for (Boat boat : ownBoats) {
            if (!boat.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public void receiveShot(Coordinate coord, Player attacker) {
        this.m_ownGrid.hit(coord.getX(),coord.getY(), this, attacker);
        attacker.setLastMove(coord);
    }


    public void setMediator(GameMediator mediator) {
        this.m_mediator = mediator;
    }
    public void notifySunkStatus(Boat sunkBoat) {
        this.m_mediator.handleShipSunk(this, sunkBoat);
    }
    public void notifyHit(Player defender, Coordinate coord) {
        this.m_mediator.handleHit(defender, coord);
    }

    public void notifyBlackHoleHit(Player defender, Coordinate coord) {
        this.m_mediator.handleBlackHoleHit(defender, coord);
    }

    public EntityType getTypeEntityAt(Coordinate coord) {
        EntityType type = this.m_ownGrid.getTypeEntityFromGrid(coord);
        return type;
    }

    public Trap findTrap(EntityType type){
        Trap trap = null;
        switch (type){
            case BLACK_HOLE :
                trap = (Trap) m_trapFacto.createBlackHole(true);
                this.m_traps.add(trap);
                break;
            case STORM:
                trap = (Trap) m_trapFacto.createStorm(true);
                this.m_traps.add(trap);
                break;
        }
        return trap;
    }

    public void findWeapon(EntityType type){
        Weapon weapon = null;
        switch (type){
            case BOMB:
                weapon = m_weaponFacto.createBomb();
                this.m_availableWeapons.add(weapon);
                break;
            case SONAR:
                weapon = m_weaponFacto.createSonar();
                this.m_availableWeapons.add(weapon);
                break;
        }
    }

    public String getName() {
        return this.m_name;
    }

    public Map<String, Integer> getShipStatusStats() {
        int intact = 0;
        int hit = 0;
        int sunk = 0;

        for (Boat boat : m_ownGrid.getOwnBoats()) {
            if (boat.isSunk()) {
                sunk++;
            }
            else if (boat.getSegmentsHit() > 0) {
                hit++;
            }
            else {
                intact++;
            }
        }

        Map<String, Integer> stats = new HashMap<>();
        stats.put("intact", intact);
        stats.put("hit", hit);
        stats.put("sunk", sunk);
        return stats;
    }

    public Map<String, Integer> getHitAccuracyStats(Player defender) {
        int hits = 0;
        int misses = 0;
        Grid opponentGrid = defender.getOwnGrid();
        int size = opponentGrid.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Cell cell = opponentGrid.getCell(r, c);
                if (cell.isHit()) {
                    if (cell.getEntity() != null) {
                        hits++;
                    } else {
                        misses++;
                    }
                }
            }
        }
        Map<String, Integer> stats = new HashMap<>();
        stats.put("hits", hits);
        stats.put("misses", misses);
        return stats;
    }

    public String getLastMove() {
        if (m_lastMove == null) return "N/A";
        return "(" + (m_lastMove.getX() + 1) + "," + (char) ('A' + m_lastMove.getY()) + ")";
    }

    public String getWeaponUsesLeft(String weaponType) {
        Weapon weapon = getWeapon(weaponType);
        if (weapon != null) {
            return String.valueOf(weapon.getUsesLeft());
        }
        return "N/A";
    }

    public Weapon getWeapon(String weaponType) {

        EntityType targetClassName;
        switch (weaponType) {
            case "BOMB":
                targetClassName = EntityType.BOMB;
                break;
            case "SONAR":
                targetClassName = EntityType.SONAR;
                break;
            case "MISSILE":
                targetClassName = EntityType.MISSILE;
                break;
            default:
                return null;
        }
        for (Weapon weapon : m_availableWeapons) {
            if (weapon.getType() == targetClassName) {
                return weapon;
            }
        }
        return null;
    }

    public void addFoundItem(EntityType type) {
        String weaponName = "";
        switch (type) {
            case NEW_BOMB: weaponName = "BOMB"; break;
            case NEW_SONAR: weaponName = "SONAR"; break;
            default: return;
        }
        Weapon w = getWeapon(weaponName);
        if (w != null) {
            w.setUsesLeft();
        } else {

            findWeapon(type == EntityType.NEW_BOMB ? EntityType.BOMB : EntityType.SONAR);
        }
    }

    public void placeNewTrap(Trap trap, Coordinate coord) {
        boolean isValid = this.m_ownGrid.isInside(coord)
                && !this.m_ownGrid.cellAlreadyFilled(coord.getX(), coord.getY())
                && !this.m_ownGrid.isAlreadyHit(coord.getX(), coord.getY())
                && !this.m_ownGrid.coordIsinIsland(coord);
        if (isValid) {
            this.m_ownGrid.placeTrap(trap, coord);
        } else {
            this.m_mediator.notifyTrapPlacementError();
        }
    }

    public void setHitCellAt(Integer x, Integer y, boolean hit){
        this.m_ownGrid.setCellHitAt(x,y,hit);
    }
    public Cell getCellAt(Integer x, Integer y){
        return this.m_ownGrid.getCell(x,y);
    }
    public EntityType getTypeOfGridEntityFromCoord(Integer x, Integer y){
        return this.m_ownGrid.getTypeEntityFromGrid(new Coordinate(x,y));
    }

    public abstract boolean placeFoundTrap(Trap trap, Coordinate coord, Grid grid);
}