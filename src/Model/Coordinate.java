package Model;

public class Coordinate {

    private Integer m_x;
    private Integer m_y;

    /**
     * Constructs a new Coordinates object.
     * @param row The row index .
     * @param column The column index .
     */
    public Coordinate(Integer row, Integer column) {
        this.m_x = row;
        this.m_y = column;
    }

    /**
     * Provides a string representation of the Coordinates.
     * @return A formatted string .
     */
    @Override
    public String toString() {
        return "row:" + m_x + ", col:" + m_y;
    }

    /**
     * Provides Row coordinates of the cell
     * @return Row
     */
    public int getX() {
        return this.m_x;
    }

    /**
     * Provides Column coordinates of the cell
     * @return Column
     */
    public Integer getY() {
        return this.m_y;
    }
}
