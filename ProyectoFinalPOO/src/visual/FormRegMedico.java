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
import java.util.ArrayList;
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

import logico.Medico;
import logico.SistemaGestion;

public class FormRegMedico extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JFormattedTextField txtTelefono;
    private JComboBox<String> cbxEspecialidad; // CAMBIO: JComboBox
    private JSpinner spnFechaNac;
    private JComboBox<String> cbxSexo;
    private JSpinner spnDuracion;
    private JSpinner spnLimite;
    
    private Medico medicoEdicion = null;

    private final Color COLOR_FONDO = new Color(253, 247, 238);
    private final Color COLOR_VERDE_BOTON = new Color(0, 168, 107);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);

    // Lista predefinida de especialidades para evitar errores de escritura
    private final String[] LISTA_ESPECIALIDADES = {
        "Seleccione...", 
        "Medicina General",
        "Cardiología",
        "Pediatría",
        "Dermatología",
        "Ginecología",
        "Neurología",
        "Ortopedia",
        "Oftalmología",
        "Psiquiatría",
        "Urología",
        "Gastroenterología",
        "Neumología"
    };

    public FormRegMedico(Medico medico) {
        this.medicoEdicion = medico;
        
        setTitle(medico == null ? "Registrar Nuevo Médico" : "Modificar Médico");
        setBounds(100, 100, 650, 550);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(20, 20, 595, 50);
        panelHeader.setBackground(COLOR_FONDO);
        panelHeader.setBorder(new LineBorder(Color.GRAY, 1, true));
        panelHeader.setLayout(new BorderLayout(0, 0));
        contentPanel.add(panelHeader);
        
        JLabel lblTitulo = new JLabel(medico == null ? "Registro de Médico" : "Edición de Médico");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panelHeader.add(lblTitulo, BorderLayout.CENTER);

        JPanel panelForm = new JPanel();
        panelForm.setBounds(20, 85, 595, 360);
        panelForm.setBackground(COLOR_FONDO);
        panelForm.setBorder(new LineBorder(Color.GRAY, 1, true));
        panelForm.setLayout(null);
        contentPanel.add(panelForm);

        // --- ID GENERADO ---
        crearLabel("ID Médico:", 30, 20, panelForm);
        txtId = new JTextField();
        txtId.setBounds(140, 20, 150, 25);
        txtId.setEditable(false); 
        txtId.setBackground(new Color(220, 220, 220));
        configurarCampo(txtId);
        panelForm.add(txtId);

        crearLabel("Nombre:", 30, 60, panelForm);
        txtNombre = new JTextField();
        txtNombre.setBounds(140, 60, 150, 25);
        configurarCampo(txtNombre);
        panelForm.add(txtNombre);

        crearLabel("Apellido:", 310, 60, panelForm);
        txtApellido = new JTextField();
        txtApellido.setBounds(390, 60, 170, 25);
        configurarCampo(txtApellido);
        panelForm.add(txtApellido);

        crearLabel("Fecha Nac.:", 30, 100, panelForm);
        spnFechaNac = new JSpinner(new SpinnerDateModel());
        spnFechaNac.setEditor(new JSpinner.DateEditor(spnFechaNac, "dd/MM/yyyy"));
        spnFechaNac.setBounds(140, 100, 150, 25);
        panelForm.add(spnFechaNac);

        crearLabel("Sexo:", 310, 100, panelForm);
        cbxSexo = new JComboBox<>();
        cbxSexo.setModel(new DefaultComboBoxModel<>(new String[] {"Seleccione", "M", "F"}));
        cbxSexo.setBackground(Color.WHITE);
        cbxSexo.setBounds(390, 100, 100, 25);
        panelForm.add(cbxSexo);

        // --- TELÉFONO CON MÁSCARA ---
        crearLabel("Teléfono:", 30, 140, panelForm);
        try {
            MaskFormatter mascaraTel = new MaskFormatter("###-###-####");
            mascaraTel.setPlaceholderCharacter('_');
            txtTelefono = new JFormattedTextField(mascaraTel);
        } catch (ParseException e) {
            e.printStackTrace();
            txtTelefono = new JFormattedTextField(); 
        }
        txtTelefono.setBounds(140, 140, 150, 25);
        txtTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelForm.add(txtTelefono);

        JLabel lblSeparator = new JLabel("__________________________________________________________________________");
        lblSeparator.setForeground(Color.LIGHT_GRAY);
        lblSeparator.setBounds(30, 170, 550, 14);
        panelForm.add(lblSeparator);

        JLabel lblTituloPro = new JLabel("Datos Profesionales");
        lblTituloPro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloPro.setForeground(COLOR_VERDE_BOTON);
        lblTituloPro.setBounds(30, 195, 200, 20);
        panelForm.add(lblTituloPro);

        crearLabel("Especialidad:", 30, 230, panelForm);
        // CAMBIO: Ahora es un JComboBox
        cbxEspecialidad = new JComboBox<>(LISTA_ESPECIALIDADES);
        cbxEspecialidad.setBackground(Color.WHITE);
        cbxEspecialidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbxEspecialidad.setBounds(140, 230, 200, 25);
        panelForm.add(cbxEspecialidad);

        crearLabel("Duración Cita:", 30, 270, panelForm);
        spnDuracion = new JSpinner();
        spnDuracion.setModel(new SpinnerNumberModel(30, 10, 120, 5)); 
        spnDuracion.setBounds(140, 270, 60, 25);
        panelForm.add(spnDuracion);
        
        JLabel lblMin = new JLabel("minutos");
        lblMin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMin.setBounds(210, 270, 50, 25);
        panelForm.add(lblMin);

        crearLabel("Límite Diario:", 310, 270, panelForm);
        spnLimite = new JSpinner();
        spnLimite.setModel(new SpinnerNumberModel(10, 1, 50, 1)); 
        spnLimite.setBounds(390, 270, 60, 25);
        panelForm.add(spnLimite);
        
        JLabel lblCitas = new JLabel("citas");
        lblCitas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCitas.setBounds(460, 270, 50, 25);
        panelForm.add(lblCitas);
        
        JPanel panelBotones = new JPanel();
        panelBotones.setBounds(20, 455, 595, 50);
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
                gestionarGuardado();
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
        
        // Lógica de inicialización
        if (this.medicoEdicion == null) {
            generarNuevoId();
        } else {
            cargarDatosSiEsEdicion();
        }
    }
    
    private void generarNuevoId() {
        int siguienteId = SistemaGestion.getInstance().getListaMedicos().size() + 1;
        String idGenerado = String.format("MED-%03d", siguienteId);
        while(SistemaGestion.getInstance().buscarMedicoPorId(idGenerado) != null) {
            siguienteId++;
            idGenerado = String.format("MED-%03d", siguienteId);
        }
        txtId.setText(idGenerado);
    }

    private void crearLabel(String texto, int x, int y, JPanel panel) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(COLOR_TEXTO);
        lbl.setBounds(x, y, 100, 25);
        panel.add(lbl);
    }

    private void configurarCampo(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setColumns(10);
    }
    
    private void cargarDatosSiEsEdicion() {
        if(medicoEdicion != null) {
            txtId.setText(medicoEdicion.getId());
            txtNombre.setText(medicoEdicion.getName());
            txtApellido.setText(medicoEdicion.getApellido());
            txtTelefono.setValue(medicoEdicion.getContacto()); 
            txtTelefono.setText(medicoEdicion.getContacto()); 
            
            // CAMBIO: Cargar especialidad en el ComboBox
            cbxEspecialidad.setSelectedItem(medicoEdicion.getEspecialidad());
            
            cbxSexo.setSelectedItem(medicoEdicion.getSexo());
            spnDuracion.setValue(medicoEdicion.getDuracionCitaMinutos());
            spnLimite.setValue(medicoEdicion.getLimiteCitasPorDia());
            
            LocalDate ld = medicoEdicion.getFechaNacimiento();
            if(ld != null) {
                Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
                spnFechaNac.setValue(date);
            }
        }
    }

    private void gestionarGuardado() {
        String telefono = txtTelefono.getText();
        // Validar que seleccionaron especialidad
        if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || 
            cbxEspecialidad.getSelectedIndex() == 0 || telefono.contains("_")) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cbxSexo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione el sexo.", "Falta Información", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String id = txtId.getText();
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String contacto = txtTelefono.getText();
            
            // CAMBIO: Obtener especialidad del ComboBox
            String especialidad = (String) cbxEspecialidad.getSelectedItem();
            
            String sexo = (String) cbxSexo.getSelectedItem();
            Date date = (Date) spnFechaNac.getValue();
            LocalDate fechaNac = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int duracion = (int) spnDuracion.getValue();
            int limite = (int) spnLimite.getValue();

            if(medicoEdicion == null) {
                Medico nuevoMedico = new Medico(id, nombre, apellido, fechaNac, sexo, contacto, 
                                                especialidad, limite, duracion, 
                                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                SistemaGestion.getInstance().registrarMedico(nuevoMedico);
                JOptionPane.showMessageDialog(this, "Médico registrado con ID: " + id);
            } else {
                medicoEdicion.setName(nombre);
                medicoEdicion.setApellido(apellido);
                medicoEdicion.setFechaNacimiento(fechaNac);
                medicoEdicion.setSexo(sexo);
                medicoEdicion.setContacto(contacto);
                medicoEdicion.setEspecialidad(especialidad);
                medicoEdicion.setDuracionCitaMinutos(duracion);
                medicoEdicion.setLimiteCitasPorDia(limite);
                JOptionPane.showMessageDialog(this, "Médico modificado correctamente.");
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}