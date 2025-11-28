package Model;

public class Coordinates {

    private Integer m_row;
    private Integer m_column;

    /**
     * Constructs a new Coordinates object.
     * @param row The row index .
     * @param column The column index .
     */
    public Coordinates(Integer row, Integer column) {
        this.m_row = row;
        this.m_column = column;
    }

    /**
     * Provides a string representation of the Coordinates.
     * @return A formatted string .
     */
    @Override
    public String toString() {
        return "row:" + m_row + ", col:" + m_column;
    }

    /**
     * Provides Row coordinates of the cell
     * @return Row
     */
    public int getX() {
        return this.m_row;
    }

    /**
     * Provides Column coordinates of the cell
     * @return Column
     */
    public Integer getY() {
        return this.m_column;
    }
}
