package com.christophersch.cellularautomata.Rulesets;

import com.christophersch.cellularautomata.Grid;
import javafx.scene.paint.Color;

public class GameOfLife implements RuleSet {

    // Empty grid
    public void initializeGrid() {}

    // Colors of each cell type (index of array = id)

    public Color getColor(int cell_id) {
        return (cell_id == 1 ? Color.WHITE : Color.BLACK);
    }

    // Update rules (https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life#Rules)
    public void updateRules(int cell_id, int x, int y) {
        switch (cell_id) {
            // Dead cell becomes alive if it has 3 alive neighbors
            case 0 -> {
                int alive_neighbors = Grid.getNeighborCount(x,y,1);
                if (alive_neighbors == 3)
                    Grid.createCell(x,y,1);
            }

            // Alive cell stays alive if it has 2 or 3 alive neighbors
            case 1 -> {
                int alive_neighbors = Grid.getNeighborCount(x,y,1);

                if (alive_neighbors == 2 || alive_neighbors == 3)
                    Grid.createCell(x,y,1);
            }
        }
    }
}
