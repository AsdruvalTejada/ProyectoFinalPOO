package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import logico.Cita;
import logico.Medico;
import logico.Paciente;
import logico.Secretaria;
import logico.SistemaGestion;

@SuppressWarnings("serial")
public class FormAgendarCita extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtCodigo;
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtApellido;

    private JSpinner spnFecha;
    private JTextField txtFechaFija;
    
    private JComboBox<Medico> cbxMedico;
    private JComboBox<String> cbxHora;
    
    private LocalDate fechaSeleccionada;
    private Paciente pacientePreseleccionado;
    private Cita citaEditar;
    private Secretaria secretariaContexto;
    private boolean modoSeleccionFecha = false; 
    
    private final Color COLOR_FONDO       = new Color(254, 251, 246);
    private final Color COLOR_TEAL        = new Color(10, 186, 181); 
    private final Color COLOR_ACCENT      = new Color(0, 200, 151);
    private final Color COLOR_TEXTO       = new Color(50, 50, 50);
    private final Color COLOR_BLOQUEADO   = new Color(230, 230, 230);

    public FormAgendarCita(LocalDate fechaIn, Cita citaIn, Secretaria secre) {
        this.fechaSeleccionada = fechaIn;
        this.citaEditar = citaIn;
        this.secretariaContexto = secre;
        this.pacientePreseleccionado = null;
        this.modoSeleccionFecha = false;
        
        inicializarComponentes();
    }

    public FormAgendarCita(Paciente pacienteIn) {
        this(pacienteIn, null);
    }

    public FormAgendarCita(Paciente pacienteIn, Secretaria secre) {
        this.fechaSeleccionada = LocalDate.now();
        this.citaEditar = null;
        this.secretariaContexto = secre;
        this.pacientePreseleccionado = pacienteIn;
        this.modoSeleccionFecha = true;
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Agendar Cita");
        setBounds(100, 100, 700, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(35, 20, 615, 50);
        panelHeader.setBackground(COLOR_FONDO);
        panelHeader.setBorder(new LineBorder(COLOR_TEAL, 1, true));
        panelHeader.setLayout(new BorderLayout(0, 0));
        contentPanel.add(panelHeader);
        
        JLabel lblTitulo = new JLabel("Agendar Nueva Cita");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(COLOR_TEAL);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panelHeader.add(lblTitulo, BorderLayout.CENTER);

        JPanel panelForm = new JPanel();
        panelForm.setBounds(35, 85, 615, 280);
        panelForm.setBackground(COLOR_FONDO);
        panelForm.setBorder(new LineBorder(COLOR_TEAL, 1, true));
        panelForm.setLayout(null);
        contentPanel.add(panelForm);
        
        JLabel lblCodigo = new JLabel("Cód. Cita:");
        lblCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCodigo.setForeground(COLOR_TEXTO);
        lblCodigo.setBounds(30, 20, 80, 25);
        panelForm.add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setEditable(false);
        txtCodigo.setBackground(COLOR_BLOQUEADO);
        txtCodigo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtCodigo.setBounds(120, 20, 150, 25);
        txtCodigo.setText("C-" + (SistemaGestion.genIdCita + 1)); 
        panelForm.add(txtCodigo);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCedula.setBounds(30, 60, 80, 25);
        panelForm.add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setBounds(120, 60, 200, 25);
        txtCedula.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelForm.add(txtCedula);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNombre.setBounds(30, 100, 80, 25);
        panelForm.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(120, 100, 180, 25);
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelForm.add(txtNombre);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblApellido.setBounds(310, 100, 60, 25);
        panelForm.add(lblApellido);

        txtApellido = new JTextField();
        txtApellido.setBounds(380, 100, 180, 25);
        txtApellido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelForm.add(txtApellido);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFecha.setForeground(COLOR_TEAL);
        lblFecha.setBounds(30, 150, 80, 25);
        panelForm.add(lblFecha);
        if (modoSeleccionFecha) {
            spnFecha = new JSpinner(new SpinnerDateModel());
            spnFecha.setEditor(new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy"));
            spnFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            spnFecha.setBounds(120, 150, 150, 25);
            spnFecha.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    Date date = (Date) spnFecha.getValue();
                    fechaSeleccionada = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    cargarHorasDisponibles();
                }
            });
            panelForm.add(spnFecha);
            
        } else {
            txtFechaFija = new JTextField();
            txtFechaFija.setEditable(false);
            txtFechaFija.setBackground(COLOR_BLOQUEADO);
            txtFechaFija.setFont(new Font("Segoe UI", Font.BOLD, 13));
            txtFechaFija.setText(fechaSeleccionada.toString());
            txtFechaFija.setBounds(120, 150, 200, 25);
            panelForm.add(txtFechaFija);
        }

        JLabel lblMedico = new JLabel("Médico:");
        lblMedico.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMedico.setBounds(30, 190, 80, 25);
        panelForm.add(lblMedico);

        cbxMedico = new JComboBox<>();
        cbxMedico.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbxMedico.setBackground(Color.WHITE);
        cbxMedico.setBounds(120, 190, 250, 25);
        panelForm.add(cbxMedico);

        cbxMedico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarHorasDisponibles();
            }
        });

        JLabel lblHora = new JLabel("Hora:");
        lblHora.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHora.setForeground(COLOR_TEAL);
        lblHora.setBounds(390, 190, 50, 25);
        panelForm.add(lblHora);

        cbxHora = new JComboBox<>();
        cbxHora.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbxHora.setBackground(Color.WHITE);
        cbxHora.setBounds(440, 190, 120, 25);
        panelForm.add(cbxHora);

        JPanel panelBotones = new JPanel();
        panelBotones.setBounds(35, 380, 615, 60);
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        contentPanel.add(panelBotones);

        JButton btnGuardar = new JButton("CONFIRMAR CITA");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setBackground(COLOR_ACCENT);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarCita();
            }
        });
        panelBotones.add(btnGuardar);

        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setBackground(new Color(255, 80, 80));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
        
        cargarMedicos();

        if (this.pacientePreseleccionado != null) {
            txtCedula.setText(pacientePreseleccionado.getId());
            txtNombre.setText(pacientePreseleccionado.getName());
            txtApellido.setText(pacientePreseleccionado.getApellido());

            txtCedula.setEditable(false);
            txtCedula.setBackground(COLOR_BLOQUEADO);
            txtNombre.setEditable(false);
            txtNombre.setBackground(COLOR_BLOQUEADO);
            txtApellido.setEditable(false);
            txtApellido.setBackground(COLOR_BLOQUEADO);
        }

        if (this.citaEditar != null) {
            txtCodigo.setText(citaEditar.getId());
            txtCedula.setText(citaEditar.getIdPaciente());
            txtNombre.setText(citaEditar.getNameVisitante()); 
            cbxMedico.setSelectedItem(citaEditar.getMedico());
        }
        
        cargarHorasDisponibles();
    }

    private void cargarMedicos() {
        cbxMedico.removeAllItems();
        
        ArrayList<Medico> medicosAMostrar;
        if (secretariaContexto != null) {
            medicosAMostrar = secretariaContexto.getMedicosAsignados();
        } else {
            medicosAMostrar = SistemaGestion.getInstance().getListaMedicos();
        }
        
        for (Medico m : medicosAMostrar) {
            cbxMedico.addItem(m); 
        }
        cbxMedico.setSelectedIndex(-1);
    }
    
    private void cargarHorasDisponibles() {
        cbxHora.removeAllItems();
        Medico medicoSel = (Medico) cbxMedico.getSelectedItem();
        
        if (medicoSel == null || fechaSeleccionada == null) {
            cbxHora.setEnabled(false);
            return;
        }
        
        ArrayList<LocalTime> slots = medicoSel.getSlotsDisponibles(fechaSeleccionada);
        
        if (slots.isEmpty()) {
            cbxHora.addItem("Sin cupo");
            cbxHora.setEnabled(false);
        } else {
            cbxHora.setEnabled(true);
            for (LocalTime hora : slots) {
                cbxHora.addItem(hora.toString());
            }
        }
    }
    
    private void guardarCita() {
        if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Faltan datos del paciente.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cbxMedico.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un médico.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cbxHora.getSelectedItem() == null || !cbxHora.isEnabled() || cbxHora.getSelectedItem().toString().equals("Sin cupo")) {
            JOptionPane.showMessageDialog(this, "Seleccione una hora válida.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String cedula = txtCedula.getText();
            String nombreCompleto = txtNombre.getText() + " " + txtApellido.getText();
            
            Medico medico = (Medico) cbxMedico.getSelectedItem();
            LocalTime hora = LocalTime.parse((String) cbxHora.getSelectedItem());
            LocalDateTime fechaHoraCita = LocalDateTime.of(fechaSeleccionada, hora);
            
            SistemaGestion.getInstance().crearCita(cedula, nombreCompleto, medico.getId(), fechaHoraCita);
            
            JOptionPane.showMessageDialog(this, "¡Cita agendada con éxito!", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
}