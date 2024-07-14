package com.example.projektnizadatak.Controllers.StanistaController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Controllers.ZivotinjeController.AzurirajZivotinjuController;
import com.example.projektnizadatak.Entiteti.Stanista.Staniste;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
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
    private TextField obrokTextField;

    @FXML
    private TableView<Staniste> stanisteTableView;

    @FXML
    private TableColumn<Staniste, String> brojZivotinjaTableColumn;

    @FXML
    private TableColumn<Staniste, String> vrstaZivotinjaTableColumn;

    @FXML
    private TableColumn<Staniste, String> obrokTableColumn;

    List<Staniste> stanista = new ArrayList<>();

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje staništa!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

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
    }

    public synchronized void dohvatiStanista(){
        String brojZivotinja = brojZivotinjaTextField.getText();
        String vrsta = vrstaZivotinjaTextField.getText();
        String obrok = obrokTextField.getText();

        stanisteTableView.setItems(FXCollections.observableList(stanista));
        List<Staniste> filtriranaStanista = new ArrayList<>();
        if(!brojZivotinja.isEmpty()){
            filtriranaStanista = stanista.stream().filter(s -> Integer.parseInt(brojZivotinja) == s.getZivotinja().size()).toList();
            stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
        }

        if (!vrsta.isEmpty()) {
            filtriranaStanista = filtriranaStanista.stream().filter(s -> s.getZivotinja().get(0).getSistematika().vrsta().contains(vrsta)).toList();
            stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
        }

        if (!obrok.isEmpty()){
            filtriranaStanista = filtriranaStanista.stream().filter(s -> s.getObrok().getVrstaHrane().contains(obrok)).toList();
            stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Potreban odabir");
            alert.setContentText("Trebate odabrati jedno stanište iz tablice.");

            alert.showAndWait();
        }
    }

    public synchronized void obrisiStaniste(){
        Staniste staniste = stanisteTableView.getSelectionModel().getSelectedItem();
        if (staniste != null){
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Potvrda brisanja");
                alert.setContentText("Jeste li sigurni da želite obrisati odabrano stanište?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    BazaPodataka.obrisiStaniste(staniste);
                    AzurirajZivotinjuController.spremiPromjenu(staniste.getClass().getSimpleName(), "-", "admin", LocalDateTime.now());
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Brisanje staništa");
                    alert2.setHeaderText("Uspješno brisanje!");
                    alert2.setContentText("Stanište vrste " + staniste.getZivotinja().get(0).getSistematika().vrsta() + " je uspješno obrisano!");

                    alert2.showAndWait();
                }
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Potreban odabir");
            alert.setContentText("Trebate odabrati jedno stanište iz tablice.");

            alert.showAndWait();
        }

        initialize();
    }


}
