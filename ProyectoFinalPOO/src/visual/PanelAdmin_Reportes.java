package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import logico.SistemaGestion;

@SuppressWarnings("serial")
public class PanelAdmin_Reportes extends JPanel {

    /**
     * Create the panel.
     */
    public PanelAdmin_Reportes(Color colorAccent) {
        setLayout(new GridLayout(1, 2, 20, 0));
        setBackground(new Color(248, 249, 250)); 
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlTop5 = crearTarjetaBase("Top 5 Enfermedades");
        add(pnlTop5);
        
        // Lógica: Obtener datos y llenar la lista
        JList<String> listaEnfermedades = new JList<>();
        listaEnfermedades.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaEnfermedades.setFixedCellHeight(35); // Altura cómoda para leer
        listaEnfermedades.setBorder(null);
        
        cargarDatosEnfermedades(listaEnfermedades);
        
        // Scroll para la lista
        JScrollPane scrollEnfermedades = new JScrollPane(listaEnfermedades);
        scrollEnfermedades.setBorder(null);
        pnlTop5.add(scrollEnfermedades, BorderLayout.CENTER);


        // --- PANEL DERECHO: ESTADO DE VACUNACIÓN ---
        JPanel pnlVacunas = crearTarjetaBase("Estado de Vacunación");
        add(pnlVacunas);
        
        // Lógica: Tabla de vacunas
        String[] columnas = {"Concepto (Vacuna - Estado)", "Cantidad"};
        DefaultTableModel modelVacunas = new DefaultTableModel(columnas, 0);
        JTable tablaVacunas = new JTable(modelVacunas);
        
        // Estilo básico de tabla
        tablaVacunas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaVacunas.setRowHeight(30);
        tablaVacunas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaVacunas.getTableHeader().setBackground(Color.WHITE);
        tablaVacunas.setShowGrid(false);
        tablaVacunas.setFillsViewportHeight(true);
        
        cargarDatosVacunas(modelVacunas);
        
        JScrollPane scrollVacunas = new JScrollPane(tablaVacunas);
        scrollVacunas.setBorder(null);
        scrollVacunas.getViewport().setBackground(Color.WHITE); // Fondo blanco si la tabla es corta
        pnlVacunas.add(scrollVacunas, BorderLayout.CENTER);
    }

    /**
     * Crea un panel blanco con sombra/borde y título (estilo "Tarjeta")
     */
    private JPanel crearTarjetaBase(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        // Borde sutil gris claro para dar efecto de tarjeta
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true), // Borde exterior
                new EmptyBorder(20, 20, 20, 20) // Margen interior (padding)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 37, 41));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        return panel;
    }

    /**
     * Carga los datos reales desde SistemaGestion
     */
    private void cargarDatosEnfermedades(JList<String> list) {
        DefaultListModel<String> model = new DefaultListModel<>();
        
        // LLAMADA A TU LÓGICA
        ArrayList<String> top5 = SistemaGestion.getInstance().getTop5Enfermedades();
        
        if (top5.isEmpty()) {
            model.addElement("No hay diagnósticos registrados aún.");
        } else {
            for (String item : top5) {
                model.addElement("• " + item);
            }
        }
        list.setModel(model);
    }

    /**
     * Carga los datos reales desde SistemaGestion
     */
    private void cargarDatosVacunas(DefaultTableModel model) {
        model.setRowCount(0); // Limpiar tabla
        
        // LLAMADA A TU LÓGICA
        HashMap<String, Integer> reporte = SistemaGestion.getInstance().getReporteVacunacion();
        
        if (reporte.isEmpty()) {
             model.addRow(new Object[]{"No hay vacunas registradas", 0});
        } else {
            // Iteramos sobre el mapa para llenar la tabla
            for (Map.Entry<String, Integer> entry : reporte.entrySet()) {
                model.addRow(new Object[]{
                    entry.getKey(),   // Ej: "Covid - Aplicadas"
                    entry.getValue()  // Ej: 15
                });
            }
        }
    }
}