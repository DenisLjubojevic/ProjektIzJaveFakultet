package com.example.projektnizadatak.Controllers.GoogleMapController;

import com.example.projektnizadatak.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class KartaZooloskog {
    @FXML
    public WebView webView;

    private boolean popravljenLayout = false;
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        WebEngine webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/com/example/projektnizadatak/karta/map.html").toExternalForm());
    }
}
