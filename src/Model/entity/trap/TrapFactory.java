package Model.entity.trap;

import Model.EntityType;

public class TrapFactory {
    public TrapFactory() {}

    public Storm createStorm() {
        return new Storm(EntityType.STORM);
    }

   public BlackHole createBlackHole(){
        return new BlackHole(EntityType.BLACK_HOLE);
   }
}
