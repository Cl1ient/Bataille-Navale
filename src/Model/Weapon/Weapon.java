package Model.Weapon;

import Model.Coordinates;
import Model.Player.Player;
import Model.ShotResult;

public interface Weapon {
    ShotResult apply(Player targetPlayer, Coordinates targetCoordinates);
    String getName();
    Integer getUsesLeft();
}
