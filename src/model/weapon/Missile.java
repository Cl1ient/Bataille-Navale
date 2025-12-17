package model.weapon;

import model.Coordinate;

import java.util.Collections;
import java.util.List;

public class Missile implements Weapon{
    private static final Integer m_unlimited_uses = -1;

    public Missile(){}

    @Override
    public List<Coordinate> generateTargets(Coordinate coord, int gridSize){
        return Collections.singletonList(coord);
    }

    @Override
    public String getName() {
        return "MISSILE";
    }
    public boolean isOffensive(){return true;}
    @Override
    public void setUsesLeft(){

    }
    @Override
    public Integer getUsesLeft() {
        return m_unlimited_uses;
    }

    @Override
    public void use() {
        // Unlimited use
    }

}
