package com.christophersch.cellularautomata;

import com.christophersch.cellularautomata.RuleSets.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AutomataApplication extends Application {

    AutomataGUI gui;
    AnimationTimer loop;

    // How many times per second should logic updates & redraws happen
    final static int FPS = 60;
    final static String application_title = "Automaton";

    // List of available rule sets
    public static ArrayList<String> rule_sets = new ArrayList<>(
            List.of(
                "Game of Life",
                "Sand",
                "Brian's Brain",
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
    }

    @Override
    public void start(Stage stage) {
        initLogic();
        initGUI(stage);
    }

    // Initialize everything relevant to the logic
    public void initLogic() {
        setRules(rule_sets.get(0));

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

        pause();

        loop.start();
    }

    public void initGUI(Stage stage) {
        gui = new AutomataGUI(stage, this);
    }

    private void pause() {
        Grid.paused = true;
    }

    private void unpause() {
        Grid.paused = false;
    }

    void pauseUnpause() {
        if (Grid.paused)
            unpause();
        else
            pause();
    }

    public static void main(String[] args) {
        launch();
    }
}