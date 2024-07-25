package com.example.projektnizadatak.Entiteti.Zivotinje;

import com.example.projektnizadatak.Entiteti.Zivotinje.Zivotinja;

public sealed interface Spol permits Zivotinja {
    String getSpol();
}