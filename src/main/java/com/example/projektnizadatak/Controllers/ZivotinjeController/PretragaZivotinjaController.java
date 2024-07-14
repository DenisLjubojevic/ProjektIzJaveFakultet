package com.example.projektnizadatak.Controllers.ZivotinjeController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Controllers.StanistaController.DetaljiStanista;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @FXML
    private Button dodajButton;
    @FXML
    private Button urediButton;
    @FXML
    private Button obrisiButton;
    @FXML
    private HBox hBox;

    private boolean popravljenLayout = false;

    public void initialize(){
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
        if(!vrsta.isEmpty()){
            filtriraneZivotinje = zivotinje.stream().filter(z -> z.getSistematika().vrsta().contains(vrsta)).toList();
            zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
        }

        if (!razred.isEmpty()) {
            if(!vrsta.isEmpty()){
                filtriraneZivotinje = filtriraneZivotinje.stream().filter(z -> z.getSistematika().razred().contains(razred)).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            }else {
                filtriraneZivotinje = zivotinje.stream().filter(z -> z.getSistematika().razred().contains(razred)).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            }
        }

        if (!starost.isEmpty()) {
            if(!vrsta.isEmpty() || !razred.isEmpty()){
                filtriraneZivotinje = filtriraneZivotinje.stream().filter(z -> z.getStarost().equals(Integer.parseInt(starost))).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            } else {
                filtriraneZivotinje = zivotinje.stream().filter(z -> z.getStarost().equals(Integer.parseInt(starost))).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            }

        }

        if (!spol.isEmpty()) {
            if(!vrsta.isEmpty() || !razred.isEmpty() || !starost.isEmpty()){
                filtriraneZivotinje = filtriraneZivotinje.stream().filter(z -> z.getSpol().contains(spol)).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            } else {
                filtriraneZivotinje = zivotinje.stream().filter(z -> z.getSpol().contains(spol)).toList();
                zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
            }
        }
    }

    public void dodajZivotinju() throws IOException {
        IzbornikController.promjeniEkran(
                "zivotinje/unosZivotinja.fxml",
                "Dodaj životinju");
    }

    public void azurirajZivotinju(){
        Zivotinja zivotinja = zivotinjaTableView.getSelectionModel().getSelectedItem();
        if (zivotinja != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/zivotinje/azurirajZivotinju.fxml"));
                Parent root = loader.load();

                AzurirajZivotinjuController azurirajZivotinju = loader.getController();
                azurirajZivotinju.dohvatiZivotinju(zivotinja);

                Stage stage = MainApplication.getMainStage();
                stage.setScene(new Scene(root));
                stage.setTitle("Promijeni životinju");
                stage.show();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Potreban odabir");
            alert.setContentText("Trebate odabrati jednu životinju iz tablice.");

            alert.showAndWait();
        }
    }

    public void obrisiZivotinju(){
        Zivotinja zivotinja = zivotinjaTableView.getSelectionModel().getSelectedItem();
        if (zivotinja != null){
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Potvrda brisanja");
                alert.setContentText("Jeste li sigurni da želite obrisati odabranu životinju?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    BazaPodataka.obrisiZivotinju(zivotinja);
                    AzurirajZivotinjuController.spremiPromjenu(zivotinja.getClass().getSimpleName(), "-", "admin", LocalDateTime.now());
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Brisanje životinje");
                    alert2.setHeaderText("Uspješno brisanje!");
                    alert2.setContentText("Životinja razreda " + zivotinja.getSistematika().razred() + " je uspješno obrisana!");

                    alert2.showAndWait();
                }
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Potreban odabir");
            alert.setContentText("Trebate odabrati jednu životinju iz tablice.");

            alert.showAndWait();
        }

        initialize();
    }
}
