package visual;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import logico.Cita;
import logico.Medico;
import logico.SistemaGestion;

@SuppressWarnings("serial")
public class CalendarioSecretaria extends JPanel {

	private JPanel panelDias;
    private JLabel lblMesAnio;
    private YearMonth mesActual;
    
    private final Color COLOR_FONDO = new Color(254, 251, 246); 
    private final Color COLOR_VERDE = new Color(0, 200, 151);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);
    private final Color COLOR_HEADER_BG = new Color(30, 30, 30);
    private final Color COLOR_PASADO = new Color(210, 210, 210);
    private final Color COLOR_FUTURO = new Color(250, 250, 250);

    public CalendarioSecretaria() {
        setLayout(new BorderLayout(20, 0));
        setBackground(COLOR_FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel panelLateral = new JPanel();
        panelLateral.setPreferredSize(new Dimension(220, 0));
        panelLateral.setBackground(COLOR_FONDO);
        panelLateral.setLayout(null);
        add(panelLateral, BorderLayout.WEST);
        
        JLabel lblRol = new JLabel("ROL SECRETARIA");
        lblRol.setHorizontalAlignment(SwingConstants.CENTER);
        lblRol.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRol.setForeground(COLOR_TEXTO);
        lblRol.setBorder(new LineBorder(Color.BLACK, 1));
        lblRol.setBounds(0, 0, 220, 50);
        panelLateral.add(lblRol);
        
        JPanel panelMedicos = new JPanel();
        panelMedicos.setLayout(null);
        panelMedicos.setBackground(COLOR_FONDO);
        panelMedicos.setBorder(new LineBorder(Color.BLACK, 1));
        panelMedicos.setBounds(0, 70, 220, 300);
        panelLateral.add(panelMedicos);
        
        JLabel lblTituloMedicos = new JLabel("<html><center>MÉDICOS<br>DISPONIBLES</center></html>");
        lblTituloMedicos.setHorizontalAlignment(SwingConstants.CENTER);
        lblTituloMedicos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloMedicos.setBounds(10, 10, 200, 40);
        panelMedicos.add(lblTituloMedicos);
        
        int yPos = 60;
        for(Medico m : SistemaGestion.getInstance().getListaMedicos()) {
            JLabel lblMed = new JLabel("• " + m.getName() + " " + m.getApellido());
            lblMed.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblMed.setForeground(COLOR_TEXTO);
            lblMed.setBounds(15, yPos, 190, 20);
            panelMedicos.add(lblMed);
            yPos += 25;
        }
        
        JButton btnBuscarPaciente = new JButton("BUSCAR PACIENTE");
        btnBuscarPaciente.setBackground(COLOR_VERDE);
        btnBuscarPaciente.setForeground(Color.WHITE);
        btnBuscarPaciente.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscarPaciente.setFocusPainted(false);
        btnBuscarPaciente.setBorderPainted(false);
        btnBuscarPaciente.setBounds(0, 450, 220, 50); 
        panelLateral.add(btnBuscarPaciente);

        JPanel panelCalendarioContenedor = new JPanel();
        panelCalendarioContenedor.setLayout(new BorderLayout(0, 10));
        panelCalendarioContenedor.setBackground(COLOR_FONDO);
        add(panelCalendarioContenedor, BorderLayout.CENTER);

        lblMesAnio = new JLabel();
        lblMesAnio.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblMesAnio.setForeground(COLOR_TEXTO);
        panelCalendarioContenedor.add(lblMesAnio, BorderLayout.NORTH);

        panelDias = new JPanel();
        panelDias.setBackground(Color.WHITE);
        panelDias.setBorder(new LineBorder(Color.BLACK, 1));
        panelDias.setLayout(new GridLayout(0, 7)); 
        panelCalendarioContenedor.add(panelDias, BorderLayout.CENTER);

        mesActual = YearMonth.now();
        construirCalendario(mesActual);
    }

    private void construirCalendario(YearMonth mes) {
        panelDias.removeAll(); 

        String mesNombre = mes.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        lblMesAnio.setText((mesNombre + " " + mes.getYear()).toUpperCase());

        String[] diasSemana = {"LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO", "DOMINGO"};
        for (String dia : diasSemana) {
            JLabel lbl = new JLabel(dia, SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setBackground(COLOR_HEADER_BG);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setBorder(new LineBorder(Color.WHITE, 1));
            panelDias.add(lbl);
        }

        LocalDate primeroDelMes = mes.atDay(1);
        int diaSemanaInicio = primeroDelMes.getDayOfWeek().getValue(); 
        
        for (int i = 1; i < diaSemanaInicio; i++) {
            JPanel vacio = new JPanel();
            vacio.setBackground(Color.WHITE);
            vacio.setBorder(new LineBorder(new Color(230, 230, 230), 1));
            panelDias.add(vacio);
        }

        int diasEnMes = mes.lengthOfMonth();
        LocalDate hoy = LocalDate.now();

        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fechaCelda = mes.atDay(dia);
            
            JPanel celda = new JPanel();
            celda.setLayout(new BorderLayout());
            celda.setBorder(new LineBorder(Color.BLACK, 1)); 
            
            JLabel lblDia = new JLabel(" " + dia);
            lblDia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblDia.setForeground(COLOR_TEXTO);
            lblDia.setBorder(new EmptyBorder(5, 5, 0, 0));
            celda.add(lblDia, BorderLayout.NORTH);

            ArrayList<Cita> citasDelDia = buscarCitasEnFecha(fechaCelda);
            boolean tieneCitas = !citasDelDia.isEmpty();

            if (tieneCitas) {
                celda.setBackground(COLOR_VERDE);
                lblDia.setForeground(Color.WHITE);
                
                String textoCitas = "<html><div style='padding:2px;text-align:center'>";
                for(Cita c : citasDelDia) {
                    textoCitas += "<br><b>" + c.getId() + "</b>";
                }
                textoCitas += "</div></html>";
                JLabel lblInfo = new JLabel(textoCitas, SwingConstants.CENTER);
                lblInfo.setForeground(Color.WHITE);
                lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                celda.add(lblInfo, BorderLayout.CENTER);
            } else {
                if (fechaCelda.isBefore(hoy)) {
                    celda.setBackground(COLOR_PASADO); 
                } else {
                    celda.setBackground(COLOR_FUTURO); 
                }
            }

            if (!fechaCelda.isBefore(hoy)) {
                celda.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        abrirOpcionesDia(fechaCelda, tieneCitas);
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
                encontradas.add(c);
            }
        }
        return encontradas;
    }

    private void abrirOpcionesDia(LocalDate fecha, boolean tieneCitaPrevia) {
        FormOpcionesDia dialog = new FormOpcionesDia(fecha, tieneCitaPrevia, this);
        dialog.setVisible(true);
    }
    
    public void refrescarCalendario() {
        construirCalendario(mesActual);
    }
}