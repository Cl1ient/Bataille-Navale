package model.entity.island;

import model.EntityType;
import model.GameListener;
import model.IslandListener;
import model.game.Game;
import model.GridEntity;
import model.player.Player;
import model.trap.BlackHole;
import model.trap.Trap;

import java.util.List;

public class NewBlackHole implements GridEntity {

    private EntityType m_type = EntityType.NEW_BLACKHOLE;
    private Integer m_size = 1;
    private List<IslandListener> m_listeners;

    public void onHit(Player attacker, Player defender, Integer x, Integer y, Integer segmentIndex) {
        Trap entity = attacker.findTrap(EntityType.BLACK_HOLE);
        notifyListener(entity);
    }

    public EntityType getType(){return this.m_type;}
    public Integer getSize(){return this.m_size;}
        @Override
    public boolean isSunk(){
        return false;
    }
    public void attachListener(IslandListener listener){
        m_listeners.add(listener);
    }

    public void notifyListener(Trap entity){
        for(IslandListener listener : m_listeners){
            listener.notifyPlaceIslandEntity(entity);
        }
    }
}
