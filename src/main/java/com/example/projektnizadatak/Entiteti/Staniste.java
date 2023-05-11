package com.example.projektnizadatak.Entiteti;

import java.util.List;

public class Staniste extends Entitet implements Hranjenje{
    private List<Zivotinja> zivotinja;

    private Sistematika sistematika;

    private Integer brojJedinki;
    private Boolean imaHrane;
    private Boolean imaVode;

    public Staniste(Integer id, List<Zivotinja> zivotinja, Integer brojJedniki, Boolean imaHrane, Boolean imaVode) {
        super(id);
        this.zivotinja = zivotinja;
        this.brojJedinki = brojJedniki;
        this.imaHrane = imaHrane;
        this.imaVode = imaVode;
        this.sistematika = new Sistematika(zivotinja.get(0).getSistematika().vrsta(), zivotinja.get(0).getSistematika().razred());
    }

    public List<Zivotinja> getZivotinja() {
        return zivotinja;
    }

    public void setZivotinja(List<Zivotinja> zivotinja) {
        this.zivotinja = zivotinja;
    }

    public Sistematika getSistematika() {
        return sistematika;
    }

    public void setSistematika(Sistematika sistematika) {
        this.sistematika = sistematika;
    }

    public Integer getBrojJedinki() {
        return brojJedinki;
    }

    public void setBrojJedinki(Integer brojJedinki) {
        this.brojJedinki = brojJedinki;
    }

    public Boolean getImaHrane() {
        return imaHrane;
    }

    public Boolean getImaVode() {
        return imaVode;
    }

    public void simulacijaJedenja() { imaHrane = false; }

    public void simulacijaPijenja() { imaVode = false; }

    @Override
    public void opskrbiHranom() {
        imaHrane = true;
    }

    @Override
    public void opskrbiVodom() {
        imaVode = true;
    }
}
