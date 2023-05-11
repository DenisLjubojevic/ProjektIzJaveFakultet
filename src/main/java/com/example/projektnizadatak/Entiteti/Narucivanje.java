package com.example.projektnizadatak.Entiteti;

import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.util.List;

public class Narucivanje<T> {
    private final List<T> objekt;
    private final Integer cijena;
    private final LocalDate datumNarudzbe;

    public Narucivanje(List<T> objekt, Integer cijena, LocalDate datum) {
        this.objekt = objekt;
        this.cijena = cijena;
        this.datumNarudzbe = datum;
    }

    public List<T> dohvatiNaruceno() {
        return objekt;
    }

    public void dodajZaNaruciti(T trazeno){
        objekt.add(trazeno);
    }

    public Integer getCijena() {
        return cijena;
    }

    public LocalDate getDatumNarudzbe() {
        return datumNarudzbe;
    }
}
