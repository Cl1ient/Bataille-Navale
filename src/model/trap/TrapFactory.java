package model.trap;

import model.GridEntity;

public class TrapFactory {
    public TrapFactory(){}

    public GridEntity createStorm(){return new Storm();}
    public GridEntity createBlackHole(){return new BlackHole(true);}
}
