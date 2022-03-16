package com.christophersch.cellularautomata;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AutomataGUI {
    AutomataApplication parent_application;
    Button pause_button;

    Stage primary_stage;
    Canvas canvas;

    VBox contents;

    HBox top_bar;

    CheckBox lighting;

    ComboBox<String> rules_combobox;

    Button reset_button;

    public AutomataGUI(Stage stage, AutomataApplication parent) {
        this.parent_application = parent;

        primary_stage = stage;

        top_bar = new HBox();
        top_bar.setPadding(new Insets(15, 12, 15, 12));
        top_bar.setSpacing(10);

        contents = new VBox(top_bar);

        canvas = new Canvas(100, 100);

        contents.getChildren().add(canvas);

        var scene = new Scene(contents, 800, 800, Color.WHITESMOKE);

        stage.setTitle(AutomataApplication.application_title);
        stage.setScene(scene);
        stage.show();

        rules_combobox = new ComboBox<>();
        rules_combobox.getItems().addAll(AutomataApplication.rule_sets);
        rules_combobox.setValue(AutomataApplication.rule_sets.get(0));
        rules_combobox.setOnAction(e ->
                parent_application.setRules(rules_combobox.getValue())
        );

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            int[] coords = translateMouseCoordinates(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            Grid.mouse_grid_x = coords[0];
            Grid.mouse_grid_y = coords[1];

            Grid.mouse_down = true;

            Grid.mouse_button = mouseEvent.getButton();
        });

        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> Grid.mouse_down = false);

        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            int[] coords = translateMouseCoordinates(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            Grid.mouse_grid_x = coords[0];
            Grid.mouse_grid_y = coords[1];
        });

        pause_button = new Button("Play");
        pause_button.setOnAction(e -> {
                parent_application.pauseUnpause();
                pause_button.setText(Grid.paused ? "Play" : "Pause");
            }
        );

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

        drawGrid();
    }



    // return x, y pair of mouse coordinates on the grid
    public int[] translateMouseCoordinates(double x, double y) {
        int[] xy = {0,0};

        xy[0] = (int) (100.0 * x/contents.getWidth());
        xy[1] = (int) (100 * (y - top_bar.getHeight())/(contents.getHeight() - top_bar.getHeight()));

        System.out.println(x + " " + y + " " + contents.getHeight() + " " + canvas.getHeight() + " " + xy[1]);

        return xy;
    }

    public void drawGrid() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

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
}
