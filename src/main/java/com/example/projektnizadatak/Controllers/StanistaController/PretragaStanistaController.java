package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Stanista.Hrana;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import javafx.beans.binding.Bindings;
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
import java.sql.Timestamp;
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
    private ChoiceBox<Hrana> hranaChoiceBox;
    @FXML
    private ChoiceBox<String> hranjenjeChoiceBox;

    @FXML
    private TableView<Staniste> stanisteTableView;

    @FXML
    private TableColumn<Staniste, String> brojZivotinjaTableColumn;

    @FXML
    private TableColumn<Staniste, String> vrstaZivotinjaTableColumn;

    @FXML
    private TableColumn<Staniste, String> hranaTableColumn;

    @FXML
    private TableColumn<Staniste, String> hranjenjeTableColumn;
    List<Staniste> stanista = new ArrayList<>();
    List<Hrana> hrane = new ArrayList<>();

    @FXML
    private Button pretraziButton;
    @FXML
    private Button dodajButton;
    @FXML
    private Button urediButton;
    @FXML
    private Button obrisiButton;
    @FXML
    private HBox hBox;
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
    private boolean popravljenLayout = false;
    public synchronized void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        if (!Objects.equals(loginScreenController.roleKorisnika, "Admin")){
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
            hrane = BazaPodataka.dohvatiSvuHranu();
        } catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje obroka!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        hranaChoiceBox.getItems().add(new Hrana(0, "Odabir", 0, "Prazno"));
        for (Hrana h: hrane) {
            hranaChoiceBox.getItems().add(h);
        }

        hranaChoiceBox.getSelectionModel().selectFirst();

        hranjenjeChoiceBox.getItems().add("Odabir");
        hranjenjeChoiceBox.getItems().add("10:00");
        hranjenjeChoiceBox.getItems().add("11:00");
        hranjenjeChoiceBox.getItems().add("12:00");
        hranjenjeChoiceBox.getItems().add("13:00");

        hranjenjeChoiceBox.getSelectionModel().selectFirst();

        brojZivotinjaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBrojJedinki())));
        vrstaZivotinjaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSistematika().vrsta()));
        hranaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHrana().getVrstaHrane()));
        hranjenjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVrijemeHranjenja().toString()));

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


        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(vrstaLabel);
        MainApplication.setupText(brojLabel);
        MainApplication.setupText(hranaLabel);
        MainApplication.setupText(hranjenjeLabel);

        MainApplication.setupButton(pretraziButton);
        MainApplication.setupButton(dodajButton);
        MainApplication.setupButton(urediButton);
        MainApplication.setupButton(obrisiButton);
    }

    private void prikaziDetaljeStanista(Staniste staniste) throws IOException {
        if (!loginScreenController.roleKorisnika.equals("Korisnik")){
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
        Hrana hrana = hranaChoiceBox.getValue();
        String hranjenje = hranjenjeChoiceBox.getValue();

        List<Staniste> filtriranaStanista = stanista;

        if (!vrsta.isEmpty()) {
            filtriranaStanista = filtriranaStanista.stream()
                    .filter(s -> s.getSistematika().vrsta().contains(vrsta))
                    .toList();
        }

        if(!brojZivotinja.isEmpty()){
            filtriranaStanista = filtriranaStanista.stream()
                    .filter(s -> Integer.parseInt(brojZivotinja) == s.getBrojJedinki())
                    .toList();
        }

        if (!hrana.getVrstaHrane().equals("Odabir")){
            filtriranaStanista = filtriranaStanista.stream()
                    .filter(s -> s.getHrana().getVrstaHrane().equals(hrana.getVrstaHrane()))
                    .toList();
        }

        if (!hranjenje.equals("Odabir")){
            filtriranaStanista = filtriranaStanista.stream()
                    .filter(s -> s.getVrijemeHranjenja().equals(Timestamp.valueOf(hranjenje)))
                    .toList();
        }

        stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
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
                    Promjene promjena = new Promjene(
                            1,
                            loginScreenController.prijavljeniKorisnik.getId(),
                            "Obrisano stanište",
                            new Timestamp(System.currentTimeMillis()));
                    try{
                        BazaPodataka.spremiPromjenu(promjena);
                    }catch (BazaPodatakaException ex){

                    }

                    BazaPodataka.obrisiStaniste(staniste);
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
