package com.example.projektnizadatak.Controllers.RasporedRadaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Zaposlenici.RasporedRada;
import com.example.projektnizadatak.Entiteti.Zaposlenici.Smjena;
import com.example.projektnizadatak.Entiteti.Zaposlenici.Zaposlenici;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class RasporedRadaController {
    @FXML
    private Label naslovLabel;
    @FXML
    private Button dodajButton;
    @FXML
    private Button obrisiButton;

    @FXML
    private TableView<RasporedRada> rasporedTableView;
    @FXML
    private TableColumn<RasporedRada, String> zaposlenikTableColumn;
    @FXML
    private TableColumn<RasporedRada, String> ponTableColumn;
    @FXML
    private TableColumn<RasporedRada, String> utoTableColumn;
    @FXML
    private TableColumn<RasporedRada, String> sriTableColumn;
    @FXML
    private TableColumn<RasporedRada, String> cetTableColumn;
    @FXML
    private TableColumn<RasporedRada, String> petTableColumn;
    @FXML
    private HBox hBox;

    List<Zaposlenici> zaposlenici = new ArrayList<>();
    List<RasporedRada> rasporedi = new ArrayList<>();

    private boolean popravljenLayout = false;
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        if (!Objects.equals(loginScreenController.roleKorisnika, "Admin") &&
                !Objects.equals(loginScreenController.roleKorisnika, "Voditelj")){
            hBox.getChildren().remove(dodajButton);
            hBox.getChildren().remove(obrisiButton);
        }

        try{
            zaposlenici = BazaPodataka.dohvatiSveZaposlenike();
        }catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje zaposlenika!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        for (Zaposlenici z: zaposlenici){
            try{
                RasporedRada noviRaspored = BazaPodataka.dohvatiRasporedPoZaposleniku(z.getId());

                if (!noviRaspored.getSmjenaPoDanima().entrySet().isEmpty()){
                    rasporedi.add(noviRaspored);
                }
            }catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Učitavanje rasporeda!",
                        "Pogreška učitavanja!",
                        ex.getMessage()
                );
            }
        }

        zaposlenikTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getZaposlenik().getIme() + " " + cellData.getValue().getZaposlenik().getPrezime()));
        ponTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSmjenaPoDanima().get("PON").ispisiSmjenu()));
        utoTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSmjenaPoDanima().get("UTO").ispisiSmjenu()));
        sriTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSmjenaPoDanima().get("SRI").ispisiSmjenu()));
        cetTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSmjenaPoDanima().get("ČET").ispisiSmjenu()));
        petTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSmjenaPoDanima().get("PET").ispisiSmjenu()));

        rasporedTableView.setItems(FXCollections.observableList(rasporedi));

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupButton(dodajButton);
        MainApplication.setupButton(obrisiButton);
    }

    public void dodajRaspored() throws IOException {
        IzbornikController.promjeniEkran(
                "rasporedRada/dodajRaspored.fxml",
                "Dodaj raspored");
    }

    public void obrisiSveRasporede(){
        try {
            Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                    Alert.AlertType.CONFIRMATION,
                    "Potvrda",
                    "Potvrda brisanja",
                    "Jeste li sigurni da želite obrisati sve raporede?"
            );

            if(result.get() == ButtonType.OK){
                Promjene promjena = new Promjene(
                        1,
                        loginScreenController.prijavljeniKorisnik.getId(),
                        "Obrisani rasporedi",
                        new Timestamp(System.currentTimeMillis()));
                try{
                    BazaPodataka.spremiPromjenu(promjena);
                }catch (BazaPodatakaException ex){

                }

                BazaPodataka.obrisiSveRasporede();
                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Brisanje rasporeda",
                        "Uspješno brisanje!",
                        "Svi rasporedi su uspješno obrisani!"
                );

                initialize();
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
    }
}
