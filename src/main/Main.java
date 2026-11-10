package main;

import controller.CineController;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CineController controller = new CineController();

    public static void main(String[] args) {
        cargarDatosPrueba();

        boolean salir = false;
        while (!salir) {
            System.out.println("\n=================================================");
            System.out.println("         SISTEMA DE GESTION CINEPASS             ");
            System.out.println("=================================================");
            System.out.println("1. Listar Cartelera y Funciones Disponibles");
            System.out.println("2. Consultar Mapa de Asientos de una Funcion");
            System.out.println("3. Realizar Compra / Reserva de Boleteria");
            System.out.println("4. Ver Historial de Ventas Realizadas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opcion: ");

            String opcion = scanner.nextLine().trim();
            switch (opcion) {
                case "1":
                    listarFunciones();
                    break;
                case "2":
                    consultarAsientos();
                    break;
                case "3":
                    flujoVenta();
                    break;
                case "4":
                    verHistorialVentas();
                    break;
                case "5":
                    System.out.println("\nGracias por utilizar CinePass. ¡Hasta pronto!");
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    private static void cargarDatosPrueba() {
        Pelicula p1 = new Pelicula("PEL01", "Dune: Parte Dos", 166, "Ciencia Ficción", 12);
        Pelicula p2 = new Pelicula("PEL02", "Spider-Man: Across the Spider-Verse", 140, "Animación", 7);
        Pelicula p3 = new Pelicula("PEL03", "Oppenheimer", 180, "Drama / Historia", 15);

        controller.registrarPelicula(p1);
        controller.registrarPelicula(p2);
        controller.registrarPelicula(p3);

        Sala sala1 = new Sala(1, 4, 6, TipoSala.IMAX);
        Sala sala2 = new Sala(2, 5, 5, TipoSala.TRES_D);
        Sala sala3 = new Sala(3, 4, 5, TipoSala.TRADICIONAL_2D);

        controller.registrarSala(sala1);
        controller.registrarSala(sala2);
        controller.registrarSala(sala3);

        Funcion f1 = new Funcion("FNC-101", p1, sala1, LocalDateTime.of(2026, 11, 10, 18, 30), 16000.0);
        Funcion f2 = new Funcion("FNC-102", p2, sala2, LocalDateTime.of(2026, 11, 10, 19, 0), 14000.0);
        Funcion f3 = new Funcion("FNC-103", p3, sala3, LocalDateTime.of(2026, 11, 10, 20, 15), 11000.0);

        f1.ocuparAsiento(new Asiento('B', 3));
        f1.ocuparAsiento(new Asiento('B', 4));

        controller.programarFuncion(f1);
        controller.programarFuncion(f2);
        controller.programarFuncion(f3);
    }

    private static void listarFunciones() {
        System.out.println("\n------------- CARTELERA Y FUNCIONES DISPONIBLES -------------");
        List<Funcion> lista = controller.buscarFuncionesDisponibles();
        for (int i = 0; i < lista.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + lista.get(i));
        }
    }

    private static void consultarAsientos() {
        Funcion funcion = seleccionarFuncion();
        if (funcion != null) {
            System.out.println("\nMapa de butacas para: " + funcion.getPelicula().getTitulo() + " (Sala " + funcion.getSala().getNumero() + ")");
            funcion.mostrarMapaButacas();
        }
    }

    private static Funcion seleccionarFuncion() {
        listarFunciones();
        List<Funcion> lista = controller.buscarFuncionesDisponibles();
        System.out.print("\nIngrese el numero de la funcion: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < lista.size()) {
                return lista.get(idx);
            } else {
                System.out.println("Seleccion fuera de rango.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada invalida.");
        }
        return null;
    }

    private static void flujoVenta() {
        System.out.println("\n--- NUEVA COMPRA DE BOLETERIA ---");
        Funcion funcion = seleccionarFuncion();
        if (funcion == null) return;

        funcion.mostrarMapaButacas();

        System.out.print("¿Cuantos boletos desea comprar?: ");
        int cantidad;
        try {
            cantidad = Integer.parseInt(scanner.nextLine().trim());
            if (cantidad <= 0) {
                System.out.println("Cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Numero no valido.");
            return;
        }

        List<Asiento> seleccionados = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) {
            System.out.print("Ingrese el codigo de la butaca " + i + " (ej. A1, B3): ");
            String codigo = scanner.nextLine().trim().toUpperCase();
            if (codigo.length() < 2) {
                System.out.println("Formato de asiento invalido.");
                return;
            }
            char fila = codigo.charAt(0);
            try {
                int col = Integer.parseInt(codigo.substring(1));
                Asiento a = new Asiento(fila, col);
                if (!funcion.estaDisponible(a)) {
                    System.out.println("El asiento " + codigo + " no esta disponible. Operacion cancelada.");
                    return;
                }
                if (seleccionados.contains(a)) {
                    System.out.println("Ha ingresado el mismo asiento dos veces. Operacion cancelada.");
                    return;
                }
                seleccionados.add(a);
            } catch (NumberFormatException e) {
                System.out.println("Numero de butaca invalido.");
                return;
            }
        }

        System.out.println("\n--- DATOS DEL CLIENTE ---");
        System.out.print("Cedula / Identificacion: ");
        String cedula = scanner.nextLine().trim();
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Correo electronico: ");
        String email = scanner.nextLine().trim();
        System.out.print("¿Es cliente frecuente / socio del club? (s/n): ");
        String esFrecuente = scanner.nextLine().trim().toLowerCase();

        Cliente cliente = esFrecuente.equals("s")
                ? new ClienteFrecuente(cedula, nombre, email, 15.0)
                : new ClienteGeneral(cedula, nombre, email);

        try {
            Venta venta = controller.realizarVenta(cliente, funcion, seleccionados);
            System.out.println("\n>>> VENTA REALIZADA CON EXITO <<<");
            System.out.println(venta.generarComprobante());
        } catch (Exception e) {
            System.out.println("Error al procesar la venta: " + e.getMessage());
        }
    }

    private static void verHistorialVentas() {
        System.out.println("\n------------- HISTORIAL DE VENTAS CONSOLIDADAS -------------");
        List<Venta> ventas = controller.getVentas();
        if (ventas.isEmpty()) {
            System.out.println("No se han registrado transacciones en esta sesion.");
            return;
        }
        for (Venta v : ventas) {
            System.out.println(v.generarComprobante());
        }
    }
}