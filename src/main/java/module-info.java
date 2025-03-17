module gui.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens gui.gui to javafx.fxml;
    exports gui.gui;
    exports system;
    opens system to javafx.fxml;
    exports model;
    opens model to javafx.fxml;
}