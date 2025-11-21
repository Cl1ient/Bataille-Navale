package Model.Trap;

import Model.Coordinates;
import Model.GridEntity;
import Model.Player.Player;
import Model.ShotResult;

public class Tornado implements GridEntity {
    private Integer size = 3;
    private Integer turnsLeft;

    public ShotResult onHit(Player attacker, Coordinates coordinates){
        return new ShotResult();
    }

    public Coordinates modifyCoordinates(Coordinates coordinates){
        return coordinates;
    }

    @Override
    public String getType() {
        return "Tornado";
    }

    @Override
    public Integer getSize(){
        return size;
    }



}
