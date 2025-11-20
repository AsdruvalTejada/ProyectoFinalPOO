package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import logico.Paciente;
import logico.SistemaGestion;

@SuppressWarnings("serial")
public class FormBuscarPaciente extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JFormattedTextField txtCedula; 
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JButton btnAgendar;
    
    private final Color COLOR_FONDO       = new Color(254, 251, 246); 
    private final Color COLOR_TEAL        = new Color(10, 186, 181);  
    private final Color COLOR_ACCENT      = new Color(0, 200, 151);   
    private final Color COLOR_TEXTO       = new Color(50, 50, 50);
    private final Color COLOR_CAMPOS_READ = new Color(230, 230, 230);

    private Paciente pacienteEncontrado = null;

    public FormBuscarPaciente() {
        setTitle("Buscar Paciente");
        setBounds(100, 100, 600, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panelHeader = new JPanel();
        panelHeader.setLayout(null);
        panelHeader.setBackground(Color.WHITE);
        panelHeader.setBorder(new LineBorder(COLOR_TEAL, 1)); 
        panelHeader.setBounds(20, 20, 545, 50);
        contentPanel.add(panelHeader);

        JLabel lblTitulo = new JLabel("BUSCAR PACIENTE");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_TEAL);
        lblTitulo.setBounds(0, 0, 545, 50);
        panelHeader.add(lblTitulo);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(null);
        panelDatos.setBackground(COLOR_FONDO);
        panelDatos.setBorder(new LineBorder(COLOR_TEAL, 1));
        panelDatos.setBounds(20, 85, 545, 200);
        contentPanel.add(panelDatos);

        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCedula.setForeground(COLOR_TEXTO);
        lblCedula.setBounds(30, 30, 80, 25);
        panelDatos.add(lblCedula);

        try {
            MaskFormatter mascaraCedula = new MaskFormatter("###-#######-#");
            mascaraCedula.setPlaceholderCharacter('_');
            txtCedula = new JFormattedTextField(mascaraCedula);
        } catch (ParseException e) {
            txtCedula = new JFormattedTextField(); 
        }
        
        txtCedula.setBounds(100, 30, 250, 30);
        txtCedula.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCedula.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_TEAL));
        txtCedula.setBackground(COLOR_FONDO);
        panelDatos.add(txtCedula);

        JButton btnBuscar = new JButton("BUSCAR");
        btnBuscar.setBounds(370, 30, 140, 30);
        btnBuscar.setBackground(COLOR_ACCENT);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarPaciente();
            }
        });
        panelDatos.add(btnBuscar);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblCodigo.setForeground(COLOR_TEXTO);
        lblCodigo.setBounds(30, 80, 80, 25);
        panelDatos.add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setEditable(false); 
        txtCodigo.setBounds(100, 80, 150, 25);
        txtCodigo.setBackground(COLOR_CAMPOS_READ);
        txtCodigo.setBorder(new LineBorder(Color.LIGHT_GRAY));
        txtCodigo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        txtCodigo.setText(" ---");
        panelDatos.add(txtCodigo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setForeground(COLOR_TEXTO);
        lblNombre.setBounds(30, 120, 80, 25);
        panelDatos.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setEditable(false);
        txtNombre.setBounds(100, 120, 150, 25);
        txtNombre.setBackground(COLOR_CAMPOS_READ);
        txtNombre.setBorder(new LineBorder(Color.LIGHT_GRAY));
        txtNombre.setText(" ---");
        panelDatos.add(txtNombre);

        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblApellido.setForeground(COLOR_TEXTO);
        lblApellido.setBounds(270, 120, 80, 25);
        panelDatos.add(lblApellido);

        txtApellido = new JTextField();
        txtApellido.setEditable(false);
        txtApellido.setBounds(340, 120, 170, 25);
        txtApellido.setBackground(COLOR_CAMPOS_READ);
        txtApellido.setBorder(new LineBorder(Color.LIGHT_GRAY));
        txtApellido.setText(" ---");
        panelDatos.add(txtApellido);

        JLabel lblTel = new JLabel("Teléfono:");
        lblTel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTel.setForeground(COLOR_TEXTO);
        lblTel.setBounds(30, 160, 80, 25);
        panelDatos.add(lblTel);

        txtTelefono = new JTextField();
        txtTelefono.setEditable(false);
        txtTelefono.setBounds(100, 160, 150, 25);
        txtTelefono.setBackground(COLOR_CAMPOS_READ);
        txtTelefono.setBorder(new LineBorder(Color.LIGHT_GRAY));
        txtTelefono.setText(" ---");
        panelDatos.add(txtTelefono);
        
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(COLOR_FONDO);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnAgendar = new JButton("AGENDAR CITA");
        btnAgendar.setBackground(Color.WHITE);
        btnAgendar.setForeground(COLOR_ACCENT); 
        btnAgendar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAgendar.setFocusPainted(false);
        btnAgendar.setBorder(new LineBorder(COLOR_ACCENT, 2)); 
        btnAgendar.setEnabled(false); 
        btnAgendar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAgendar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                irAAgendar();
            }
        });
        buttonPane.add(btnAgendar);

        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBackground(new Color(255, 80, 80)); 
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnCancelar);
    }
    
    private void buscarPaciente() {
        String cedula = txtCedula.getText();
        String cedulaLimpia = cedula.replace("-", "").replace("_", "").trim();
        
        if(cedulaLimpia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        pacienteEncontrado = SistemaGestion.getInstance().buscarPacientePorId(cedula);
        
        if(pacienteEncontrado != null) {
            txtCodigo.setText(pacienteEncontrado.getId());
            txtNombre.setText(pacienteEncontrado.getName());
            txtApellido.setText(pacienteEncontrado.getApellido());
            txtTelefono.setText(pacienteEncontrado.getContacto());
            
            btnAgendar.setEnabled(true);
            btnAgendar.setBackground(COLOR_ACCENT);
            btnAgendar.setForeground(Color.WHITE);
            btnAgendar.setBorder(null);
            
        } else {
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Paciente no encontrado.", "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarCampos() {
        txtCodigo.setText(" ---");
        txtNombre.setText(" ---");
        txtApellido.setText(" ---");
        txtTelefono.setText(" ---");
        btnAgendar.setEnabled(false);

        btnAgendar.setBackground(Color.WHITE);
        btnAgendar.setForeground(COLOR_ACCENT);
        btnAgendar.setBorder(new LineBorder(COLOR_ACCENT, 2));
        
        pacienteEncontrado = null;
    }

    private void irAAgendar() {
        if(pacienteEncontrado != null) {
            dispose(); 
            FormAgendarCita formCita = new FormAgendarCita(pacienteEncontrado);
            formCita.setVisible(true);
        }
    }
}