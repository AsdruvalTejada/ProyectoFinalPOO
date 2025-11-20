package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
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
import javax.swing.table.DefaultTableModel;

import logico.Cita;
import logico.Medico;
import logico.SistemaGestion;
import logico.Usuario;
import logico.Paciente;

public class PrincipalMedico extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Medico medicoActual;
    private JTable tableAgenda;
    private DefaultTableModel modelAgenda;
    private JLabel lblDetalleNombre;
    private JLabel lblDetalleHora;
    private JButton btnIniciarConsulta;
    private JButton btnVerHistorial;
    
    private final Color COLOR_FONDO = new Color(254, 251, 246);
    private final Color COLOR_VERDE = new Color(0, 200, 151);
    private final Color COLOR_ROJO = new Color(220, 53, 69);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);
    private final Color COLOR_HEADER_BG = new Color(30, 30, 30);

    public PrincipalMedico(Usuario user) {
        this.medicoActual = SistemaGestion.getInstance().getMedicoLogueado(user);

        setTitle("Panel Médico - Dr. " + (medicoActual != null ? medicoActual.getApellido() : "N/A"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1100, 700);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_FONDO);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BorderLayout(20, 0));
        setContentPane(contentPane);
        
        if (medicoActual == null) {
            JOptionPane.showMessageDialog(this, "Error: Usuario no vinculado a un Médico.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        JPanel panelAgenda = new JPanel();
        panelAgenda.setPreferredSize(new Dimension(400, 0));
        panelAgenda.setLayout(new BorderLayout(0, 10));
        panelAgenda.setBackground(COLOR_FONDO);
        contentPane.add(panelAgenda, BorderLayout.WEST);
        
        JLabel lblTituloAgenda = new JLabel("AGENDA DE HOY");
        lblTituloAgenda.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloAgenda.setForeground(COLOR_TEXTO);
        panelAgenda.add(lblTituloAgenda, BorderLayout.NORTH);

        String[] headers = {"Hora", "Paciente / Visitante", "ID Cita"};
        modelAgenda = new DefaultTableModel();
        modelAgenda.setColumnIdentifiers(headers);
        
        tableAgenda = new JTable(modelAgenda);
        tableAgenda.setRowHeight(30);
        tableAgenda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableAgenda.getTableHeader().setBackground(COLOR_HEADER_BG);
        tableAgenda.getTableHeader().setForeground(Color.WHITE);
        tableAgenda.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableAgenda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Ocultar la columna ID Cita para comodidad por ahora
        // tableAgenda.getColumnModel().getColumn(2).setMinWidth(0);
        // tableAgenda.getColumnModel().getColumn(2).setMaxWidth(0);
        // tableAgenda.getColumnModel().getColumn(2).setWidth(0);
        
        JScrollPane scrollAgenda = new JScrollPane(tableAgenda);
        scrollAgenda.setBorder(new LineBorder(Color.GRAY, 1));
        panelAgenda.add(scrollAgenda, BorderLayout.CENTER);
        
        JButton btnCerrarSesion = new JButton("CERRAR SESION");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrarSesion.setBackground(COLOR_ROJO);
        btnCerrarSesion.setForeground(Color.BLACK);
        btnCerrarSesion.setFocusPainted(false);

        btnCerrarSesion.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        btnCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(PrincipalMedico.this, 
                        "¿Seguro que desea salir?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();

                    Login login = new Login();
                    login.setVisible(true);
                }
            }
        });

        panelAgenda.add(btnCerrarSesion, BorderLayout.SOUTH);

        tableAgenda.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tableAgenda.getSelectedRow() != -1) {
                actualizarPanelDetalle();
            }
        });

        JPanel panelDetalle = new JPanel();
        panelDetalle.setBackground(Color.WHITE);
        panelDetalle.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        panelDetalle.setLayout(null); 
        contentPane.add(panelDetalle, BorderLayout.CENTER);
        
        JLabel lblInfoTitulo = new JLabel("INFORMACIÓN DE LA CITA");
        lblInfoTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblInfoTitulo.setForeground(COLOR_VERDE);
        lblInfoTitulo.setBounds(20, 20, 500, 30);
        panelDetalle.add(lblInfoTitulo);
        
        JLabel lblTagPaciente = new JLabel("Paciente:");
        lblTagPaciente.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTagPaciente.setBounds(50, 80, 100, 25);
        panelDetalle.add(lblTagPaciente);
        
        lblDetalleNombre = new JLabel("---");
        lblDetalleNombre.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDetalleNombre.setBounds(150, 80, 300, 25);
        panelDetalle.add(lblDetalleNombre);
        
        JLabel lblTagHora = new JLabel("Hora:");
        lblTagHora.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTagHora.setBounds(50, 120, 100, 25);
        panelDetalle.add(lblTagHora);
        
        lblDetalleHora = new JLabel("---");
        lblDetalleHora.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDetalleHora.setBounds(150, 120, 300, 25);
        panelDetalle.add(lblDetalleHora);
        
        btnIniciarConsulta = new JButton("INICIAR CONSULTA");
        btnIniciarConsulta.setBackground(COLOR_VERDE);
        btnIniciarConsulta.setForeground(Color.BLACK);
        btnIniciarConsulta.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnIniciarConsulta.setFocusPainted(false);
        btnIniciarConsulta.setEnabled(true); 
        btnIniciarConsulta.setBounds(150, 200, 250, 50);
        
        btnIniciarConsulta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirTriaje();
            }
        });
        
        panelDetalle.add(btnIniciarConsulta);
        btnVerHistorial = new JButton("VER HISTORIAL");
        btnVerHistorial.setBackground(COLOR_VERDE);
        btnVerHistorial.setForeground(Color.BLACK);
        btnVerHistorial.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVerHistorial.setFocusPainted(false);
        btnVerHistorial.setEnabled(true); 
        btnVerHistorial.setBounds(150, 270, 250, 50);

        panelDetalle.add(btnVerHistorial);
        btnVerHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tableAgenda.getSelectedRow();
                if(row == -1) {
                	JOptionPane.showMessageDialog(PrincipalMedico.this, "Debe seleccionar un paciente primero");
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
                        JOptionPane.showMessageDialog(null, "Este visitante aún no tiene historial.");
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
        
        for (Cita c : agendaMedico) {
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
    
    private void actualizarPanelDetalle() {
        int row = tableAgenda.getSelectedRow();
        if (row != -1) {
            String nombre = (String) modelAgenda.getValueAt(row, 1);
            String hora = (String) modelAgenda.getValueAt(row, 0);
            
            lblDetalleNombre.setText(nombre);
            lblDetalleHora.setText(hora);
            btnIniciarConsulta.setEnabled(true);
        }
    }
    
    private void abrirTriaje() {
        int row = tableAgenda.getSelectedRow();
        if(row == -1) {
        	JOptionPane.showMessageDialog(this, "Debe seleccionar una cita primero.");
        	return;
        }
        String idCita = (String) modelAgenda.getValueAt(row, 2);
        //(Recolección de datos basicos)
        FormTriaje triaje = new FormTriaje(idCita, this);
        triaje.setVisible(true);
    }
    
    public void refrescarAgenda() {
        cargarCitasDelDia();
        lblDetalleNombre.setText("---");
        lblDetalleHora.setText("---");
        btnIniciarConsulta.setEnabled(false);
    }
}