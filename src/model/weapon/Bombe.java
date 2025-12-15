package model.weapon;

import model.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Bombe implements Weapon{
    private Integer m_useLeft = 100;


    @Override
    public List<Coordinate> generateTargets(Coordinate coord){
        List<Coordinate> targets = new ArrayList<>();

        targets.add(coord);
        targets.add(coord.getRelative(0,1)); // Right
        targets.add(coord.getRelative(0,-1)); // Left
        targets.add(coord.getRelative(1,0)); // Top
        targets.add(coord.getRelative(-1,0)); // Bottom
        return targets;
    }

    @Override
    public String getName(){
        return "Bombe";
    }

    @Override
    public Integer getUsesLeft(){
        return m_useLeft;
    }

    public boolean isOffensive(){return true;}

    @Override
    public void use(){
        if(this.m_useLeft > 0 ){
            this.m_useLeft--;
        }
    }

}
