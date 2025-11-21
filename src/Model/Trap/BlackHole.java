package Model.Trap;

import Model.Coordinates;
import Model.GridEntity;
import Model.Player.Player;
import Model.ShotResult;

public class BlackHole implements GridEntity {
    private Integer size;
    public BlackHole(){}

    public ShotResult onHit(Player attacker, Coordinates coordinates){
        return new ShotResult();
    }

    @Override
    public String getType() {
        return "blackhole";
    }

    @Override
    public Integer getSize(){
        return size;
    }

}
