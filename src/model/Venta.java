package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private String codigoVenta;
    private LocalDateTime fechaHora;
    private Cliente cliente;
    private List<Boleto> boletos;
    private double total;

    public Venta(String codigoVenta, Cliente cliente) {
        this.codigoVenta = codigoVenta;
        this.cliente = cliente;
        this.fechaHora = LocalDateTime.now();
        this.boletos = new ArrayList<>();
        this.total = 0.0;
    }

    public void agregarBoleto(Boleto boleto) {
        boletos.add(boleto);
    }

    public double calcularTotal() {
        double subtotal = 0.0;
        for (Boleto b : boletos) {
            subtotal += b.getPrecioFinal();
        }
        double descuento = cliente.calcularDescuento(subtotal);
        this.total = subtotal - descuento;

        if (cliente instanceof ClienteFrecuente) {
            ((ClienteFrecuente) cliente).acumularPuntos(this.total);
        }

        return this.total;
    }

    public String generarComprobante() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("============================================================\n");
        sb.append("                  CINEPASS - FACTURA DE VENTA               \n");
        sb.append("============================================================\n");
        sb.append(String.format("Código Venta : %s%n", codigoVenta));
        sb.append(String.format("Fecha y Hora : %s%n", fechaHora.format(dtf)));
        sb.append(String.format("Cliente      : %s (ID: %s)%n", cliente.getNombre(), cliente.getId()));
        sb.append(String.format("Tipo Cliente : %s%n", cliente.getClass().getSimpleName()));
        sb.append("------------------------------------------------------------\n");
        sb.append("DETALLE DE ENTRADAS:\n");

        double subtotal = 0.0;
        for (Boleto b : boletos) {
            sb.append("  * ").append(b.imprimirDetalle()).append("\n");
            subtotal += b.getPrecioFinal();
        }

        double descuento = cliente.calcularDescuento(subtotal);

        sb.append("------------------------------------------------------------\n");
        sb.append(String.format("Subtotal     : $%,10.2f%n", subtotal));
        sb.append(String.format("Descuento    : -$%,9.2f%n", descuento));
        sb.append(String.format("TOTAL A PAGAR: $%,10.2f%n", total));

        if (cliente instanceof ClienteFrecuente) {
            ClienteFrecuente cf = (ClienteFrecuente) cliente;
            sb.append(String.format("Puntos Totales Acumulados: %d pts%n", cf.getPuntosAcumulados()));
        }
        sb.append("============================================================\n");
        sb.append("       ¡Gracias por su compra! Disfrute la función.        \n");
        sb.append("============================================================\n");
        return sb.toString();
    }

    public String getCodigoVenta() { return codigoVenta; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public Cliente getCliente() { return cliente; }
    public List<Boleto> getBoletos() { return boletos; }
    public double getTotal() { return total; }
}