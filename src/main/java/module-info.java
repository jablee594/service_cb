module service.cb {
    requires java.xml;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    opens Application to javafx.fxml;
    exports Application;
}