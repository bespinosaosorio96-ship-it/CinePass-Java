package model;

import java.util.Objects;

public class Asiento{
    private char fila;
    private int numero;


    public Asiento(char fila, int numero) {
        this.fila = fila;
        this.numero = numero;
    }

    public String getCodigo() {
        return "" + fila + numero;

    }

    public char getFila() {
        return fila;
    }
    public void setFila(char fila) {
        this.fila = fila;
    }
    public int getNumero() {
        return numero;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override 
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asiento asiento = (Asiento) o;
        return fila == asiento.fila && numero == asiento.numero;
    }

    @Override 
    public int  hashCode() {
        return Objects.hash(fila, numero);
    }

    @Override 
    public String toString() {
        return getCodigo();
    }


}
