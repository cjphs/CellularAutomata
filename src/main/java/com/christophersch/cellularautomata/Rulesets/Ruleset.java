package com.christophersch.cellularautomata.Rulesets;

import javafx.scene.paint.Color;

public interface Ruleset {
    public void initializeGrid();

    public Color getColor(int cell_id);

    public void updateRules(int cell_id, int x, int y);
}
