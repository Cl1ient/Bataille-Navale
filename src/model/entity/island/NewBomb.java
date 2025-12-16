package model.entity.island;

import model.EntityType;
import model.game.Game;
import model.GridEntity;
import model.player.Player;

public class NewBomb implements GridEntity {
    private EntityType m_type = EntityType.NEW_BOMB;
    private Integer m_size = 1;

    public NewBomb(){}

    public void onHit(Player attacker, Player defender, Integer x, Integer y, Integer segmentIndex){

    }

    public EntityType getType(){return this.m_type;}
    public Integer getSize(){return this.m_size;}
        @Override
    public boolean isSunk(){
        return false;
    }
}
