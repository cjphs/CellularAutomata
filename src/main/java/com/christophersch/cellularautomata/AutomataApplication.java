package com.christophersch.cellularautomata;

import com.christophersch.cellularautomata.Cells.AliveCell;
import com.christophersch.cellularautomata.Cells.Cell;
import com.christophersch.cellularautomata.Cells.DeadCell;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class AutomataApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Grid.getGridInstance();

        initUI(stage);
    }

    public void initUI(Stage stage) {
        var root = new Pane();

        Grid.fillGrid(new DeadCell());
        Grid.setCell(40, 40, new AliveCell());
        Grid.setCell(41, 40, new AliveCell());
        Grid.setCell(42, 40, new AliveCell());

        System.out.println();

        Canvas canvas = new Canvas(1000, 1000);



        var scene = new Scene(root, 1000, 1000, Color.WHITESMOKE);

        stage.setTitle("Lines");
        stage.setScene(scene);
        stage.show();


        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawGrid(gc);
        root.getChildren().add(canvas);

        new AnimationTimer() {
            int x = 40;
            @Override public void handle(long currentNanoTime) {
                Grid.update();
                drawGrid(gc);

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    // Do nothing
                }
            }
        }.start();

    }

    private void drawGrid(GraphicsContext gc) {
        for (Cell cell : Grid.getCells()) {
            gc.setFill(cell.cell_color);
            gc.fillOval(cell.x*10, cell.y*10, 10, 10);
        }
    }


    public static void main(String[] args) {
        launch();
    }
}