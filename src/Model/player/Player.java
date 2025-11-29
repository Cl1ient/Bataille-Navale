package Model.player;

import Model.Coordinate;
import Model.EntityType;
import Model.Game.GameConfiguration;
import Model.GridEntity;
import Model.Map.Grid;
import Model.Weapon.Weapon;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class Player {

    // ---------------------
    // A BESOIN DE GRID ET GAME_CONFIG POUR FRONCTIONNER CORRECTEMENT
    // ---------------------

    protected String m_name;
    protected Grid m_ownGrid;
    protected Grid m_shotGrid;
    private Integer m_nbBoatRemaning;
    protected List<Weapon> availableWeapons;
    //private WeaponFactory WFacto;
    // private  List<Trap> traps; TODO


    public Player(GameConfiguration config) {
        this.m_name = config.getNickName();
        this.m_ownGrid = new Grid(config.getGridSize());
        this.m_shotGrid = new Grid(config.getGridSize());
        this.availableWeapons = new ArrayList<>();
        this.m_nbBoatRemaning = 5;
        // this.traps = new ArrayList<>(); TODO

        this.placeEntity(config.getGridEntityPlacement());

    }

    public void placeEntity(Map<EntityType, List<Coordinate>> entityPlacement) {
        this.m_ownGrid.placeEntity(entityPlacement);
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
        return this.m_nbBoatRemaning == 0;
    }

    public void receiveShot(Coordinate coord) {
        this.m_ownGrid.hit(coord.getX(),coord.getY());
    }


}
