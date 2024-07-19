package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Stanista.Obrok;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PretragaStanistaController {
    @FXML
    private TextField brojZivotinjaTextField;

    @FXML
    private TextField vrstaZivotinjaTextField;

    @FXML
    private ChoiceBox<Obrok> obrokChoiceBox;

    @FXML
    private TableView<Staniste> stanisteTableView;

    @FXML
    private TableColumn<Staniste, String> brojZivotinjaTableColumn;

    @FXML
    private TableColumn<Staniste, String> vrstaZivotinjaTableColumn;

    @FXML
    private TableColumn<Staniste, String> obrokTableColumn;

    List<Staniste> stanista = new ArrayList<>();
    List<Obrok> obroci = new ArrayList<>();

    @FXML
    private Button dodajButton;
    @FXML
    private Button urediButton;
    @FXML
    private Button obrisiButton;
    @FXML
    private HBox hBox;

    private boolean popravljenLayout = false;
    public synchronized void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        if (!Objects.equals(loginScreenController.roleKorisnika, "admin")){
            hBox.getChildren().remove(dodajButton);
            hBox.getChildren().remove(urediButton);
            hBox.getChildren().remove(obrisiButton);
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
            obroci = BazaPodataka.dohvatiSveObroke();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje obroka!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        obrokChoiceBox.getItems().add(new Obrok(0, "Odabir", 0, LocalTime.now(), "Prazno"));
        for (Obrok o: obroci) {
            obrokChoiceBox.getItems().add(o);
        }

        obrokChoiceBox.getSelectionModel().selectFirst();

        brojZivotinjaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBrojJedinki())));
        vrstaZivotinjaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSistematika().vrsta()));
        obrokTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObrok().getVrstaHrane()));

        stanisteTableView.setItems(FXCollections.observableList(stanista));

        stanisteTableView.setRowFactory(tv -> {
            TableRow<Staniste> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()){
                    Staniste rowData = row.getItem();
                    try {
                        prikaziDetaljeStanista(rowData);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
    }

    private void prikaziDetaljeStanista(Staniste staniste) throws IOException {
        if (loginScreenController.roleKorisnika.equals("admin")){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/stanista/detaljiStanista.fxml"));
                Parent root = loader.load();

                DetaljiStanista detaljiStanista = loader.getController();
                detaljiStanista.prikaziDetalje(staniste);

                Stage stage = MainApplication.getMainStage();
                stage.setScene(new Scene(root));
                stage.setTitle("Detalji staništa");
                stage.show();
            }catch (IOException e) {
                e.printStackTrace();
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

    public synchronized void dohvatiStanista(){
        String vrsta = vrstaZivotinjaTextField.getText();
        String brojZivotinja = brojZivotinjaTextField.getText();
        Obrok obrok = obrokChoiceBox.getValue();

        stanisteTableView.setItems(FXCollections.observableList(stanista));
        List<Staniste> filtriranaStanista = new ArrayList<>();

        if (!vrsta.isEmpty() && obrok.getVrstaHrane().equals("Odabir")) {
            filtriranaStanista = stanista.stream().filter(s -> s.getSistematika().vrsta().contains(vrsta)).toList();
            stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
        }

        if(!brojZivotinja.isEmpty() && obrok.getVrstaHrane().equals("Odabir")){
            if (!vrsta.isEmpty()){
                filtriranaStanista = filtriranaStanista.stream().filter(s -> Integer.parseInt(brojZivotinja) == s.getBrojJedinki()).toList();
                stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
            }else{
                filtriranaStanista = stanista.stream().filter(s -> Integer.parseInt(brojZivotinja) == s.getBrojJedinki()).toList();
                stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
            }

        }

        if (!obrok.getVrstaHrane().equals("Odabir")){
            if (!vrsta.isEmpty() || !brojZivotinja.isEmpty()){
                filtriranaStanista = filtriranaStanista.stream().filter(s -> s.getObrok().equals(obrok)).toList();
                stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
            }else{
                filtriranaStanista = stanista.stream().filter(s -> s.getObrok().equals(obrok)).toList();
                stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
            }

        }
    }

    public void dodajStaniste() throws IOException {
        IzbornikController.promjeniEkran(
                "stanista/unosStanista.fxml",
                "Dodaj stanište");
    }

    public void azurirajStaniste(){
        Staniste staniste = stanisteTableView.getSelectionModel().getSelectedItem();
        if (staniste != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/stanista/azurirajStaniste.fxml"));
                Parent root = loader.load();

                AzurirajStanisteController azurirajStaniste = loader.getController();
                azurirajStaniste.dohvatiStaniste(staniste);

                Stage stage = MainApplication.getMainStage();
                stage.setScene(new Scene(root));
                stage.setTitle("Promijeni stanište");
                stage.show();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jedno stanište iz tablice."
            );
        }
    }

    public synchronized void obrisiStaniste(){
        Staniste staniste = stanisteTableView.getSelectionModel().getSelectedItem();
        if (staniste != null){
            try {
                Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                        Alert.AlertType.CONFIRMATION,
                        "Potvrda",
                        "Potvrda brisanja",
                        "Jeste li sigurni da želite obrisati odabrano stanište?"
                );

                if(result.get() == ButtonType.OK){
                    BazaPodataka.obrisiStaniste(staniste);
                    AzurirajZivotinjuController.spremiPromjenu(staniste.getClass().getSimpleName(), "-", "admin", LocalDateTime.now());
                    MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Brisanje staništa",
                            "Uspješno brisanje!",
                            "Stanište vrste " + staniste.getZivotinja().get(0).getSistematika().vrsta() + " je uspješno obrisano!"
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
                    "Trebate odabrati jedno stanište iz tablice."
            );
        }

        initialize();
    }


}
