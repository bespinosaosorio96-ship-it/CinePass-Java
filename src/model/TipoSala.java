package model;

public enum TipoSala {
    TRADICIONAL_2D(1.0),
    TRES_D(1.25),
    IMAX(1.5);

    private final double multiplicadorPrecio;

    TipoSala(double multiplicadorPrecio) {
        this.multiplicadorPrecio = multiplicadorPrecio;
    }

    public double getMultiplicadorPrecio() {
        return multiplicadorPrecio;
    }
}