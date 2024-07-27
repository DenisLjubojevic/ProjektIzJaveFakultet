package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class DetaljiStanista {
    @FXML
    private Label vrstaLabel;

    @FXML
    private Label razredLabel;

    @FXML
    private Label brojJedinkiLabel;

    @FXML
    private Label obrokLabel;
    @FXML
    private ImageView slikaStanista;
    @FXML
    private BorderPane borderPane;

    private Staniste staniste;

    public void prikaziDetalje(Staniste staniste) {
        MainApplication.popraviLayout();
        this.staniste = staniste;
        vrstaLabel.setText(staniste.getSistematika().vrsta());
        razredLabel.setText(staniste.getSistematika().razred());
        brojJedinkiLabel.setText(staniste.getBrojJedinki().toString());
        obrokLabel.setText(staniste.getObrok().getVrstaHrane());

        Image image = MainApplication.byteArrayToImage(staniste.getSlikaStanista());
        if (image != null){
            slikaStanista.setImage(image);
        }
        slikaStanista.fitWidthProperty().bind(borderPane.widthProperty().divide(2.5));
        slikaStanista.fitHeightProperty().bind(borderPane.heightProperty().divide(2));
    }



    @FXML
    private void prikaziDetaljeObroka() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/stanista/detaljiObroka.fxml"));
            Parent root = loader.load();

            DetaljiObrokaController detaljiObrokaController = loader.getController();
            detaljiObrokaController.prikaziDetalje(staniste.getObrok());

            Stage stage = MainApplication.getMainStage();
            stage.setScene(new Scene(root));
            stage.setTitle("Detalji obroka");
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void promjeniBojuTeksta() {
        obrokLabel.setTextFill(Color.BLUE);
    }

    @FXML
    private void resetirajBojuTeksta() {
        obrokLabel.setTextFill(Color.BLACK);
    }
}
