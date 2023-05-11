package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import com.example.projektnizadatak.Util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class PretragaZivotinjaController {
    @FXML
    private TextField vrstaZivotinjeTextField;

    @FXML
    private TextField razredZivotinjeTextField;

    @FXML
    private TextField starostZivotinjeTextField;

    @FXML
    private RadioButton muskoRadioButton;

    @FXML
    private RadioButton zenskoRadioButton;

    @FXML
    private TableView<Zivotinja> zivotinjaTableView;

    @FXML
    private TableColumn<Zivotinja, String> vrstaZivotinjeTableColumn;

    @FXML
    private TableColumn<Zivotinja, String> razredZivotinjeTableColumn;

    @FXML
    private TableColumn<Zivotinja, String> starostZivotinjeTableColumn;

    @FXML
    private TableColumn<Zivotinja, String> spolZivotinjeTableColumn;

    private List<Zivotinja> zivotinje;
    final ToggleGroup spolToggleGroup = new ToggleGroup();

    public void initialize(){
        try{
            zivotinje = BazaPodataka.dohvatiSveZivotinje();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje životinja!");
            alert.setHeaderText("Pogreška učitavanja!");
            alert.setContentText(ex.getMessage());

            alert.showAndWait();
        }

        muskoRadioButton.setToggleGroup(spolToggleGroup);
        zenskoRadioButton.setToggleGroup(spolToggleGroup);

        vrstaZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSistematika().vrsta()));
        razredZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSistematika().razred()));
        starostZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStarost().toString()));
        spolZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpol()));

        zivotinjaTableView.setItems(FXCollections.observableList(zivotinje));
    }

    public void dohvatiZivotinje(){
        String vrsta = vrstaZivotinjeTextField.getText();
        String razred = razredZivotinjeTextField.getText();
        String starost = starostZivotinjeTextField.getText();

        String spol;
        if(muskoRadioButton.isSelected()){
            spol = "Muško";
        }else if(zenskoRadioButton.isSelected()){
            spol = "Žensko";
        } else {
            spol = "";
        }

        zivotinjaTableView.setItems(FXCollections.observableList(zivotinje));
        List<Zivotinja> filtriraneZivotinje = new ArrayList<>();
        if(vrsta.length() != 0){
            filtriraneZivotinje = zivotinje.stream().filter(z -> z.getSistematika().vrsta().contains(vrsta)).toList();
            zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
        }

        if (razred.length() != 0) {
            if(vrsta.length() != 0){
                filtriraneZivotinje = filtriraneZivotinje.stream().filter(z -> z.getSistematika().razred().contains(razred)).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            }else {
                filtriraneZivotinje = zivotinje.stream().filter(z -> z.getSistematika().razred().contains(razred)).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            }
        }

        if (starost.length() != 0) {
            if(vrsta.length() != 0 || razred.length() != 0){
                filtriraneZivotinje = filtriraneZivotinje.stream().filter(z -> z.getStarost().equals(Integer.parseInt(starost))).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            } else {
                filtriraneZivotinje = zivotinje.stream().filter(z -> z.getStarost().equals(Integer.parseInt(starost))).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            }

        }

        if (spol.length() != 0) {
            if(vrsta.length() != 0 || razred.length() != 0 || starost.length() != 0){
                filtriraneZivotinje = filtriraneZivotinje.stream().filter(z -> z.getSpol().contains(spol)).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            } else {
                filtriraneZivotinje = zivotinje.stream().filter(z -> z.getSpol().contains(spol)).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            }
        }
    }
}
