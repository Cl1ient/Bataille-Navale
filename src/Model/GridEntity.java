package Model;

import Model.Game.Game;
import Model.player.Player;

public interface GridEntity {
    public void onHit(Game game, Player attacker, Player defender, Integer x, Integer y);
    public EntityType getType();
    public Integer getSize();
}
