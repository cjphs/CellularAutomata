package com.christophersch.cellularautomata.RuleSets;

import javafx.scene.paint.Color;

public interface RuleSet {
    void initializeGrid();

    Color getColor(int cell_id);

    void updateRules(int cell_id, int x, int y);

    void updateCA();
}
