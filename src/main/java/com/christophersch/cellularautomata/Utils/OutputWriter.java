package com.christophersch.cellularautomata.Utils;

import com.christophersch.cellularautomata.AutomataApplication;
import com.christophersch.cellularautomata.Grid;

import java.io.FileWriter;
import java.util.ArrayList;

public class OutputWriter extends Observer {
    static ArrayList<Integer> active_cell_counts = new ArrayList<>();

    public OutputWriter(AutomataApplication application) {
        super(application);
    }

    public void writeCellCountsToFile() {
        try {
            FileWriter writer = new FileWriter("output.txt");
            int i = 0;
            for (int count : active_cell_counts) {
                writer.write(i++ + " " + count + System.lineSeparator());
            }
            writer.close();
        } catch(Exception e) {
            System.out.println("Write error");
        }

    }


    @Override
    public void update() {
        if (!Grid.paused)
            active_cell_counts.add(Grid.active_cell_count);

    }
}
