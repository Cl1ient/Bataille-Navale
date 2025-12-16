package model.entity.island;

import model.EntityType;
import model.IslandListener;
import model.game.Game;
import model.GridEntity;
import model.player.Player;
import model.trap.Trap;
import model.weapon.Weapon;

import java.util.List;

public class NewBomb implements GridEntity {
    private EntityType m_type = EntityType.NEW_BOMB;
    private Integer m_size = 1;
    private List<IslandListener> m_listeners;

    public NewBomb(){}

    public void onHit(Player attacker, Player defender, Integer x, Integer y, Integer segmentIndex){
        attacker.findWeapon(EntityType.BOMB);
        notifyListener(EntityType.BOMB);
    }

    public EntityType getType(){return this.m_type;}

    public Integer getSize(){return this.m_size;}

    public void attachListener(IslandListener listener){
        m_listeners.add(listener);
    }

    public void notifyListener(EntityType weaponType){
        for(IslandListener listener : m_listeners){
            listener.notifyWeaponFind(weaponType);
        }
    }

    @Override
    public boolean isSunk(){
        return false;
    }
}
