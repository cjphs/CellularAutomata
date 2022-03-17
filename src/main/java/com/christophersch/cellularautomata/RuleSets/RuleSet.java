package com.christophersch.cellularautomata.RuleSets;

import javafx.scene.paint.Color;

public interface RuleSet {
    // The highest cell ID that exists within the CA
    int getMaxCellID();

    // The initial configuration of the CA on the grid
    void initializeGrid();

    // Different colours for each cell type
    Color getColor(int cell_id);

    // Main update rule
    void updateRules(int cell_id, int x, int y);

    // Automaton-specific update (mainly used by the 1D automata)
    void updateCA();
}
