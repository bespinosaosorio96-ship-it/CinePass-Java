package main;

import controller.CineController;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VentanaCine extends JFrame {
    private final CineController controller;

    // Componentes del Panel Norte
    private JComboBox<Funcion> cboFunciones;
    private JTextField txtIdCliente;
    private JTextField txtNombreCliente;
    private JTextField txtEmailCliente;
    private JComboBox<String> cboTipoCliente;
    private JButton btnConfirmarVenta;
    private JButton btnTotalesTaquilla;

    // Componentes del Panel Central
    private JPanel panelMapaAsientos;
    private JTextArea txtComprobantes;
    private final List<Asiento> asientosSeleccionados;

    private static final Color COLOR_DISPONIBLE = new Color(144, 238, 144); // Verde claro
    private static final Color COLOR_OCUPADO = new Color(220, 80, 80);      // Rojo suave
    private static final Color COLOR_SELECCIONADO = new Color(255, 215, 0); // Dorado/Amarillo

    public VentanaCine() {
        this.controller = new CineController();
        this.controller.cargarDatosIniciales();
        this.asientosSeleccionados = new ArrayList<>();

        inicializarVentana();
        construirInterfaz();
        actualizarMapaAsientos();
    }

    private void inicializarVentana() {
        setTitle("CinePass - Sistema de Gestión y Reserva de Boletería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 750);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(12, 12));
        panelPrincipal.setBorder(new EmptyBorder(12, 12, 12, 12));

        // ==========================================
        // 1. PANEL NORTE (Formulario y Controles)
        // ==========================================
        JPanel panelNorte = new JPanel(new GridLayout(6, 2, 8, 8));
        panelNorte.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos de la Reserva y Cliente",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13)));

        // Fila 1: Selección de Función
        panelNorte.add(new JLabel("Función / Película:"));
        cboFunciones = new JComboBox<>();
        for (Funcion f : controller.buscarFuncionesDisponibles()) {
            cboFunciones.addItem(f);
        }
        cboFunciones.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                asientosSeleccionados.clear();
                actualizarMapaAsientos();
            }
        });
        panelNorte.add(cboFunciones);

        // Fila 2: Documento de Identidad
        panelNorte.add(new JLabel("Doc. Identidad Cliente:"));
        txtIdCliente = new JTextField();
        panelNorte.add(txtIdCliente);

        // Fila 3: Nombre del Cliente
        panelNorte.add(new JLabel("Nombre Cliente:"));
        txtNombreCliente = new JTextField();
        panelNorte.add(txtNombreCliente);

        // Fila 4: Correo del Cliente
        panelNorte.add(new JLabel("Correo Electrónico:"));
        txtEmailCliente = new JTextField();
        panelNorte.add(txtEmailCliente);

        // Fila 5: Tipo de Cliente
        panelNorte.add(new JLabel("Tipo de Cliente:"));
        cboTipoCliente = new JComboBox<>(new String[]{
                "Cliente General (0% Desc.)",
                "Cliente Frecuente (15% Desc.)"
        });
        panelNorte.add(cboTipoCliente);

        // Fila 6: Botones de Acción
        btnConfirmarVenta = new JButton("Confirmar Venta");
        btnConfirmarVenta.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnConfirmarVenta.setBackground(new Color(60, 140, 220));
        btnConfirmarVenta.setForeground(Color.BLACK);
        btnConfirmarVenta.setFocusPainted(false);
        btnConfirmarVenta.addActionListener(e -> confirmarVenta());
        panelNorte.add(btnConfirmarVenta);

        btnTotalesTaquilla = new JButton("Ver Totales Taquilla");
        btnTotalesTaquilla.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnTotalesTaquilla.setBackground(new Color(75, 175, 80));
        btnTotalesTaquilla.setForeground(Color.BLACK);
        btnTotalesTaquilla.setFocusPainted(false);
        btnTotalesTaquilla.addActionListener(e -> mostrarTotalesTaquilla());
        panelNorte.add(btnTotalesTaquilla);

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // ==========================================
        // 2. PANEL CENTRAL (Mapa de Butacas y Comprobantes)
        // ==========================================
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));

        // Subpanel Izquierdo: Visualización de Asientos
        JPanel panelIzquierdo = new JPanel(new BorderLayout(8, 8));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Selección de Asientos",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13)));

        JLabel lblPantalla = new JLabel("=== PANTALLA ===", SwingConstants.CENTER);
        lblPantalla.setOpaque(true);
        lblPantalla.setBackground(new Color(50, 50, 50));
        lblPantalla.setForeground(Color.WHITE);
        lblPantalla.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblPantalla.setBorder(new EmptyBorder(6, 6, 6, 6));
        panelIzquierdo.add(lblPantalla, BorderLayout.NORTH);

        panelMapaAsientos = new JPanel();
        JScrollPane scrollAsientos = new JScrollPane(panelMapaAsientos);
        scrollAsientos.setBorder(BorderFactory.createEmptyBorder());
        panelIzquierdo.add(scrollAsientos, BorderLayout.CENTER);

        panelCentral.add(panelIzquierdo);

        // Subpanel Derecho: Facturación / Comprobantes
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Historial de Comprobantes de Venta",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 13)));

        txtComprobantes = new JTextArea();
        txtComprobantes.setEditable(false);
        txtComprobantes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtComprobantes.setText("--- Sin transacciones registradas en la sesión actual ---\n");

        JScrollPane scrollComprobantes = new JScrollPane(txtComprobantes);
        panelDerecho.add(scrollComprobantes, BorderLayout.CENTER);

        panelCentral.add(panelDerecho);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    private void actualizarMapaAsientos() {
        panelMapaAsientos.removeAll();
        asientosSeleccionados.clear();

        Funcion funcion = (Funcion) cboFunciones.getSelectedItem();
        if (funcion == null) {
            panelMapaAsientos.revalidate();
            panelMapaAsientos.repaint();
            return;
        }

        Sala sala = funcion.getSala();
        int filas = sala.getFilas();
        int columnas = sala.getColumnas();

        panelMapaAsientos.setLayout(new GridLayout(filas, columnas, 6, 6));
        panelMapaAsientos.setBorder(new EmptyBorder(10, 10, 10, 10));

        Map<String, EstadoAsiento> estados = funcion.getEstadoAsientos();

        for (int i = 0; i < filas; i++) {
            char filaChar = (char) ('A' + i);
            for (int j = 1; j <= columnas; j++) {
                String codigo = "" + filaChar + j;
                EstadoAsiento estado = estados.get(codigo);
                Asiento asiento = new Asiento(filaChar, j);

                JToggleButton btnAsiento = new JToggleButton(codigo);
                btnAsiento.setFont(new Font("SansSerif", Font.BOLD, 11));
                btnAsiento.setFocusPainted(false);

                if (estado == EstadoAsiento.OCUPADO) {
                    btnAsiento.setText(codigo + " (X)");
                    btnAsiento.setBackground(COLOR_OCUPADO);
                    btnAsiento.setForeground(Color.BLACK);
                    btnAsiento.setEnabled(false);
                } else {
                    btnAsiento.setBackground(COLOR_DISPONIBLE);
                    btnAsiento.setForeground(Color.BLACK);

                    btnAsiento.addActionListener(e -> {
                        if (btnAsiento.isSelected()) {
                            btnAsiento.setBackground(COLOR_SELECCIONADO);
                            asientosSeleccionados.add(asiento);
                        } else {
                            btnAsiento.setBackground(COLOR_DISPONIBLE);
                            asientosSeleccionados.remove(asiento);
                        }
                    });
                }

                panelMapaAsientos.add(btnAsiento);
            }
        }

        panelMapaAsientos.revalidate();
        panelMapaAsientos.repaint();
    }

    private void confirmarVenta() {
        String id = txtIdCliente.getText().trim();
        String nombre = txtNombreCliente.getText().trim();
        String email = txtEmailCliente.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos del cliente (Documento, Nombre y Correo) son obligatorios.",
                    "Campos Incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (asientosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar al menos un asiento en el mapa de butacas.",
                    "Sin Asientos Seleccionados",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Funcion funcionSeleccionada = (Funcion) cboFunciones.getSelectedItem();
        if (funcionSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "No se ha seleccionado ninguna función.",
                    "Función Inválida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Instanciación polimórfica del cliente
        Cliente cliente;
        int tipoIndex = cboTipoCliente.getSelectedIndex();
        if (tipoIndex == 1) {
            // Cliente Frecuente con 15% de descuento
            cliente = new ClienteFrecuente(id, nombre, email, 15.0);
        } else {
            // Cliente General con 0% de descuento
            cliente = new ClienteGeneral(id, nombre, email);
        }

        try {
            Venta venta = controller.realizarVenta(cliente, funcionSeleccionada, new ArrayList<>(asientosSeleccionados));

            // Si es la primera venta, limpiar el texto inicial
            if (controller.getVentas().size() == 1) {
                txtComprobantes.setText("");
            }
            txtComprobantes.append(venta.generarComprobante() + "\n");
            txtComprobantes.setCaretPosition(txtComprobantes.getDocument().getLength());

            JOptionPane.showMessageDialog(this,
                    String.format("¡Venta confirmada exitosamente!\nCódigo: %s\nTotal: $%,.0f (%d boletos)",
                            venta.getCodigoVenta(), venta.getTotal(), venta.getBoletos().size()),
                    "Venta Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

            // Limpiar formulario y refrescar asientos
            txtIdCliente.setText("");
            txtNombreCliente.setText("");
            txtEmailCliente.setText("");
            cboTipoCliente.setSelectedIndex(0);
            asientosSeleccionados.clear();
            actualizarMapaAsientos();

        } catch (IllegalStateException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error al Procesar Venta",
                    JOptionPane.ERROR_MESSAGE);
            actualizarMapaAsientos();
        }
    }

    private void mostrarTotalesTaquilla() {
        int totalBoletos = controller.getTotalBoletosVendidos();
        double totalDescuentos = controller.calcularTotalDescuentos();
        double totalRecaudado = controller.calcularTotalRecaudado();

        String mensaje = String.format(
                "========================================\n" +
                "         TOTALES DE TAQUILLA CINEPASS   \n" +
                "========================================\n" +
                "  • Boletos Vendidos     : %d\n" +
                "  • Descuentos Otorgados : $%,.0f\n" +
                "  • Total Neto Recaudado : $%,.0f\n" +
                "========================================",
                totalBoletos, totalDescuentos, totalRecaudado);

        JOptionPane.showMessageDialog(this,
                mensaje,
                "Totales de Taquilla",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Establecer Look & Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            VentanaCine ventana = new VentanaCine();
            ventana.setVisible(true);
        });
    }
}
