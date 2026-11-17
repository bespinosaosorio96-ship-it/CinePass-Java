package controller;

import model.*;
import java.time.LocalDateTime;
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

    public void cargarDatosIniciales() {
        if (!peliculas.isEmpty()) return;

        Pelicula p1 = new Pelicula("PEL01", "Dune: Parte Dos", 166, "Ciencia Ficción", 12);
        Pelicula p2 = new Pelicula("PEL02", "Spider-Man: Across the Spider-Verse", 140, "Animación", 7);
        Pelicula p3 = new Pelicula("PEL03", "Oppenheimer", 180, "Drama / Historia", 15);

        registrarPelicula(p1);
        registrarPelicula(p2);
        registrarPelicula(p3);

        Sala sala1 = new Sala(1, 4, 6, TipoSala.IMAX);
        Sala sala2 = new Sala(2, 5, 5, TipoSala.TRES_D);
        Sala sala3 = new Sala(3, 4, 5, TipoSala.TRADICIONAL_2D);

        registrarSala(sala1);
        registrarSala(sala2);
        registrarSala(sala3);

        Funcion f1 = new Funcion("FNC-101", p1, sala1, LocalDateTime.of(2026, 11, 10, 18, 30), 16000.0);
        Funcion f2 = new Funcion("FNC-102", p2, sala2, LocalDateTime.of(2026, 11, 10, 19, 0), 14000.0);
        Funcion f3 = new Funcion("FNC-103", p3, sala3, LocalDateTime.of(2026, 11, 10, 20, 15), 11000.0);

        f1.ocuparAsiento(new Asiento('B', 3));
        f1.ocuparAsiento(new Asiento('B', 4));

        programarFuncion(f1);
        programarFuncion(f2);
        programarFuncion(f3);
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

    public int getTotalBoletosVendidos() {
        int total = 0;
        for (Venta v : ventas) {
            total += v.getBoletos().size();
        }
        return total;
    }

    public double calcularTotalDescuentos() {
        double desc = 0.0;
        for (Venta v : ventas) {
            double subtotal = 0.0;
            for (Boleto b : v.getBoletos()) {
                subtotal += b.getPrecioFinal();
            }
            desc += v.getCliente().calcularDescuento(subtotal);
        }
        return desc;
    }

    public double calcularTotalRecaudado() {
        double rec = 0.0;
        for (Venta v : ventas) {
            rec += v.getTotal();
        }
        return rec;
    }

    public List<Pelicula> getPeliculas() { return peliculas; }
    public List<Sala> getSalas() { return salas; }
    public List<Funcion> getFunciones() { return funciones; }
    public List<Venta> getVentas() { return ventas; }
}
