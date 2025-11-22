package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import logico.Cita;
import logico.Medico;
import logico.SistemaGestion;
import logico.Usuario;
import logico.Paciente;

public class PrincipalMedico extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Medico medicoActual;
    
    // Componentes de la Agenda
    private JTable tableAgenda;
    private DefaultTableModel modelAgenda;
    private JLabel lblDetalleNombre;
    private JLabel lblDetalleHora;
    private JButton btnIniciarConsulta;
    private JButton btnVerHistorial;
    
    // --- NUEVA PALETA DE COLORES (Estilo Secretaria/Admin) ---
    private final Color COLOR_THEME = new Color(0, 190, 165); // Turquesa Vibrante
    private final Color COLOR_BG_MAIN = Color.WHITE;          // Fondo Limpio
    private final Color COLOR_TEXT_HEADER = new Color(0, 150, 136); // Turquesa Oscuro para textos
    private final Color COLOR_TEXT_BODY = new Color(60, 60, 60);    // Gris Oscuro
    private final Color COLOR_LOGOUT = new Color(255, 85, 85);      // Rojo suave

    public PrincipalMedico(Usuario user) {
        // Obtener el objeto Medico real
        this.medicoActual = SistemaGestion.getInstance().getMedicoLogueado(user);

        setTitle("Panel Médico - Clínica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 700);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_BG_MAIN);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BorderLayout(20, 0));
        setContentPane(contentPane);
        
        if (medicoActual == null) {
            JOptionPane.showMessageDialog(this, "Error: Usuario no vinculado a un Médico.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        // ==================================================
        // PANEL IZQUIERDO: AGENDA (Misma posición, Nuevo Estilo)
        // ==================================================
        JPanel panelAgenda = new JPanel();
        panelAgenda.setPreferredSize(new Dimension(420, 0));
        panelAgenda.setLayout(new BorderLayout(0, 15));
        panelAgenda.setBackground(COLOR_BG_MAIN);
        contentPane.add(panelAgenda, BorderLayout.WEST);
        
        // --- Encabezado Agenda ---
        JPanel pnlHeaderAgenda = new JPanel(new BorderLayout());
        pnlHeaderAgenda.setBackground(COLOR_BG_MAIN);
        // Borde inferior turquesa para decorar
        pnlHeaderAgenda.setBorder(new MatteBorder(0, 0, 2, 0, COLOR_THEME)); 
        
        JLabel lblTituloAgenda = new JLabel("Agenda de Hoy");
        lblTituloAgenda.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTituloAgenda.setForeground(COLOR_TEXT_HEADER);
        pnlHeaderAgenda.add(lblTituloAgenda, BorderLayout.WEST);
        
        // Fecha formateada bonita (ej. "21 de Noviembre")
        String fechaStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM"));
        JLabel lblFecha = new JLabel(fechaStr);
        lblFecha.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblFecha.setForeground(Color.GRAY);
        lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);
        pnlHeaderAgenda.add(lblFecha, BorderLayout.EAST);
        
        panelAgenda.add(pnlHeaderAgenda, BorderLayout.NORTH);
        
        // --- Tabla Estilizada ---
        String[] headers = {"Hora", "Paciente / Visitante", "ID Cita"};
        modelAgenda = new DefaultTableModel();
        modelAgenda.setColumnIdentifiers(headers);
        
        tableAgenda = new JTable(modelAgenda);
        tableAgenda.setRowHeight(40); // Filas más altas para mejor lectura
        tableAgenda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableAgenda.setSelectionBackground(new Color(230, 248, 245)); // Fondo selección suave
        tableAgenda.setSelectionForeground(Color.BLACK);
        tableAgenda.setGridColor(new Color(240, 240, 240));
        tableAgenda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableAgenda.setShowVerticalLines(false);
        
        // Estilizar Cabecera de la Tabla
        JTableHeader header = tableAgenda.getTableHeader();
        header.setBackground(COLOR_THEME);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setOpaque(true);
        
        // Ocultar columna ID visualmente
        tableAgenda.getColumnModel().getColumn(2).setMinWidth(0);
        tableAgenda.getColumnModel().getColumn(2).setMaxWidth(0);
        tableAgenda.getColumnModel().getColumn(2).setWidth(0);
        
        // Ajustar ancho de hora
        tableAgenda.getColumnModel().getColumn(0).setMaxWidth(80);
        tableAgenda.getColumnModel().getColumn(0).setPreferredWidth(80);
        
        JScrollPane scrollAgenda = new JScrollPane(tableAgenda);
        scrollAgenda.setBorder(new LineBorder(new Color(230, 230, 230), 1));
        scrollAgenda.getViewport().setBackground(Color.WHITE);
        panelAgenda.add(scrollAgenda, BorderLayout.CENTER);
        
        // --- Botón Cerrar Sesión (Estilizado Rojo) ---
        JButton btnCerrarSesion = new JButton("CERRAR SESIÓN");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setBackground(COLOR_LOGOUT);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setPreferredSize(new Dimension(0, 40));
        
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Salir del sistema?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login().setVisible(true);
            }
        });
        panelAgenda.add(btnCerrarSesion, BorderLayout.SOUTH);
        
        // Listener Tabla
        tableAgenda.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tableAgenda.getSelectedRow() != -1) {
                actualizarPanelDetalle();
            }
        });

        // ==================================================
        // PANEL DERECHO: DETALLE (Misma posición, Nuevo Estilo)
        // ==================================================
        JPanel panelDetalle = new JPanel();
        panelDetalle.setBackground(Color.WHITE);
        // Borde sutil gris alrededor del detalle
        panelDetalle.setBorder(new LineBorder(new Color(230, 230, 230), 1, true)); 
        panelDetalle.setLayout(null); 
        contentPane.add(panelDetalle, BorderLayout.CENTER);
        
        // --- Encabezado Detalle (Fondo Turquesa) ---
        JPanel pnlHeaderDetalle = new JPanel();
        pnlHeaderDetalle.setBounds(0, 0, 1000, 60); // Ancho suficiente
        pnlHeaderDetalle.setBackground(COLOR_THEME);
        pnlHeaderDetalle.setLayout(null);
        panelDetalle.add(pnlHeaderDetalle);
        
        JLabel lblInfoTitulo = new JLabel("DETALLE DE LA CITA");
        lblInfoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblInfoTitulo.setForeground(Color.WHITE);
        lblInfoTitulo.setBounds(20, 0, 300, 60);
        pnlHeaderDetalle.add(lblInfoTitulo);
        
        JLabel lblDrName = new JLabel("Dr. " + medicoActual.getApellido());
        lblDrName.setHorizontalAlignment(SwingConstants.RIGHT);
        lblDrName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDrName.setForeground(new Color(225, 255, 250));
        lblDrName.setBounds(300, 0, 300, 60); // Ajustar según ancho ventana
        pnlHeaderDetalle.add(lblDrName);
        
        // --- Datos ---
        JLabel lblTagPaciente = new JLabel("PACIENTE:");
        lblTagPaciente.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTagPaciente.setForeground(Color.GRAY);
        lblTagPaciente.setBounds(30, 100, 100, 20);
        panelDetalle.add(lblTagPaciente);
        
        lblDetalleNombre = new JLabel("---");
        lblDetalleNombre.setFont(new Font("Segoe UI", Font.PLAIN, 22)); // Letra grande
        lblDetalleNombre.setForeground(COLOR_TEXT_BODY);
        lblDetalleNombre.setBounds(30, 120, 400, 30);
        panelDetalle.add(lblDetalleNombre);
        
        JLabel lblTagHora = new JLabel("HORA PROGRAMADA:");
        lblTagHora.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTagHora.setForeground(Color.GRAY);
        lblTagHora.setBounds(30, 180, 150, 20);
        panelDetalle.add(lblTagHora);
        
        lblDetalleHora = new JLabel("---");
        lblDetalleHora.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        lblDetalleHora.setForeground(COLOR_TEXT_BODY);
        lblDetalleHora.setBounds(30, 200, 300, 30);
        panelDetalle.add(lblDetalleHora);
        
        // --- Botones de Acción (Centrados y Grandes) ---
        btnIniciarConsulta = new JButton("INICIAR CONSULTA");
        btnIniciarConsulta.setBackground(COLOR_THEME);
        btnIniciarConsulta.setForeground(Color.WHITE);
        btnIniciarConsulta.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnIniciarConsulta.setFocusPainted(false);
        btnIniciarConsulta.setBorderPainted(false);
        btnIniciarConsulta.setEnabled(false); 
        btnIniciarConsulta.setBounds(150, 300, 300, 60); // Botón grande central
        
        btnIniciarConsulta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirTriaje();
            }
        });
        panelDetalle.add(btnIniciarConsulta);
        
        btnVerHistorial = new JButton("VER HISTORIAL CLÍNICO");
        btnVerHistorial.setBackground(new Color(240, 240, 240)); // Gris claro
        btnVerHistorial.setForeground(COLOR_TEXT_BODY);
        btnVerHistorial.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVerHistorial.setFocusPainted(false);
        btnVerHistorial.setBorder(new LineBorder(Color.LIGHT_GRAY, 1)); // Borde sutil
        btnVerHistorial.setEnabled(false); 
        btnVerHistorial.setBounds(150, 380, 300, 40); // Debajo del principal

        panelDetalle.add(btnVerHistorial);
        
        btnVerHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tableAgenda.getSelectedRow();
                if(row == -1) {
                    JOptionPane.showMessageDialog(PrincipalMedico.this, "Seleccione una cita primero.");
                    return;
                }
                
                String idCita = (String) modelAgenda.getValueAt(row, 2);
                Cita cita = SistemaGestion.getInstance().buscarCitaPorId(idCita);
                if (cita != null) {
                    Paciente paciente = SistemaGestion.getInstance().buscarPacientePorId(cita.getIdPaciente());
                    if (paciente != null) {
                        HistorialPaciente historial = new HistorialPaciente(paciente, medicoActual);
                        historial.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(PrincipalMedico.this, "Este es un visitante nuevo (Sin historial).");
                    }
                }
            }
        });
        
        cargarCitasDelDia();
    }
    
    private void cargarCitasDelDia() {
        modelAgenda.setRowCount(0);
        LocalDate hoy = LocalDate.now();
        ArrayList<Cita> agendaMedico = medicoActual.getAgenda();
        
        if (agendaMedico == null) return;

        for (Cita c : agendaMedico) {
            if (c != null && c.getFechaCitada() != null && c.getEstado() != null) {
                if (c.getFechaCitada().toLocalDate().equals(hoy) && c.getEstado().equalsIgnoreCase("pendiente")) {
                    Object[] fila = {
                        c.getFechaCitada().toLocalTime().toString(),
                        c.getNameVisitante(),
                        c.getId()
                    };
                    modelAgenda.addRow(fila);
                }
            }
        }
    }
    
    private void actualizarPanelDetalle() {
        int row = tableAgenda.getSelectedRow();
        if (row != -1) {
            String nombre = (String) modelAgenda.getValueAt(row, 1);
            String hora = (String) modelAgenda.getValueAt(row, 0);
            
            lblDetalleNombre.setText(nombre);
            lblDetalleHora.setText(hora);
            btnIniciarConsulta.setEnabled(true);
            btnVerHistorial.setEnabled(true);
        }
    }
    
    private void abrirTriaje() {
        int row = tableAgenda.getSelectedRow();
        if(row == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una cita primero.");
            return;
        }
        String idCita = (String) modelAgenda.getValueAt(row, 2);
        FormTriaje triaje = new FormTriaje(idCita, this);
        triaje.setVisible(true);
    }
    
    public void refrescarAgenda() {
        cargarCitasDelDia();
        lblDetalleNombre.setText("---");
        lblDetalleHora.setText("---");
        btnIniciarConsulta.setEnabled(false);
        btnVerHistorial.setEnabled(false);
    }
}