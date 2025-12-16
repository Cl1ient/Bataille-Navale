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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class Player {

    protected String m_name;
    protected Grid m_ownGrid;
    protected Grid m_shotGrid;
    private Integer m_nbBoatRemaning;
    private GameMediator m_mediator;
    protected List<Weapon> availableWeapons;
    //private WeaponFactory WFacto;
    private  List<Trap> m_traps;


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

    public void placeRandomNewEntity(){

    }

    public Grid getOwnGrid(){return this.m_ownGrid;}
    public Grid getShotGrid(){return this.m_shotGrid;}
    public Integer getGridSize(){return this.m_ownGrid.getSize();}

    public void addWeapon(Weapon w){
        availableWeapons.add(w);
    }

    public void markMiss(Coordinate coord){
        this.m_ownGrid.markMiss(coord.getX(), coord.getY());
    }

    public GridEntity getGridEntityFromCoord(Coordinate coord){
        return this.m_ownGrid.getEntityFromCoord(coord);
    }

    public void triggerTornado(Coordinate coord){
        this.m_ownGrid.triggerTornado(coord);
    }

    public void loseOneBoat(){
        this.m_nbBoatRemaning --;
    }

    public boolean hasLost(){
        List<Boat> ownBoats = this.m_ownGrid.getOwnBoats();
        System.out.println(ownBoats);
        for (Boat boat : ownBoats) {
            if (!boat.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public void receiveShot(Coordinate coord, Player attacker) {
        this.m_ownGrid.hit(coord.getX(),coord.getY(), this, attacker);
    }
    public String getNickName() {return this.m_name;}

    public Cell getTargetCell(int x, int y) {return this.m_ownGrid.getCell(x, y);}

    // TODO mettre dans l'uml

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

    public void activateTrap(EntityType type){
        Integer i = 0;
        while(type != this.m_traps.get(i).getType() && i < this.m_traps.size()){
            i++;
        }
        this.m_traps.get(i).activate();
    }

    public String getName() {
        return this.m_name;
    }
}
