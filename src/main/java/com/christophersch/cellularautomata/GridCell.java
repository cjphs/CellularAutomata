package com.christophersch.cellularautomata;

import com.christophersch.cellularautomata.Cells.Cell;
import javafx.scene.paint.Color;

public class GridCell {
    int x;
    int y;
    Cell cell_type;

    public GridCell(int x, int y, Cell cell_type) {
        this.x = x;
        this.y = y;
        this.cell_type = cell_type;
    }

    public Color getColor() {
        return cell_type.cell_color;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
