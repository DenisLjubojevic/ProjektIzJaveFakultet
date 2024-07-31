package com.example.projektnizadatak.Controllers.ZivotinjeController;

import com.example.projektnizadatak.Controllers.LoginController.loginScreenController;
import com.example.projektnizadatak.Controllers.MenuController.IzbornikController;
import com.example.projektnizadatak.Entiteti.Promjene;
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
import java.sql.Timestamp;
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
    @FXML
    private Button stanjeButton;

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

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(razredLabel);
        MainApplication.setupText(vrstaLabel);
        MainApplication.setupText(starostLabel);

        MainApplication.setupRadioButton(zenskoRadioButton);
        MainApplication.setupRadioButton(muskoRadioButton);

        MainApplication.setupButton(pretraziButton);
        MainApplication.setupButton(dodajButton);
        MainApplication.setupButton(urediButton);
        MainApplication.setupButton(obrisiButton);
        MainApplication.setupButton(stanjeButton);
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

        List<Zivotinja> filtriraneZivotinje = zivotinje;

        if(!vrsta.isEmpty()){
            filtriraneZivotinje = filtriraneZivotinje.stream()
                    .filter(z -> z.getSistematika().vrsta().contains(vrsta))
                    .toList();
        }

        if (!razred.isEmpty()) {
            filtriraneZivotinje = filtriraneZivotinje.stream()
                    .filter(z -> z.getSistematika().razred().contains(razred))
                    .toList();
        }

        if (!starost.isEmpty()) {
            filtriraneZivotinje = filtriraneZivotinje.stream()
                    .filter(z -> z.getStarost().equals(Integer.parseInt(starost)))
                    .toList();
        }

        if (!spol.isEmpty()) {
            filtriraneZivotinje = filtriraneZivotinje.stream()
                    .filter(z -> z.getSpol().contains(spol))
                    .toList();
        }

        zivotinjaTableView.setItems(FXCollections.observableList(filtriraneZivotinje));
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
                    Promjene promjena = new Promjene(
                            1,
                            loginScreenController.prijavljeniKorisnik.getId(),
                            "Obrisana životinja",
                            new Timestamp(System.currentTimeMillis()));
                    try{
                        BazaPodataka.spremiPromjenu(promjena);
                    }catch (BazaPodatakaException ex){

                    }

                    BazaPodataka.obrisiZivotinju(zivotinja);
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

    public void pregledajZdravstveneKartone(){
        Zivotinja zivotinja = zivotinjaTableView.getSelectionModel().getSelectedItem();
        if (zivotinja != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projektnizadatak/zivotinje/pregledZdravstvenihKartona.fxml"));
                Parent root = loader.load();

                PregledZdravstvenihKartonaController pregledKartonaController = loader.getController();
                pregledKartonaController.setOdabranaZivotinja(zivotinja);

                Stage stage = MainApplication.getMainStage();
                stage.setScene(new Scene(root));
                stage.setTitle("Pregled zdravstvenih kartona");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jednu životinju iz tablice."
            );
        }
    }
}
