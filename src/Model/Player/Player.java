package Model.Player;

import Model.Coordinates;
import Model.ShotResult;
import Model.Weapon.Weapon;

public abstract class Player {
    public abstract boolean isDefeated();
    public abstract ShotResult shoot(Coordinates coordinates, Weapon weapon, Player player);
    public abstract ShotResult receiveSHot(Coordinates coordinates, Weapon weapon);
    public abstract void update();
}
