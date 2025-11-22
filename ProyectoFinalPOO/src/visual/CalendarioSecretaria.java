package visual;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFrame;

import logico.Cita;
import logico.Medico;
import logico.Secretaria;
import logico.SistemaGestion;

@SuppressWarnings("serial")
public class CalendarioSecretaria extends JPanel {

    private JPanel panelDias;
    private JLabel lblMesAnio;
    private YearMonth mesActual;
    private Secretaria secretariaLogueada;

    private final Color COLOR_TEAL_PRINCIPAL = new Color(10, 186, 181);
    private final Color COLOR_FONDO_BLANCO   = Color.WHITE;
    private final Color COLOR_TEXTO_OSCURO   = new Color(50, 50, 50);
    private final Color COLOR_TEXTO_BLANCO   = Color.WHITE;
    private final Color COLOR_ROJO_CERRAR    = new Color(255, 80, 80);
    private final Color COLOR_CELDA_HOY      = new Color(220, 245, 244); 
    private final Color COLOR_CELDA_CITA     = new Color(0, 200, 151);
    private final Color COLOR_CELDA_PASADO   = new Color(245, 245, 245); 

    public CalendarioSecretaria(Secretaria secre) {
        this.secretariaLogueada = secre;
        
        setLayout(new BorderLayout(0, 0));
        setBackground(COLOR_FONDO_BLANCO);

        JPanel panelLateral = new JPanel();
        panelLateral.setPreferredSize(new Dimension(240, 0)); 
        panelLateral.setBackground(COLOR_TEAL_PRINCIPAL);
        panelLateral.setLayout(null);
        add(panelLateral, BorderLayout.WEST);
        
        JLabel lblRol = new JLabel("SECRETARIA");
        lblRol.setHorizontalAlignment(SwingConstants.CENTER);
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblRol.setForeground(COLOR_TEXTO_BLANCO);
        lblRol.setBounds(0, 30, 240, 30);
        panelLateral.add(lblRol);
        
        JLabel lblNombreSec = new JLabel(secre != null ? secre.getName() : "");
        lblNombreSec.setHorizontalAlignment(SwingConstants.CENTER);
        lblNombreSec.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNombreSec.setForeground(new Color(200, 255, 250)); 
        lblNombreSec.setBounds(0, 60, 240, 20);
        panelLateral.add(lblNombreSec);
        
        JLabel lblTituloMedicos = new JLabel("MIS MÉDICOS ASIGNADOS");
        lblTituloMedicos.setHorizontalAlignment(SwingConstants.LEFT);
        lblTituloMedicos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTituloMedicos.setForeground(COLOR_TEXTO_BLANCO);
        lblTituloMedicos.setBounds(20, 110, 200, 20);
        panelLateral.add(lblTituloMedicos);
        
        JPanel panelListaMedicos = new JPanel();
        panelListaMedicos.setLayout(null);
        panelListaMedicos.setBackground(new Color(255, 255, 255, 30)); 
        panelListaMedicos.setBounds(20, 140, 200, 300);
        panelLateral.add(panelListaMedicos);
        
        int yPos = 15;
        ArrayList<Medico> misMedicos = (secre != null) ? secre.getMedicosAsignados() : new ArrayList<>();
        
        if(misMedicos.isEmpty()) {
            JLabel lblVacio = new JLabel("<html>Sin médicos asignados.<br>Contacte al Admin.</html>");
            lblVacio.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblVacio.setForeground(new Color(230, 230, 230));
            lblVacio.setBounds(15, 15, 180, 40);
            panelListaMedicos.add(lblVacio);
        } else {
            for(Medico m : misMedicos) {
                JLabel lblMed = new JLabel("- Dr. " + m.getApellido());
                lblMed.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                lblMed.setForeground(COLOR_TEXTO_BLANCO);
                lblMed.setBounds(15, yPos, 180, 20);
                panelListaMedicos.add(lblMed);
                yPos += 30;
            }
        }
        
        JButton btnBuscarPaciente = new JButton("BUSCAR PACIENTE");
        btnBuscarPaciente.setBackground(COLOR_CELDA_CITA); 
        btnBuscarPaciente.setForeground(COLOR_TEXTO_BLANCO);
        btnBuscarPaciente.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscarPaciente.setFocusPainted(false);
        btnBuscarPaciente.setBorderPainted(false); 
        btnBuscarPaciente.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBuscarPaciente.setBounds(20, 550, 200, 40);
        
        btnBuscarPaciente.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnBuscarPaciente.setBackground(Color.WHITE);
                btnBuscarPaciente.setForeground(COLOR_CELDA_CITA);
            }
            public void mouseExited(MouseEvent e) {
                btnBuscarPaciente.setBackground(COLOR_CELDA_CITA);
                btnBuscarPaciente.setForeground(COLOR_TEXTO_BLANCO);
            }
        });
        
        btnBuscarPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FormBuscarPaciente buscador = new FormBuscarPaciente();
                buscador.setVisible(true);
                refrescarCalendario();
            }
        });
        panelLateral.add(btnBuscarPaciente);
        
        JButton btnCerrarSesion = new JButton("CERRAR SESIÓN");
        btnCerrarSesion.setBackground(COLOR_ROJO_CERRAR); 
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorderPainted(false);
        btnCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrarSesion.setBounds(20, 600, 200, 40);
        
        btnCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
        panelLateral.add(btnCerrarSesion);

        JPanel panelCalendarioContenedor = new JPanel();
        panelCalendarioContenedor.setLayout(new BorderLayout(0, 20));
        panelCalendarioContenedor.setBackground(COLOR_FONDO_BLANCO);
        panelCalendarioContenedor.setBorder(new EmptyBorder(30, 30, 30, 30)); 
        add(panelCalendarioContenedor, BorderLayout.CENTER);

        JPanel pnlHeaderCal = new JPanel(new BorderLayout());
        pnlHeaderCal.setBackground(COLOR_FONDO_BLANCO);
        
        lblMesAnio = new JLabel();
        lblMesAnio.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblMesAnio.setForeground(COLOR_TEAL_PRINCIPAL); 
        pnlHeaderCal.add(lblMesAnio, BorderLayout.WEST);
        
        JPanel pnlNav = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlNav.setBackground(COLOR_FONDO_BLANCO);
        
        JButton btnAnt = createNavButton("<"); 
        JButton btnSig = createNavButton(">");
        
        btnAnt.addActionListener(e -> { 
            mesActual = mesActual.minusMonths(1); 
            construirCalendario(mesActual); 
        });
        btnSig.addActionListener(e -> { 
            mesActual = mesActual.plusMonths(1); 
            construirCalendario(mesActual); 
        });
        
        pnlNav.add(btnAnt);
        pnlNav.add(btnSig);
        pnlHeaderCal.add(pnlNav, BorderLayout.EAST);
        
        panelCalendarioContenedor.add(pnlHeaderCal, BorderLayout.NORTH);

        panelDias = new JPanel();
        panelDias.setBackground(COLOR_FONDO_BLANCO);
        panelDias.setLayout(new GridLayout(0, 7, 5, 5)); 
        panelCalendarioContenedor.add(panelDias, BorderLayout.CENTER);

        mesActual = YearMonth.now();
        construirCalendario(mesActual);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24)); 
        btn.setForeground(COLOR_TEAL_PRINCIPAL);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new LineBorder(COLOR_TEAL_PRINCIPAL, 1));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(50, 35)); 
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_TEAL_PRINCIPAL);
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(COLOR_TEAL_PRINCIPAL);
            }
        });
        return btn;
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) topFrame.dispose();
            Login login = new Login();
            login.setVisible(true);
        }
    }

    private void construirCalendario(YearMonth mes) {
        panelDias.removeAll(); 
        String mesNombre = mes.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        lblMesAnio.setText((mesNombre + " " + mes.getYear()).toUpperCase());

        String[] diasSemana = {"LUN", "MAR", "MIE", "JUE", "VIE", "SAB", "DOM"}; 
        for (String dia : diasSemana) {
            JLabel lbl = new JLabel(dia, SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setBackground(COLOR_TEAL_PRINCIPAL); 
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panelDias.add(lbl);
        }

        LocalDate primeroDelMes = mes.atDay(1);
        int diaSemanaInicio = primeroDelMes.getDayOfWeek().getValue(); 
        
        for (int i = 1; i < diaSemanaInicio; i++) {
            JPanel vacio = new JPanel();
            vacio.setBackground(COLOR_FONDO_BLANCO);
            panelDias.add(vacio);
        }

        int diasEnMes = mes.lengthOfMonth();
        LocalDate hoy = LocalDate.now();

        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fechaCelda = mes.atDay(dia);
            JPanel celda = new JPanel();
            celda.setLayout(new BorderLayout());
            celda.setBackground(Color.WHITE);
            celda.setBorder(new LineBorder(new Color(230, 230, 230), 1));
            
            JLabel lblDia = new JLabel(" " + dia);
            lblDia.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblDia.setForeground(COLOR_TEXTO_OSCURO);
            lblDia.setBorder(new EmptyBorder(5, 5, 0, 0));
            celda.add(lblDia, BorderLayout.NORTH);

            if (fechaCelda.equals(hoy)) {
                celda.setBackground(COLOR_CELDA_HOY); 
                lblDia.setForeground(COLOR_TEAL_PRINCIPAL);
                lblDia.setText(" " + dia + " (Hoy)");
            }

            ArrayList<Cita> citasDelDia = buscarCitasEnFecha(fechaCelda);
            boolean tieneCitas = !citasDelDia.isEmpty();

            if (tieneCitas) {
                JPanel indicador = new JPanel();
                indicador.setBackground(COLOR_CELDA_CITA);
                indicador.setPreferredSize(new Dimension(5, 0));
                celda.add(indicador, BorderLayout.WEST);
                
                int contadorMios = 0;
                for(Cita c : citasDelDia) {
                    if(secretariaLogueada != null && secretariaLogueada.getMedicosAsignados().contains(c.getMedico())) {
                        contadorMios++;
                    } else if (secretariaLogueada == null) {
                        contadorMios++;
                    }
                }
                
                if(contadorMios > 0) {
                    String textoInfo = "<html><div style='padding:5px; color:#00a86b'><b>" + contadorMios + " Citas</b></div></html>";
                    JLabel lblInfo = new JLabel(textoInfo, SwingConstants.CENTER);
                    lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    celda.add(lblInfo, BorderLayout.CENTER);
                }
            }

            if (fechaCelda.isBefore(hoy)) {
                celda.setBackground(COLOR_CELDA_PASADO);
                lblDia.setForeground(Color.GRAY);
            }

            if (!fechaCelda.isBefore(hoy)) {
                celda.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                celda.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        abrirOpcionesDia(fechaCelda, tieneCitas);
                    }
                    public void mouseEntered(MouseEvent e) {
                        if(!fechaCelda.equals(hoy)) celda.setBackground(new Color(240, 255, 250));
                    }
                    public void mouseExited(MouseEvent e) {
                        if(!fechaCelda.equals(hoy)) celda.setBackground(Color.WHITE);
                    }
                });
            }
            panelDias.add(celda);
        }
        panelDias.revalidate();
        panelDias.repaint();
    }

    private ArrayList<Cita> buscarCitasEnFecha(LocalDate fecha) {
        ArrayList<Cita> encontradas = new ArrayList<>();
        for (Cita c : SistemaGestion.getInstance().getListaCitas()) {
            if (c.getFechaCitada().toLocalDate().equals(fecha) && !c.getEstado().equalsIgnoreCase("Cancelada")) {
                if(secretariaLogueada == null || secretariaLogueada.getMedicosAsignados().contains(c.getMedico())) {
                    encontradas.add(c);
                }
            }
        }
        return encontradas;
    }

    private void abrirOpcionesDia(LocalDate fecha, boolean tieneCitaPrevia) {
        FormOpcionesDia dialog = new FormOpcionesDia(fecha, tieneCitaPrevia, this, secretariaLogueada);
        dialog.setVisible(true);
        refrescarCalendario();
    }
    
    public void refrescarCalendario() {
        construirCalendario(mesActual);
    }
}