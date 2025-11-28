package Model.Weapon;

import Model.Coordinate;
import Model.Player.Player;

public class Sonar implements Weapon{
    private Integer useLeft;
    public Sonar(){
        this.useLeft = 2;
    }

    @Override
    public ShotResult apply(Player targetPlayer, Coordinate targetCoordinate) {
        return new ShotResult();
    }

    @Override
    public String getName(){
        return "Sonar";
    }
    @Override
    public Integer getUsesLeft() {
        return this.useLeft;
    }

    public void use(){
        this.useLeft--;
    }
}
