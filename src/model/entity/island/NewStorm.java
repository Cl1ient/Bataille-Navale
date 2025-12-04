package model.entity.island;

import model.EntityType;
import model.game.Game;
import model.GridEntity;
import model.player.Player;

public class NewStorm implements GridEntity {
    private EntityType m_type = EntityType.NEW_BOMB;
    private Integer m_size = 1;

    public NewStorm(){}

    public void onHit(Player attacker, Player defender, Integer x, Integer y){

    }

    public EntityType getType(){return this.m_type;}
    public Integer getSize(){return this.m_size;}
}
