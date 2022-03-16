package com.christophersch.cellularautomata;

import com.christophersch.cellularautomata.Rulesets.GameOfLife;
import com.christophersch.cellularautomata.Rulesets.Sand;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutomataApplication extends Application {
    Button pause_button;
    AnimationTimer loop;

    Stage primary_stage;
    Canvas canvas;

    VBox contents;

    HBox top_bar;

    CheckBox lighting;

    ComboBox<String> rules_combobox;

    Button reset_button;

    ArrayList<String> rules_combobox_items = new ArrayList<>(
            List.of(
                "Game of Life",
                "Sand"
            )
    );

    private void setRules(String rule_selection) {
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
    public void start(Stage stage) throws IOException {
        initUI(stage);
        initLogic();
    }

    // Initialize everything relevant to the logic
    public void initLogic() {
        setRules(rules_combobox.getValue());

        // Main update loop
        loop = new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                // update
                Grid.update();

                // display
                GraphicsContext gc = canvas.getGraphicsContext2D();
                drawGrid(gc);

                try {
                    Thread.sleep(1000/FPS);
                } catch (InterruptedException e) {
                    // Do nothing
                }
            }
        };

        pause();
        // first draw
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawGrid(gc);

        loop.start();
    }

    public void initUI(Stage stage) {
        primary_stage = stage;

        top_bar = new HBox();
        top_bar.setPadding(new Insets(15, 12, 15, 12));
        top_bar.setSpacing(10);

        contents = new VBox(top_bar);

        canvas = new Canvas(100, 100);

        contents.getChildren().add(canvas);

        var scene = new Scene(contents, 800, 800, Color.WHITESMOKE);

        stage.setTitle("Lines");
        stage.setScene(scene);
        stage.show();

        rules_combobox = new ComboBox<String>();
        rules_combobox.getItems().addAll(rules_combobox_items);
        rules_combobox.setValue(rules_combobox_items.get(0));
        rules_combobox.setOnAction(e ->
            setRules(rules_combobox.getValue())
        );

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int[] coords = translateMouseCoordinates(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                Grid.mouse_grid_x = coords[0];
                Grid.mouse_grid_y = coords[1];

                Grid.mouse_down = true;

                Grid.mouse_button = mouseEvent.getButton();
            }
        });

        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Grid.mouse_down = false;
            }
        });

        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int[] coords = translateMouseCoordinates(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                Grid.mouse_grid_x = coords[0];
                Grid.mouse_grid_y = coords[1];
            }
        });

        pause_button = new Button("Play");
        pause_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Grid.paused = !Grid.paused;

                pause_button.setText(Grid.paused ? "Play" : "Pause");
            }
        });

        reset_button = new Button("Reset");
        reset_button.setOnAction(e ->
            Grid.resetGrid()
        );

        lighting = new CheckBox("Top-down lighting");
        lighting.setSelected(false);

        top_bar.getChildren().add(pause_button);
        top_bar.getChildren().add(reset_button);
        top_bar.getChildren().add(rules_combobox);
        top_bar.getChildren().add(lighting);

    }

    // return x, y pair of mouse coordinates on the grid
    private int[] translateMouseCoordinates(double x, double y) {
        int[] xy = {0,0};

        xy[0] = (int) (100.0 * x/contents.getWidth());
        xy[1] = (int) (100 * (y - top_bar.getHeight())/(contents.getHeight() - top_bar.getHeight()));

        System.out.println(x + " " + y + " " + contents.getHeight() + " " + canvas.getHeight() + " " + xy[1]);

        return xy;
    }

    private void pause() {
        Grid.paused = true;
        pause_button.setText("Play");
    }

    private void unpause() {
        Grid.paused = false;
        pause_button.setText("Pause");
    }

    private void drawGrid(GraphicsContext gc) {
        int[] lightcolumn = new int[Grid.grid_width];
        double lightDepth = 10.0;

        double scale_x = contents.getWidth()/100.0;
        double scale_y = (contents.getHeight() - top_bar.getHeight())/100.0 ;

        canvas.setWidth(contents.getWidth());
        canvas.setHeight(contents.getHeight());

        gc.setFill(Color.WHITESMOKE);
        gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());


        for(int x = 0; x < Grid.grid_width; x++) {
            for(int y = 0; y < Grid.grid_height; y++) {
                if (Grid.grid[x][y] == 0)
                    lightcolumn[x] = -1;
                else if (lightcolumn[x] < lightDepth)
                    lightcolumn[x]++;

                gc.setGlobalAlpha(1);
                gc.setFill(Grid.rule_set.getColor(Grid.grid[x][y]));
                gc.fillOval(scale_x * x, scale_y * y, scale_x, scale_y);

                if (lighting.isSelected()) {
                    gc.setGlobalAlpha(lightcolumn[x] / lightDepth);
                    gc.setFill(Color.BLACK);
                    gc.fillOval(scale_x * x, scale_y * y, scale_x, scale_y);
                }
            }
        }
    }


    public static void main(String[] args) {
        launch();
    }
}