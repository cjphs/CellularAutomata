package com.christophersch.cellularautomata.Cells;

import com.christophersch.cellularautomata.Grid;
import javafx.scene.paint.Color;

public class DeadCell extends Cell {
    public DeadCell() {
        this.cell_color = Color.YELLOW;
    }

    public void updateRule() {
        if (getNeighborCount(new AliveCell()) == 3) {
            Grid.setCell(x,y,new AliveCell());
        }
        else {
            Grid.setCell(x, y, new DeadCell());
        }
    };

    public Cell clone() {
        DeadCell cell = new DeadCell();
        cell.cell_color = cell_color;
        return cell;
    }
}
