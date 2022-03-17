package com.christophersch.cellularautomata;

import com.christophersch.cellularautomata.RuleSets.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AutomataApplication extends Application {

    static AutomataGUI gui;
    AnimationTimer loop;

    // How many times per second should logic updates & redraws happen
    final static int FPS = 60;
    final static String application_title = "Automaton";

    // List of available rule sets
    public static ArrayList<String> rule_sets = new ArrayList<>(
            List.of(
                "Brian's Brain",
                "Game of Life",
                "Sand",
                "Rule 22",
                "Rule 30"
            )
    );

    // Bind rule sets to respective objects
    void setRules(String rule_selection) {
        switch(rule_selection) {
            case "Game of Life" -> Grid.setRuleSet(new GameOfLife());
            case "Sand" -> Grid.setRuleSet(new Sand());
            case "Brian's Brain" -> Grid.setRuleSet(new BriansBrain());
            case "Rule 22" -> Grid.setRuleSet(new ElementaryRule22());
            case "Rule 30" -> Grid.setRuleSet(new ElementaryRule30());
        }

        pause();
        Grid.resetGrid();
        Grid.rule_set.initializeGrid();
        gui.cell_selection_combobox.getItems().clear();
        gui.cell_selection_preview.setFill(Grid.rule_set.getColor(Grid.mouse_cell_selection));

        ArrayList<Integer> cell_ids = new ArrayList<>();
        for(int i = 1; i < Grid.rule_set.getMaxCellID()+1; i++) {
            cell_ids.add(i);
        }
        gui.cell_selection_combobox.getItems().addAll(cell_ids);
        gui.cell_selection_combobox.setValue(1);
    }

    @Override
    public void start(Stage stage) {
        initLogic();
        initGUI(stage);

        setRules(rule_sets.get(0));
        pause();
    }

    // Initialize everything relevant to the logic
    public void initLogic() {

        // Main update loop
        loop = new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                // update
                Grid.update();

                // display
                gui.drawGrid();

                // sleep, repeat
                try {
                    Thread.sleep(1000/FPS);
                } catch (InterruptedException e) {
                    // Do nothing
                }
            }
        };

        loop.start();
    }

    public void initGUI(Stage stage) {
        gui = new AutomataGUI(stage, this);
    }

    void pause() {
        Grid.paused = true;
        gui.pause_button.setGraphic(gui.play_icon);
    }

    private void unpause() {
        Grid.paused = false;
        gui.pause_button.setGraphic(gui.pause_icon);
    }

    void pauseUnpause() {
        if (Grid.paused)
            unpause();
        else
            pause();

        gui.pause_button.setGraphic(Grid.paused ? gui.play_icon : gui.pause_icon);
    }

    public static void main(String[] args) {
        launch();
    }
}