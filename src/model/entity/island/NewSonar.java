package model.entity.island;

import model.EntityType;
import model.game.Game;
import model.GridEntity;
import model.player.Player;

public class NewSonar implements GridEntity {

    private EntityType m_type = EntityType.NEW_SONAR;
    private Integer m_size = 1;

    public NewSonar(){}

    public void onHit(Player attacker, Player defender, Integer x, Integer y){

    }

    public EntityType getType(){return this.m_type;}
    public Integer getSize(){return this.m_size;}
}
