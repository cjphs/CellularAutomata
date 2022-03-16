package com.christophersch.cellularautomata;

import javafx.scene.paint.Color;

public class GridCell {
    int x;
    int y;
    int cell_id;

    public GridCell(int x, int y, int cell_id) {
        this.x = x;
        this.y = y;
        this.cell_id = cell_id;
    }

    public Color getColor() {
        return Grid.rule_set.getColor(cell_id);
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
