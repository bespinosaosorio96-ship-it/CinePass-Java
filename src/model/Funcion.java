package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Funcion {
    private String id;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDateTime horario;
    private double precioBase;
    private Map<String, EstadoAsiento> estadoAsientos;

    public Funcion(String id, Pelicula pelicula, Sala sala, LocalDateTime horario, double precioBase) {
        this.id = id;
        this.pelicula = pelicula;
        this.sala = sala;
        this.horario = horario;
        this.precioBase = precioBase;
        this.estadoAsientos = new HashMap<>();
        inicializarMapaAsientos();
    }

    public void inicializarMapaAsientos() {
        for (int i = 0; i < sala.getFilas(); i++) {
            char letraFila = (char) ('A' + i);
            for (int col = 1; col <= sala.getColumnas(); col++) {
                String codigo = "" + letraFila + col;
                estadoAsientos.put(codigo, EstadoAsiento.DISPONIBLE);
            }
        }
    }

    public boolean estaDisponible(Asiento asiento) {
        EstadoAsiento estado = estadoAsientos.get(asiento.getCodigo());
        return estado == EstadoAsiento.DISPONIBLE;
    }

    public boolean ocuparAsiento(Asiento asiento) {
        if (estaDisponible(asiento)) {
            estadoAsientos.put(asiento.getCodigo(), EstadoAsiento.OCUPADO);
            return true;
        }
        return false;
    }

    public void mostrarMapaButacas() {
        System.out.println("\n============== PANTALLA ==============");
        System.out.print("    ");
        for (int c = 1; c <= sala.getColumnas(); c++) {
            System.out.printf("%4d", c);
        }
        System.out.println();

        for (int f = 0; f < sala.getFilas(); f++) {
            char letra = (char) ('A' + f);
            System.out.printf(" %c |", letra);
            for (int c = 1; c <= sala.getColumnas(); c++) {
                String cod = "" + letra + c;
                EstadoAsiento estado = estadoAsientos.get(cod);
                String rep = (estado == EstadoAsiento.DISPONIBLE) ? "[O]" : "[X]";
                System.out.printf("%4s", rep);
            }
            System.out.println();
        }
        System.out.println("Leyenda: [O] Disponible   [X] Ocupado/Reservado\n");
    }

    public String getId() { return id; }
    public Pelicula getPelicula() { return pelicula; }
    public Sala getSala() { return sala; }
    public LocalDateTime getHorario() { return horario; }
    public double getPrecioBase() { return precioBase; }
    public Map<String, EstadoAsiento> getEstadoAsientos() { return estadoAsientos; }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("[%s] %s | %s | %s | Tarifa base: $%,.2f",
                id, pelicula.getTitulo(), sala.toString(), horario.format(dtf), precioBase);
    }
}