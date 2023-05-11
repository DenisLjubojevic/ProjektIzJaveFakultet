package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Staniste;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BrisanjeStanistaController {
    @FXML
    private TableView<Staniste> stanisteTableView;

    @FXML
    private TableColumn<Staniste, String> brojZivotinjaTableColumn;

    @FXML
    private TableColumn<Staniste, String> razredZivotinjaTableColumn;

    @FXML
    private TableColumn<Staniste, String> imaHraneTableColumn;

    @FXML
    private TableColumn<Staniste, String> imaVodeTableColumn;

    List<Staniste> stanista = new ArrayList<>();
    public synchronized void initialize(){
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
        razredZivotinjaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSistematika().razred()));
        imaHraneTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImaHrane().toString()));
        imaVodeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImaVode().toString()));

        stanisteTableView.setItems(FXCollections.observableList(stanista));
    }

    public synchronized void obrisiStaniste(){
        Staniste staniste = stanisteTableView.getSelectionModel().getSelectedItem();
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Potvrda brisanja");
            alert.setContentText("Jeste li sigurni da želite obrisati odabranu aktivnost");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                BazaPodataka.obrisiStaniste(staniste);
                AzurirajZivotinjuController.spremiPromjenu(staniste.getClass().getSimpleName(), "-", "admin", LocalDateTime.now());
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Brisanje staništa");
                alert2.setHeaderText("Uspješno brisanje!");
                alert2.setContentText("Stanište razreda " + staniste.getZivotinja().get(0).getSistematika().razred() + " je uspješno obrisano!");

                alert2.showAndWait();
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
}
