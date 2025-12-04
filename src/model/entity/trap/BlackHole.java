package model.entity.trap;

import model.EntityType;
import model.player.Player;

public class BlackHole implements Trap {
    private EntityType m_type;
    public BlackHole(EntityType type) {
        this.m_type = type;
    }

    @Override
    public void onHit(Player attacker, Player defender, int x, int y){
        // attacker.receiveShot(new Coordinate(x,y));
    }

    public EntityType getType() {
        return this.m_type;
    }

}
