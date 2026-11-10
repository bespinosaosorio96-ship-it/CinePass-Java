package model;

public class ClienteGeneral extends Cliente {
    public ClienteGeneral(String id, String nombre, String email) {
        super(id, nombre, email);
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return 0.0;
    }

    @Override
    public String toString() {
        return nombre + " (ID: " + id + ", Cliente General)";
    }
}