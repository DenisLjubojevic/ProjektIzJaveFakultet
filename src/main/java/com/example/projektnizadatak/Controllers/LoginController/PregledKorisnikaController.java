package com.example.projektnizadatak.Controllers.LoginController;

import com.example.projektnizadatak.Entiteti.Korisnici.Korisnik;
import com.example.projektnizadatak.Entiteti.Korisnici.Role;
import com.example.projektnizadatak.Entiteti.Promjene;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PregledKorisnikaController {
    @FXML
    private Label naslovLabel;
    @FXML
    private Label azurirajLabel;
    @FXML
    private ChoiceBox<Role> rolaChoiceBox;
    @FXML
    private Button promjeniButton;
    @FXML
    private Button obrisiButton;

    @FXML
    private TableView<Korisnik> korisniciTableView;
    @FXML
    private TableColumn<Korisnik, String> usernameTableColumn;
    @FXML
    private TableColumn<Korisnik, String> passwordTableColumn;
    @FXML
    private TableColumn<Korisnik, String> roleTableColumn;

    List<Korisnik> korisnici = new ArrayList<>();
    private boolean popravljenLayout = false;
    public void initialize(){
        if (!popravljenLayout){
            MainApplication.popraviLayout();
            popravljenLayout = true;
        }

        for (Role r: Role.values()){
            rolaChoiceBox.getItems().add(r);
        }
        rolaChoiceBox.getSelectionModel().selectFirst();

        try{
            korisnici =  BazaPodataka.dohvatiKorisnike();
        }catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje korisnika!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }

        usernameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKorisnickoIme()));
        passwordTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLozinka()));
        roleTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().getRolaKorisnika()));

        korisniciTableView.setItems(FXCollections.observableList(korisnici));

        MainApplication.setupNaslov(naslovLabel);
        MainApplication.setupText(azurirajLabel);
        MainApplication.setupButton(promjeniButton);
        MainApplication.setupButton(obrisiButton);
    }

    public void promjeniRolu(){
        Korisnik korisnik = korisniciTableView.getSelectionModel().getSelectedItem();

        if (korisnik != null){
            Role novaRola = rolaChoiceBox.getSelectionModel().getSelectedItem();

            Korisnik promjenjenKorisnik = new Korisnik(korisnik.getId(), korisnik.getKorisnickoIme(), korisnik.getLozinka(), novaRola);
            try{
                Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                        Alert.AlertType.CONFIRMATION,
                        "Potvrda",
                        "Potvrda promjene",
                        "Jeste li sigurni da želite korisniku\n" +
                                korisnik.getKorisnickoIme() + " promijeniti rolu?"
                );

                if(result.get() == ButtonType.OK){
                    Promjene promjena = new Promjene(
                            1,
                            loginScreenController.prijavljeniKorisnik.getId(),
                            "Promjena role",
                            new Timestamp(System.currentTimeMillis()));
                    try{
                        BazaPodataka.spremiPromjenu(promjena);
                    }catch (BazaPodatakaException ex){

                    }

                    BazaPodataka.azurirajKorisnika(promjenjenKorisnik);
                    MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Promjena role",
                            "Uspješna promjena!",
                            "Korisnik " + korisnik.getKorisnickoIme() + "\n" +
                                    "ima rolu: " + promjenjenKorisnik.getRole().getRolaKorisnika()
                    );
                    initialize();
                }

            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jednog korisnika iz tablice."
            );
        }
    }

    public void obrisiKorisnika(){
        Korisnik korisnik = korisniciTableView.getSelectionModel().getSelectedItem();
        if (korisnik != null){
            try{
                Optional<ButtonType> result = MainApplication.showAlertDialogConfirmation(
                        Alert.AlertType.CONFIRMATION,
                        "Potvrda",
                        "Potvrda promjene",
                        "Jeste li sigurni da želite\n" +
                                "obrisati korisnika " +  korisnik.getKorisnickoIme() + "?"
                );

                if(result.get() == ButtonType.OK) {
                    Promjene promjena = new Promjene(
                            1,
                            loginScreenController.prijavljeniKorisnik.getId(),
                            "Promjena role",
                            new Timestamp(System.currentTimeMillis()));
                    try {
                        BazaPodataka.spremiPromjenu(promjena);
                    } catch (BazaPodatakaException ex) {

                    }

                    BazaPodataka.obrisiKorisnika(korisnik);
                    MainApplication.showAlertDialog(
                            Alert.AlertType.INFORMATION,
                            "Brisanje korisnika",
                            "Uspješno brisanje!",
                            "Korisnik " + korisnik.getKorisnickoIme() + " je uspješno obrisan!"
                    );

                    initialize();
                }
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }
        }else{
            MainApplication.showAlertDialog(
                    Alert.AlertType.INFORMATION,
                    "Pogreška",
                    "Potreban odabir",
                    "Trebate odabrati jednog korisnika iz tablice."
            );
        }

    }
}
