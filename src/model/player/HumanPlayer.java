package model.player;

import model.Coordinate;
import model.game.GameConfiguration;
import model.map.Grid;
import model.trap.Trap;
import model.weapon.WeaponFactory;

import java.util.ArrayList;
import java.util.List;

public class HumanPlayer extends Player{
    WeaponFactory m_weaponFactory;
    public HumanPlayer(GameConfiguration config){
        super(config);
        this.placeEntity(config.getGridEntityPlacement());

         WeaponFactory weaponFactory = new WeaponFactory();

        this.m_availableWeapons.add(weaponFactory.createMissile());
        this.m_availableWeapons.add(weaponFactory.createBomb());
        this.m_availableWeapons.add(weaponFactory.createSonar());

    }
    @Override
    public boolean placeFoundTrap(Trap trap, Coordinate coord, Grid grid) {
        if (!grid.isInside(coord)) return false;
        if (grid.cellAlreadyFilled(coord.getX(), coord.getY()) || grid.coordIsinIsland(coord) || grid.isAlreadyHit(coord.getX(), coord.getY())){
            System.out.println("[GRID] Impossible de placer le piège : Case " + coord + " déjà occupée !");
            return false;
        }
        List<Coordinate> coords = new ArrayList<>();
        coords.add(coord);
        return grid.placeSingleEntity(grid.realTrap(trap), coords);
    }


}