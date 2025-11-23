package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import logico.Consulta;
import logico.Enfermedad;
import logico.SistemaGestion;
import logico.Vacuna;
import logico.Paciente; 
import logico.Medico;   

public class PanelConsultaActiva extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private Consulta consultaActual;
    @SuppressWarnings("unused")
    private PrincipalMedico padre;
    
    private JComboBox<String> cbxDiagnostico;
    private ArrayList<Enfermedad> listaEnfermedades; 
    private JTextArea txtTratamiento;
    private JCheckBox chckbxImportante;
    private JTable tableVacunas;
    private DefaultTableModel modelVacunas;

    private final Color COLOR_THEME = new Color(0, 190, 165); 
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_TEXTO_HEADER = new Color(0, 150, 136); 
    private final Color COLOR_TEXTO = new Color(60, 60, 60);

    @SuppressWarnings("serial")
	public PanelConsultaActiva(Consulta consulta, PrincipalMedico padre) {
        this.consultaActual = consulta;
        this.padre = padre;
        
        setTitle("Consulta Médica en Curso");
        setBounds(100, 100, 1000, 720);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_THEME);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitle = new JLabel("HISTORIA CLÍNICA ACTIVA");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        getContentPane().add(pnlHeader, BorderLayout.NORTH);

        contentPanel.setBackground(COLOR_BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(null);
        panelInfo.setBackground(new Color(245, 250, 250));
        panelInfo.setBorder(new MatteBorder(0, 0, 2, 0, COLOR_THEME)); 
        panelInfo.setBounds(20, 10, 940, 100);
        contentPanel.add(panelInfo);

        JLabel lblNombre = new JLabel(consulta.getPaciente().getNombreCompleto().toUpperCase());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(COLOR_TEXTO_HEADER);
        lblNombre.setBounds(20, 15, 400, 25);
        panelInfo.add(lblNombre);
        
        JLabel lblDatosFijos = new JLabel("EDAD: " + consulta.getPaciente().getEdad() + " AÑOS  |  SANGRE: " + consulta.getPaciente().getTipoSangre());
        lblDatosFijos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDatosFijos.setForeground(Color.GRAY);
        lblDatosFijos.setBounds(20, 45, 400, 20);
        panelInfo.add(lblDatosFijos);
        
        crearBadgeVital(panelInfo, "PESO", consulta.getPeso() + " lb", 500, 25);
        crearBadgeVital(panelInfo, "PRESIÓN", consulta.getPresionArterial(), 600, 25);

        JButton btnHistorial = new JButton("VER HISTORIAL");
        btnHistorial.setBackground(new Color(70, 130, 180)); 
        btnHistorial.setForeground(Color.WHITE);
        btnHistorial.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnHistorial.setFocusPainted(false);
        btnHistorial.setBounds(780, 30, 140, 40);
        
        btnHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirHistorialPaciente();
            }
        });
        panelInfo.add(btnHistorial);

        JPanel panelDiag = new JPanel();
        panelDiag.setBorder(new TitledBorder(new LineBorder(new Color(200, 200, 200)), "Evaluación Médica", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), COLOR_TEXTO_HEADER));
        panelDiag.setBackground(COLOR_BG);
        panelDiag.setBounds(20, 130, 550, 460);
        contentPanel.add(panelDiag);
        panelDiag.setLayout(null);

        JLabel lblMotivo = new JLabel("Motivo Consulta:");
        lblMotivo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMotivo.setForeground(COLOR_TEXTO);
        lblMotivo.setBounds(20, 30, 200, 20);
        panelDiag.add(lblMotivo);
        
        JScrollPane scrollSintomas = new JScrollPane();
        scrollSintomas.setBounds(20, 50, 510, 60);
        scrollSintomas.setBorder(new LineBorder(new Color(230, 230, 230)));
        panelDiag.add(scrollSintomas);
        JTextArea txtSintomas = new JTextArea(consulta.getSintomas());
        txtSintomas.setEditable(false);
        txtSintomas.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        txtSintomas.setLineWrap(true);
        txtSintomas.setBackground(new Color(250, 250, 250));
        scrollSintomas.setViewportView(txtSintomas);

        JLabel lblDiagnostico = new JLabel("Diagnóstico (CIE-10):");
        lblDiagnostico.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDiagnostico.setForeground(COLOR_TEXTO);
        lblDiagnostico.setBounds(20, 125, 200, 20);
        panelDiag.add(lblDiagnostico);
        
        cbxDiagnostico = new JComboBox<>();
        cbxDiagnostico.setBackground(Color.WHITE);
        cbxDiagnostico.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbxDiagnostico.setBounds(20, 145, 510, 30);
        panelDiag.add(cbxDiagnostico);

        JLabel lblTratamiento = new JLabel("Tratamiento / Indicaciones:");
        lblTratamiento.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTratamiento.setForeground(COLOR_TEXTO);
        lblTratamiento.setBounds(20, 190, 200, 20);
        panelDiag.add(lblTratamiento);
        
        txtTratamiento = new JTextArea();
        txtTratamiento.setLineWrap(true);
        txtTratamiento.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollTrat = new JScrollPane(txtTratamiento);
        scrollTrat.setBounds(20, 215, 510, 200);
        panelDiag.add(scrollTrat);
        
        chckbxImportante = new JCheckBox("Marcar como Importante para Resumen");
        chckbxImportante.setBackground(COLOR_BG);
        chckbxImportante.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chckbxImportante.setForeground(COLOR_TEXTO);
        chckbxImportante.setBounds(20, 425, 300, 23);
        panelDiag.add(chckbxImportante);

        JPanel panelVacunas = new JPanel();
        panelVacunas.setBorder(new TitledBorder(new LineBorder(new Color(200, 200, 200)), "Plan de Vacunación", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 14), COLOR_TEXTO_HEADER));
        panelVacunas.setBackground(COLOR_BG);
        panelVacunas.setBounds(590, 130, 370, 460);
        contentPanel.add(panelVacunas);
        panelVacunas.setLayout(new BorderLayout(0, 10));
        
        JLabel lblInstruccionVac = new JLabel("Seleccione las vacunas aplicadas hoy:");
        lblInstruccionVac.setBorder(new EmptyBorder(10, 10, 0, 10));
        lblInstruccionVac.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        panelVacunas.add(lblInstruccionVac, BorderLayout.NORTH);
        
        modelVacunas = new DefaultTableModel(new Object[][] {}, new String[] { "Vacuna", "Aplicar" }) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; 
            }
        };
        
        tableVacunas = new JTable(modelVacunas);
        tableVacunas.setRowHeight(30);
        tableVacunas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableVacunas.setShowVerticalLines(false);
        tableVacunas.setGridColor(new Color(240, 240, 240));

        JTableHeader headerV = tableVacunas.getTableHeader();
        headerV.setBackground(COLOR_THEME);
        headerV.setForeground(Color.WHITE);
        headerV.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        tableVacunas.getColumnModel().getColumn(1).setMaxWidth(60);
        
        JScrollPane scrollVacunas = new JScrollPane(tableVacunas);
        scrollVacunas.setBorder(new EmptyBorder(5, 10, 10, 10));
        scrollVacunas.getViewport().setBackground(Color.WHITE);
        panelVacunas.add(scrollVacunas, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPane.setBackground(COLOR_BG);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton btnFinalizar = new JButton("FINALIZAR CONSULTA");
        btnFinalizar.setBackground(Color.BLACK);
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFinalizar.setFocusPainted(false);
        btnFinalizar.setBorderPainted(false);
        btnFinalizar.setPreferredSize(new java.awt.Dimension(200, 40));
        
        btnFinalizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                finalizarConsulta();
            }
        });
        buttonPane.add(btnFinalizar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(240, 240, 240));
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setPreferredSize(new java.awt.Dimension(100, 40));
        
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnCancelar);
        
        cargarDatosIniciales();
    }

    private void crearBadgeVital(JPanel panel, String titulo, String valor, int x, int y) {
        JPanel badge = new JPanel(new BorderLayout());
        badge.setBackground(Color.WHITE);
        badge.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        badge.setBounds(x, y, 80, 45);
        
        JLabel lblT = new JLabel(titulo, SwingConstants.CENTER);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblT.setForeground(Color.GRAY);
        
        JLabel lblV = new JLabel(valor, SwingConstants.CENTER);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblV.setForeground(new Color(0, 150, 136)); 
        
        badge.add(lblT, BorderLayout.NORTH);
        badge.add(lblV, BorderLayout.CENTER);
        panel.add(badge);
    }
    
    private void cargarDatosIniciales() {
        listaEnfermedades = SistemaGestion.getInstance().getCatalogoEnfermedades();
        cbxDiagnostico.removeAllItems();
        for (Enfermedad enf : listaEnfermedades) {
            cbxDiagnostico.addItem(enf.getNombre());
        }

        ArrayList<Vacuna> catalogo = SistemaGestion.getInstance().getCatalogoVacunas();
        modelVacunas.setRowCount(0);
        
        for (Vacuna v : catalogo) {
            modelVacunas.addRow(new Object[]{v.getNombre(), false});
        }
    }
    
    private void finalizarConsulta() {
        if (cbxDiagnostico.getSelectedIndex() == -1 || txtTratamiento.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe indicar Diagnóstico y Tratamiento.", "Faltan Datos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Enfermedad diagnosticoSeleccionado = listaEnfermedades.get(cbxDiagnostico.getSelectedIndex());
        String tratamiento = txtTratamiento.getText();
        boolean esImportante = chckbxImportante.isSelected();

        ArrayList<Vacuna> vacunasAplicadasHoy = new ArrayList<>();
        ArrayList<Vacuna> catalogo = SistemaGestion.getInstance().getCatalogoVacunas();
        
        for (int i = 0; i < modelVacunas.getRowCount(); i++) {
            Boolean isChecked = (Boolean) modelVacunas.getValueAt(i, 1);
            if (isChecked) {
                vacunasAplicadasHoy.add(catalogo.get(i));
            }
        }
        boolean exito = SistemaGestion.getInstance().finalizarConsulta(
            consultaActual.getId(), 
            diagnosticoSeleccionado, 
            tratamiento, 
            esImportante, 
            vacunasAplicadasHoy
        );
        
        if (exito) {
            JOptionPane.showMessageDialog(this, "Consulta finalizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirHistorialPaciente() {
        Paciente paciente = consultaActual.getPaciente();
        Medico medico = consultaActual.getMedico();
        
        HistorialPaciente dialogHistorial = new HistorialPaciente(paciente, medico);
        dialogHistorial.setModal(true);
        dialogHistorial.setVisible(true);
    }
}