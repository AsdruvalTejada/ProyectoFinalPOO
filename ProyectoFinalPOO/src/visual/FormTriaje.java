package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
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
import javax.swing.text.MaskFormatter;

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
    private Paciente pacienteExistente = null;

    private JSpinner spnPeso;
    private JTextField txtPresion;
    private JTextArea txtSintomas;

    private JPanel panelDatosPersonales;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JSpinner dateNacimiento; 
    private JComboBox<String> cbxSexo;
    private JFormattedTextField txtContacto; 
    private JComboBox<String> cbxTipoSangre;
    private JSpinner spnEstatura;

    private final Color COLOR_THEME = new Color(0, 190, 165);
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_BORDER = new Color(200, 200, 200);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);
    private final Color COLOR_BLOQUEADO = new Color(240, 240, 240);

    public FormTriaje(String idCita, PrincipalMedico padre) {
        this.idCita = idCita;
        this.padre = padre;

        setTitle("Triaje - Toma de Signos Vitales");
        setBounds(100, 100, 520, 650);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_THEME);
        panelHeader.setBorder(new EmptyBorder(15, 10, 15, 10));
        JLabel lblTitulo = new JLabel("TRIAJE INICIAL");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelHeader.add(lblTitulo, BorderLayout.CENTER);
        getContentPane().add(panelHeader, BorderLayout.NORTH);

        contentPanel.setBackground(COLOR_BG);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        Cita cita = SistemaGestion.getInstance().buscarCitaPorId(idCita);
        if (cita == null) {
            JOptionPane.showMessageDialog(this, "Error crítico: No se encontró la cita.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose(); 
            return;    
        }
        
        pacienteExistente = SistemaGestion.getInstance().buscarPacientePorId(cita.getIdPaciente());
        esPacienteNuevo = (pacienteExistente == null);

        JPanel panelClinico = new JPanel();
        TitledBorder borderClinico = new TitledBorder(new LineBorder(COLOR_THEME, 1, true), "Signos Vitales y Motivo", TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), COLOR_THEME);
        panelClinico.setBorder(borderClinico);
        panelClinico.setBackground(COLOR_BG);
        panelClinico.setBounds(10, 10, 480, 200);
        contentPanel.add(panelClinico);
        panelClinico.setLayout(null);

        JLabel lblPeso = new JLabel("Peso (Lb):");
        lblPeso.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPeso.setForeground(COLOR_TEXTO);
        lblPeso.setBounds(20, 30, 80, 20);
        panelClinico.add(lblPeso);

        spnPeso = new JSpinner();
        spnPeso.setModel(new SpinnerNumberModel(150.0, 1.0, 500.0, 1.0));
        spnPeso.setBounds(90, 30, 80, 25);
        panelClinico.add(spnPeso);

        JLabel lblPresion = new JLabel("Presión:");
        lblPresion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPresion.setForeground(COLOR_TEXTO);
        lblPresion.setBounds(220, 30, 60, 20);
        panelClinico.add(lblPresion);

        txtPresion = new JTextField();
        txtPresion.setToolTipText("Ej. 120/80");
        txtPresion.setBounds(280, 30, 100, 25);
        panelClinico.add(txtPresion);

        JLabel lblSintomas = new JLabel("Síntomas / Motivo:");
        lblSintomas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSintomas.setForeground(COLOR_TEXTO);
        lblSintomas.setBounds(20, 70, 200, 20);
        panelClinico.add(lblSintomas);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 95, 440, 85);
        scrollPane.setBorder(new LineBorder(COLOR_BORDER));
        panelClinico.add(scrollPane);

        txtSintomas = new JTextArea();
        txtSintomas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSintomas.setLineWrap(true);
        scrollPane.setViewportView(txtSintomas);

        panelDatosPersonales = new JPanel();
        String tituloPanel = esPacienteNuevo ? "Registro Nuevo Paciente" : "Datos del Paciente (Lectura)";
        TitledBorder borderDatos = new TitledBorder(new LineBorder(Color.GRAY, 1, true), tituloPanel, TitledBorder.LEADING, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), Color.GRAY);
        panelDatosPersonales.setBorder(borderDatos);
        panelDatosPersonales.setBackground(COLOR_BG);
        panelDatosPersonales.setBounds(10, 230, 480, 280);
        contentPanel.add(panelDatosPersonales);
        panelDatosPersonales.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNombre.setBounds(20, 30, 60, 20);
        panelDatosPersonales.add(lblNombre);
        
        txtNombre = new JTextField();
        txtNombre.setBounds(80, 30, 150, 25);
        panelDatosPersonales.add(txtNombre);
        
        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblApellido.setBounds(250, 30, 60, 20);
        panelDatosPersonales.add(lblApellido);
        
        txtApellido = new JTextField();
        txtApellido.setBounds(310, 30, 150, 25);
        panelDatosPersonales.add(txtApellido);
        
        JLabel lblNacimiento = new JLabel("F. Nacim:");
        lblNacimiento.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNacimiento.setBounds(20, 70, 60, 20);
        panelDatosPersonales.add(lblNacimiento);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateNacimiento = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateNacimiento, "dd/MM/yyyy");
        dateNacimiento.setEditor(dateEditor);
        dateNacimiento.setBounds(80, 70, 110, 25);
        panelDatosPersonales.add(dateNacimiento);
        
        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSexo.setBounds(200, 70, 40, 20);
        panelDatosPersonales.add(lblSexo);
        
        cbxSexo = new JComboBox<>();
        cbxSexo.setModel(new DefaultComboBoxModel<>(new String[] {"M", "F"}));
        cbxSexo.setBackground(Color.WHITE);
        cbxSexo.setBounds(240, 70, 50, 25);
        panelDatosPersonales.add(cbxSexo);
        
        JLabel lblSangre = new JLabel("Sangre:");
        lblSangre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSangre.setBounds(310, 70, 50, 20);
        panelDatosPersonales.add(lblSangre);
        
        cbxTipoSangre = new JComboBox<>(new String[] {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
        cbxTipoSangre.setBackground(Color.WHITE);
        cbxTipoSangre.setBounds(360, 70, 60, 25);
        panelDatosPersonales.add(cbxTipoSangre);
        
        JLabel lblEstatura = new JLabel("Estatura (m):");
        lblEstatura.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstatura.setBounds(20, 110, 80, 20);
        panelDatosPersonales.add(lblEstatura);
        
        spnEstatura = new JSpinner();
        spnEstatura.setModel(new SpinnerNumberModel(1.70, 0.50, 2.50, 0.01));
        spnEstatura.setBounds(100, 110, 60, 25);
        panelDatosPersonales.add(spnEstatura);
        
        JLabel lblContacto = new JLabel("Contacto:");
        lblContacto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblContacto.setBounds(180, 110, 60, 20);
        panelDatosPersonales.add(lblContacto);
        
        try {
            MaskFormatter mascaraTel = new MaskFormatter("###-###-####");
            mascaraTel.setPlaceholderCharacter('_');
            txtContacto = new JFormattedTextField(mascaraTel);
        } catch (ParseException e) {
            e.printStackTrace();
            txtContacto = new JFormattedTextField(); 
        }
        txtContacto.setBounds(240, 110, 220, 25);
        panelDatosPersonales.add(txtContacto);

        if (esPacienteNuevo) {
            Calendar cal = Calendar.getInstance();
            cal.set(2000, Calendar.JANUARY, 1);
            dateNacimiento.setValue(cal.getTime());
            
            String nombreCompleto = cita.getNameVisitante();
            if(nombreCompleto != null && !nombreCompleto.isEmpty()) {
                int ultimoEspacio = nombreCompleto.lastIndexOf(" ");
                if (ultimoEspacio != -1) {
                    txtNombre.setText(nombreCompleto.substring(0, ultimoEspacio));
                    txtApellido.setText(nombreCompleto.substring(ultimoEspacio + 1));
                } else {
                    txtNombre.setText(nombreCompleto);
                    txtApellido.setText("");
                }
            }
            txtContacto.setValue(null); 
            
        } else {
            txtNombre.setText(pacienteExistente.getName());
            txtNombre.setEditable(false);
            txtNombre.setBackground(COLOR_BLOQUEADO);
            
            txtApellido.setText(pacienteExistente.getApellido());
            txtApellido.setEditable(false);
            txtApellido.setBackground(COLOR_BLOQUEADO);
            
            txtContacto.setText(pacienteExistente.getContacto());
            txtContacto.setEditable(false);
            txtContacto.setBackground(COLOR_BLOQUEADO);
            
            LocalDate ld = pacienteExistente.getFechaNacimiento();
            if(ld != null) {
                Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
                dateNacimiento.setValue(date);
            }
            dateNacimiento.setEnabled(false);
            
            cbxSexo.setSelectedItem(pacienteExistente.getSexo());
            cbxSexo.setEnabled(false);
            
            cbxTipoSangre.setSelectedItem(pacienteExistente.getTipoSangre());
            cbxTipoSangre.setEnabled(false);
            
            spnEstatura.setValue(pacienteExistente.getEstatura());
            spnEstatura.setEnabled(false);
        }

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
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(cancelButton);
    }
    
    private void procesarInicioConsulta() {
        String sintomas = txtSintomas.getText().trim();
        float peso = Float.parseFloat(spnPeso.getValue().toString());
        String presion = txtPresion.getText().trim();
        
        if (sintomas.isEmpty() || presion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete los campos de Síntomas y Presión Arterial.", "Faltan Datos Clínicos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreFinal = "";
        String apellidoFinal = "";
        LocalDate nacimiento = LocalDate.now();
        String sexo = "";
        String contacto = "";
        String sangre = "";
        float estatura = 0;
        
        if (esPacienteNuevo) {
            String contactoRaw = txtContacto.getText();
            boolean contactoVacio = contactoRaw.replace("-", "").replace("_", "").trim().isEmpty();
            String nombreRaw = txtNombre.getText().trim();
            String apellidoRaw = txtApellido.getText().trim();

            if (nombreRaw.isEmpty() || apellidoRaw.isEmpty() || contactoVacio) {
                JOptionPane.showMessageDialog(this, "Para pacientes nuevos, es OBLIGATORIO completar:\n- Nombre\n- Apellido\n- Número de Contacto", "Datos Personales Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            nombreFinal = nombreRaw;
            apellidoFinal = apellidoRaw;

            Date dateValue = (Date) dateNacimiento.getValue();
            nacimiento = dateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            sexo = cbxSexo.getSelectedItem().toString();
            contacto = txtContacto.getText();
            sangre = cbxTipoSangre.getSelectedItem().toString();
            estatura = Float.parseFloat(spnEstatura.getValue().toString());
            
            Cita citaObj = SistemaGestion.getInstance().buscarCitaPorId(idCita);
            if(citaObj != null) {
                citaObj.setNameVisitante(nombreFinal + " " + apellidoFinal); 
            }
        } else {
            estatura = pacienteExistente.getEstatura(); 
        }
        
        Consulta nuevaConsulta = SistemaGestion.getInstance().iniciarConsulta(
                idCita, 
                apellidoFinal, 
                nacimiento, 
                sexo, 
                contacto, 
                sintomas, 
                sangre, 
                estatura, 
                peso, 
                presion
        );
        
        if (nuevaConsulta != null) {
            dispose();
             PanelConsultaActiva panelConsulta = new PanelConsultaActiva(nuevaConsulta, padre);
             panelConsulta.setVisible(true);
            
            padre.refrescarAgenda();
        } else {
            JOptionPane.showMessageDialog(this, "Error al iniciar la consulta.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }
}