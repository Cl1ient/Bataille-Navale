package model.entity.island;

import model.EntityType;
import model.IslandListener;
import model.game.Game;
import model.GridEntity;
import model.map.Grid;
import model.player.Player;
import model.trap.Trap;

import java.util.List;

public class NewStorm implements GridEntity {
    private EntityType m_type = EntityType.NEW_STORM;
    private Integer m_size = 1;
    private List<IslandListener> m_listeners;

    public NewStorm(){}
    @Override
    public void onHit(Player attacker, Player defender, Integer x, Integer y,Integer segmentIndex){
        Trap entity = attacker.findTrap(EntityType.STORM);
        notifyListener(entity, attacker);
    }
    @Override
    public EntityType getType(){return this.m_type;}
    @Override
    public Integer getSize(){return this.m_size;}

    public void attachListener(IslandListener listener){
        m_listeners.add(listener);
    }

    public void notifyListener(Trap entity, Player player){
        for(IslandListener listener : m_listeners){
            listener.notifyPlaceIslandEntity(entity, player);
        }
    }
        @Override
    public boolean isSunk(){
        return false;
    }

    public void registerToGrid(Grid grid){}
}
