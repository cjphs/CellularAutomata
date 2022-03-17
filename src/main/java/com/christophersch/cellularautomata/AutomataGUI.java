package com.christophersch.cellularautomata;

import com.christophersch.cellularautomata.Utils.OutputWriter;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class AutomataGUI {
    AutomataApplication parent_application;
    Button pause_button;
    Button step_button;

    Stage primary_stage;
    Canvas canvas;

    VBox contents;

    ToolBar top_bar;
    ToolBar bottom_bar;

    CheckBox lighting;

    ComboBox<String> rules_combobox;

    ComboBox<String> cell_selection_combobox;

    Circle cell_selection_preview;

    Label generations_label;

    Button reset_button;

    Button write_output_button;
    OutputWriter output_writer;

    ImageView play_icon     = loadImage("/play.png");
    ImageView pause_icon    = loadImage("/pause.png");
    ImageView step_icon     = loadImage("/step.png");

    String text_output_button_begin = "Begin tracking output";
    String text_output_button_stop = "Stop tracking & write to file";

    String text_tooltip_pause_button = "Play/pause the simulation";
    String text_tooltip_reset_button = "Reset the simulation. Resetting twice clears the CA's initial grid.";
    String text_tooltip_rules_combobox = "Choose one of the available CAs";
    String text_tooltip_step_button = "Simulate one step of the CA";
    String text_tooltip_lighting_button = "Toggle top-down lighting";

    String text_brush = "Brush:";
    String text_lighting_checkbox = "Toggle lighting";
    String text_reset_button = "Reset";

    private void initializeGUI() {
        output_writer = null;
        write_output_button.setText(text_output_button_begin);

        pause_button.setGraphic(play_icon);
    }

    public AutomataGUI(Stage stage, AutomataApplication parent) {
        this.parent_application = parent;

        primary_stage = stage;

        top_bar = new ToolBar();
        top_bar.setPadding(new Insets(15, 12, 15, 12));

        bottom_bar = new ToolBar();
        bottom_bar.setPadding(new Insets(15, 12, 15, 12));
        bottom_bar.setPrefHeight(10);

        write_output_button = new Button();
        write_output_button.setOnAction(e -> {
            if (output_writer == null) {
                output_writer = new OutputWriter(parent_application);
                write_output_button.setText(text_output_button_stop);
            } else {
                output_writer.writeCellCountsToFile();
                output_writer.end();
                output_writer = null;
                write_output_button.setText(text_output_button_begin);
            }
        });

        generations_label = new Label();
        bottom_bar.getItems().add(generations_label);

        canvas = new Canvas(100, 100);

        rules_combobox = new ComboBox<>();
        rules_combobox.getItems().addAll(AutomataApplication.rule_sets);
        rules_combobox.setValue(AutomataApplication.rule_sets.get(0));
        rules_combobox.setOnAction(e -> {
            parent_application.setRules(rules_combobox.getValue());
            initializeGUI();
        });

        pause_button = new Button();
        pause_button.setOnAction(e -> parent_application.pauseUnpause()
        );

        step_button = new Button();
        step_button.setGraphic(step_icon);
        step_button.setOnAction(e -> {
            Grid.paused = false;
            Grid.update();
            parent_application.pause();
        });

        reset_button = new Button(text_reset_button);
        reset_button.setOnAction(e -> {
            parent_application.pause();
            Grid.resetGrid();
            if (!Grid.unaltered) {
                Grid.rule_set.initializeGrid();
                Grid.unaltered = true;
            }
            initializeGUI();
        });

        lighting = new CheckBox(text_lighting_checkbox);
        lighting.setSelected(false);


        cell_selection_combobox = new ComboBox<>();
        cell_selection_combobox.setOnAction(e -> {
            if (cell_selection_combobox.getValue() != null){
                String selected = cell_selection_combobox.getValue();
                Grid.mouse_cell_selection = cell_selection_combobox.getItems().indexOf(selected) + 1;
                cell_selection_preview.setFill(Grid.rule_set.getColor(Grid.mouse_cell_selection));
            }
        });

        final Pane spacer = new Pane();
        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        cell_selection_preview = new Circle();
        cell_selection_preview.setRadius(6);
        cell_selection_preview.setStroke(Color.BLACK);
        cell_selection_preview.setStrokeWidth(1);

        top_bar.getItems().add(rules_combobox);
        top_bar.getItems().add(reset_button);
        top_bar.getItems().add(new Separator());
        top_bar.getItems().add(pause_button);
        top_bar.getItems().add(step_button);
        top_bar.getItems().add(new Separator());
        top_bar.getItems().add(new Label(text_brush));
        top_bar.getItems().add(cell_selection_combobox);
        top_bar.getItems().add(cell_selection_preview);
        top_bar.getItems().add(new Separator());
        top_bar.getItems().add(spacer);
        top_bar.getItems().add(lighting);

        bottom_bar.getItems().add(spacer);
        bottom_bar.getItems().add(write_output_button);

        pause_button.setTooltip(    new Tooltip(text_tooltip_pause_button)      );
        reset_button.setTooltip(    new Tooltip(text_tooltip_reset_button)      );
        step_button.setTooltip(     new Tooltip(text_tooltip_step_button)       );
        rules_combobox.setTooltip(  new Tooltip(text_tooltip_rules_combobox)    );
        lighting.setTooltip(        new Tooltip(text_tooltip_lighting_button)   );

        contents = new VBox();
        contents.getChildren().addAll(top_bar, canvas, bottom_bar);

        Scene scene = new Scene(contents, 800, 800, Color.GRAY);

        stage.setTitle(AutomataApplication.application_title);
        stage.setScene(scene);
        stage.show();

        addMouseEvents(scene);

        drawGrid();
        initializeGUI();
    }

    private void addMouseEvents(Scene scene) {
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
    }

    private ImageView loadImage(String url) {
        Image image = new Image(url);
        return new ImageView(image);
    }


    // return x, y pair of mouse coordinates on the grid
    public int[] translateMouseCoordinates(double x, double y) {
        int[] xy = {0,0};

        xy[0] = (int) (100.0 * x/contents.getWidth());
        xy[1] = (int) (100 * (y - top_bar.getHeight())/(contents.getHeight() - top_bar.getHeight() - bottom_bar.getHeight()));

        return xy;
    }

    public void drawGrid() {
        if (Grid.rule_set == null)
            return;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        int[] light_column = new int[Grid.grid_width];
        double lightDepth = 10.0;

        double scale_x = contents.getWidth()/100.0;
        double scale_y = (contents.getHeight() - top_bar.getHeight() - bottom_bar.getHeight())/100.0 ;

        canvas.setWidth(100 * scale_x);
        canvas.setHeight(100 * scale_y);

        gc.setFill(Color.DARKGRAY);
        gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());

        for(int x = 0; x < Grid.grid_width; x++) {
            for(int y = 0; y < Grid.grid_height; y++) {
                if (Grid.grid[x][y] == 0)
                    light_column[x] = -1;
                else if (light_column[x] < lightDepth)
                    light_column[x]++;

                gc.setGlobalAlpha(1);
                gc.setFill(Grid.rule_set.getColor(Grid.grid[x][y]));
                gc.fillOval(scale_x * x, scale_y * y, scale_x, scale_y);

                if (lighting.isSelected()) {
                    gc.setGlobalAlpha(light_column[x] / lightDepth);
                    gc.setFill(Color.BLACK);
                    gc.fillOval(scale_x * x, scale_y * y, scale_x, scale_y);
                }
            }
        }

        generations_label.setText(Grid.getGenerations() + " generations");
    }
}
