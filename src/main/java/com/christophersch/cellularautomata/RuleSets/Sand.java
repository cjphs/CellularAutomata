package com.christophersch.cellularautomata.RuleSets;

import com.christophersch.cellularautomata.Grid;
import javafx.scene.paint.Color;

public class Sand implements RuleSet {
    @Override
    public int getMaxCellID() {
        return 4;
    }

    public void initializeGrid() {
        Grid.fillGrid(0);

        Grid.fillRegion(2,94,93, 2, 1);

        Grid.fillRegion(40, 40, 20, 3, 1);
        Grid.fillRegion(0,99,100,1,4);
        Grid.setCell(50, 4, 3);

        Grid.fillRegion(10, 30, 30, 15, 2);
    }

    public Color getColor(int cell_id) {
        return switch(cell_id) {
            case 1 -> Color.BLACK;
            case 2 -> Color.ORANGERED;
            case 3 -> Color.AQUA;
            case 4 -> Color.PURPLE;

            default -> Color.WHITE;
        };
    }

    @Override
    public String getName(int cell_id) {
        return switch(cell_id) {
            case 1 -> "Solid";
            case 2 -> "Sand";
            case 3 -> "Gated source";
            case 4 -> "Sink";

            default -> "Air";
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
                else if (Grid.getCell(x,y+1) == 4)
                    Grid.createCell(x,y,0);
                else
                    Grid.createCell(x,y,2);

            }

            case 3 -> {
                Grid.createCell(x,y,3);

                if ((Grid.generations % 60 > 30))
                    Grid.createCell(x,y+1,2);
            }

            case 4 -> Grid.createCell(x,y,4);
        }
    }

    @Override
    public void updateCA() {

    }
}
