package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Entiteti.Stanista.Obrok;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UnosStanistaController {
    @FXML
    private TextField brojZivotinjaTextField;

    @FXML
    private TextField vrstaZivotinjaTextField;

    @FXML
    private ChoiceBox<Obrok> obrokChoiceBox;
    @FXML
    private ImageView odabranaSlika;

    private byte[] slikaStanista;

    List<Obrok> obroci = new ArrayList<>();
    private boolean popravljenLayout = false;
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            obroci = BazaPodataka.dohvatiSveObroke();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje zaposlenika!",
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
        Image placeholder = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/com/example/projektnizadatak/Images/imagePlaceholder.jpg")));
        odabranaSlika.setImage(placeholder);
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

    public synchronized void dodajStaniste() {
        List<Staniste> staniste = new ArrayList<>();
        List<Zivotinja> zivotinje = new ArrayList<>();

        try{
            staniste = BazaPodataka.dohvatiSvaStanista();
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

        String brojZivotinja = brojZivotinjaTextField.getText();
        String vrsta = vrstaZivotinjaTextField.getText();
        Obrok obrok = obrokChoiceBox.getValue();

        List<Zivotinja> odabraneZivotinje = new ArrayList<>();
        Integer id = staniste.size();

        for (Zivotinja z: zivotinje) {
            if(z.getSistematika().vrsta().equals(vrsta)){
                odabraneZivotinje.add(z);
            }
        }

        if(vrsta.isBlank() || brojZivotinja.isBlank() || slikaStanista == null){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Spremanje staništa!",
                    "Pogreška spremanja!",
                    "Potrebno je unjeti sve podatke!"
            );
        } else if (odabraneZivotinje.isEmpty()) {
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Spremanje staništa!",
                    "Pogreška spremanja!",
                    "Potrebno je unjeti vrstu životinja koja već postoji!"
            );
        } else {

            Staniste novoStaniste = new Staniste(id + 1, odabraneZivotinje, Integer.valueOf(brojZivotinja), obrok, slikaStanista);
            try{
                BazaPodataka.spremiStaniste(novoStaniste);
                AzurirajZivotinjuController.spremiPromjenu( "-", novoStaniste.getClass().getSimpleName(), "admin", LocalDateTime.now());

                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Spremanje staništa!",
                        "Uspješno spremljeno stanište!",
                        "Stanište " + vrsta + " je uspješno spremljeno!"
                );
            } catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Spremanje životinje!",
                        "Pogreška prilikom spremanja!",
                        ex.getMessage()
                );
            }
        }
    }
}
