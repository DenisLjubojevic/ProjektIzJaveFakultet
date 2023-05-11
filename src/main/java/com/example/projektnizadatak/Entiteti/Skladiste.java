package com.example.projektnizadatak.Entiteti;

import java.io.Serializable;
import java.util.Set;

public class Skladiste implements Serializable {
    private Set<Hrana> hranaSet;
    private Integer velicinaSkladista;

    public Skladiste(Set<Hrana> hranaSet, Integer velicinaSkladista) {
        this.hranaSet = hranaSet;
        this.velicinaSkladista = velicinaSkladista;
    }

    public Set<Hrana> getHranaSet() {
        return hranaSet;
    }

    public void setHranaSet(Set<Hrana> hranaSet) {
        this.hranaSet = hranaSet;
    }

    public Integer getVelicinaSkladista() {
        return velicinaSkladista;
    }

    public void setVelicinaSkladista(Integer velicinaSkladista) {
        this.velicinaSkladista = velicinaSkladista;
    }

    public Integer slobodanProstor(){
        return (velicinaSkladista - hranaSet.size());
    }
}
