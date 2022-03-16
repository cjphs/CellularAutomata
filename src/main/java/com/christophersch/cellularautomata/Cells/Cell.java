package com.christophersch.cellularautomata.Cells;

import com.christophersch.cellularautomata.Grid;
import javafx.scene.paint.Color;

public abstract class Cell {
    public Color cell_color = Color.BLACK;

    public int x = 0;
    public int y = 0;

    public void updateRule() {};

    public Cell clone() { return null; };

    public int getNeighborCount() {
        return Grid.getNeighbors(this).size();
    }

    public int getNeighborCount(Cell type) {
        return Grid.getNeighbors(this, type).size();
    }
}
