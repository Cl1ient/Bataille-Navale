package model.entity.trap;

import model.EntityType;
import model.player.Player;

public interface Trap {

    void onHit(Player attacker, Player defender, int x, int y);
    EntityType getType();
}
