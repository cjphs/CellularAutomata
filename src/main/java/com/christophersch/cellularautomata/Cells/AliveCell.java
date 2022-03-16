package com.christophersch.cellularautomata.Cells;

import com.christophersch.cellularautomata.Grid;
import javafx.scene.paint.Color;

public class AliveCell extends Cell {
    public AliveCell() {
        this.cell_color = Color.BLACK;
    }

    public void updateRule() {
        if (getNeighborCount(new AliveCell()) == 2 || getNeighborCount(new AliveCell()) == 3) {
            Grid.setCell(x,y,new AliveCell());
        }
        else
            Grid.setCell(x,y,new DeadCell());
    };

    public Cell clone() {
        AliveCell alive_cell = new AliveCell();
        return alive_cell;
    }
}
