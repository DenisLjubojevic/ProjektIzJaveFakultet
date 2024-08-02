package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Stanista.Hrana;
import com.example.projektnizadatak.Entiteti.Zivotinje.Sistematika;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;

public class AzurirajStanisteController {
    @FXML
    private TextField brojZivotinjaTextField;

    @FXML
    private TextField vrstaZivotinjaTextField;

    @FXML
    private ChoiceBox<Hrana> hranaChoiceBox;

    @FXML
    private ChoiceBox<String> hranjenjeChoiceBox;


    @FXML
    private ImageView odabranaSlika;
    @FXML
    private Label naslovLabel;
    @FXML
    private Label vrstaLabel;
    @FXML
    private Label brojLabel;
    @FXML
    private Label hranaLabel;
    @FXML
    private Label hranjenjeLabel;
    @FXML
    private Label slikaLabel;
    @FXML
    private Button odaberiButton;
    @FXML
    private Button spremiButton;
    @FXML
    private BorderPane borderPane;
    List<Staniste> stanista = new ArrayList<>();
    List<Zivotinja> zivotinje = new ArrayList<>();
    List<Hrana> hrane = new ArrayList<>();
    Staniste trazenoStaniste;

    private boolean popravljenLayout = false;
    private byte[] slikaStanista;

    public synchronized void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            stanista = BazaPodataka.dohvatiSvaStanista();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje staništa!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        try{
            zivotinje = BazaPodataka.dohvatiSveZivotinje();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje životinja!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        try{
            hrane = BazaPodataka.dohvatiSvuHranu();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje obroka!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        for (Hrana h: hrane) {
            if (hranaChoiceBox.getItems().contains(h)){
                continue;
            }
            hranaChoiceBox.getItems().add(h);
        }

        hranaChoiceBox.getSelectionModel().selectFirst();

        hranjenjeChoiceBox.getItems().add("10:00");
        hranjenjeChoiceBox.getItems().add("11:00");
        hranjenjeChoiceBox.getItems().add("12:00");
        hranjenjeChoiceBox.getItems().add("13:00");

        hranjenjeChoiceBox.getSelectionModel().selectFirst();

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(vrstaLabel);
        MainApplication.setupText(brojLabel);
        MainApplication.setupText(hranaLabel);
        MainApplication.setupText(hranjenjeLabel);
        MainApplication.setupText(slikaLabel);

        MainApplication.setupButton(odaberiButton);
        MainApplication.setupButton(spremiButton);

        odabranaSlika.fitWidthProperty().bind(borderPane.widthProperty().divide(7.5));
        odabranaSlika.fitHeightProperty().bind(borderPane.heightProperty().divide(6.78));
    }

    @FXML
    private void odaberiSliku(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null){
            try{
                slikaStanista = Files.readAllBytes(Paths.get(file.toURI()));
                Image image = new Image(file.toURI().toString());
                odabranaSlika.setImage(image);
            }catch (Exception ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Odabir slike!",
                        "Pogreška prilikom učitavanja slike!",
                        ex.getMessage()
                );
            }
        }
    }

    public synchronized void dohvatiStaniste(Staniste staniste){
        vrstaZivotinjaTextField.setText(staniste.getSistematika().vrsta());
        brojZivotinjaTextField.setText(String.valueOf(staniste.getZivotinja().size()));
        hranaChoiceBox.getSelectionModel().select(staniste.getHrana());

        if (staniste.getVrijemeHranjenja() != null){
            hranjenjeChoiceBox.getSelectionModel().select(staniste.getVrijemeHranjenja().toString());
        }else{
            hranjenjeChoiceBox.getSelectionModel().selectFirst();
        }


        odabranaSlika.setImage(MainApplication.byteArrayToImage(staniste.getSlikaStanista()));

        trazenoStaniste = staniste;
    }

    public synchronized void izmjeniStaniste(){
        String vrsta = vrstaZivotinjaTextField.getText();
        String brojJedinki = brojZivotinjaTextField.getText();
        Hrana hrana = hranaChoiceBox.getValue();
        String hranjenje = hranjenjeChoiceBox.getValue();
        slikaStanista = MainApplication.imageToByteArray(odabranaSlika.getImage());

        List<Zivotinja> odabraneZivotinje = new ArrayList<>();
        boolean imaVrste = false;
        for (Zivotinja z: zivotinje) {
            if(z.getSistematika().vrsta().equals(vrsta)){
                odabraneZivotinje.add(z);
                imaVrste = true;
            }
        }
        if (imaVrste){
            trazenoStaniste.setBrojJedinki(Integer.valueOf(brojJedinki));
            trazenoStaniste.setZivotinja(odabraneZivotinje);
            trazenoStaniste.setSistematika(new Sistematika(odabraneZivotinje.get(0).getSistematika().vrsta(), odabraneZivotinje.get(0).getSistematika().razred()));
            trazenoStaniste.setHrana(hrana);
            trazenoStaniste.setVrijemeHranjenja(LocalTime.parse(hranjenje));
            trazenoStaniste.setSlikaStanista(slikaStanista);

            try {
                Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                        Alert.AlertType.CONFIRMATION,
                        "Potvrda",
                        "Potvrda izmjene",
                        "Jeste li sigurni da želite urediti odabrano stanište?"
                );

                if(result.get() == ButtonType.OK){
                    Promjene promjena = new Promjene(
                            1,
                            loginScreenController.prijavljeniKorisnik.getId(),
                            "Ažurirano stanište",
                            new Timestamp(System.currentTimeMillis()));
                    try{
                        BazaPodataka.spremiPromjenu(promjena);
                    }catch (BazaPodatakaException ex){
                        MainApplication.showAlertDialog(
                                Alert.AlertType.ERROR,
                                "Pogreška!",
                                "Pogreška spremanja promjene!",
                                ex.getMessage()
                        );
                    }

                    BazaPodataka.azurirajStaniste(trazenoStaniste);
                    MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Izmjena staništa",
                            "Uspješna izmjena!",
                            "Stanište " + vrsta + " je uspješno izmjenjeno!"
                    );
                }
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }

            initialize();
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Problem",
                    "Neuspješna izmjena!",
                    vrsta + " ne postoji u bazi podataka!"
            );
        }



    }

}
