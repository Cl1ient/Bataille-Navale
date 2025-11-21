package Model.Weapon;

import Model.Coordinates;
import Model.Player.Player;
import Model.ShotResult;

public class Bomb implements Weapon{
    private Integer useLeft;
    public Bomb(){
        this.useLeft = 2;
    }

    @Override
    public ShotResult apply(Player targetPlayer, Coordinates targetCoordinates) {
        return new ShotResult();
    }

    @Override
    public String getName(){
        return "Bomb";
    }
    @Override
    public Integer getUsesLeft() {
        return this.useLeft;
    }

    public void use(){
        this.useLeft--;
    }
}
