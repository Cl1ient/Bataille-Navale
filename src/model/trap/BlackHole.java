package model.trap;

import model.EntityType;
import model.game.Game;
import model.GridEntity;
import model.player.Player;

public class BlackHole implements GridEntity {
    private Integer m_size = 1;
    private final EntityType m_type = EntityType.BLACK_HOLE;
    public BlackHole(){}

    public void onHit(Player attacker, Player defender, Integer x, Integer y){
        //defender.getOwnGrid().markHitTrap(new Coordinate(x,y));
        //game.processShot(defender, attacker, x, y);

    }

    @Override
    public EntityType getType(){return this.m_type;}

    public Integer getSize() {return this.m_size;}

}
