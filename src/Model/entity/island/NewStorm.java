package Model.entity.island;

import Model.EntityType;
import Model.player.Player;

public class NewStorm {
    private EntityType m_type = EntityType.NEW_BOMB;

    public NewStorm(){}

    public void onHit(Player attacker, Player defender, int x, int y){

    }

    public EntityType getM_type(){
        return this.m_type;
    }
}
