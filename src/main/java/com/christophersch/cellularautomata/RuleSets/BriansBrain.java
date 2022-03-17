package com.christophersch.cellularautomata.RuleSets;

import com.christophersch.cellularautomata.Grid;
import javafx.scene.paint.Color;

public class BriansBrain implements RuleSet {

    // https://en.wikipedia.org/wiki/Brian%27s_Brain
    @Override
    public int getMaxCellID() {
        return 2;
    }

    @Override
    public String getName(int cell_id) {
        return switch(cell_id) {
            case 1 -> "Alive";
            case 2 -> "Dying";
            default -> "Dead";
        };
    }

    @Override
    public Color getColor(int cell_id) {
        return switch(cell_id) {
            case 1 -> Color.WHITE;
            case 2 -> Color.BLUE;
            default -> Color.BLACK;
        };
    }

    @Override
    public void initializeGrid() {
        Grid.setCell(49,49,1);
        Grid.setCell(50,49,1);
    }



    @Override
    public void updateRules(int cell_id, int x, int y) {
        switch(cell_id) {
            case 0 -> {
                int on_neighbor_count = Grid.getNeighborCount(x,y,1);

                if (on_neighbor_count == 2)
                    Grid.createCell(x,y,1);
            }

            case 2 -> {
            }

            case 1 -> Grid.createCell(x,y,2);

        }
    }

    @Override
    public void updateCA() {

    }
}
