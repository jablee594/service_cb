module service.cb {
    requires java.xml;
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.h2database;

    opens Application to javafx.fxml;
    exports Application;
}