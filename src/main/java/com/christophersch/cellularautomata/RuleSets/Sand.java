package com.christophersch.cellularautomata.RuleSets;

import com.christophersch.cellularautomata.Grid;
import javafx.scene.paint.Color;

public class Sand implements RuleSet {
    @Override
    public int getMaxCellID() {
        return 3;
    }

    public void initializeGrid() {
        Grid.fillGrid(0);

        Grid.fillRegion(2,94,93, 2, 1);

        Grid.fillRegion(40, 40, 20, 3, 1);
        Grid.fillRegion(52, 52, 15, 2, 1);
        Grid.fillRegion(10,90,4,9, 1);
        Grid.setCell(50, 4, 3);

        Grid.fillRegion(10, 30, 30, 15, 2);
    }

    public Color getColor(int cell_id) {
        return switch(cell_id) {
            case 1 -> Color.BLACK;
            case 2 -> Color.ORANGERED;
            case 3 -> Color.AQUA;

            default -> Color.WHITE;
        };
    }

    public void updateRules(int cell_id, int x, int y) {
        switch (cell_id) {
            case 0 -> {}

            case 1 -> Grid.createCell(x,y,1);


            case 2 -> {
                // on ground
                if (Grid.getCell(x,y+1) == 0)
                    Grid.createCell(x,y+1,2);
                else if (Grid.getCell(x, y + 1) == 1) {
                    Grid.createCell(x, y, 2);
                } else if (Grid.getCell(x, y + 1) == 2) {

                    if (Grid.getCell(x+1, y) == 0 && Grid.getCell(x+1, y+1) == 0)
                        Grid.createCell(x+1,y,2);
                    else if (Grid.getCell(x-1, y) == 0 && Grid.getCell(x-1, y+1) == 0)
                        Grid.createCell(x-1,y,2);
                    else
                        Grid.createCell(x,y,2);

                }
                else
                    Grid.createCell(x,y,2);

            }

            case 3 -> {
                Grid.createCell(x,y,3);

                if ((Grid.generations % 60 > 30))
                    Grid.createCell(x,y+1,2);
            }
        }
    }

    @Override
    public void updateCA() {

    }
}
