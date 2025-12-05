package model;

public class ScanResult {
    private Coordinate m_coord;
    private EntityType m_type;

    public ScanResult(Coordinate coord, EntityType type){
        this.m_coord = coord;
        this.m_type = type;
    }

    public Coordinate getCoord(){
        return this.m_coord;
    }

    public EntityType getType(){
        return this.m_type;
    }
}
