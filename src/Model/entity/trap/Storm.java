package Model.entity.trap;

import Model.EntityType;
import Model.player.Player;

public class Storm implements Trap {
    private EntityType m_type;
    public Storm(EntityType type) {
        this.m_type = type;
    }

    @Override
    public void onHit(Player attacker, Player defender, int x, int y){
        // TODO

    }

    public EntityType getType() {
        return this.m_type;
    }
}
