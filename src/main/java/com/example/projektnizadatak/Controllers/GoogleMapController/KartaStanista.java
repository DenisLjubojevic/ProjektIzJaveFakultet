package com.example.projektnizadatak.Controllers.GoogleMapController;


import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.StanistaController.DetaljiStanista;
import com.example.projektnizadatak.Entiteti.Korisnici.Role;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class KartaStanista {
    @FXML
    private ImageView kartaImage;
    @FXML
    private Button lavStanisteButton;
    @FXML
    private Button pingvinStanisteButton;
    @FXML
    private Button zmijaStanisteButton;
    @FXML
    private Button hippoStanisteButton;

    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        Image zooMap = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/com/example/projektnizadatak/Images/kartaZoo.jpg")));
        kartaImage.setImage(zooMap);

        lavStanisteButton.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> kartaImage.getBoundsInParent().getMinX() + kartaImage.getBoundsInParent().getWidth() * 0.48,
                kartaImage.boundsInParentProperty()
        ));


        lavStanisteButton.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> kartaImage.getBoundsInParent().getMinY() + kartaImage.getBoundsInParent().getHeight() * 0.46,
                kartaImage.boundsInParentProperty()
        ));

        pingvinStanisteButton.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> kartaImage.getBoundsInParent().getMinX() + kartaImage.getBoundsInParent().getWidth() * 0.385,
                kartaImage.boundsInParentProperty()
        ));


        pingvinStanisteButton.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> kartaImage.getBoundsInParent().getMinY() + kartaImage.getBoundsInParent().getHeight() * 0.67,
                kartaImage.boundsInParentProperty()
        ));

        zmijaStanisteButton.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> kartaImage.getBoundsInParent().getMinX() + kartaImage.getBoundsInParent().getWidth() * 0.27,
                kartaImage.boundsInParentProperty()
        ));


        zmijaStanisteButton.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> kartaImage.getBoundsInParent().getMinY() + kartaImage.getBoundsInParent().getHeight() * 0.78,
                kartaImage.boundsInParentProperty()
        ));

        hippoStanisteButton.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> kartaImage.getBoundsInParent().getMinX() + kartaImage.getBoundsInParent().getWidth() * 0.68,
                kartaImage.boundsInParentProperty()
        ));


        hippoStanisteButton.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> kartaImage.getBoundsInParent().getMinY() + kartaImage.getBoundsInParent().getHeight() * 0.48,
                kartaImage.boundsInParentProperty()
        ));
    }

    public void showHabitatDetails(javafx.event.ActionEvent actionEvent) {
        if (!loginScreenController.roleKorisnika.equals(Role.KORISNIK)){
            Button stisnutiGumb = (Button) actionEvent.getSource();
            Optional<Staniste> odabranoStaniste = pronadjiStaniste(stisnutiGumb);

            if (odabranoStaniste.isPresent()){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/stanista/detaljiStanista.fxml"));
                    Parent root = loader.load();

                    DetaljiStanista detaljiStanista = loader.getController();
                    detaljiStanista.prikaziDetalje(odabranoStaniste.get());

                    Stage stage = MainApplication.getMainStage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Detalji staništa");
                    stage.show();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.WARNING,
                    "Odbijeno",
                    "Pristup odbijen",
                    "Nemate pravo pristupa tim podacima!"
            );
        }
    }


    private Optional<Staniste> pronadjiStaniste(Button gumb){
        if (gumb == lavStanisteButton){
            try{
                return BazaPodataka.dohvatiStaniste(69);
            }catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Učitavanje staništa!",
                        "Nismo uspjeli dohvatiti stanište!",
                        ex.getMessage()
                );
            }
        } else if (gumb == pingvinStanisteButton) {
            try{
                return BazaPodataka.dohvatiStaniste(70);
            }catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Učitavanje staništa!",
                        "Nismo uspjeli dohvatiti stanište!",
                        ex.getMessage()
                );
            }
        }else if (gumb == zmijaStanisteButton) {
            try{
                return BazaPodataka.dohvatiStaniste(97);
            }catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Učitavanje staništa!",
                        "Nismo uspjeli dohvatiti stanište!",
                        ex.getMessage()
                );
            }
        }else if (gumb == hippoStanisteButton) {
            try{
                return BazaPodataka.dohvatiStaniste(98);
            }catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Učitavanje staništa!",
                        "Nismo uspjeli dohvatiti stanište!",
                        ex.getMessage()
                );
            }
        }

        return Optional.empty();
    }


}
