package model.trap;

import model.EntityType;
import model.player.Player;

public interface Trap{
    EntityType getType();
    void activate();
}
