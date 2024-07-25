package com.example.projektnizadatak.Entiteti.Zivotinje;

import com.example.projektnizadatak.Entiteti.Entitet;

public final class Zivotinja extends Entitet implements Spol {
    private Sistematika sistematika;
    private Integer starost;
    private String spol;

    public Zivotinja(Integer id,Sistematika sistematika, Integer starost, String spol) {
        super(id);
        this.sistematika = sistematika;
        this.starost = starost;
        this.spol = spol;
    }

    public Sistematika getSistematika() {
        return sistematika;
    }

    public void setSistematika(Sistematika sistematika) {
        this.sistematika = sistematika;
    }

    public Integer getStarost() {
        return starost;
    }

    public void setStarost(Integer starost) {
        this.starost = starost;
    }

    public String getSpol() {
        return spol;
    }

    public void setSpol(String spol) {
        this.spol = spol;
    }
}
