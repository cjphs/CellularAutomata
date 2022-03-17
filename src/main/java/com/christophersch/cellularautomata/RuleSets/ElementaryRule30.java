package com.christophersch.cellularautomata.RuleSets;

import com.christophersch.cellularautomata.Grid;

public class ElementaryRule30 extends ElementaryCA {
    @Override
    public void updateRules(int cell_id, int x, int y) {


        boolean[] b = getThree(x,y);

        if (
                b[0] && !b[1] && !b[2] || !b[0] && b[1] && b[2] || !b[0] && b[1] || !b[0] && b[2]
        )
            Grid.createCell(x,y+1,1);

        if (b[0])
            Grid.createCell(x-1,y,1);
        if (b[1])
            Grid.createCell(x,y,1);
        if (b[2])
            Grid.createCell(x+1,y,1);
    }
}
