package model.entity.trap;

import model.Coordinate;
import model.EntityType;
import model.player.Player;
import model.weapon.Weapon;

public class BlackHole implements Trap {
    private EntityType m_type;
    public BlackHole(EntityType type) {
        this.m_type = type;
    }

    @Override
    public void onHit(Player attacker, Player defender, int x, int y){
        Coordinate reboundCoord = new Coordinate(x, y);
        defender.notifyBlackHoleHit(attacker, reboundCoord);
    }

    public EntityType getType() {
        return this.m_type;
    }


}
