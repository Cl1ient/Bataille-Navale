package Model.Trap;

import Model.GridEntity;

public class TrapFactory {
    public TrapFactory(){}

    public GridEntity createStorm(){return new Storm();}
    public GridEntity createBlackHole(){return new BlackHole();}
}
