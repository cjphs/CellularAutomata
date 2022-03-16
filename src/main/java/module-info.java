module com.christophersch.cellularautomata {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.christophersch.cellularautomata to javafx.fxml;
    exports com.christophersch.cellularautomata;
    exports com.christophersch.cellularautomata.Cells;
    opens com.christophersch.cellularautomata.Cells to javafx.fxml;
}