package com.example.projektnizadatak.Controllers;

import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class PromjeneController {
    @FXML
    private Label naslovLabel;
    @FXML
    private Label brisanjeLabel;
    @FXML
    private Button obrisiButton;
    @FXML
    private Button obrisiSveButton;

    @FXML
    private TableView<Promjene> promjeneTableView;

    @FXML
    private TableColumn<Promjene, String> korisnikTableColumn;

    @FXML
    private TableColumn<Promjene, String> opisPromjeneTableColumn;

    @FXML
    private TableColumn<Promjene, String> vrijemePromjeneTableColumn;

    List<Promjene> promjene;

    private boolean popravljenLayout = false;

    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        try{
            promjene = BazaPodataka.dohvatiPromjene();
        }catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje promjena!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        korisnikTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsernameById()));
        opisPromjeneTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOpisPromjene()));
        vrijemePromjeneTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVrijemePromjene().toString()));

        promjeneTableView.setItems(FXCollections.observableList(promjene));

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(brisanjeLabel);
        MainApplication.setupButton(obrisiButton);
        MainApplication.setupButton(obrisiSveButton);
    }

    public void obrisiSvePromjene() {
        Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                Alert.AlertType.CONFIRMATION,
                "Potvrda",
                "Potvrda brisanja",
                "Jeste li sigurni da želite obrisati sve promjene?"
        );

        if (result.get().equals(ButtonType.OK)){
            try{
                BazaPodataka.obrisiSvePromjene();
                MainApplication.showAlertDialog(
                        Alert.AlertType.INFORMATION,
                        "Brisanje promjena",
                        "Uspješno brisanje!",
                        "Sve promjene su uspješno obrisane!"
                );
            }catch (BazaPodatakaException ex){
                MainApplication.showAlertDialog(
                        Alert.AlertType.ERROR,
                        "Brisanje promjena!",
                        "Pogreška brisanja!",
                        ex.getMessage()
                );
            }
        }

        initialize();
    }

    public void obrisiPromjenu() {
        Promjene promjena = promjeneTableView.getSelectionModel().getSelectedItem();

        if (promjena != null){
            Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                    Alert.AlertType.CONFIRMATION,
                    "Potvrda",
                    "Potvrda brisanja",
                    "Jeste li sigurni da želite obrisati odabranu promjenu?"
            );

            if (result.get().equals(ButtonType.OK)){
                try{
                    BazaPodataka.obrisiPromjenu(promjena);
                    MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Brisanje promjena",
                            "Uspješno brisanje!",
                            "Promjena '" + promjena.getOpisPromjene() + "' je uspješno obrisana!"
                    );
                }catch (BazaPodatakaException ex){
                    MainApplication.showAlertDialog(
                            Alert.AlertType.ERROR,
                            "Brisanje promjena!",
                            "Pogreška brisanja!",
                            ex.getMessage()
                    );
                }
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jednu promjenu iz tablice."
            );
        }

        initialize();
    }
}
