package model.player;

import model.Coordinate;
import model.EntityType;
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
    protected List<Weapon> availableWeapons;
    private List<Trap> m_traps;
    private Coordinate m_lastMove = null;
    private int m_totalShipSegments = 0;
    private TrapFactory m_trapFacto = new TrapFactory();
    private WeaponFactory m_weaponFacto = new WeaponFactory();
    private int mTornadoTurnsRemaining = 0;

    public Player(GameConfiguration config) {
        this.m_name = config.getNickName();
        this.m_ownGrid = new Grid(config.getGridSize(), config.isIslandMode());
        this.m_shotGrid = new Grid(config.getGridSize(), config.isIslandMode());
        this.availableWeapons = new ArrayList<>();
        this.m_nbBoatRemaning = 1;
        this.m_traps = new ArrayList<>();
        this.placeEntity(config.getGridEntityPlacement());
    }

    public void placeEntity(Map<EntityType, List<Coordinate>> entityPlacement) {
        this.m_ownGrid.placeEntity(entityPlacement);
    }

    public void triggerTornadoEffect() {
        this.mTornadoTurnsRemaining = 3;
    }

    public boolean isUnderTornadoInfluence() {
        return this.mTornadoTurnsRemaining > 0;
    }

    public void decrementTornadoEffect() {
        if (this.mTornadoTurnsRemaining > 0) {
            this.mTornadoTurnsRemaining--;
        }
    }

    public Grid getOwnGrid(){return this.m_ownGrid;}
    public Grid getShotGrid(){return this.m_shotGrid;}
    public Integer getGridSize(){return this.m_ownGrid.getSize();}
    public GridEntity getGridEntityFromCoord(Coordinate coord){return this.m_ownGrid.getEntityFromCoord(coord);}
    public void loseOneBoat(){this.m_nbBoatRemaning --;}

    public void addWeapon(Weapon w){
        availableWeapons.add(w);
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
    public String getNickName() {return this.m_name;}

    public Cell getTargetCell(int x, int y) {return this.m_ownGrid.getCell(x, y);}

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
                weapon = (Weapon) m_weaponFacto.createBomb();
                this.availableWeapons.add(weapon);
                break;
            case SONAR:
                weapon = (Weapon) m_weaponFacto.createSonar();
                this.availableWeapons.add(weapon);
                break;
        }
    }

    public boolean isPocessWeapon(String weaponName){
        System.out.println(availableWeapons.size());
        for(Weapon weapon : availableWeapons){
            System.out.println(weapon.getName());
            if(weapon.getName() == weaponName){return true;}

        }
        return false;
    }

    public Trap activateTrap(EntityType type){
        Integer i = 0;
        while(type != this.m_traps.get(i).getType() && i < this.m_traps.size()){
            i++;
        }
        this.m_traps.get(i).activate();
        return m_traps.get(i);
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

    public int getTotalShipSegments() {
        return m_totalShipSegments;
    }

    public void setLastMove(Coordinate coord) {
        this.m_lastMove = coord;
    }

    public String getLastMove() {
        if (m_lastMove == null) return "N/A";
        return "(" + (m_lastMove.getX() + 1) + "," + (char) ('A' + m_lastMove.getY()) + ")";
    }

    private int calculateTotalShipSegments() {
        return m_ownGrid.getOwnBoats().stream().mapToInt(Boat::getSize).sum();
    }

    public void updateTotalShipSegments() {
        this.m_totalShipSegments = calculateTotalShipSegments();
    }

    public String getWeaponUsesLeft(String weaponType) {
        Weapon weapon = getWeapon(weaponType);
        if (weapon != null) {
            return String.valueOf(weapon.getUsesLeft());
        }
        return "N/A";
    }

    public Weapon getWeapon(String weaponType) {
        String targetClassName;
        switch (weaponType) {
            case "BOMB":
                targetClassName = "Bombe";
                break;
            case "SONAR":
                targetClassName = "Sonar";
                break;
            case "MISSILE":
                targetClassName = "Missile";
                break;
            default:
                return null;
        }
        for (Weapon weapon : availableWeapons) {
            if (weapon.getClass().getSimpleName().equals(targetClassName)) {
                return weapon;
            }
        }
        return null;
    }

}