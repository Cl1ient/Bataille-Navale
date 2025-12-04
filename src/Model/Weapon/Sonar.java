package Model.Weapon;

import Model.Coordinate;
import Model.Map.Grid;

import java.util.ArrayList;
import java.util.List;

public class Sonar implements Weapon{
    private Integer m_useLeft = 1;
    public Sonar(){}
    @Override
    public List<Coordinate> generateTargets(Coordinate coord) {
        List<Coordinate> targets = new ArrayList<>();

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                targets.add(coord.getRelative(dy, dx));
            }
        }

        return targets;
    }

    @Override
    public String getName(){
        return "Sonar";
    }

    public boolean isOffensive(){return false;}

    @Override
    public Integer getUsesLeft(){
        return this.m_useLeft;
    }

    @Override
    public void use(){
        if(this.m_useLeft > 0){
            this.m_useLeft--;
        }
    }
}
