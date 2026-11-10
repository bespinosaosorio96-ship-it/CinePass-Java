package model;

public class Sala {
    private int numero;
    private int filas;
    private int columnas;
    private TipoSala tipoSala;

    public Sala(int numero, int filas, int columnas, TipoSala tipoSala) {
        this.numero = numero;
        this.filas = filas;
        this.columnas = columnas;
        this.tipoSala = tipoSala;
    }

    public int getCapacidadTotal() {
        return filas * columnas;
    }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getFilas() { return filas; }
    public void setFilas(int filas) { this.filas = filas; }

    public int getColumnas() { return columnas; }
    public void setColumnas(int columnas) { this.columnas = columnas; }

    public TipoSala getTipoSala() { return tipoSala; }
    public void setTipoSala(TipoSala tipoSala) { this.tipoSala = tipoSala; }

    @Override
    public String toString() {
        return "Sala " + numero + " [" + tipoSala + "] (Capacidad: " + getCapacidadTotal() + ")";
    }
}