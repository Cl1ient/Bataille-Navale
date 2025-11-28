package Model.entity.island;

import Model.EntityType;
import Model.Player.Player;

public class NewSonar {

    private EntityType m_type = EntityType.NEW_SONAR;

    public NewSonar(){}

    public void onHit(Player attacker, Player defender, int x, int y){

    }

    public EntityType getM_type(){
        return this.m_type;
    }
}
