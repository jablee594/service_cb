package com.example.kursovik;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button butin;

    @FXML
    private Button butmen;

    @FXML
    private TextField enterdat;

    @FXML
    private TextField entervol;

    @FXML
    void initialize() {
        assert butin != null : "fx:id=\"butin\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert butmen != null : "fx:id=\"butmen\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert enterdat != null : "fx:id=\"enterdat\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert entervol != null : "fx:id=\"entervol\" was not injected: check your FXML file 'hello-view.fxml'.";

    }

}