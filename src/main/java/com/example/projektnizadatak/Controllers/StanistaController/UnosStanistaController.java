package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Entiteti.Stanista.Hrana;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UnosStanistaController {
    @FXML
    private TextField brojZivotinjaTextField;

    @FXML
    private TextField vrstaZivotinjaTextField;

    @FXML
    private ChoiceBox<Hrana> hranaChoiceBox;
    @FXML
    private ChoiceBox<String> vrijemeHranjenjaChoiceBox;
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
    private Label vrijemeHranjenjaLabel;
    @FXML
    private Label slikaLabel;
    @FXML
    private Button odaberiButton;
    @FXML
    private Button spremiButton;
    @FXML
    private BorderPane borderPane;

    private byte[] slikaStanista;

    List<Hrana> hrane = new ArrayList<>();
    private boolean popravljenLayout = false;
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            hrane = BazaPodataka.dohvatiSvuHranu();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje zaposlenika!",
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

        vrijemeHranjenjaChoiceBox.getItems().add("10:00");
        vrijemeHranjenjaChoiceBox.getItems().add("11:00");
        vrijemeHranjenjaChoiceBox.getItems().add("12:00");
        vrijemeHranjenjaChoiceBox.getItems().add("13:00");

        vrijemeHranjenjaChoiceBox.getSelectionModel().selectFirst();

        Image placeholder = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("/com/example/projektnizadatak/Images/imagePlaceholder.jpg")));
        odabranaSlika.setImage(placeholder);

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(vrstaLabel);
        MainApplication.setupText(brojLabel);
        MainApplication.setupText(hranaLabel);
        MainApplication.setupText(vrijemeHranjenjaLabel);
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
        Hrana hrana = hranaChoiceBox.getValue();
        String vrijemeHranjenja = vrijemeHranjenjaChoiceBox.getValue();

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
            Staniste novoStaniste = new Staniste(id + 1, odabraneZivotinje, Integer.valueOf(brojZivotinja), hrana, LocalTime.parse(vrijemeHranjenja), slikaStanista);
            try{
                Promjene promjena = new Promjene(
                        1,
                        loginScreenController.prijavljeniKorisnik.getId(),
                        "Dodano stanište",
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

                BazaPodataka.spremiStaniste(novoStaniste);
                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Spremanje staništa!",
                        "Uspješno spremljeno stanište!",
                        "Stanište " + vrsta + " je uspješno spremljeno!"
                );
            } catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Spremanje staništa!",
                        "Pogreška prilikom spremanja!",
                        ex.getMessage()
                );
            }
        }
    }
}
