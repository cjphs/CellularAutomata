module com.christophersch.cellularautomata {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.christophersch.cellularautomata to javafx.fxml;
    exports com.christophersch.cellularautomata;
    exports com.christophersch.cellularautomata.RuleSets;
    opens com.christophersch.cellularautomata.RuleSets to javafx.fxml;
}