package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Calendar;
import java.awt.event.ActionEvent;

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
    private JTextField txtContacto;
    private JComboBox<String> cbxTipoSangre;
    private JSpinner spnEstatura;

    private final Color COLOR_FONDO = new Color(254, 251, 246);
    private final Color COLOR_VERDE = new Color(0, 200, 151);
    private final Color COLOR_BLOQUEADO = new Color(230, 230, 230); 

    public FormTriaje(String idCita, PrincipalMedico padre) {
        this.idCita = idCita;
        this.padre = padre;

        setTitle("Triaje Inicial - Toma de Signos Vitales");
        setBounds(100, 100, 500, 600);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
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
        panelClinico.setBorder(new TitledBorder(null, "Signos Vitales y Motivo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelClinico.setBackground(COLOR_FONDO);
        panelClinico.setBounds(10, 10, 464, 200);
        contentPanel.add(panelClinico);
        panelClinico.setLayout(null);

        JLabel lblPeso = new JLabel("Peso (Lb):");
        lblPeso.setBounds(10, 30, 80, 14);
        panelClinico.add(lblPeso);

        spnPeso = new JSpinner();
        spnPeso.setModel(new SpinnerNumberModel(150.0, 1.0, 500.0, 1.0));
        spnPeso.setBounds(80, 27, 70, 20);
        panelClinico.add(spnPeso);

        JLabel lblPresion = new JLabel("Presión:");
        lblPresion.setBounds(170, 30, 60, 14);
        panelClinico.add(lblPresion);

        txtPresion = new JTextField();
        txtPresion.setToolTipText("Ej. 120/80");
        txtPresion.setBounds(230, 27, 80, 20);
        panelClinico.add(txtPresion);
        txtPresion.setColumns(10);

        JLabel lblSintomas = new JLabel("Síntomas / Motivo:");
        lblSintomas.setBounds(10, 60, 150, 14);
        panelClinico.add(lblSintomas);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 80, 444, 100);
        panelClinico.add(scrollPane);

        txtSintomas = new JTextArea();
        scrollPane.setViewportView(txtSintomas);

        panelDatosPersonales = new JPanel();
        String tituloPanel = esPacienteNuevo ? "Registro Nuevo Paciente (Llenar)" : "Datos del Paciente (Lectura)";
        panelDatosPersonales.setBorder(new TitledBorder(null, tituloPanel, TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelDatosPersonales.setBackground(COLOR_FONDO);
        panelDatosPersonales.setBounds(10, 220, 464, 280);
        contentPanel.add(panelDatosPersonales);
        panelDatosPersonales.setLayout(null);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(10, 25, 60, 14);
        panelDatosPersonales.add(lblNombre);
        
        txtNombre = new JTextField();
        txtNombre.setBounds(80, 22, 150, 20);
        panelDatosPersonales.add(txtNombre);
        
        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setBounds(240, 25, 60, 14);
        panelDatosPersonales.add(lblApellido);
        
        txtApellido = new JTextField();
        txtApellido.setBounds(300, 22, 150, 20);
        panelDatosPersonales.add(txtApellido);
        
        JLabel lblNacimiento = new JLabel("F. Nacim:");
        lblNacimiento.setBounds(10, 55, 60, 14);
        panelDatosPersonales.add(lblNacimiento);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateNacimiento = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateNacimiento, "dd/MM/yyyy");
        dateNacimiento.setEditor(dateEditor);
        dateNacimiento.setBounds(80, 52, 100, 20);
        panelDatosPersonales.add(dateNacimiento);
        
        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setBounds(200, 55, 46, 14);
        panelDatosPersonales.add(lblSexo);
        
        cbxSexo = new JComboBox<>();
        cbxSexo.setModel(new DefaultComboBoxModel<>(new String[] {"M", "F"}));
        cbxSexo.setBounds(240, 52, 50, 22);
        panelDatosPersonales.add(cbxSexo);
        
        JLabel lblSangre = new JLabel("Sangre:");
        lblSangre.setBounds(310, 55, 50, 14);
        panelDatosPersonales.add(lblSangre);
        
        cbxTipoSangre = new JComboBox<>();
        cbxTipoSangre.setModel(new DefaultComboBoxModel<>(new String[] {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"}));
        cbxTipoSangre.setBounds(360, 52, 50, 22);
        panelDatosPersonales.add(cbxTipoSangre);
        
        JLabel lblEstatura = new JLabel("Estatura (m):");
        lblEstatura.setBounds(10, 90, 80, 14);
        panelDatosPersonales.add(lblEstatura);
        
        spnEstatura = new JSpinner();
        spnEstatura.setModel(new SpinnerNumberModel(1.70, 0.50, 2.50, 0.01));
        spnEstatura.setBounds(90, 87, 60, 20);
        panelDatosPersonales.add(spnEstatura);
        
        JLabel lblContacto = new JLabel("Contacto:");
        lblContacto.setBounds(170, 90, 60, 14);
        panelDatosPersonales.add(lblContacto);
        
        txtContacto = new JTextField();
        txtContacto.setBounds(230, 87, 180, 20);
        panelDatosPersonales.add(txtContacto);

        if (esPacienteNuevo) {
            // CASO 1: PACIENTE NUEVO -> Recuperar datos de la Cita y Separar Nombre
            Calendar cal = Calendar.getInstance();
            cal.set(2000, Calendar.JANUARY, 1);
            dateNacimiento.setValue(cal.getTime());
            
            String nombreCompleto = cita.getNameVisitante();
            if(nombreCompleto != null && !nombreCompleto.isEmpty()) {
                // Lógica simple para separar nombre y apellido
                int ultimoEspacio = nombreCompleto.lastIndexOf(" ");
                if (ultimoEspacio != -1) {
                    txtNombre.setText(nombreCompleto.substring(0, ultimoEspacio));
                    txtApellido.setText(nombreCompleto.substring(ultimoEspacio + 1));
                } else {
                    txtNombre.setText(nombreCompleto);
                    txtApellido.setText("");
                }
            }
            txtContacto.setText("");
            // Los campos permanecen editables para correcciones
            
        } else {
            // CASO 2: PACIENTE EXISTENTE -> BLOQUEAR TODO
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
        buttonPane.setBackground(COLOR_FONDO);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton okButton = new JButton("INICIAR CONSULTA");
        okButton.setBackground(COLOR_VERDE);
        okButton.setForeground(Color.WHITE);
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                procesarInicioConsulta();
            }
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
        
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(cancelButton);
    }
    
    private void procesarInicioConsulta() {
        String sintomas = txtSintomas.getText();
        float peso = Float.parseFloat(spnPeso.getValue().toString());
        String presion = txtPresion.getText();
        
        if (sintomas.isEmpty() || presion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete Síntomas y Presión Arterial.", "Datos Faltantes", JOptionPane.WARNING_MESSAGE);
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
            if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Verifique Nombre y Apellido del paciente.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            nombreFinal = txtNombre.getText();
            apellidoFinal = txtApellido.getText();

            Date dateValue = (Date) dateNacimiento.getValue();
            nacimiento = dateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            sexo = cbxSexo.getSelectedItem().toString();
            contacto = txtContacto.getText();
            sangre = cbxTipoSangre.getSelectedItem().toString();
            estatura = Float.parseFloat(spnEstatura.getValue().toString());
            
            Cita citaObj = SistemaGestion.getInstance().buscarCitaPorId(idCita);
            if(citaObj != null) {
                // Actualizamos el nombre concatenado en la cita por si el médico lo corrigió
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