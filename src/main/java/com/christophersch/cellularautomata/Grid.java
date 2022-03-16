package com.christophersch.cellularautomata;

import com.christophersch.cellularautomata.Rulesets.Ruleset;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.Arrays;

public class Grid {
    static Grid grid_instance;

    public static int ticks = 0;

    public static boolean paused = true;

    static int mouse_grid_x = 0;
    static int mouse_grid_y = 0;
    static boolean mouse_down = false;
    static MouseButton mouse_button = MouseButton.PRIMARY;

    static Ruleset rule_set;

    // stores the individual cells (quick access)
    static ArrayList<GridCell> cells = new ArrayList<GridCell>();

    static int grid_width = 100;
    static int grid_height = 100;

    // stores the actual 2D grid of cells
    public static int[][] grid = new int[grid_width][grid_height];
    static int[][] next_grid = new int[grid_width][grid_height];

    public static void setRuleSet(Ruleset rules) {
        rule_set = rules;
        rule_set.initializeGrid();
    }

    public static void resetGrid() {
        grid = new int[grid_width][grid_height];
        next_grid = new int[grid_width][grid_height];
    }

    public static void update() {
        // Initialize the next iteration of the grid used by createCell();
        next_grid = new int[grid_width][grid_height];


        if (mouse_down && inBounds(mouse_grid_x,mouse_grid_y)) {
            if (mouse_button == MouseButton.PRIMARY)
                //fillRegion(mouse_grid_x-1,mouse_grid_y-1,2,2,1);
                setCell(mouse_grid_x,mouse_grid_y,1);
            else if (mouse_button == MouseButton.SECONDARY)
                setCell(mouse_grid_x,mouse_grid_y,0);
        }

        // Loop over the grid, update each cell accordingly

        if (!paused) {
            for (int x = 0; x < grid_width; x++) {
                for (int y = 0; y < grid_height; y++) {
                    rule_set.updateRules(grid[x][y], x, y);
                }
            }

            for (int x = 0; x < grid_width; x++) {
                if (grid_height >= 0) System.arraycopy(next_grid[x], 0, grid[x], 0, grid_height);
            }

            ticks++;
        };


    }

    // Fills the grid with a specific cell type
    public static void fillGrid(int cell) {
        for(int x = 0; x < grid_width; x++) {
            for(int y = 0; y < grid_height; y++) {
                setCell(x,y,cell);
            }
        }
    }

    // Removes a cell at specific coordinates
    public static void deleteCell(int x, int y) {
        next_grid[x][y] = 0;
    }

    // Creates a new cell in the next generation
    public static void createCell(int x, int y, int cell_id) {
        if (inBounds(x,y))
            next_grid[x][y] = cell_id;
    }

    // Creates a cell in the current generation
    public static int getCell(int x, int y) {
        if (inBounds(x,y))
            return grid[x][y];
        else
            return -1;
    }

    public static Boolean inBounds(int x, int y) {
        return (x >= 0 && y >= 0 && x < grid_width && y < grid_height);
    }

    public static void setCell(int x, int y, int cell_id) {
        if (inBounds(x,y))
            grid[x][y] = cell_id;
    }

    public static void fillRegion(int x, int y, int w, int h, int cells) {
        for(int xx = 0; xx < w; xx++) {
            for(int yy = 0; yy < h; yy++) {
                if (inBounds(x+xx,y+yy))
                    grid[x + xx][y + yy] = cells;
            }
        }
    }


    public static int getNeighborCount(int x, int y, int cell_type) {
        int count = 0;

        for(int xx = -1; xx < 2; xx++) {
            for(int yy = -1; yy < 2; yy++) {
                if (!(xx == 0 & yy == 0) && x + xx >= 0 && y + yy >= 0 && x + xx < grid_width && y + yy < grid_height)
                    if (grid[x + xx][y + yy] == cell_type)
                        count++;

            }
        }

        return count;
    }

    public static ArrayList<GridCell> getCells() {
        return cells;
    }

    public static boolean cellsAdjacent(int x, int y, GridCell cell2) {
        return (
                (Math.abs(x - cell2.x) == 1) && (Math.abs(y - cell2.y) == 1) ||
                (Math.abs(x - cell2.x) == 0) && (Math.abs(y - cell2.y) == 1) ||
                (Math.abs(x - cell2.x) == 1) && (Math.abs(y - cell2.y) == 0)
            );
    }
}
