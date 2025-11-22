package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import logico.Cita;
import logico.Consulta;
import logico.Paciente;
import logico.SistemaGestion;

public class FormTriaje extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private PrincipalMedico padre;
    private String idCita;
    private boolean esPacienteNuevo = false;

    // Componentes
    private JSpinner spnPeso;
    private JTextField txtPresion;
    private JTextArea txtSintomas;
    private JPanel panelDatosPersonales;
    private JTextField txtApellido;
    private JSpinner dateNacimiento; 
    private JComboBox<String> cbxSexo;
    private JTextField txtContacto;
    private JComboBox<String> cbxTipoSangre;
    private JSpinner spnEstatura;
    
    // --- PALETA DE COLORES ---
    private final Color COLOR_THEME = new Color(0, 190, 165); // Turquesa
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    private final Color COLOR_BORDER = new Color(220, 220, 220);

    public FormTriaje(String idCita, PrincipalMedico padre) {
        this.idCita = idCita;
        this.padre = padre;

        setTitle("Triaje - Toma de Signos Vitales");
        setBounds(100, 100, 520, 650);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        
        // Header Visual
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(COLOR_THEME);
        panelHeader.setBorder(new EmptyBorder(15, 10, 15, 10));
        getContentPane().add(panelHeader, BorderLayout.NORTH);
        panelHeader.setLayout(new BorderLayout(0, 0));
        
        JLabel lblTitulo = new JLabel("TRIAJE INICIAL");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelHeader.add(lblTitulo, BorderLayout.CENTER);

        contentPanel.setBackground(COLOR_BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // Lógica Inicial
        Cita cita = SistemaGestion.getInstance().buscarCitaPorId(idCita);
        // Protección (por si acaso)
        if(cita == null) { dispose(); return; } 
        
        Paciente pacienteExistente = SistemaGestion.getInstance().buscarPacientePorId(cita.getIdPaciente());
        if (pacienteExistente == null) {
            esPacienteNuevo = true;
        }
        
        // --- PANEL CLÍNICO ---
        JPanel panelClinico = new JPanel();
        // Borde estilizado con título turquesa
        TitledBorder borderClinico = new TitledBorder(new LineBorder(COLOR_THEME, 1, true), "Signos Vitales y Motivo", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), COLOR_THEME);
        panelClinico.setBorder(borderClinico);
        panelClinico.setBackground(COLOR_BG);
        panelClinico.setBounds(10, 10, 480, 200);
        contentPanel.add(panelClinico);
        panelClinico.setLayout(null);

        JLabel lblPeso = new JLabel("Peso (Lb):");
        lblPeso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPeso.setBounds(20, 30, 80, 20);
        panelClinico.add(lblPeso);

        spnPeso = new JSpinner();
        spnPeso.setModel(new SpinnerNumberModel(150.0, 1.0, 500.0, 1.0));
        spnPeso.setBounds(90, 30, 80, 25);
        panelClinico.add(spnPeso);

        JLabel lblPresion = new JLabel("Presión:");
        lblPresion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPresion.setBounds(220, 30, 60, 20);
        panelClinico.add(lblPresion);

        txtPresion = new JTextField();
        txtPresion.setToolTipText("Ej. 120/80");
        txtPresion.setBounds(280, 30, 100, 25);
        panelClinico.add(txtPresion);

        JLabel lblSintomas = new JLabel("Síntomas:");
        lblSintomas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSintomas.setBounds(20, 70, 200, 20);
        panelClinico.add(lblSintomas);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 95, 440, 85);
        scrollPane.setBorder(new LineBorder(COLOR_BORDER)); // Borde suave
        panelClinico.add(scrollPane);

        txtSintomas = new JTextArea();
        txtSintomas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSintomas.setLineWrap(true);
        scrollPane.setViewportView(txtSintomas);

        // --- PANEL DEMOGRÁFICO (Solo si es nuevo) ---
        panelDatosPersonales = new JPanel();
        TitledBorder borderDatos = new TitledBorder(new LineBorder(Color.GRAY, 1, true), "Datos del Nuevo Paciente", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), Color.GRAY);
        panelDatosPersonales.setBorder(borderDatos);
        panelDatosPersonales.setBackground(COLOR_BG);
        panelDatosPersonales.setBounds(10, 230, 480, 280);
        contentPanel.add(panelDatosPersonales);
        panelDatosPersonales.setLayout(null);
        
        if (!esPacienteNuevo) {
            panelDatosPersonales.setVisible(false);
            setBounds(100, 100, 530, 350); // Ventana más pequeña si no es nuevo
        } else {
            JLabel lblNombre = new JLabel("Visitante: " + cita.getNameVisitante());
            lblNombre.setForeground(COLOR_THEME);
            lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblNombre.setBounds(20, 30, 300, 20);
            panelDatosPersonales.add(lblNombre);
            
            JLabel lblApellido = new JLabel("Apellido:");
            lblApellido.setBounds(20, 70, 60, 20);
            panelDatosPersonales.add(lblApellido);
            txtApellido = new JTextField();
            txtApellido.setBounds(80, 70, 150, 25);
            panelDatosPersonales.add(txtApellido);
            
            JLabel lblNacimiento = new JLabel("F. Nacim:");
            lblNacimiento.setBounds(250, 70, 60, 20);
            panelDatosPersonales.add(lblNacimiento);
            
            SpinnerDateModel dateModel = new SpinnerDateModel();
            dateNacimiento = new JSpinner(dateModel);
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateNacimiento, "dd/MM/yyyy");
            dateNacimiento.setEditor(dateEditor);
            Calendar cal = Calendar.getInstance();
            cal.set(2000, Calendar.JANUARY, 1);
            dateNacimiento.setValue(cal.getTime());
            dateNacimiento.setBounds(310, 70, 140, 25);
            panelDatosPersonales.add(dateNacimiento);
            
            JLabel lblSexo = new JLabel("Sexo:");
            lblSexo.setBounds(20, 120, 40, 20);
            panelDatosPersonales.add(lblSexo);
            cbxSexo = new JComboBox<>(new String[] {"M", "F"});
            cbxSexo.setBackground(Color.WHITE);
            cbxSexo.setBounds(80, 120, 60, 25);
            panelDatosPersonales.add(cbxSexo);
            
            JLabel lblSangre = new JLabel("Sangre:");
            lblSangre.setBounds(160, 120, 60, 20);
            panelDatosPersonales.add(lblSangre);
            cbxTipoSangre = new JComboBox<>(new String[] {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
            cbxTipoSangre.setBackground(Color.WHITE);
            cbxTipoSangre.setBounds(210, 120, 60, 25);
            panelDatosPersonales.add(cbxTipoSangre);
            
            JLabel lblEstatura = new JLabel("Estatura:");
            lblEstatura.setBounds(290, 120, 60, 20);
            panelDatosPersonales.add(lblEstatura);
            spnEstatura = new JSpinner(new SpinnerNumberModel(1.70, 0.50, 2.50, 0.01));
            spnEstatura.setBounds(350, 120, 60, 25);
            panelDatosPersonales.add(spnEstatura);
            
            JLabel lblContacto = new JLabel("Contacto:");
            lblContacto.setBounds(20, 170, 60, 20);
            panelDatosPersonales.add(lblContacto);
            txtContacto = new JTextField();
            txtContacto.setBounds(80, 170, 200, 25);
            panelDatosPersonales.add(txtContacto);
        }

        // --- BOTONES ---
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(COLOR_BG);
        buttonPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton okButton = new JButton("INICIAR CONSULTA");
        okButton.setBackground(COLOR_THEME);
        okButton.setForeground(Color.WHITE);
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        okButton.setFocusPainted(false);
        okButton.setBorderPainted(false);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                procesarInicioConsulta();
            }
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
        
        JButton cancelButton = new JButton("CANCELAR");
        cancelButton.setBackground(new Color(240, 240, 240));
        cancelButton.setForeground(COLOR_TEXTO);
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
    }
    
    private void procesarInicioConsulta() {
        String sintomas = txtSintomas.getText();
        float peso = Float.parseFloat(spnPeso.getValue().toString());
        String presion = txtPresion.getText();
        
        if (sintomas.isEmpty() || presion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete Síntomas y Presión Arterial.", "Faltan Datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String apellido = "";
        LocalDate nacimiento = LocalDate.now();
        String sexo = "";
        String contacto = "";
        String sangre = "";
        float estatura = 0;
        
        if (esPacienteNuevo) {
            if (txtApellido.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete los datos del paciente.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            apellido = txtApellido.getText();
            Date dateValue = (Date) dateNacimiento.getValue();
            nacimiento = dateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            sexo = cbxSexo.getSelectedItem().toString();
            contacto = txtContacto.getText();
            sangre = cbxTipoSangre.getSelectedItem().toString();
            estatura = Float.parseFloat(spnEstatura.getValue().toString());
        }
        
        Consulta nuevaConsulta = SistemaGestion.getInstance().iniciarConsulta(
                idCita, apellido, nacimiento, sexo, contacto, sintomas, sangre, estatura, peso, presion
        );
        
        if (nuevaConsulta != null) {
            dispose();
            PanelConsultaActiva panelConsulta = new PanelConsultaActiva(nuevaConsulta, padre);
            panelConsulta.setVisible(true);
            padre.refrescarAgenda();
        } else {
            JOptionPane.showMessageDialog(this, "Error al iniciar la consulta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}