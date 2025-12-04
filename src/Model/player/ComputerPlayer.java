package Model.player;

import Model.Coordinate;
import Model.EntityType;
import Model.Game.GameConfiguration;
import Model.Weapon.Weapon;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ComputerPlayer extends Player{

    public ComputerPlayer(GameConfiguration config) {
        super(config);
        this.m_name = "Computer";
    }

    @Override
    public void placeEntity(Map<EntityType, List<Coordinate>> entityPlacement) {

        for(Map.Entry<EntityType, List<Coordinate>> entry : entityPlacement.entrySet()){

            this.m_ownGrid.randomPlacementEntity(entry.getKey());
        }
    }

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
}
