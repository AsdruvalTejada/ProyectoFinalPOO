package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import logico.Cita;
import logico.Medico;
import logico.Paciente;
import logico.SistemaGestion;
import logico.Usuario;

@SuppressWarnings("serial")
public class PrincipalMedico extends JFrame {

    private JPanel contentPane;
    private Medico medicoActual;
    private JTable tableAgenda;
    private DefaultTableModel modelAgenda;
    
    private JLabel lblDetalleNombre;
    private JLabel lblDetalleHora;
    private JButton btnIniciarConsulta;
    private JButton btnVerHistorial;
    private JPanel panelCardDetalle; 
    
    private JSpinner spnFechaAgenda;
    private JButton btnAlertaCitasFuturas;
    
    private final Color COLOR_FONDO        = new Color(254, 251, 246); 
    private final Color COLOR_TEAL_MAIN    = new Color(10, 186, 181);  
    private final Color COLOR_VERDE_ACCION = new Color(0, 200, 151);   
    private final Color COLOR_TEXTO_DARK   = new Color(50, 50, 50);    
    private final Color COLOR_BLANCO       = Color.WHITE;
    private final Color COLOR_ROJO_SALIR   = new Color(255, 80, 80);   
    private final Color COLOR_AZUL_INFO    = new Color(0, 102, 204); 
    private final Color COLOR_ALERTA       = new Color(255, 140, 0);  

