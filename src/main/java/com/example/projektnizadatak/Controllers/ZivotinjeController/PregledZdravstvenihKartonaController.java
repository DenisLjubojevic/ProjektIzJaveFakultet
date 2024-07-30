package com.example.projektnizadatak.Controllers.ZivotinjeController;

import com.example.projektnizadatak.Controllers.StanistaController.AzurirajStanisteController;
import com.example.projektnizadatak.Entiteti.Zivotinje.ZdravstveniKarton;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PregledZdravstvenihKartonaController {
    @FXML
    private TableView<ZdravstveniKarton> kartoniTableView;
    @FXML
    private TableColumn<ZdravstveniKarton, String> datumPregledaTableColumn;
    @FXML
    private TableColumn<ZdravstveniKarton, String> dijagnozaTableColumn;
    @FXML
    private TableColumn<ZdravstveniKarton, String> terapijaTableColumn;
    @FXML
    private TableColumn<ZdravstveniKarton, String> napomeneTableColumn;

    @FXML
    private Label naslovLabel;
    @FXML
    private Button obrisiButton;
    @FXML
    private Button spremiButton;

    private Zivotinja odabranaZivotinja;

    private boolean popravljenLayout = false;
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupButton(obrisiButton);
        MainApplication.setupButton(spremiButton);
    }

    public void setOdabranaZivotinja(Zivotinja zivotinja) {
        this.odabranaZivotinja = zivotinja;
        ucitajZdravstveneKartone();
    }

    private void ucitajZdravstveneKartone(){
        try{
            List<ZdravstveniKarton> kartoni = BazaPodataka.dohvatiZdravsteveneKartoneZaZivotinju(odabranaZivotinja.getId());

            datumPregledaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatumPregleda().toString()));
            dijagnozaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDijagnoza()));
            terapijaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTerapija()));
            napomeneTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNapomena()));


            kartoniTableView.setItems(FXCollections.observableList(kartoni));
        }catch (BazaPodatakaException e) {
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje zdravstvenih kartona!",
                    "Pogreška učitavanja!",
                    e.getMessage()
            );
        }
    }

    public void dodajNoviKarton(){
        if (odabranaZivotinja != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/zivotinje/unosZdravstvenogKartona.fxml"));
                Parent root = loader.load();

                UnosZdravstvenogKartonaController unosKartona = loader.getController();
                unosKartona.setOdabranaZivotinja(odabranaZivotinja);

                Stage stage = MainApplication.getMainStage();
                stage.setScene(new Scene(root));
                stage.setTitle("Unos zdravstvenog karotna");
                stage.show();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void obrisiKarton(){
        ZdravstveniKarton karton = kartoniTableView.getSelectionModel().getSelectedItem();

        if (karton != null){
            try {
                Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                        Alert.AlertType.CONFIRMATION,
                        "Potvrda",
                        "Potvrda brisanja",
                        "Jeste li sigurni da želite obrisati odabrano stanje?"
                );
                if(result.get() == ButtonType.OK){
                    BazaPodataka.obrisiZdravstveniKarton(karton);MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Brisanje zdravstvenog kartona",
                            "Uspješno brisanje!",
                            "Karton dijagnoze " + karton.getDijagnoza() + " je uspješno obrisan!"
                    );
                }
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jedno stanje iz tablice."
            );
        }

        ucitajZdravstveneKartone();
    }
}
