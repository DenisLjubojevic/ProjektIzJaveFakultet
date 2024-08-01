package com.example.projektnizadatak.Entiteti.Zaposlenici;

import com.example.projektnizadatak.Iznimke.BazaPodatakaException;
import com.example.projektnizadatak.MainApplication;
import com.example.projektnizadatak.Util.BazaPodataka;
import javafx.scene.control.Alert;

import java.util.Map;

public class RasporedRada {
    private Integer id;
    private Integer zaposlenikId;
    private Map<String, Smjena> smjenaPoDanima;

    public RasporedRada(Integer id, Integer zaposlenikId, Map<String, Smjena> smjenaPoDanima) {
        this.id = id;
        this.zaposlenikId = zaposlenikId;
        this.smjenaPoDanima = smjenaPoDanima;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getZaposlenikId() {
        return zaposlenikId;
    }

    public void setZaposlenikId(Integer zaposlenikId) {
        this.zaposlenikId = zaposlenikId;
    }

    public Zaposlenici getZaposlenik(){
        try{
            return BazaPodataka.dohvatiZaposlenika(this.zaposlenikId).get();
        }catch (BazaPodatakaException ex){
            MainApplication.showAlertDialog(
                    Alert.AlertType.ERROR,
                    "Učitavanje zaposlenika!",
                    "Pogreška učitavanja!",
                    ex.getMessage()
            );
        }
        return null;
    }

    public Map<String, Smjena> getSmjenaPoDanima() {
        return smjenaPoDanima;
    }

    public void dodajSmjenu(String dan, Smjena smjena) {
        this.smjenaPoDanima.put(dan, smjena);
    }
}
