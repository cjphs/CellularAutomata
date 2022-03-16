package com.christophersch.cellularautomata;

import com.christophersch.cellularautomata.Rulesets.GameOfLife;
import com.christophersch.cellularautomata.Rulesets.Sand;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AutomataApplication extends Application {

    AnimationTimer loop;

    static AutomataGUI gui;

    public static ArrayList<String> rule_sets = new ArrayList<>(
            List.of(
                "Game of Life",
                "Sand"
            )
    );

    void setRules(String rule_selection) {
        switch(rule_selection) {
            case "Game of Life" -> Grid.setRuleSet(new GameOfLife());
            case "Sand" -> Grid.setRuleSet(new Sand());
        }

        pause();
        Grid.resetGrid();
        Grid.rule_set.initializeGrid();
    }

    final static int FPS = 60;

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

    void pauseUnpause() {
        if (Grid.paused)
            unpause();
        else
            pause();
    }

    private void unpause() {
        Grid.paused = false;
    }




    public static void main(String[] args) {
        launch();
    }
}