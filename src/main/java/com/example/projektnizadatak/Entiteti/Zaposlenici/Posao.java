package com.example.projektnizadatak.Entiteti.Zaposlenici;

import com.example.projektnizadatak.Entiteti.Entitet;

public abstract class Posao extends Entitet {
    private String zanimanje;
    private Integer cijenaPoSatu;
    private Integer mjesecnaSatnica;

    public Posao(Integer id, String zanimanje, Integer cijenaPoSatu, Integer mjesecnaSatnica) {
        super(id);
        this.zanimanje = zanimanje;
        this.cijenaPoSatu = cijenaPoSatu;
        this.mjesecnaSatnica = mjesecnaSatnica;
    }

    public String getZanimanje() {
        return zanimanje;
    }

    public void setZanimanje(String zanimanje) {
        this.zanimanje = zanimanje;
    }

    public Integer getCijenaPoSatu() {
        return cijenaPoSatu;
    }

    public void setCijenaPoSatu(Integer cijenaPoSatu) {
        this.cijenaPoSatu = cijenaPoSatu;
    }

    public Integer getMjesecnaSatnica() {
        return mjesecnaSatnica;
    }

    public void setMjesecnaSatnica(Integer mjesecnaSatnica) {
        this.mjesecnaSatnica = mjesecnaSatnica;
    }

    abstract void izracunajPlacu();
}
