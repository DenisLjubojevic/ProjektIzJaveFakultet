package com.example.projektnizadatak.Util;

import com.example.projektnizadatak.Entiteti.*;

import java.io.*;
import java.util.*;

public class Datoteke {

    public static  void serijalizirajPromjene(Promjene promjena){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Datoteke/promjene.dat"));
            out.writeObject(promjena);
            out.close();
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public static Optional<Promjene> deserijalizirajPromjene() {
        Promjene promjene;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("Datoteke/promjene.dat"));
            promjene = (Promjene) in.readObject();

            return Optional.of(promjene);
        } catch (IOException ex) {
            System.err.println(ex);
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
        }

        return Optional.empty();
    }
}
