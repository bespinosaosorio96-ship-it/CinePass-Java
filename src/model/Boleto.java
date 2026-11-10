package model;

public class Boleto {
    private String id;
    private Funcion funcion;
    private Asiento asiento;
    private double precioFinal;

    public Boleto(String id, Funcion funcion, Asiento asiento, double precioFinal) {
        this.id = id;
        this.funcion = funcion;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
    }

    public String getId() { return id; }
    public Funcion getFuncion() { return funcion; }
    public Asiento getAsiento() { return asiento; }
    public double getPrecioFinal() { return precioFinal; }

    public String imprimirDetalle() {
        return String.format("Ticket #%s | %s | Sala %d (%s) | Asiento %s | Valor: $%,.2f",
                id,
                funcion.getPelicula().getTitulo(),
                funcion.getSala().getNumero(),
                funcion.getSala().getTipoSala(),
                asiento.getCodigo(),
                precioFinal);
    }

    @Override
    public String toString() {
        return imprimirDetalle();
    }
}