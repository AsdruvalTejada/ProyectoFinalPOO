package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingConstants;

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
    private PrincipalMedico padre;

    private JComboBox<String> cbxDiagnostico;
    private ArrayList<Enfermedad> listaEnfermedades;
    private JTextArea txtTratamiento;
    private JCheckBox chckbxImportante;
    private JTable tableVacunas;
    private DefaultTableModel modelVacunas;

    private final Color COLOR_FONDO = new Color(254, 251, 246);
    private final Color COLOR_VERDE = new Color(0, 200, 151);

    public PanelConsultaActiva(Consulta consulta, PrincipalMedico padre) {
        this.consultaActual = consulta;
        this.padre = padre;
        
        setTitle("Consulta Médica en Curso - " + consulta.getPaciente().getNombreCompleto());
        setBounds(100, 100, 900, 700);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(new TitledBorder(new LineBorder(new Color(192, 192, 192)), "Información del Paciente y Signos Vitales", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBounds(10, 10, 864, 120);
        contentPanel.add(panelInfo);
        panelInfo.setLayout(null);
        
        JLabel lblNombre = new JLabel("Paciente: " + consulta.getPaciente().getNombreCompleto());
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setBounds(20, 25, 300, 20);
        panelInfo.add(lblNombre);
        
        JLabel lblEdad = new JLabel("Edad: " + consulta.getPaciente().getEdad() + " años");
        lblEdad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEdad.setBounds(20, 50, 150, 20);
        panelInfo.add(lblEdad);
        
        JLabel lblSangre = new JLabel("Tipo Sangre: " + consulta.getPaciente().getTipoSangre());
        lblSangre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSangre.setBounds(180, 50, 150, 20);
        panelInfo.add(lblSangre);
        
        JButton btnVerHistorial = new JButton("Ver Historial Clínico");
        btnVerHistorial.setBackground(COLOR_VERDE);
        btnVerHistorial.setForeground(Color.WHITE);
        btnVerHistorial.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVerHistorial.setFocusPainted(false);

        btnVerHistorial.setBounds(650, 80, 180, 30); 
        
        btnVerHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirHistorialPaciente();
            }
        });
        
        panelInfo.add(btnVerHistorial);

        JLabel lblPeso = new JLabel("Peso: " + consulta.getPeso() + " lb");
        lblPeso.setForeground(new Color(0, 128, 0));
        lblPeso.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPeso.setBounds(350, 25, 120, 20);
        panelInfo.add(lblPeso);
        
        JLabel lblPresion = new JLabel("Presión: " + consulta.getPresionArterial());
        lblPresion.setForeground(new Color(0, 128, 0));
        lblPresion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPresion.setBounds(350, 50, 150, 20);
        panelInfo.add(lblPresion);
        
        JLabel lblSintomasTitulo = new JLabel("Motivo/Síntomas:");
        lblSintomasTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSintomasTitulo.setBounds(500, 25, 120, 20);
        panelInfo.add(lblSintomasTitulo);
        
        JTextArea txtSintomasRead = new JTextArea();
        txtSintomasRead.setText(consulta.getSintomas());
        txtSintomasRead.setEditable(false);
        txtSintomasRead.setBackground(new Color(245, 245, 245));
        txtSintomasRead.setLineWrap(true);
        JScrollPane scrollSintomas = new JScrollPane(txtSintomasRead);
        scrollSintomas.setBounds(500, 50, 340, 55);
        panelInfo.add(scrollSintomas);

        JPanel panelDiag = new JPanel();
        panelDiag.setBorder(new TitledBorder(null, "Diagnóstico y Tratamiento", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelDiag.setBackground(COLOR_FONDO);
        panelDiag.setBounds(10, 140, 500, 450);
        contentPanel.add(panelDiag);
        panelDiag.setLayout(null);
        
        JLabel lblEnfermedad = new JLabel("Enfermedad Diagnosticada:");
        lblEnfermedad.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEnfermedad.setBounds(20, 30, 200, 20);
        panelDiag.add(lblEnfermedad);
        
        cbxDiagnostico = new JComboBox<>();
        cbxDiagnostico.setBounds(20, 55, 450, 30);
        panelDiag.add(cbxDiagnostico);
        
        JLabel lblTratamiento = new JLabel("Tratamiento / Receta:");
        lblTratamiento.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTratamiento.setBounds(20, 100, 200, 20);
        panelDiag.add(lblTratamiento);
        
        txtTratamiento = new JTextArea();
        txtTratamiento.setLineWrap(true);
        JScrollPane scrollTrat = new JScrollPane(txtTratamiento);
        scrollTrat.setBounds(20, 125, 450, 250);
        panelDiag.add(scrollTrat);
        
        chckbxImportante = new JCheckBox("Marcar como Importante para Resumen");
        chckbxImportante.setBackground(COLOR_FONDO);
        chckbxImportante.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chckbxImportante.setBounds(20, 400, 300, 23);
        panelDiag.add(chckbxImportante);

        JPanel panelVacunas = new JPanel();
        panelVacunas.setBorder(new TitledBorder(null, "Registro de Vacunación", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelVacunas.setBackground(COLOR_FONDO);
        panelVacunas.setBounds(520, 140, 354, 450);
        contentPanel.add(panelVacunas);
        panelVacunas.setLayout(new BorderLayout(0, 0));

        modelVacunas = new DefaultTableModel(new Object[][] {}, new String[] { "Vacuna", "Aplicar Ahora" }) {
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
        tableVacunas.setRowHeight(25);
        tableVacunas.getColumnModel().getColumn(1).setPreferredWidth(80);
        tableVacunas.getColumnModel().getColumn(1).setMaxWidth(80);
        
        JScrollPane scrollVacunas = new JScrollPane(tableVacunas);
        panelVacunas.add(scrollVacunas, BorderLayout.CENTER);
        
        JLabel lblInfoVac = new JLabel("<html><center>Marque las vacunas que se aplicaron<br>HOY al paciente.</center></html>");
        lblInfoVac.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfoVac.setBorder(new EmptyBorder(5, 5, 5, 5));
        panelVacunas.add(lblInfoVac, BorderLayout.SOUTH);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(COLOR_FONDO);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton btnFinalizar = new JButton("FINALIZAR CONSULTA");
        btnFinalizar.setBackground(Color.BLACK);
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFinalizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                finalizarConsulta();
            }
        });
        buttonPane.add(btnFinalizar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnCancelar);
        
        cargarDatosIniciales();
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
            javax.swing.JOptionPane.showMessageDialog(this, "Debe seleccionar un diagnóstico y escribir un tratamiento.");
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
            javax.swing.JOptionPane.showMessageDialog(this, "Consulta finalizada correctamente.", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar los datos.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
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