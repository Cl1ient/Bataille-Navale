package Model;

import Model.Player.Player;

public interface GridEntity {
    ShotResult onHit(Player attacker, Coordinates coordinates);
    String getType();
    Integer getSize();
}
