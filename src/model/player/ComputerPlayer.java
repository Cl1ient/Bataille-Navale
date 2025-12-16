package model.player;

import model.Coordinate;
import model.EntityType;
import model.game.GameConfiguration;
import model.map.Grid;
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
        this.availableWeapons.add(this.m_weaponFactory.createMissile());
        this.availableWeapons.add(this.m_weaponFactory.createBomb());
        this.availableWeapons.add(this.m_weaponFactory.createSonar());

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

        for (Weapon w : this.availableWeapons) {
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
}
