package com.example.projektnizadatak.Entiteti.Zivotinje;

import com.example.projektnizadatak.Entiteti.Entitet;

import java.time.LocalDate;

public class ZdravstveniKarton extends Entitet {
    private LocalDate datumPregleda;
    private String dijagnoza;
    private String terapija;
    private String napomena;

    public ZdravstveniKarton(Integer id,LocalDate datumPregleda, String dijagnoza, String terapija, String napomena) {
        super(id);
        this.datumPregleda = datumPregleda;
        this.dijagnoza = dijagnoza;
        this.terapija = terapija;
        this.napomena = napomena;
    }

    public LocalDate getDatumPregleda() {
        return datumPregleda;
    }

    public void setDatumPregleda(LocalDate datumPregleda) {
        this.datumPregleda = datumPregleda;
    }

    public String getDijagnoza() {
        return dijagnoza;
    }

    public void setDijagnoza(String dijagnoza) {
        this.dijagnoza = dijagnoza;
    }

    public String getTerapija() {
        return terapija;
    }

    public void setTerapija(String terapija) {
        this.terapija = terapija;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
