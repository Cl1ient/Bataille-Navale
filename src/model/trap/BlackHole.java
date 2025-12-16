package model.trap;

import model.Coordinate;
import model.EntityType;
import model.GridEntity;
import model.player.Player;

public class BlackHole implements GridEntity, Trap {
    private Integer m_size = 1;
    private boolean m_activate;
    private final EntityType m_type = EntityType.BLACK_HOLE;
    public BlackHole(boolean islandMod){
        this.m_activate = !islandMod;
    }

    public void onHit(Player attacker, Player defender, Integer x, Integer y,Integer segmentIndex){
        if(m_activate){
            Coordinate coord = new Coordinate(x, y);
            defender.notifyBlackHoleHit(defender, coord);
            attacker.receiveShot(new Coordinate(x, y), defender);
        }

    }

    @Override
    public EntityType getType(){return this.m_type;}

    public Integer getSize() {return this.m_size;}

    public void activate(){
        this.m_activate = true;
    }

}
