package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
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
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import ds.desktop.notify.DesktopNotify;
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
    private JSpinner spnFechaAgenda;
    private JButton btnAlertaCitasFuturas;
    private JLabel lblIcono;
    private JLabel lblDetalleNombre;
    private JLabel lblDetalleHora;
    private JButton btnIniciarConsulta;
    private JButton btnVerHistorial;

    private final Color COLOR_THEME = new Color(0, 51, 102); 
    private final Color COLOR_BG_MAIN = Color.WHITE;
    private final Color COLOR_TEXT_BODY = new Color(60, 60, 60);
    private final Color COLOR_LOGOUT = new Color(255, 85, 85);
    private final Color COLOR_ALERTA = new Color(255, 140, 0); 
    private final Color COLOR_BTN_BLUE = new Color(13, 110, 253); 

    public PrincipalMedico(Usuario user) {
        this.medicoActual = SistemaGestion.getInstance().getMedicoLogueado(user);

        setTitle("Panel Médico - Dr. " + (medicoActual != null ? medicoActual.getApellido() : "N/A"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1200, 750);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 242, 245)); 
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        if (medicoActual == null) {
            JOptionPane.showMessageDialog(this, "Error: Usuario no vinculado a un Médico.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JPanel panelAgenda = new JPanel();
        panelAgenda.setPreferredSize(new Dimension(420, 0));
        panelAgenda.setLayout(new BorderLayout(0, 0));
        panelAgenda.setBackground(COLOR_THEME);
        contentPane.add(panelAgenda, BorderLayout.WEST);

        JPanel pnlHeaderAgenda = new JPanel(new BorderLayout(0, 10));
        pnlHeaderAgenda.setBackground(COLOR_THEME);
        pnlHeaderAgenda.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel lblTituloAgenda = new JLabel("MI AGENDA");
        lblTituloAgenda.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloAgenda.setForeground(Color.WHITE);
        pnlHeaderAgenda.add(lblTituloAgenda, BorderLayout.NORTH);

        JPanel pnlControlesSup = new JPanel(new BorderLayout(10, 5));
        pnlControlesSup.setOpaque(false);

        JPanel pnlFecha = new JPanel(new BorderLayout(5,0));
        pnlFecha.setOpaque(false);
        JLabel lblVer = new JLabel("Ver fecha:");
        lblVer.setForeground(Color.WHITE);
        lblVer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pnlFecha.add(lblVer, BorderLayout.WEST);
        
        spnFechaAgenda = new JSpinner(new SpinnerDateModel());
        spnFechaAgenda.setEditor(new JSpinner.DateEditor(spnFechaAgenda, "dd/MM/yyyy"));
        spnFechaAgenda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spnFechaAgenda.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        spnFechaAgenda.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                cargarCitasDeFechaSeleccionada();
            }
        });
        pnlFecha.add(spnFechaAgenda, BorderLayout.CENTER);
        pnlControlesSup.add(pnlFecha, BorderLayout.NORTH);

        btnAlertaCitasFuturas = new JButton("!!! VER CITAS PENDIENTES");
        estilizarBoton(btnAlertaCitasFuturas, COLOR_ALERTA);
        btnAlertaCitasFuturas.setVisible(false); 
        btnAlertaCitasFuturas.addActionListener(e -> irAProximaCitaFutura());
        pnlControlesSup.add(btnAlertaCitasFuturas, BorderLayout.SOUTH);
        
        pnlHeaderAgenda.add(pnlControlesSup, BorderLayout.CENTER);
        panelAgenda.add(pnlHeaderAgenda, BorderLayout.NORTH);

        String[] headers = {"Hora", "Paciente", "Estado", "ID"};
        modelAgenda = new DefaultTableModel(null, headers) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tableAgenda = new JTable(modelAgenda);
        tableAgenda.setRowHeight(40);
        tableAgenda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableAgenda.setShowVerticalLines(false);
        tableAgenda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableAgenda.getColumnModel().getColumn(3).setMinWidth(0);
        tableAgenda.getColumnModel().getColumn(3).setMaxWidth(0);
        tableAgenda.getColumnModel().getColumn(3).setWidth(0);

        JTableHeader header = tableAgenda.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(240,240,240));
        header.setForeground(COLOR_TEXT_BODY);
        
        JScrollPane scrollAgenda = new JScrollPane(tableAgenda);
        scrollAgenda.setBorder(null);
        panelAgenda.add(scrollAgenda, BorderLayout.CENTER);

        JPanel pnlBotonesSide = new JPanel(new BorderLayout(0, 10));
        pnlBotonesSide.setBackground(COLOR_THEME);
        pnlBotonesSide.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JButton btnGestionAgenda = new JButton("GESTIONAR MI HORARIO");
        estilizarBoton(btnGestionAgenda, COLOR_BTN_BLUE);
        btnGestionAgenda.addActionListener(e -> abrirGestionAgenda());
        pnlBotonesSide.add(btnGestionAgenda, BorderLayout.NORTH);

        JButton btnCerrarSesion = new JButton("CERRAR SESIÓN");
        estilizarBoton(btnCerrarSesion, COLOR_LOGOUT);
        btnCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea salir?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                Login login = new Login();
                login.setVisible(true);
            }
        });
        pnlBotonesSide.add(btnCerrarSesion, BorderLayout.SOUTH);
        
        panelAgenda.add(pnlBotonesSide, BorderLayout.SOUTH);

        tableAgenda.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tableAgenda.getSelectedRow() != -1) {
                actualizarPanelDetalle();
            }
        });

        JPanel panelDetalle = new JPanel();
        panelDetalle.setBackground(COLOR_BG_MAIN);
        panelDetalle.setLayout(new BorderLayout());
        panelDetalle.setBorder(new MatteBorder(0, 1, 0, 0, new Color(220, 220, 220)));
        contentPane.add(panelDetalle, BorderLayout.CENTER);
        
        JLabel lblTituloDetalle = new JLabel("DETALLE DE LA CITA");
        lblTituloDetalle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTituloDetalle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloDetalle.setForeground(COLOR_THEME);
        lblTituloDetalle.setBorder(new EmptyBorder(40, 0, 0, 0));
        panelDetalle.add(lblTituloDetalle, BorderLayout.NORTH);

        JPanel pnlCentro = new JPanel(new GridBagLayout());
        pnlCentro.setBackground(new Color(240, 242, 245));
        pnlCentro.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        pnlCentro.setPreferredSize(new Dimension(600, 500));
        panelDetalle.add(pnlCentro);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        lblIcono = new JLabel(); 
        lblIcono.setPreferredSize(new Dimension(120, 120));
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);

        String rutaImg = "recursos/Paciente.png"; 
        File archivoImg = new File(rutaImg);
        if (archivoImg.exists()) {
            cargarImagen(lblIcono, rutaImg);
        } else {
            lblIcono.setText("<html><div style='font-size:50px'>👤</div></html>");
            lblIcono.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
        }
        pnlCentro.add(lblIcono, gbc);

        JLabel lblSeleccione = new JLabel("Paciente seleccionado:");
        lblSeleccione.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSeleccione.setForeground(Color.GRAY);
        pnlCentro.add(lblSeleccione, gbc);

        lblDetalleNombre = new JLabel("---");
        lblDetalleNombre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblDetalleNombre.setForeground(COLOR_TEXT_BODY);
        pnlCentro.add(lblDetalleNombre, gbc);

        lblDetalleHora = new JLabel("--:--");
        lblDetalleHora.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblDetalleHora.setForeground(Color.GRAY);
        pnlCentro.add(lblDetalleHora, gbc);
        
        gbc.insets = new Insets(40, 0, 15, 0); 

        btnIniciarConsulta = new JButton("INICIAR CONSULTA");
        estilizarBoton(btnIniciarConsulta, COLOR_THEME);
        btnIniciarConsulta.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnIniciarConsulta.setPreferredSize(new Dimension(350, 55));
        btnIniciarConsulta.setEnabled(false);
        btnIniciarConsulta.addActionListener(e -> abrirTriaje());
        pnlCentro.add(btnIniciarConsulta, gbc);
        
        gbc.insets = new Insets(5, 0, 10, 0);

        btnVerHistorial = new JButton("Ver Historial Clínico");
        btnVerHistorial.setBackground(COLOR_BG_MAIN);
        btnVerHistorial.setForeground(COLOR_THEME);
        btnVerHistorial.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVerHistorial.setFocusPainted(false);
        btnVerHistorial.setBorder(new LineBorder(COLOR_THEME, 2));
        btnVerHistorial.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVerHistorial.setPreferredSize(new Dimension(350, 45));
        btnVerHistorial.setEnabled(false);
        btnVerHistorial.addActionListener(e -> verHistorial());

        btnVerHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if(btnVerHistorial.isEnabled()) {
                    btnVerHistorial.setBackground(COLOR_THEME);
                    btnVerHistorial.setForeground(Color.WHITE);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if(btnVerHistorial.isEnabled()) {
                    btnVerHistorial.setBackground(COLOR_BG_MAIN);
                    btnVerHistorial.setForeground(COLOR_THEME);
                }
            }
        });
        pnlCentro.add(btnVerHistorial, gbc);

        cargarCitasDeFechaSeleccionada();
        verificarCitasFuturas();
        listenServer();
    }

    private void estilizarBoton(JButton btn, Color colorBg) {
        btn.setBackground(colorBg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void cargarImagen(JLabel lbl, String ruta) {
        ImageIcon iconOriginal = new ImageIcon(ruta);
        Image imgEscalada = iconOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        lbl.setIcon(new ImageIcon(imgEscalada));
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
        lblDetalleNombre.setText("---");
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
            lblDetalleHora.setText("Hora: " + hora + "  |  " + estado);
            
            boolean esPendiente = estado.equalsIgnoreCase("PENDIENTE");
            btnIniciarConsulta.setEnabled(esPendiente);
            
            if(esPendiente) {
                btnIniciarConsulta.setBackground(COLOR_THEME);
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
        if(row == -1) 
        	return;
        String idCita = (String) modelAgenda.getValueAt(row, 3); 
        FormTriaje triaje = new FormTriaje(idCita, this);
        triaje.setVisible(true);
    }
    
    private void verHistorial() {
        int row = tableAgenda.getSelectedRow();
        if(row == -1) 
        	return;
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
                @SuppressWarnings("resource")
                java.net.Socket s = new java.net.Socket("127.0.0.1", 7000);
                java.io.DataOutputStream out = new java.io.DataOutputStream(s.getOutputStream());
                java.io.DataInputStream in = new java.io.DataInputStream(s.getInputStream());
                
                out.writeUTF("LOGIN:" + medicoActual.getId());
                
                while(true) {
                    String mensaje = in.readUTF();
                    if (mensaje.startsWith("RECORDATORIO:")) {
                        String textoAlerta = mensaje.split(":")[1];
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            DesktopNotify.showDesktopMessage(
                                "Recordatorio de Agenda", 
                                textoAlerta, 
                                DesktopNotify.INFORMATION,
                                8000L
                            );                                  
                            java.awt.Toolkit.getDefaultToolkit().beep();
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