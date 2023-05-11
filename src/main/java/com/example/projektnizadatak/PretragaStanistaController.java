package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Staniste;
import com.example.projektnizadatak.Entiteti.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import com.example.projektnizadatak.Util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class PretragaStanistaController {
    @FXML
    private TextField brojZivotinjaTextField;

    @FXML
    private TextField razredZivotinjaTextField;

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

    public synchronized void dohvatiStanista(){
        String brojZivotinja = brojZivotinjaTextField.getText();
        String razred = razredZivotinjaTextField.getText();

        stanisteTableView.setItems(FXCollections.observableList(stanista));
        List<Staniste> filtriranaStanista = new ArrayList<>();
        if(brojZivotinja.length() != 0){
            filtriranaStanista = stanista.stream().filter(s -> Integer.parseInt(brojZivotinja) == s.getZivotinja().size()).toList();
            stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
        }

        if (razred.length() != 0) {
            if(brojZivotinja.length() != 0){
                filtriranaStanista = filtriranaStanista.stream().filter(s -> s.getZivotinja().get(0).getSistematika().razred().contains(razred)).toList();
                stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
            }else {
                filtriranaStanista = stanista.stream().filter(s -> s.getZivotinja().get(0).getSistematika().razred().contains(razred)).toList();
                stanisteTableView.setItems(FXCollections.observableList(filtriranaStanista));
            }
        }
    }

    public synchronized void NahraniZivotinju(){
        Staniste staniste = stanisteTableView.getSelectionModel().getSelectedItem();
        staniste.opskrbiHranom();

        try {
            BazaPodataka.azurirajStaniste(staniste);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Opskrbljivanje staništa hranom!");
            alert.setHeaderText("Uspješno opskrbljeno!");
            alert.setContentText("Životinje razreda " + staniste.getZivotinja().get(0).getSistematika().razred() + " su uspješno dobile hranu!");

            alert.showAndWait();

        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }

    public void NapojiZivotinju(){
        Staniste staniste = stanisteTableView.getSelectionModel().getSelectedItem();
        staniste.opskrbiVodom();

        try {
            BazaPodataka.azurirajStaniste(staniste);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Opskrbljivanje staništa vodom!");
            alert.setHeaderText("Uspješno opskrbljeno!");
            alert.setContentText("Životinje razreda " + staniste.getZivotinja().get(0).getSistematika().razred() + " su uspješno dobile vodu!");

            alert.showAndWait();
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
}
