package com.example.projektnizadatak.Entiteti.Zaposlenici;

public class Zaposlenici extends Posao {
    private String ime;
    private String prezime;
    private Integer placa;

    public Zaposlenici(Integer id,String zanimanje, Integer cijenaPoSatu, Integer mjesecnaSatnica, String ime, String prezime) {
        super(id, zanimanje, cijenaPoSatu, mjesecnaSatnica);
        this.ime = ime;
        this.prezime = prezime;
        this.placa = getCijenaPoSatu() * getMjesecnaSatnica();
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Integer getPlaca() {
        return placa;
    }

    public void setPlaca(Integer placa) {
        this.placa = placa;
    }

    @Override
    void izracunajPlacu() {
        this.placa = getCijenaPoSatu() * getMjesecnaSatnica();
    }
}
