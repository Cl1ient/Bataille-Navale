package model.player;

import model.game.GameConfiguration;
import model.weapon.WeaponFactory;

public class HumanPlayer extends Player{
    WeaponFactory m_weaponFactory;
    public HumanPlayer(GameConfiguration config){
        super(config);
        this.placeEntity(config.getGridEntityPlacement());

         WeaponFactory weaponFactory = new WeaponFactory();

        this.availableWeapons.add(weaponFactory.createMissile());
        this.availableWeapons.add(weaponFactory.createBomb());
        this.availableWeapons.add(weaponFactory.createSonar());

    }


}