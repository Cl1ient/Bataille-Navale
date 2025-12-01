package Model.entity.trap;

import Model.EntityType;
import Model.player.Player;

public interface Trap {

    void onHit(Player attacker, Player defender, int x, int y);
    EntityType getType();
}
