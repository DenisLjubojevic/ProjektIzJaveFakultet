package com.example.projektnizadatak.Entiteti;

import com.example.projektnizadatak.Entiteti.Korisnici.Korisnik;
import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.scene.control.Alert;

import java.io.Serializable;
import java.sql.Timestamp;

public class Promjene extends Entitet implements Serializable {
    private Integer userId;
    private String opisPromjene;
    private Timestamp vrijemePromjene;

    public Promjene(Integer id, Integer userId, String opisPromjene, Timestamp vrijemePromjene) {
        super(id);
        this.userId = userId;
        this.opisPromjene = opisPromjene;
        this.vrijemePromjene = vrijemePromjene;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOpisPromjene() {
        return opisPromjene;
    }

    public void setOpisPromjene(String opisPromjene) {
        this.opisPromjene = opisPromjene;
    }

    public Timestamp getVrijemePromjene() {
        return vrijemePromjene;
    }

    public void setVrijemePromjene(Timestamp vrijemePromjene) {
        this.vrijemePromjene = vrijemePromjene;
    }

    public String getUsernameById(){
        try{
            Korisnik korisnik = BazaPodataka.dohvatiKorisnikById(userId);
            if (korisnik != null){
                return korisnik.getKorisnickoIme();
            }
        }catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje korisnika!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }
        return null;
    }
}
