package Model.Trap;

import Model.Coordinate;
import Model.Game.Game;
import Model.GridEntity;
import Model.player.Player;

public class BlackHole implements GridEntity {
    private Integer m_size = 1;
    public BlackHole(){}

    public void onHit(Game game, Player attacker, Player defender, Integer x, Integer y){
        //defender.getOwnGrid().markHitTrap(new Coordinate(x,y));
        //game.processShot(defender, attacker, x, y);

    }

    @Override
    public String getType() {
        return "blackhole";
    }

    public Integer getSize() {return this.m_size;}

}
