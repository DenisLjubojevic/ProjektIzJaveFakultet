package com.example.projektnizadatak.Controllers.AktivnostiController;

import com.example.projektnizadatak.Entiteti.Aktivnost;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class PretragaAktivnostiController {
    @FXML
    private TextField nazivTextField;

    @FXML
    private TextField cijenaTextField;

    @FXML
    private TextField trajanjeTextField;

    @FXML
    private TableView<Aktivnost> aktivnostiTableView;

    @FXML
    private TableColumn<Aktivnost, String> nazivTableColumn;

    @FXML
    private TableColumn<Aktivnost, String> cijenaTableColumn;

    @FXML
    private TableColumn<Aktivnost, String> trajanjeTableColumn;

    List<Aktivnost> aktivnosti = new ArrayList<>();
    public void initialize(){
        try{
            aktivnosti = BazaPodataka.dohvatiSveAktivnosti();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje aktivnosti!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        nazivTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNaziv()));
        cijenaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCijena().toString()));
        trajanjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTrajanje().toString()));

        aktivnostiTableView.setItems(FXCollections.observableList(aktivnosti));
    }

    public void dohvatiAktivnosti(){
        String naziv = nazivTextField.getText();
        String cijena = cijenaTextField.getText();
        String trajanje = trajanjeTextField.getText();

        aktivnostiTableView.setItems(FXCollections.observableList(aktivnosti));
        List<Aktivnost> filtriraneAktivnosti = new ArrayList<>();
        if(naziv.length() != 0){
            filtriraneAktivnosti = aktivnosti.stream().filter(a -> a.getNaziv().contains(naziv)).toList();
            aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
        }

        if (cijena.length() != 0) {
            if(naziv.length() != 0){
                filtriraneAktivnosti = filtriraneAktivnosti.stream().filter(a -> a.getCijena().equals(Integer.parseInt(cijena))).toList();
                aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
            }else {
                filtriraneAktivnosti = aktivnosti.stream().filter(a -> a.getCijena().equals(Integer.parseInt(cijena))).toList();
                aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
            }
        }

        if (trajanje.length() != 0) {
            if(naziv.length() != 0 || cijena.length() != 0){
                filtriraneAktivnosti = filtriraneAktivnosti.stream().filter(a -> a.getTrajanje().equals(Integer.parseInt(trajanje))).toList();
                aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
            } else {
                filtriraneAktivnosti = aktivnosti.stream().filter(a -> a.getTrajanje().equals(Integer.parseInt(trajanje))).toList();
                aktivnostiTableView.setItems(FXCollections.observableList(filtriraneAktivnosti));
            }

        }
    }
}
