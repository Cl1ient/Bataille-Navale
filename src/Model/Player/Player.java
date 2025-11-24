package Model.Player;

import Model.Coordinate;
import Model.ShotResult;
import Model.Weapon.Weapon;

public abstract class Player {
    public abstract boolean isDefeated();
    public abstract ShotResult shoot(Coordinate coordinate, Weapon weapon, Player player);
    public abstract ShotResult receiveSHot(Coordinate coordinate, Weapon weapon);
    public abstract void update();
}
