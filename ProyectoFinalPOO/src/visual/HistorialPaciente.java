package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Desktop; 
import java.awt.Font;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.io.File; 
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane; 
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import logico.Consulta;
import logico.GeneradorReportes; 
import logico.Medico;
import logico.Paciente;
import logico.Vacuna;

public class HistorialPaciente extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable tableHistorial;
    private DefaultTableModel modelHistorial;
    private Paciente paciente;
    private Medico medicoLogueado;
    private final Color COLOR_FONDO = new Color(254, 251, 246);

    public HistorialPaciente(Paciente paciente, Medico medico) {
        this.paciente = paciente;
        this.medicoLogueado = medico;
        initUI();
    }

    public HistorialPaciente(Paciente paciente) {
        this.paciente = paciente;
        this.medicoLogueado = null;
        initUI();
    }

    private void initUI() {
        setTitle("Historial Médico: " + paciente.getNombreCompleto());
        setBounds(100, 100, 800, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panelDatos = new JPanel();
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBorder(new TitledBorder(null, "Datos del Paciente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelDatos.setBounds(10, 11, 764, 85); 
        contentPanel.add(panelDatos);
        panelDatos.setLayout(null);
        
        JLabel lblNombre = new JLabel("Nombre: " + paciente.getNombreCompleto());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setBounds(20, 25, 300, 20);
        panelDatos.add(lblNombre);
        
        JLabel lblCedula = new JLabel("ID: " + paciente.getId());
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCedula.setBounds(20, 45, 200, 20);
        panelDatos.add(lblCedula);
        
        JLabel lblVacunas = new JLabel("Vacunas Aplicadas: " + paciente.getVacunasAplicadas().size());
        lblVacunas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblVacunas.setForeground(new Color(0, 128, 0));
        lblVacunas.setBounds(400, 25, 200, 20);
        panelDatos.add(lblVacunas);

        JButton btnVerVacunas = new JButton("Ver Vacunas");
        btnVerVacunas.addActionListener(e -> mostrarVacunas());
        btnVerVacunas.setBounds(600, 15, 140, 25);
        panelDatos.add(btnVerVacunas);

        JButton btnExportarPDF = new JButton("Exportar PDF");
        btnExportarPDF.setBackground(new Color(220, 53, 69));
        btnExportarPDF.setForeground(Color.WHITE);
        btnExportarPDF.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnExportarPDF.setBounds(600, 45, 140, 25); 
        btnExportarPDF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportarHistorialAPdf();
            }
        });
        panelDatos.add(btnExportarPDF);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 105, 764, 315); 
        contentPanel.add(scrollPane);
        
        String[] headers = {"Fecha", "ID Consulta", "Médico", "Diagnóstico", "Tratamiento"};
        modelHistorial = new DefaultTableModel();
        modelHistorial.setColumnIdentifiers(headers);
        
        tableHistorial = new JTable(modelHistorial);
        tableHistorial.setRowHeight(25);
        tableHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scrollPane.setViewportView(tableHistorial);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(COLOR_FONDO);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());
        buttonPane.add(closeButton);
        
        cargarHistorialFiltrado();
    }
    
    private void cargarHistorialFiltrado() {
        modelHistorial.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        ArrayList<Consulta> listaVisible;
        
        if (medicoLogueado == null) {
            listaVisible = paciente.getHistorialConsultas();
        } else {
            listaVisible = medicoLogueado.verHistorialPaciente(paciente);
        }
        
        for (Consulta c : listaVisible) {
            String diagnostico = (c.getDiagnostico() != null) ? c.getDiagnostico().getNombre() : "Sin Diagnóstico";
            String medicoNombre = c.getMedico().getNombreCompleto();
            
            Object[] fila = {
                c.getFechaConsulta().format(fmt),
                c.getId(),
                medicoNombre,
                diagnostico,
                c.getTratamiento()
            };
            modelHistorial.addRow(fila);
        }
    }
    
    private void mostrarVacunas() {
        StringBuilder sb = new StringBuilder("--- VACUNAS APLICADAS ---\n\n");
        for(Vacuna v : paciente.getVacunasAplicadas()) {
            sb.append("• ").append(v.getNombre()).append("\n");
        }
        javax.swing.JOptionPane.showMessageDialog(this, sb.toString());
    }
    
    private void exportarHistorialAPdf() {
        try {

            String nombreArchivo = "Historial_" + paciente.getId() + ".pdf";
            String rutaTemporal = System.getProperty("java.io.tmpdir") + nombreArchivo;
            
            File archivoPDF = new File(rutaTemporal);
           
            boolean exito = GeneradorReportes.generarHistorialPDF(paciente, rutaTemporal);
            
            if (exito) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(archivoPDF);
                } else {
                    JOptionPane.showMessageDialog(this, "PDF generado en: " + rutaTemporal + "\nPero no se pudo abrir automáticamente.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al generar el documento PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al intentar abrir el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}