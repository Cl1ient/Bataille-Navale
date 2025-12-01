package Model.entity.trap;

import Model.Coordinate;
import Model.EntityType;
import Model.Weapon.Weapon;
import Model.player.Player;

public class BlackHole implements Trap {
    private EntityType m_type;
    public BlackHole(EntityType type) {
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
