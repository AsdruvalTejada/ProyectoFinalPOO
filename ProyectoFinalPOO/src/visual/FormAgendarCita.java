package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

import logico.Cita;
import logico.Medico;
import logico.SistemaGestion;

public class FormAgendarCita extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField txtCodigo;
    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtFecha;
    private JComboBox<Medico> cbxMedico;
    private JComboBox<String> cbxHora;
    private LocalDate fechaSeleccionada;
    private Cita citaEditar;

    private final Color COLOR_FONDO = new Color(253, 247, 238);
    private final Color COLOR_VERDE_BOTON = new Color(0, 168, 107);
    private final Color COLOR_GRIS_CAMPO = new Color(220, 220, 220);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);

    public FormAgendarCita(LocalDate fechaIn, Cita citaIn) {
        this.fechaSeleccionada = fechaIn;
        this.citaEditar = citaIn;
        setTitle("Agendar Cita");
        setBounds(100, 100, 700, 450);
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
        panelHeader.setBorder(new LineBorder(Color.GRAY, 1, true));
        panelHeader.setLayout(new BorderLayout(0, 0));
        contentPanel.add(panelHeader);
        
        JLabel lblTitulo = new JLabel("Agendar Cita");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        panelHeader.add(lblTitulo, BorderLayout.CENTER);

        JPanel panelForm = new JPanel();
        panelForm.setBounds(35, 85, 615, 240);
        panelForm.setBackground(COLOR_FONDO);
        panelForm.setBorder(new LineBorder(Color.GRAY, 1, true));
        panelForm.setLayout(null);
        contentPanel.add(panelForm);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblCodigo.setForeground(COLOR_TEXTO);
        lblCodigo.setBounds(30, 20, 80, 25);
        panelForm.add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setEditable(false);
        txtCodigo.setBackground(COLOR_GRIS_CAMPO);
        txtCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCodigo.setBounds(120, 20, 200, 25);
        panelForm.add(txtCodigo);
        txtCodigo.setColumns(10);
        txtCodigo.setText("C - 0 " + (SistemaGestion.genIdCita + 1)); 

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblCedula.setForeground(COLOR_TEXTO);
        lblCedula.setBounds(30, 60, 80, 25);
        panelForm.add(lblCedula);

        txtCedula = new JTextField();
        txtCedula.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCedula.setBounds(120, 60, 300, 25);
        panelForm.add(txtCedula);
        txtCedula.setColumns(10);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblNombre.setForeground(COLOR_TEXTO);
        lblNombre.setBounds(30, 100, 80, 25);
        panelForm.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNombre.setBounds(120, 100, 200, 25);
        panelForm.add(txtNombre);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblApellido.setForeground(COLOR_TEXTO);
        lblApellido.setBounds(340, 100, 80, 25);
        panelForm.add(lblApellido);

        txtApellido = new JTextField();
        txtApellido.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtApellido.setBounds(420, 100, 170, 25);
        panelForm.add(txtApellido);

        JLabel lblMedico = new JLabel("Médico:");
        lblMedico.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblMedico.setForeground(COLOR_TEXTO);
        lblMedico.setBounds(30, 140, 80, 25);
        panelForm.add(lblMedico);

        cbxMedico = new JComboBox<>();
        cbxMedico.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbxMedico.setBackground(Color.WHITE);
        cbxMedico.setBounds(120, 140, 280, 25);
        panelForm.add(cbxMedico);
   
        cbxMedico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarHorasDisponibles();
            }
        });

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblFecha.setForeground(COLOR_TEXTO);
        lblFecha.setBounds(30, 180, 80, 25);
        panelForm.add(lblFecha);

        txtFecha = new JTextField();
        txtFecha.setEditable(false);
        txtFecha.setBackground(COLOR_GRIS_CAMPO);
        txtFecha.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        txtFecha.setText(" Se auto rellena con la fecha seleccionada del calendario");
        txtFecha.setBounds(120, 180, 280, 25);
        panelForm.add(txtFecha);
        
        JLabel lblHora = new JLabel("Hora:");
        lblHora.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblHora.setForeground(COLOR_TEXTO);
        lblHora.setBounds(420, 180, 60, 25);
        panelForm.add(lblHora);

        cbxHora = new JComboBox<>();
        cbxHora.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbxHora.setBackground(Color.WHITE);
        cbxHora.setBounds(480, 180, 110, 25);
        panelForm.add(cbxHora);

        JPanel panelBotones = new JPanel();
        panelBotones.setBounds(35, 340, 615, 60);
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(new LineBorder(Color.GRAY, 1, true));
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        contentPanel.add(panelBotones);

        JButton btnGuardar = new JButton("GUARDAR CITA");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setBackground(COLOR_VERDE_BOTON);
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
        btnCancelar.setBackground(COLOR_VERDE_BOTON);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnCancelar);
        
        cargarMedicos();
        
        if (this.fechaSeleccionada != null) {
            txtFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtFecha.setText(this.fechaSeleccionada.toString());
        }
        
        if (this.citaEditar != null) {
            txtCodigo.setText(citaEditar.getId());
            txtCedula.setText(citaEditar.getIdPaciente());
            txtNombre.setText(citaEditar.getNameVisitante()); 
            // ... aquí podremos cargar el médico y la hora original
        }
        
        cargarHorasDisponibles();
    }

    private void cargarMedicos() {
        cbxMedico.removeAllItems();
        for (Medico m : SistemaGestion.getInstance().getListaMedicos()) {
            cbxMedico.addItem(m); 
        }
    }
    
    private void cargarHorasDisponibles() {
        cbxHora.removeAllItems();
        Medico medicoSel = (Medico) cbxMedico.getSelectedItem();
        
        if (medicoSel != null && fechaSeleccionada != null) {
            ArrayList<LocalTime> slots = medicoSel.getSlotsDisponibles(fechaSeleccionada);
            
            if (slots.isEmpty()) {
                cbxHora.addItem("---");
                cbxHora.setEnabled(false);
            } else {
                cbxHora.setEnabled(true);
                for (LocalTime hora : slots) {
                    cbxHora.addItem(hora.toString());
                }
            }
        }
    }
    
    private void guardarCita() {
        if (txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llene los campos obligatorios.", "Faltan Datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (cbxHora.getSelectedItem() == null || !cbxHora.isEnabled()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un horario válido.", "Hora Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String cedula = txtCedula.getText();
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String nombreCompleto = nombre + " " + apellido;
            
            Medico medico = (Medico) cbxMedico.getSelectedItem();
            LocalTime hora = LocalTime.parse((String) cbxHora.getSelectedItem());
            LocalDateTime fechaHoraCita = LocalDateTime.of(fechaSeleccionada, hora);
            
            SistemaGestion.getInstance().crearCita(cedula, nombreCompleto, medico.getId(), fechaHoraCita);
            
            JOptionPane.showMessageDialog(this, "Cita agendada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }
}
