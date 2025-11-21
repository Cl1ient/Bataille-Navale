package Model.Weapon;

import Model.Coordinates;
import Model.Player.Player;
import Model.ShotResult;

public class Sonar implements Weapon{
    private Integer useLeft;
    public Sonar(){
        this.useLeft = 2;
    }

    @Override
    public ShotResult apply(Player targetPlayer, Coordinates targetCoordinates) {
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
