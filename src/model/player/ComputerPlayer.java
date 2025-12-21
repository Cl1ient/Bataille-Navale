package model.player;

import model.Coordinate;
import model.EntityType;
import model.game.GameConfiguration;
import model.map.Grid;
import model.trap.Trap;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ComputerPlayer extends Player{

    private final WeaponFactory m_weaponFactory;

    public ComputerPlayer(GameConfiguration config) {
        super(config);
        m_weaponFactory = new WeaponFactory();
        this.m_name = "Le Goat En Personne";
        this.m_availableWeapons.add(this.m_weaponFactory.createMissile());
        this.m_availableWeapons.add(this.m_weaponFactory.createBomb());
        this.m_availableWeapons.add(this.m_weaponFactory.createSonar());

    }

    @Override
    public void placeEntity(Map<EntityType, List<Coordinate>> entityPlacement) {

        for(Map.Entry<EntityType, List<Coordinate>> entry : entityPlacement.entrySet()){
            this.m_ownGrid.randomPlacementEntity(entry.getKey());
        }
    }


    public Coordinate choseCoord(Grid opponentGrid) {
        Random rand = new Random();
        int size = opponentGrid.getSize();
        int x = rand.nextInt(size);
        int y = rand.nextInt(size);
        while(opponentGrid.isAlreadyHit(x, y)){
            x = rand.nextInt(size);
            y = rand.nextInt(size);
        }
        return new Coordinate(x, y);
    }

    public Weapon choseWeapon(){

        List<Weapon> usableWeapons = new ArrayList<>();
        for (Weapon w : this.m_availableWeapons) {
            if (w.getUsesLeft() != 0) {
                usableWeapons.add(w);
            }
        }
        if (usableWeapons.isEmpty()) {
            return null;
        }
        Random rand = new Random();
        int index = rand.nextInt(usableWeapons.size());
        return usableWeapons.get(index);
    }

    public void placeRandomEntities(Map<EntityType, Integer> entityCounts) {
        if (entityCounts == null) return;
        for (Map.Entry<EntityType, Integer> entry : entityCounts.entrySet()) {
            EntityType type = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                this.m_ownGrid.randomPlacementEntity(type);
            }
        }
    }

    @Override
    public boolean placeFoundTrap(Trap trap, Coordinate coord, Grid grid) {
        Random rand = new Random();
        int attempts = 0;

        while (attempts < 1000) {
            int x = rand.nextInt(grid.getSize());
            int y = rand.nextInt(grid.getSize());
            if (grid.isInside(x, y) && !grid.cellAlreadyFilled(x, y) && !grid.isPosOnIsland(x, y) && !grid.isAlreadyHit(x, y)) {

                List<Coordinate> coords = new ArrayList<>();
                coords.add(new Coordinate(x, y));

                System.out.println("[GRID] L'ordinateur a placé son piège " + trap.getType() + " en (" + x + "," + y + ")");

                return grid.placeSingleEntity(grid.realTrap(trap), coords);
            }else{
                System.out.println("Toute les cases sont déjà occupé");
            }
            attempts++;
        }

        System.out.println("[GRID] Erreur : L'ordinateur n'a pas trouvé de place pour son piège.");
        return false;
    }

}
