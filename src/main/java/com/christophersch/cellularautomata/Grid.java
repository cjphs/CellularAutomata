package com.christophersch.cellularautomata;

import com.christophersch.cellularautomata.Cells.Cell;

import java.util.ArrayList;

public class Grid {
    static Grid grid_instance;

    // stores the actual 2D grid of cells
    static Cell[][] grid;

    // stores the individual cells (quick access)
    static ArrayList<Cell> cells = new ArrayList<Cell>();

    static int grid_width = 100;
    static int grid_height = 100;

    public static Grid getGridInstance() {
        if (grid_instance == null)
            grid_instance = new Grid();
        return grid_instance;
    }

    public static void update() {
        ArrayList<Cell> previous_cells = new ArrayList<Cell>(cells);
        cells.clear();

        // update each cell
        for(Cell cell : previous_cells) {
            cell.updateRule();
        }

        previous_cells.clear();
    }

    public static void fillGrid(Cell cell) {
        for(int x = 0; x < grid_width; x++) {
            for(int y = 0; y < grid_height; y++) {
                setCell(x,y,cell.clone());
            }
        }
    }

    public static void deleteCell(int x, int y) {
        cells.removeIf(cell -> cell.x == x && cell.y == y);
    }

    public static void setCell(int x, int y, Cell cell) {
        deleteCell(x,y);
        cell.x = x;
        cell.y = y;
        cells.add(cell);
    }

    public static ArrayList<Cell> getNeighbors(Cell cell) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>();

        for(Cell cell2 : cells) {
            if (cellsAdjacent(cell, cell2))
                neighbors.add(cell2);
        }

        return neighbors;
    }

    public static ArrayList<Cell> getNeighbors(Cell cell, Cell type) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>();

        for(Cell cell2 : cells) {
            if (cellsAdjacent(cell, cell2) && (cell.getClass().equals(type.getClass())));
                neighbors.add(cell2);
        }

        return neighbors;
    }

    public static ArrayList<Cell> getCells() {
        return cells;
    }

    public static boolean cellsAdjacent(Cell cell1, Cell cell2) {
        return (
                (Math.abs(cell1.x - cell2.x) == 1) && (Math.abs(cell1.y - cell2.y) == 1) ||
                (Math.abs(cell1.x - cell2.x) == 0) && (Math.abs(cell1.y - cell2.y) == 1) ||
                (Math.abs(cell1.x - cell2.x) == 1) && (Math.abs(cell1.y - cell2.y) == 0)
            );
    }
}