    public PrincipalMedico(Usuario user) {
        this.medicoActual = SistemaGestion.getInstance().getMedicoLogueado(user);

        setTitle("Panel Médico - Dr. " + (medicoActual != null ? medicoActual.getApellido() : "N/A"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 750); 
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_FONDO);
        contentPane.setBorder(null); 
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        if (medicoActual == null) {
            JOptionPane.showMessageDialog(this, "Error: Usuario no vinculado a un Médico.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        JPanel panelAgenda = new JPanel();
        panelAgenda.setPreferredSize(new Dimension(450, 0)); 
        panelAgenda.setLayout(new BorderLayout(0, 0));
        panelAgenda.setBackground(COLOR_BLANCO);
        panelAgenda.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));
        contentPane.add(panelAgenda, BorderLayout.WEST);
        
        JPanel pnlHeaderAgenda = new JPanel(new BorderLayout(0, 15));
        pnlHeaderAgenda.setBackground(COLOR_TEAL_MAIN); 
        pnlHeaderAgenda.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTituloAgenda = new JLabel("MI AGENDA");
        lblTituloAgenda.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTituloAgenda.setForeground(COLOR_BLANCO);
        pnlHeaderAgenda.add(lblTituloAgenda, BorderLayout.NORTH);
        
        JPanel pnlFecha = new JPanel(new BorderLayout(10, 0));
        pnlFecha.setOpaque(false); 
        
        JLabel lblVerFecha = new JLabel("Ver fecha:");
        lblVerFecha.setForeground(COLOR_BLANCO);
        lblVerFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlFecha.add(lblVerFecha, BorderLayout.WEST);
        
        spnFechaAgenda = new JSpinner(new SpinnerDateModel());
        spnFechaAgenda.setEditor(new JSpinner.DateEditor(spnFechaAgenda, "dd/MM/yyyy"));
        spnFechaAgenda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spnFechaAgenda.setBorder(BorderFactory.createEmptyBorder()); 
        spnFechaAgenda.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                cargarCitasDeFechaSeleccionada();
            }
        });
        pnlFecha.add(spnFechaAgenda, BorderLayout.CENTER);
        pnlHeaderAgenda.add(pnlFecha, BorderLayout.SOUTH);
        
        panelAgenda.add(pnlHeaderAgenda, BorderLayout.NORTH);

        String[] headers = {"Hora", "Paciente", "Estado", "ID"};
        modelAgenda = new DefaultTableModel(null, headers) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tableAgenda = new JTable(modelAgenda);
        tableAgenda.setRowHeight(40); 
        tableAgenda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableAgenda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableAgenda.setShowVerticalLines(false);
        tableAgenda.setGridColor(new Color(240, 240, 240));
        
        tableAgenda.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableAgenda.getTableHeader().setBackground(new Color(245, 245, 245));
        tableAgenda.getTableHeader().setForeground(COLOR_TEXTO_DARK);
        tableAgenda.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        tableAgenda.getColumnModel().getColumn(3).setMinWidth(0);
        tableAgenda.getColumnModel().getColumn(3).setMaxWidth(0);
        tableAgenda.getColumnModel().getColumn(3).setWidth(0);
        
        JScrollPane scrollAgenda = new JScrollPane(tableAgenda);
        scrollAgenda.setBorder(null); 
        scrollAgenda.getViewport().setBackground(COLOR_BLANCO);
        panelAgenda.add(scrollAgenda, BorderLayout.CENTER);
        
        JPanel panelBotonesIzq = new JPanel();
        panelBotonesIzq.setBackground(COLOR_BLANCO);
        panelBotonesIzq.setLayout(new BorderLayout(0, 10));
        panelBotonesIzq.setBorder(new EmptyBorder(20, 20, 20, 20));
        panelAgenda.add(panelBotonesIzq, BorderLayout.SOUTH);
        
        JPanel pnlGestionBtns = new JPanel(new BorderLayout(0, 10));
        pnlGestionBtns.setBackground(COLOR_BLANCO);
        
        btnAlertaCitasFuturas = new JButton("!!! VER CITAS PENDIENTES");
        estilizarBoton(btnAlertaCitasFuturas, COLOR_ALERTA);
        btnAlertaCitasFuturas.setVisible(false); 
        btnAlertaCitasFuturas.addActionListener(e -> irAProximaCitaFutura());
        pnlGestionBtns.add(btnAlertaCitasFuturas, BorderLayout.NORTH);
        
        JButton btnGestionAgenda = new JButton("GESTIONAR MI HORARIO");
        estilizarBoton(btnGestionAgenda, COLOR_AZUL_INFO);
        btnGestionAgenda.addActionListener(e -> abrirGestionAgenda());
        pnlGestionBtns.add(btnGestionAgenda, BorderLayout.CENTER);
        
        panelBotonesIzq.add(pnlGestionBtns, BorderLayout.CENTER);
        
        JButton btnCerrarSesion = new JButton("CERRAR SESIÓN");
        estilizarBoton(btnCerrarSesion, COLOR_ROJO_SALIR);
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
        panelBotonesIzq.add(btnCerrarSesion, BorderLayout.SOUTH);

        tableAgenda.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tableAgenda.getSelectedRow() != -1) {
                actualizarPanelDetalle();
            }
        });

        JPanel panelDerechoContainer = new JPanel();
        panelDerechoContainer.setBackground(COLOR_FONDO);
        panelDerechoContainer.setLayout(null); 
        contentPane.add(panelDerechoContainer, BorderLayout.CENTER);
        
        panelCardDetalle = new JPanel();
        panelCardDetalle.setBounds(50, 50, 600, 500); 
        panelCardDetalle.setBackground(COLOR_BLANCO);
        panelCardDetalle.setLayout(null);
        panelCardDetalle.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        panelDerechoContainer.add(panelCardDetalle);
        
        JPanel pnlHeaderCard = new JPanel();
        pnlHeaderCard.setBounds(0, 0, 600, 60);
        pnlHeaderCard.setBackground(new Color(250, 250, 250));
        pnlHeaderCard.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        pnlHeaderCard.setLayout(new BorderLayout());
        panelCardDetalle.add(pnlHeaderCard);
        
        JLabel lblInfoTitulo = new JLabel("DETALLE DE LA CITA");
        lblInfoTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblInfoTitulo.setForeground(COLOR_TEAL_MAIN);
        pnlHeaderCard.add(lblInfoTitulo, BorderLayout.CENTER);
        
        JLabel lblAvatar = new JLabel();
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        lblAvatar.setBounds(250, 80, 100, 100); 

        String rutaA = "recursos/Paciente.png"; 
        File archivoA = new File(rutaA);
        
        boolean imagenCargada = false;

        if (archivoA.exists()) {
            cargarImagen(lblAvatar, rutaA);
            imagenCargada = true;
        } 
        
        if (!imagenCargada) {
            lblAvatar.setText("PACIENTE");
            lblAvatar.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblAvatar.setForeground(Color.LIGHT_GRAY);
            lblAvatar.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
        }
        panelCardDetalle.add(lblAvatar);
        
        lblDetalleNombre = new JLabel("Seleccione un paciente");
        lblDetalleNombre.setHorizontalAlignment(SwingConstants.CENTER);
        lblDetalleNombre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblDetalleNombre.setForeground(COLOR_TEXTO_DARK);
        lblDetalleNombre.setBounds(50, 190, 500, 30);
        panelCardDetalle.add(lblDetalleNombre);
        
        lblDetalleHora = new JLabel("--:--");
        lblDetalleHora.setHorizontalAlignment(SwingConstants.CENTER);
        lblDetalleHora.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblDetalleHora.setForeground(Color.GRAY);
        lblDetalleHora.setBounds(50, 225, 500, 25);
        panelCardDetalle.add(lblDetalleHora);
        
        btnIniciarConsulta = new JButton("INICIAR CONSULTA");
        estilizarBoton(btnIniciarConsulta, COLOR_VERDE_ACCION);
        btnIniciarConsulta.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnIniciarConsulta.setBounds(100, 280, 400, 50);
        btnIniciarConsulta.setEnabled(false); 
        btnIniciarConsulta.addActionListener(e -> abrirTriaje());
        panelCardDetalle.add(btnIniciarConsulta);
        
        btnVerHistorial = new JButton("Ver Historial Clínico");
        btnVerHistorial.setBackground(COLOR_BLANCO);
        btnVerHistorial.setForeground(COLOR_AZUL_INFO);
        btnVerHistorial.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVerHistorial.setFocusPainted(false);
        btnVerHistorial.setBorder(new LineBorder(COLOR_AZUL_INFO, 2));
        btnVerHistorial.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVerHistorial.setBounds(100, 350, 400, 40);
        btnVerHistorial.setEnabled(false);
        
        btnVerHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if(btnVerHistorial.isEnabled()) {
                    btnVerHistorial.setBackground(COLOR_AZUL_INFO);
                    btnVerHistorial.setForeground(Color.WHITE);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if(btnVerHistorial.isEnabled()) {
                    btnVerHistorial.setBackground(Color.WHITE);
                    btnVerHistorial.setForeground(COLOR_AZUL_INFO);
                }
            }
        });
        btnVerHistorial.addActionListener(e -> verHistorial());
        panelCardDetalle.add(btnVerHistorial);
        
        cargarCitasDeFechaSeleccionada();
        
        verificarCitasFuturas();
        listenServer();
    }
    
    private void verificarCitasFuturas() {
        LocalDate hoy = LocalDate.now();
        int citasPendientesFuturas = 0;
        
        for (Cita c : SistemaGestion.getInstance().getListaCitas()) {
            if(c.getMedico().getId().equals(medicoActual.getId()) && 
               c.getEstado().equalsIgnoreCase("pendiente") &&
               c.getFechaCitada().toLocalDate().isAfter(hoy)) {
                citasPendientesFuturas++;
            }
        }
        
        if(citasPendientesFuturas > 0) {
            btnAlertaCitasFuturas.setText("!!! " + citasPendientesFuturas + " CITAS FUTURAS");
            btnAlertaCitasFuturas.setVisible(true);
        } else {
            btnAlertaCitasFuturas.setVisible(false);
        }
    }
    
    private void irAProximaCitaFutura() {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaMasProxima = null;
        
        for (Cita c : SistemaGestion.getInstance().getListaCitas()) {
            if(c.getMedico().getId().equals(medicoActual.getId()) && 
               c.getEstado().equalsIgnoreCase("pendiente") &&
               c.getFechaCitada().toLocalDate().isAfter(hoy)) {
                
                LocalDate fechaCita = c.getFechaCitada().toLocalDate();
                if (fechaMasProxima == null || fechaCita.isBefore(fechaMasProxima)) {
                    fechaMasProxima = fechaCita;
                }
            }
        }
        
        if (fechaMasProxima != null) {
            Date date = Date.from(fechaMasProxima.atStartOfDay(ZoneId.systemDefault()).toInstant());
            spnFechaAgenda.setValue(date);
        }
    }

    private void cargarImagen(JLabel lbl, String ruta) {
        ImageIcon iconOriginal = new ImageIcon(ruta);
        Image imgEscalada = iconOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        lbl.setIcon(new ImageIcon(imgEscalada));
    }
    
    private void estilizarBoton(JButton btn, Color colorBg) {
        btn.setBackground(colorBg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    private void abrirGestionAgenda() {
        FormGestionAgenda gestion = new FormGestionAgenda(medicoActual);
        gestion.setVisible(true);
        cargarCitasDeFechaSeleccionada();
    }
    
    private void cargarCitasDeFechaSeleccionada() {
        modelAgenda.setRowCount(0);
        Date date = (Date) spnFechaAgenda.getValue();
        LocalDate fechaSeleccionada = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        ArrayList<Cita> todasLasCitas = SistemaGestion.getInstance().getListaCitas();
        
        for (Cita c : todasLasCitas) {
            if(c.getMedico().getId().equals(medicoActual.getId())) {
                if (c.getFechaCitada().toLocalDate().equals(fechaSeleccionada) && !c.getEstado().equalsIgnoreCase("Cancelada")) {
                    Object[] fila = {
                        c.getFechaCitada().toLocalTime().toString(),
                        c.getNameVisitante(),
                        c.getEstado().toUpperCase(),
                        c.getId()
                    };
                    modelAgenda.addRow(fila);
                }
            }
        }
        
        lblDetalleNombre.setText("Seleccione un paciente");
        lblDetalleNombre.setForeground(Color.GRAY);
        lblDetalleHora.setText("--:--");
        btnIniciarConsulta.setEnabled(false);
        btnVerHistorial.setEnabled(false);
        
        verificarCitasFuturas();
    }
    
    private void actualizarPanelDetalle() {
        int row = tableAgenda.getSelectedRow();
        if (row != -1) {
            String nombre = (String) modelAgenda.getValueAt(row, 1);
            String hora = (String) modelAgenda.getValueAt(row, 0);
            String estado = (String) modelAgenda.getValueAt(row, 2);
            
            lblDetalleNombre.setText(nombre);
            lblDetalleNombre.setForeground(COLOR_TEXTO_DARK);
            lblDetalleHora.setText("Hora de cita: " + hora + "  |  Estado: " + estado);
            
            boolean esPendiente = estado.equalsIgnoreCase("PENDIENTE");
            btnIniciarConsulta.setEnabled(esPendiente);
            if(esPendiente) {
                btnIniciarConsulta.setBackground(COLOR_VERDE_ACCION);
                btnIniciarConsulta.setText("INICIAR CONSULTA");
            } else {
                btnIniciarConsulta.setBackground(Color.LIGHT_GRAY);
                btnIniciarConsulta.setText("CONSULTA " + estado);
            }
            
            btnVerHistorial.setEnabled(true);
        }
    }
    
    private void abrirTriaje() {
        int row = tableAgenda.getSelectedRow();
        if(row == -1) return;
        String idCita = (String) modelAgenda.getValueAt(row, 3); 
        FormTriaje triaje = new FormTriaje(idCita, this);
        triaje.setVisible(true);
    }
    
    private void verHistorial() {
        int row = tableAgenda.getSelectedRow();
        if(row == -1) return;
        String idCita = (String) modelAgenda.getValueAt(row, 3);
        
        Cita cita = SistemaGestion.getInstance().buscarCitaPorId(idCita);
        if (cita != null) {
            Paciente paciente = SistemaGestion.getInstance().buscarPacientePorId(cita.getIdPaciente());
            if (paciente != null) {
                HistorialPaciente historial = new HistorialPaciente(paciente, medicoActual);
                historial.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Paciente nuevo, sin historial previo en sistema.");
            }
        }
    }
    public void refrescarAgenda() {
        cargarCitasDeFechaSeleccionada();
    }
    
    private void listenServer() {
        Thread hiloEscucha = new Thread(() -> {
            try {
                java.net.Socket s = new java.net.Socket("127.0.0.1", 7000);
                java.io.DataOutputStream out = new java.io.DataOutputStream(s.getOutputStream());
                java.io.DataInputStream in = new java.io.DataInputStream(s.getInputStream());
                
                // 1. Identificarse con el ID del Médico actual
                out.writeUTF("LOGIN:" + medicoActual.getId());
                
                // 2. Bucle infinito escuchando al servidor
                while(true) {
                    String mensaje = in.readUTF(); // Se queda esperando aquí
                    
                    if (mensaje.startsWith("ALERTA:")) {
                        String textoAlerta = mensaje.split(":")[1];
                        
                        // Actualizar la UI desde el hilo del socket
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(PrincipalMedico.this, 
                                textoAlerta, 
                                "⏰ RECORDATORIO DE CITA", 
                                JOptionPane.INFORMATION_MESSAGE);
                                
                            // Opcional: Recargar la tabla automáticamente
                            refrescarAgenda(); 
                        });
                    }
                }
            } catch (Exception e) {
                System.out.println("No se pudo conectar al servidor de alertas.");
            }
        });
        hiloEscucha.start();
    }
}