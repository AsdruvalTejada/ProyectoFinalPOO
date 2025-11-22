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
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

import logico.Paciente;
import logico.SistemaGestion;

@SuppressWarnings("serial")
public class FormRegPaciente extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JFormattedTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JFormattedTextField txtTelefono;
    private JSpinner spnFechaNac;
    private JComboBox<String> cbxSexo;
    private JComboBox<String> cbxSangre;
    private JSpinner spnEstatura;
    
    private Paciente pacienteEdicion = null;

    private final Color COLOR_FONDO = new Color(254, 251, 246);
    private final Color COLOR_VERDE_BOTON = new Color(0, 168, 107);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);

    public FormRegPaciente(Paciente paciente) {
        this.pacienteEdicion = paciente;
        
        setTitle(paciente == null ? "Registrar Nuevo Paciente" : "Modificar Datos Paciente");
        setBounds(100, 100, 600, 480);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(20, 20, 545, 50);
        panelHeader.setBackground(COLOR_FONDO);
        panelHeader.setBorder(new LineBorder(Color.GRAY, 1, true));
        panelHeader.setLayout(new BorderLayout(0, 0));
        contentPanel.add(panelHeader);
        
        JLabel lblTitulo = new JLabel(paciente == null ? "Registro de Paciente" : "Edición de Paciente");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panelHeader.add(lblTitulo, BorderLayout.CENTER);

        JPanel panelForm = new JPanel();
        panelForm.setBounds(20, 85, 545, 290);
        panelForm.setBackground(COLOR_FONDO);
        panelForm.setBorder(new LineBorder(Color.GRAY, 1, true));
        panelForm.setLayout(null);
        contentPanel.add(panelForm);

        crearLabel("Cédula:", 30, 20, panelForm);
        try {
            MaskFormatter maskCedula = new MaskFormatter("###-#######-#");
            maskCedula.setPlaceholderCharacter('_');
            txtId = new JFormattedTextField(maskCedula);
        } catch (ParseException e) {
            txtId = new JFormattedTextField();
        }
        txtId.setBounds(120, 20, 150, 25);
        configurarCampo(txtId);
        panelForm.add(txtId);

        crearLabel("Nombre:", 30, 60, panelForm);
        txtNombre = new JTextField();
        txtNombre.setBounds(120, 60, 150, 25);
        configurarCampo(txtNombre);
        panelForm.add(txtNombre);

        crearLabel("Apellido:", 290, 60, panelForm);
        txtApellido = new JTextField();
        txtApellido.setBounds(360, 60, 150, 25);
        configurarCampo(txtApellido);
        panelForm.add(txtApellido);

        crearLabel("Fecha Nac.:", 30, 100, panelForm);
        spnFechaNac = new JSpinner(new SpinnerDateModel());
        spnFechaNac.setEditor(new JSpinner.DateEditor(spnFechaNac, "dd/MM/yyyy"));
        spnFechaNac.setBounds(120, 100, 150, 25);
        panelForm.add(spnFechaNac);

        crearLabel("Sexo:", 290, 100, panelForm);
        cbxSexo = new JComboBox<>();
        cbxSexo.setModel(new DefaultComboBoxModel<>(new String[] {"Seleccione", "M", "F"}));
        cbxSexo.setBackground(Color.WHITE);
        cbxSexo.setBounds(360, 100, 100, 25);
        panelForm.add(cbxSexo);

        crearLabel("Teléfono:", 30, 140, panelForm);
        try {
            MaskFormatter maskTel = new MaskFormatter("###-###-####");
            maskTel.setPlaceholderCharacter('_');
            txtTelefono = new JFormattedTextField(maskTel);
        } catch (ParseException e) {
            txtTelefono = new JFormattedTextField();
        }
        txtTelefono.setBounds(120, 140, 150, 25);
        configurarCampo(txtTelefono);
        panelForm.add(txtTelefono);
        
        crearLabel("Sangre:", 290, 140, panelForm);
        cbxSangre = new JComboBox<>();
        cbxSangre.setModel(new DefaultComboBoxModel<>(new String[] {"Desc", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"}));
        cbxSangre.setBackground(Color.WHITE);
        cbxSangre.setBounds(360, 140, 100, 25);
        panelForm.add(cbxSangre);

        crearLabel("Estatura (m):", 30, 180, panelForm);
        spnEstatura = new JSpinner();
        spnEstatura.setModel(new SpinnerNumberModel(1.70, 0.30, 2.50, 0.01));
        spnEstatura.setBounds(120, 180, 80, 25);
        panelForm.add(spnEstatura);

        JPanel panelBotones = new JPanel();
        panelBotones.setBounds(20, 390, 545, 50);
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        contentPanel.add(panelBotones);

        JButton btnGuardar = new JButton("GUARDAR");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setBackground(COLOR_VERDE_BOTON);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });
        panelBotones.add(btnGuardar);

        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
        
        cargarDatosSiEsEdicion();
    }

    private void crearLabel(String texto, int x, int y, JPanel panel) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(COLOR_TEXTO);
        lbl.setBounds(x, y, 90, 25);
        panel.add(lbl);
    }

    private void configurarCampo(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setColumns(10);
    }
    
    private void cargarDatosSiEsEdicion() {
        if(pacienteEdicion != null) {
            txtId.setText(pacienteEdicion.getId());
            txtId.setEditable(false);
            txtId.setBackground(new Color(230, 230, 230));
            
            txtNombre.setText(pacienteEdicion.getName());
            txtApellido.setText(pacienteEdicion.getApellido());
            txtTelefono.setValue(pacienteEdicion.getContacto()); 
            cbxSexo.setSelectedItem(pacienteEdicion.getSexo());
            cbxSangre.setSelectedItem(pacienteEdicion.getTipoSangre());
            spnEstatura.setValue(pacienteEdicion.getEstatura()); 
            
            LocalDate ld = pacienteEdicion.getFechaNacimiento();
            if(ld != null) {
                Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
                spnFechaNac.setValue(date);
            }
        }
    }

    private void guardar() {
        String cedula = txtId.getText();
        String telefono = txtTelefono.getText();
        
        if (cedula.contains("_") || txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (cbxSexo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione el sexo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String sexo = (String) cbxSexo.getSelectedItem();
        String sangre = (String) cbxSangre.getSelectedItem();
        
        float estatura = Float.parseFloat(spnEstatura.getValue().toString());
        
        Date date = (Date) spnFechaNac.getValue();
        LocalDate fechaNac = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if(pacienteEdicion == null) {
            if(SistemaGestion.getInstance().buscarPacientePorId(cedula) != null) {
                 JOptionPane.showMessageDialog(this, "Ya existe un paciente con esta cédula.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }
            
            Paciente nuevo = new Paciente(cedula, nombre, apellido, fechaNac, sexo, telefono, sangre, estatura);
            nuevo.inicializarRegistroVacunas(SistemaGestion.getInstance().getCatalogoVacunas());
            
            SistemaGestion.getInstance().registrarPaciente(nuevo);
            JOptionPane.showMessageDialog(this, "Paciente registrado correctamente.");
        } else {
            pacienteEdicion.setName(nombre);
            pacienteEdicion.setApellido(apellido);
            pacienteEdicion.setFechaNacimiento(fechaNac);
            pacienteEdicion.setSexo(sexo);
            pacienteEdicion.setContacto(telefono);
            pacienteEdicion.setTipoSangre(sangre);
            pacienteEdicion.setEstatura(estatura);
            JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        }
        dispose();
    }
}