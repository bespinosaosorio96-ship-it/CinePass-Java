package controller;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CineController {
    private List<Pelicula> peliculas;
    private List<Sala> salas;
    private List<Funcion> funciones;
    private List<Venta> ventas;

    public CineController() {
        this.peliculas = new ArrayList<>();
        this.salas = new ArrayList<>();
        this.funciones = new ArrayList<>();
        this.ventas = new ArrayList<>();
    }

    public void registrarPelicula(Pelicula p) {
        peliculas.add(p);
    }

    public void registrarSala(Sala s) {
        salas.add(s);
    }

    public void programarFuncion(Funcion f) {
        funciones.add(f);
    }

    public List<Funcion> buscarFuncionesDisponibles() {
        return new ArrayList<>(funciones);
    }

    public Venta realizarVenta(Cliente cliente, Funcion funcion, List<Asiento> asientos) throws IllegalStateException {
        if (asientos == null || asientos.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un asiento.");
        }

        for (Asiento a : asientos) {
            if (!funcion.estaDisponible(a)) {
                throw new IllegalStateException("El asiento " + a.getCodigo() + " ya no se encuentra disponible.");
            }
        }

        String codigoVenta = "V-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Venta venta = new Venta(codigoVenta, cliente);
        double precioBoleto = funcion.getPrecioBase() * funcion.getSala().getTipoSala().getMultiplicadorPrecio();

        for (Asiento a : asientos) {
            funcion.ocuparAsiento(a);
            String ticketId = "T-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            Boleto boleto = new Boleto(ticketId, funcion, a, precioBoleto);
            venta.agregarBoleto(boleto);
        }

        venta.calcularTotal();
        ventas.add(venta);

        return venta;
    }

    public List<Pelicula> getPeliculas() { return peliculas; }
    public List<Sala> getSalas() { return salas; }
    public List<Funcion> getFunciones() { return funciones; }
    public List<Venta> getVentas() { return ventas; }
}