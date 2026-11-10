package model;

public class ClienteFrecuente extends Cliente {
    private double porcentajeDescuento;
    private int puntosAcumulados;

    public ClienteFrecuente(String id, String nombre, String email, double porcentajeDescuento) {
        super(id, nombre, email);
        this.porcentajeDescuento = porcentajeDescuento;
        this.puntosAcumulados = 0;
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * (porcentajeDescuento / 100.0);
    }

    public void acumularPuntos(double monto) {
        this.puntosAcumulados += (int) (monto / 1000.0);
    }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    public int getPuntosAcumulados() { return puntosAcumulados; }
    public void setPuntosAcumulados(int puntosAcumulados) { this.puntosAcumulados = puntosAcumulados; }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ", Cliente Frecuente, Dcto: " + porcentajeDescuento + "%, Puntos: " + puntosAcumulados + ")";
    }
}