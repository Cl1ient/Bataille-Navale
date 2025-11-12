package Model.Map;

import Model.GridEntity;
import Model.ShotResult;

public class GridCell implements GridEntity {

    private boolean isHit;
    private GridEntity m_entity;
    private Integer m_indexInEntity;

    /**
     * Construct a new GridCell
     * @param entity
     * @param indexInEntity
     */
    public GridCell(GridEntity entity, Integer indexInEntity) {
        this.m_entity = entity;
        this.m_indexInEntity = indexInEntity;
    }




}
