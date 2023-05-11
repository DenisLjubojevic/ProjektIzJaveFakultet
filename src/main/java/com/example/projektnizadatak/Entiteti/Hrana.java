package com.example.projektnizadatak.Entiteti;

import java.io.Serializable;

public class Hrana implements Serializable {
    private String naziv;
    private Integer kolicina;

    public Hrana(String naziv, Integer kolicina) {
        this.naziv = naziv;
        this.kolicina = kolicina;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }
}
