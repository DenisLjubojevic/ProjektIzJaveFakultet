package com.example.projektnizadatak.Controllers.ZivotinjeController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    private Label razredLabel;
    @FXML
    private Label vrstaLabel;
    @FXML
    private Label starostLabel;

    private boolean popravljenLayout = false;

    private BooleanProperty muskoSelected = new SimpleBooleanProperty(false);
    private BooleanProperty zenskoSelected = new SimpleBooleanProperty(false);

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
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje životinja!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        muskoRadioButton.setToggleGroup(spolToggleGroup);
        zenskoRadioButton.setToggleGroup(spolToggleGroup);

        muskoRadioButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> handleRadioButtonAction(muskoRadioButton, muskoSelected));
        zenskoRadioButton.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> handleRadioButtonAction(zenskoRadioButton, zenskoSelected));

        vrstaZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSistematika().vrsta()));
        razredZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSistematika().razred()));
        starostZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStarost().toString()));
        spolZivotinjeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpol()));

        zivotinjaTableView.setItems(FXCollections.observableList(zivotinje));

        naslovLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(16).asString(), "px"));
        razredLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(42).asString(), "px"));
        vrstaLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(42).asString(), "px"));
        starostLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(42).asString(), "px"));

        muskoRadioButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(46).asString(), "px"));
        zenskoRadioButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(46).asString(), "px"));


        pretraziButton.styleProperty().bind(
                Bindings.concat("-fx-pref-width: ", MainApplication.getMainStage().widthProperty().divide(8.5).asString(), "px; ",
                        "-fx-pref-height: ", MainApplication.getMainStage().heightProperty().divide(13).asString(), "px; ",
                        "-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(50).asString(), "px"));
        dodajButton.styleProperty().bind(
                Bindings.concat("-fx-pref-width: ", MainApplication.getMainStage().widthProperty().divide(8.5).asString(), "px; ",
                        "-fx-pref-height: ", MainApplication.getMainStage().heightProperty().divide(13).asString(), "px; ",
                        "-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(50).asString(), "px"));
        urediButton.styleProperty().bind(
                Bindings.concat("-fx-pref-width: ", MainApplication.getMainStage().widthProperty().divide(8.5).asString(), "px; ",
                        "-fx-pref-height: ", MainApplication.getMainStage().heightProperty().divide(13).asString(), "px; ",
                        "-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(50).asString(), "px"));
        obrisiButton.styleProperty().bind(
                Bindings.concat("-fx-pref-width: ", MainApplication.getMainStage().widthProperty().divide(8.5).asString(), "px; ",
                        "-fx-pref-height: ", MainApplication.getMainStage().heightProperty().divide(13).asString(), "px; ",
                        "-fx-font-size: ", MainApplication.getMainStage().widthProperty().divide(50).asString(), "px"));
    }
    
    private void handleRadioButtonAction(RadioButton radioButton, BooleanProperty odabraniSpol){
        if (odabraniSpol.get()){
            spolToggleGroup.selectToggle(null);
            zenskoSelected.set(false);
            muskoSelected.set(false);
        }else{
            odabraniSpol.set(true);
        }
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
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jednu životinju iz tablice."
            );
        }
    }

    public void obrisiZivotinju(){
        Zivotinja zivotinja = zivotinjaTableView.getSelectionModel().getSelectedItem();
        if (zivotinja != null){
            try {
                Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                        Alert.AlertType.CONFIRMATION,
                        "Potvrda",
                        "Potvrda brisanja",
                        "Jeste li sigurni da želite obrisati odabranu životinju?"
                );
                if(result.get() == ButtonType.OK){
                    BazaPodataka.obrisiZivotinju(zivotinja);
                    AzurirajZivotinjuController.spremiPromjenu(zivotinja.getClass().getSimpleName(), "-", "admin", LocalDateTime.now());
                    MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Brisanje životinje",
                            "Uspješno brisanje!",
                            "Životinja razreda " + zivotinja.getSistematika().razred() + " je uspješno obrisana!"
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
                    "Trebate odabrati jednu životinju iz tablice."
            );
        }

        initialize();
    }
}
