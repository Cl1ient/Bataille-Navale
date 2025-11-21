package Model.Map;

import Model.GridEntity;
import Model.ShotResult;

public class GridCell {
    private boolean isHit;
    private GridEntity m_entity;
    private Integer m_indexInEntity;

    /**
     * Construct a new GridCell
     * @param entity type of the entity in the cell (Boat, Trap)
     * @param indexInEntity index of the Entity
     */
    public GridCell(GridEntity entity, Integer indexInEntity) {
        this.m_entity = entity;
        this.m_indexInEntity = indexInEntity;
    }

    public ShotResult hit(){
        return null;
    }

    public boolean isOccuped() {
        return m_entity != null;
    }




}
