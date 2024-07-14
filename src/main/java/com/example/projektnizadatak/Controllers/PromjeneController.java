package com.example.projektnizadatak.Controllers;

import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PromjeneController {
    @FXML
    private TableView<Map.Entry<Integer, String>> staraVrijednostTableView;

    @FXML
    private TableView<Map.Entry<Integer, String>> novaVrijednostTableView;

    @FXML
    private TableView<Map.Entry<Integer, String>> roleTableView;

    @FXML
    private TableView<Map.Entry<Integer, String>> vrijemeTableView;

    @FXML
    private TableColumn<Map.Entry<Integer, String>, String> staraVrijednostTableColumn = new TableColumn<>("Key");

    @FXML
    private TableColumn<Map.Entry<Integer, String>, String> novaVrijednostTableColumn = new TableColumn<>("Key");

    @FXML
    private TableColumn<Map.Entry<Integer, String>, String> roleTableColumn = new TableColumn<>("Key");

    @FXML
    private TableColumn<Map.Entry<Integer, String>, String> datumIVrijemeTableColumn;

    public void initialize(){
        Optional<Promjene> promjena = Datoteke.deserijalizirajPromjene();

        if(promjena.isPresent()){
            staraVrijednostTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Integer, String>, String>, ObservableValue<String>>() {

                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Integer, String>, String> p) {
                    // this callback returns property for just one cell, you can't use a loop here
                    // for first column we use key
                    return new SimpleStringProperty(p.getValue().getValue());
                }
            });

            novaVrijednostTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Integer, String>, String>, ObservableValue<String>>() {

                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Integer, String>, String> p) {
                    // this callback returns property for just one cell, you can't use a loop here
                    // for first column we use key
                    return new SimpleStringProperty(p.getValue().getValue());
                }
            });

            roleTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Integer, String>, String>, ObservableValue<String>>() {

                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Integer, String>, String> p) {
                    // this callback returns property for just one cell, you can't use a loop here
                    // for first column we use key
                    return new SimpleStringProperty(p.getValue().getValue());
                }
            });

            datumIVrijemeTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Integer, String>, String>, ObservableValue<String>>() {

                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Integer, String>, String> p) {
                    // this callback returns property for just one cell, you can't use a loop here
                    // for first column we use key
                    return new SimpleStringProperty(p.getValue().getValue());
                }
            });

            ObservableList<Map.Entry<Integer, String>> items1 = FXCollections.observableArrayList(promjena.get().getStaraVrijednost().entrySet());
            staraVrijednostTableView.setItems(items1);

            ObservableList<Map.Entry<Integer, String>> items2 = FXCollections.observableArrayList(promjena.get().getNovaVrijednost().entrySet());
            novaVrijednostTableView.setItems(items2);

            ObservableList<Map.Entry<Integer, String>> items3 = FXCollections.observableArrayList(promjena.get().getRole().entrySet());
            roleTableView.setItems(items3);

            ObservableList<Map.Entry<Integer, String>> items4 = FXCollections.observableArrayList(promjena.get().getDatumIVrijeme().entrySet());
            vrijemeTableView.setItems(items4);
        }
    }

    public void izbrisiPromjene() {
        Promjene promejena = new Promjene(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
        Datoteke.serijalizirajPromjene(promejena);
        initialize();
    }
}
