package battleship;

public class Field {
    private final int rows;
    private final int columns;
    public Field(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}
