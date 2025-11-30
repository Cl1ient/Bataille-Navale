package Model.Map;

import Model.GridEntity;

public class Cell {

    private GridEntity m_entity;
    private boolean m_hitBoat;
    private boolean m_hitTrap;
    private boolean m_miss;

    /**
     * Constructs a new Cell.
     * Initializes the cell to be empty, not hit, and not missed.
     */
    public Cell() {
        this.m_entity = null;
        this.m_hitBoat = false;
        this.m_hitTrap = false;
        this.m_miss = false;
    }

    /**
     * Retrieves the entity contained in this cell.
     * @return The contained {@link GridEntity}, or {@code null} if the cell is empty.
     */
    public GridEntity getEntity() {
        return m_entity;
    }

    /**
     * Sets the entity contained in this cell.
     * @param e The {@link GridEntity} to place in the cell.
     */
    public void setEntity(GridEntity e) {
        this.m_entity = e;
    }

    /**
     * Sets the hit status of this cell.
     * @param hit The new hit status.
     */
    public void setHit(boolean hit) {
        this.m_miss = hit;
    }

    /**
     * Sets the miss status of this cell.
     * @param miss The new miss status.
     */
    public void setMiss(boolean miss) {
        this.m_miss = miss;
    }

    public boolean isHit() {return this.m_hitBoat || this.m_hitTrap || this.m_miss;}

    public boolean isFilled(){return this.m_entity != null;}
}