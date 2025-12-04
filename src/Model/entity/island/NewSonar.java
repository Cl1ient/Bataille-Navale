package Model.entity.island;

import Model.EntityType;
import Model.Game.Game;
import Model.GridEntity;
import Model.player.Player;

public class NewSonar implements GridEntity {

    private EntityType m_type = EntityType.NEW_SONAR;
    private Integer m_size = 1;

    public NewSonar(){}

    public void onHit(Game game, Player attacker, Player defender, Integer x, Integer y){

    }

    public EntityType getType(){return this.m_type;}
    public Integer getSize(){return this.m_size;}
}
