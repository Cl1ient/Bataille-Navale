package model.player;

import model.Coordinate;
import model.EntityType;
import model.game.GameConfiguration;
import model.weapon.Weapon;
import model.weapon.WeaponFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ComputerPlayer extends Player{

    private final WeaponFactory m_weaponFactory;

    public ComputerPlayer(GameConfiguration config) {
        super(config);
        m_weaponFactory = new WeaponFactory();
        this.m_name = "Computer";
        this.availableWeapons.add(this.m_weaponFactory.createMissile());

    }
    /*
    public void placeEntity(Map<EntityType, List<Coordinate>> entityPlacement) {
        this.m_ownGrid.placeEntity(entityPlacement);
    }
    */

    public Coordinate choseCoord(){
        // choisis une coordonnée de manière aléatoire et la renvoie

        Random rand = new Random();

        int x = rand.nextInt(this.m_ownGrid.getSize()); // 0 à 9
        int y = rand.nextInt(this.m_ownGrid.getSize()); // 0 à 9

        while(this.m_ownGrid.isAlreadyHit(x, y)){
            x = rand.nextInt(this.m_ownGrid.getSize()); // 0 à 9
            y = rand.nextInt(this.m_ownGrid.getSize()); // 0 à 9
        }
        return new Coordinate(x, y);
    }

    public Weapon choseWeapon(){
        // choisis une arme de manière aléatoire parmi la liste d'arme disponible et la renvoie

        Random rand = new Random();
        int nb = rand.nextInt(100);
        int index = nb % this.availableWeapons.size();
        return this.availableWeapons.get(index);
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
