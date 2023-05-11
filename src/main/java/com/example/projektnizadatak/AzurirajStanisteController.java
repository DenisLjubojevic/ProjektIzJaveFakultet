package com.example.projektnizadatak;

import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Entiteti.Sistematika;
import com.example.projektnizadatak.Entiteti.Staniste;
import com.example.projektnizadatak.Entiteti.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.Util.BazaPodataka;
import com.example.projektnizadatak.Util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AzurirajStanisteController {
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

    private String stariBrojJedinki;
    private String stariRazred;
    List<Staniste> stanista = new ArrayList<>();
    List<Zivotinja> zivotinje = new ArrayList<>();
    Staniste trazenoStaniste;

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

        try{
            zivotinje = BazaPodataka.dohvatiSveZivotinje();
        } catch (BazaPodatakaException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Učitavanje životinja!");
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

    public synchronized void dohvatiStaniste(){
        Staniste staniste = stanisteTableView.getSelectionModel().getSelectedItem();
        brojZivotinjaTextField.setText(String.valueOf(staniste.getZivotinja().size()));
        razredZivotinjaTextField.setText(staniste.getZivotinja().get(0).getSistematika().razred());

        stariBrojJedinki = String.valueOf(staniste.getZivotinja().size());
        stariRazred = staniste.getZivotinja().get(0).getSistematika().razred();
        trazenoStaniste = staniste;
    }

    public synchronized void izmjeniStaniste(){
        String brojJedinki = brojZivotinjaTextField.getText();
        String razred = razredZivotinjaTextField.getText();

        List<Zivotinja> odabraneZivotinje = new ArrayList<>();
        for (Zivotinja z: zivotinje) {
            if(z.getSistematika().razred().equals(razred)){
                odabraneZivotinje.add(z);
            }
        }

        trazenoStaniste.setBrojJedinki(Integer.valueOf(brojJedinki));
        trazenoStaniste.setZivotinja(odabraneZivotinje);
        trazenoStaniste.setSistematika(new Sistematika(odabraneZivotinje.get(0).getSistematika().vrsta(), odabraneZivotinje.get(0).getSistematika().razred()));

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Potvrda izmjene");
            alert.setContentText("Jeste li sigurni da želite promjeniti odabranu aktivnost");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                if(!stariBrojJedinki.equals(brojJedinki)){
                    AzurirajZivotinjuController.spremiPromjenu(stariBrojJedinki, brojJedinki, "admin", LocalDateTime.now());
                }

                if(!stariRazred.equals(razred)){
                    AzurirajZivotinjuController.spremiPromjenu(stariRazred, razred, "admin", LocalDateTime.now());
                }

                BazaPodataka.azurirajStaniste(trazenoStaniste);
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Izmjena staništa");
                alert2.setHeaderText("Uspješna izmjena!");
                alert2.setContentText("Stanište razreda " + razred + " je uspješno izmjenjeno!");

                alert2.showAndWait();
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }

}
