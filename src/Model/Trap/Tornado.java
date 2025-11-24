package Model.Trap;

import Model.Coordinate;
import Model.GridEntity;
import Model.Player.Player;
import Model.ShotResult;

public class Tornado implements GridEntity {
    private Integer size = 3;
    private Integer turnsLeft;

    public ShotResult onHit(Player attacker, Coordinate coordinate){
        return null;
    }

    public Coordinate modifyCoordinates(Coordinate coordinate){
        return coordinate;
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
