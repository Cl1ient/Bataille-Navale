package model.trap;

import model.Coordinate;
import model.EntityType;
import model.game.Game;
import model.GridEntity;
import model.map.Grid;
import model.player.Player;

import java.util.Random;

public class Storm implements GridEntity, Trap {
    private Integer m_size = 1;
    private boolean m_isConsumed = false;
    private boolean m_activate;
    private Integer turnsLeft = 3;
    private final EntityType m_type = EntityType.STORM;

    public Storm(boolean islandMod){
        this.m_activate = !islandMod;
    }

    public void onHit(Player attacker, Player defender, Integer x, Integer y, Integer segmentIndex){
        if (m_isConsumed) return;
        attacker.triggerTornadoEffect();
        Coordinate coord = modifyCoordinates(attacker.getGridSize());
        defender.receiveShot(coord, attacker);
        m_isConsumed = true;
        System.out.println("je suis dans la tornade !!!");
    }

    public Coordinate modifyCoordinates(Integer size){

        Random rand = new Random();

        int x = rand.nextInt(size);
        int y = rand.nextInt(size);

        return new Coordinate(x,y);
    }

    @Override
    public EntityType getType(){return this.m_type;}

    public Integer getSize() {return this.m_size;}

    public void activate(){}
        @Override
    public boolean isSunk(){
        return false;
    }

    public void registerToGrid(Grid grid){}

}
