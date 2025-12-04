package model;

import model.game.Game;
import model.player.Player;

public interface GridEntity {
    public void onHit(Player attacker, Player defender, Integer x, Integer y);
    public EntityType getType();
    public Integer getSize();
}
