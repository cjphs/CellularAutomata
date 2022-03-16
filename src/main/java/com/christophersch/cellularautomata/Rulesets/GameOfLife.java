package com.christophersch.cellularautomata.Rulesets;

import com.christophersch.cellularautomata.Grid;
import javafx.scene.paint.Color;

public class GameOfLife implements Ruleset {
    public void initializeGrid() {};

    public Color getColor(int cell_id) {
        return (cell_id == 0 ? Color.WHITE: Color.BLUE);
    }

    public void updateRules(int cell_id, int x, int y) {
        switch (cell_id) {
            case 0 -> {
                int alive_neighbors = Grid.getNeighborCount(x,y,1);
                if (alive_neighbors == 3)
                    Grid.createCell(x,y,1);
                else
                    Grid.createCell(x,y,0);
            }

            case 1 -> {
                int alive_neighbors = Grid.getNeighborCount(x,y,1);

                if (alive_neighbors == 2 || alive_neighbors == 3)
                    Grid.createCell(x,y,1);
                else
                    Grid.createCell(x,y,0);
            }
        }
    }
}
