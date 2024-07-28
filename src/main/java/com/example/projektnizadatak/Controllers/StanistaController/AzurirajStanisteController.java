package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Stanista.Obrok;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

public class AzurirajStanisteController {
    @FXML
    private TextField brojZivotinjaTextField;

    @FXML
    private TextField vrstaZivotinjaTextField;

    @FXML
    private ChoiceBox<Obrok> obrokChoiceBox;
    @FXML
    private ImageView odabranaSlika;
    private String stariBrojJedinki;
    private String staraVrsta;
    List<Staniste> stanista = new ArrayList<>();
    List<Zivotinja> zivotinje = new ArrayList<>();
    List<Obrok> obroci = new ArrayList<>();
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
            obroci = BazaPodataka.dohvatiSveObroke();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje obroka!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        for (Obrok o: obroci) {
            if (obrokChoiceBox.getItems().contains(o)){
                continue;
            }
            obrokChoiceBox.getItems().add(o);
        }

        obrokChoiceBox.getSelectionModel().selectFirst();
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
        vrstaZivotinjaTextField.setText(staniste.getZivotinja().get(0).getSistematika().vrsta());
        brojZivotinjaTextField.setText(String.valueOf(staniste.getZivotinja().size()));
        obrokChoiceBox.getSelectionModel().select(staniste.getObrok());
        odabranaSlika.setImage(MainApplication.byteArrayToImage(staniste.getSlikaStanista()));

        stariBrojJedinki = String.valueOf(staniste.getZivotinja().size());
        staraVrsta = staniste.getZivotinja().get(0).getSistematika().vrsta();
        trazenoStaniste = staniste;
    }

    public synchronized void izmjeniStaniste(){
        String vrsta = vrstaZivotinjaTextField.getText();
        String brojJedinki = brojZivotinjaTextField.getText();
        Obrok obrok = obrokChoiceBox.getValue();
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
            trazenoStaniste.setObrok(obrok);
            trazenoStaniste.setSlikaStanista(slikaStanista);

            try {
                Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                        Alert.AlertType.CONFIRMATION,
                        "Potvrda",
                        "Potvrda izmjene",
                        "Jeste li sigurni da želite urediti odabrano stanište?"
                );

                if(result.get() == ButtonType.OK){
                    if(!stariBrojJedinki.equals(brojJedinki)){
                        AzurirajZivotinjuController.spremiPromjenu(stariBrojJedinki, brojJedinki, "admin", LocalDateTime.now());
                    }

                    if(!staraVrsta.equals(vrsta)){
                        AzurirajZivotinjuController.spremiPromjenu(staraVrsta, vrsta, "admin", LocalDateTime.now());
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
