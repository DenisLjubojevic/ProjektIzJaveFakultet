package com.example.projektnizadatak.Entiteti;

public class Aktivnost{
    private Integer id;
    private String naziv;
    private Integer cijena;
    private Integer trajanje;

    public String getNaziv() {
        return naziv;
    }
    public Integer getCijena() {
        return cijena;
    }

    public Integer getTrajanje() {
        return trajanje;
    }

    public Integer getId() { return id; }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setCijena(Integer cijena) {
        this.cijena = cijena;
    }

    public void setTrajanje(Integer trajanje) {
        this.trajanje = trajanje;
    }

    public static class Builder{
        private Integer id;
        private String naziv;
        private Integer cijena;
        private Integer trajanje;

        public Builder (Integer id, String naziv, Integer cijena){
            this.id = id;
            this.naziv = naziv;
            this.cijena = cijena;
        }

        public Builder saTrajanjem(Integer trajanje){
            this.trajanje = trajanje;

            return this;
        }

        public Aktivnost build(){
            Aktivnost aktivnost = new Aktivnost();
            aktivnost.id = this.id;
            aktivnost.naziv = this.naziv;
            aktivnost.cijena = this.cijena;
            aktivnost.trajanje = this.trajanje;

            return aktivnost;
        }
    }
}
