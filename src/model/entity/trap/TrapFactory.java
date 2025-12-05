package model.entity.trap;

import model.EntityType;

public class TrapFactory {
    public TrapFactory() {}

    public Storm createStorm() {
        return new Storm(EntityType.STORM);
    }

   public BlackHole createBlackHole(){
        return new BlackHole(EntityType.BLACK_HOLE);
   }
}
