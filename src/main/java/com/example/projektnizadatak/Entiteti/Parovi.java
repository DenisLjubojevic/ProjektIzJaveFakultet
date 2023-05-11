package com.example.projektnizadatak.Entiteti;

public class Parovi<S, T> {
    private S parametar1;
    private T parametar2;

    public Parovi(S parametar1, T parametar2) {
        this.parametar1 = parametar1;
        this.parametar2 = parametar2;
    }

    public S getParametar1() {
        return parametar1;
    }

    public T getParametar2() {
        return parametar2;
    }

    public void ispisParova(){
        System.out.println(parametar1.getClass().getSimpleName() + " se brine o" + parametar2.getClass().getSimpleName());
    }
}
