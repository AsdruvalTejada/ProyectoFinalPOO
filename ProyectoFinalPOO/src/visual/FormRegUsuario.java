package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import logico.Medico;
import logico.Secretaria;
import logico.SistemaGestion;
import logico.Usuario;

public class FormRegUsuario extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPass;
    private JComboBox<String> cbxRol;
    private JComboBox<String> cbxPersona;
    
    private Usuario usuarioEdicion = null;

    private final Color COLOR_FONDO = new Color(253, 247, 238);
    private final Color COLOR_VERDE_BOTON = new Color(0, 168, 107);
    private final Color COLOR_TEXTO = new Color(60, 60, 60);

    public FormRegUsuario(Usuario user) {
        this.usuarioEdicion = user;
        
        setTitle(user == null ? "Registrar Nuevo Usuario" : "Editar Credenciales");
        setBounds(100, 100, 500, 480);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(20, 20, 445, 50);
        panelHeader.setBackground(COLOR_FONDO);
        panelHeader.setBorder(new LineBorder(Color.GRAY, 1, true));
        panelHeader.setLayout(new BorderLayout(0, 0));
        contentPanel.add(panelHeader);
        
        JLabel lblTitulo = new JLabel(user == null ? "Creación de Acceso" : "Edición de Acceso");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        panelHeader.add(lblTitulo, BorderLayout.CENTER);

        JPanel panelForm = new JPanel();
        panelForm.setBounds(20, 85, 445, 280);
        panelForm.setBackground(COLOR_FONDO);
        panelForm.setBorder(new LineBorder(Color.GRAY, 1, true));
        panelForm.setLayout(null);
        contentPanel.add(panelForm);

        JLabel lblRol = new JLabel("Tipo de Usuario:");
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblRol.setBounds(30, 20, 120, 25);
        panelForm.add(lblRol);
        
        cbxRol = new JComboBox<>();
        cbxRol.setModel(new DefaultComboBoxModel<>(new String[] {"Seleccione...", "MEDICO", "SECRETARIA", "ADMIN"}));
        cbxRol.setBackground(Color.WHITE);
        cbxRol.setBounds(160, 20, 200, 25);
        cbxRol.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarPersonasDisponibles();
            }
        });
        panelForm.add(cbxRol);

        JLabel lblPersona = new JLabel("Vincular a:");
        lblPersona.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPersona.setBounds(30, 60, 120, 25);
        panelForm.add(lblPersona);
        
        cbxPersona = new JComboBox<>();
        cbxPersona.setBackground(Color.WHITE);
        cbxPersona.setBounds(160, 60, 250, 25);
        cbxPersona.setEnabled(false);
        panelForm.add(cbxPersona);

        JLabel lblSep = new JLabel("_______________________________________________________");
        lblSep.setForeground(Color.LIGHT_GRAY);
        lblSep.setBounds(30, 90, 400, 14);
        panelForm.add(lblSep);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setBounds(30, 120, 100, 25);
        panelForm.add(lblUser);
        
        txtUsername = new JTextField();
        txtUsername.setBounds(160, 120, 200, 25);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelForm.add(txtUsername);
        
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPass.setBounds(30, 160, 100, 25);
        panelForm.add(lblPass);
        
        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 160, 200, 25);
        panelForm.add(txtPassword);
        
        JLabel lblConfirm = new JLabel("Confirmar:");
        lblConfirm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblConfirm.setBounds(30, 200, 100, 25);
        panelForm.add(lblConfirm);
        
        txtConfirmPass = new JPasswordField();
        txtConfirmPass.setBounds(160, 200, 200, 25);
        panelForm.add(txtConfirmPass);
        
        if(usuarioEdicion != null) {
            JLabel lblNota = new JLabel("<html><font size='2' color='gray'>Deje las contraseñas en blanco para mantener la actual.</font></html>");
            lblNota.setBounds(160, 230, 250, 30);
            panelForm.add(lblNota);
        }

        JPanel panelBotones = new JPanel();
        panelBotones.setBounds(20, 380, 445, 50);
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        contentPanel.add(panelBotones);

        JButton btnRegistrar = new JButton("GUARDAR");
        btnRegistrar.setBackground(COLOR_VERDE_BOTON);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gestionarGuardado();
            }
        });
        panelBotones.add(btnRegistrar);

        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
        
        cargarDatosSiEsEdicion();
    }
    
    private void cargarPersonasDisponibles() {
        cbxPersona.removeAllItems();
        String rolSel = (String) cbxRol.getSelectedItem();
        
        if (rolSel == null || rolSel.equals("Seleccione...")) {
            cbxPersona.setEnabled(false);
            return;
        }
        
        cbxPersona.setEnabled(true);
        ArrayList<Usuario> usuariosExistentes = SistemaGestion.getInstance().getListaUsuarios();
        
        if (rolSel.equals("ADMIN")) {
            cbxPersona.addItem("N/A - Administrador");
            cbxPersona.setEnabled(false); 
            
        } else if (rolSel.equals("MEDICO")) {
            for (Medico m : SistemaGestion.getInstance().getListaMedicos()) {
                boolean tieneUsuario = false;
                for (Usuario u : usuariosExistentes) {
                    if (u.getIdPersonaVinculada().equals(m.getId())) {
                         if(usuarioEdicion != null && u.equals(usuarioEdicion)) {
                             tieneUsuario = false; 
                         } else {
                             tieneUsuario = true;
                         }
                         break;
                    }
                }
                if (!tieneUsuario) {
                    cbxPersona.addItem(m.getId() + " - " + m.getNombreCompleto());
                }
            }
            
        } else if (rolSel.equals("SECRETARIA")) {
            for (Secretaria s : SistemaGestion.getInstance().getListaSecretarias()) {
                boolean tieneUsuario = false;
                for (Usuario u : usuariosExistentes) {
                    if (u.getIdPersonaVinculada().equals(s.getId())) {
                        if(usuarioEdicion != null && u.equals(usuarioEdicion)) {
                             tieneUsuario = false; 
                         } else {
                             tieneUsuario = true;
                         }
                        break;
                    }
                }
                if (!tieneUsuario) {
                    cbxPersona.addItem(s.getId() + " - " + s.getNombreCompleto());
                }
            }
        }
    }
    
    private void cargarDatosSiEsEdicion() {
        if(usuarioEdicion != null) {
            txtUsername.setText(usuarioEdicion.getUsername());
            cbxRol.setSelectedItem(usuarioEdicion.getRol());
            cbxRol.setEnabled(false);
            
            cargarPersonasDisponibles();
            
            String idVinculado = usuarioEdicion.getIdPersonaVinculada();
            for(int i=0; i<cbxPersona.getItemCount(); i++) {
                String item = cbxPersona.getItemAt(i);
                if(item.startsWith(idVinculado)) {
                    cbxPersona.setSelectedIndex(i);
                    break;
                }
            }
            cbxPersona.setEnabled(false);
        }
    }

    private void gestionarGuardado() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirmPass.getPassword());
        
        if (user.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario es obligatorio.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        for (Usuario u : SistemaGestion.getInstance().getListaUsuarios()) {
            if (u.getUsername().equalsIgnoreCase(user)) {
                if(usuarioEdicion == null || !u.equals(usuarioEdicion)) {
                    JOptionPane.showMessageDialog(this, "El nombre de usuario ya está en uso.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        
        if(usuarioEdicion == null) {
            if(pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar una contraseña.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String rol = (String) cbxRol.getSelectedItem();
            String seleccionPersona = (String) cbxPersona.getSelectedItem();
            
            if (rol.equals("Seleccione...") || (seleccionPersona == null && !rol.equals("ADMIN"))) {
                JOptionPane.showMessageDialog(this, "Faltan datos de vinculación.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String idVinculado = "";
            if (rol.equals("ADMIN")) {
                idVinculado = "ADMIN-" + (SistemaGestion.getInstance().getListaUsuarios().size() + 1);
            } else {
                idVinculado = seleccionPersona.split(" - ")[0];
            }

            Usuario nuevoUsuario = new Usuario(user, pass, rol, idVinculado);
            SistemaGestion.getInstance().getListaUsuarios().add(nuevoUsuario);
            JOptionPane.showMessageDialog(this, "Usuario creado exitosamente.");
            
        } else {
            if(!pass.isEmpty()) {
                if (!pass.equals(confirm)) {
                    JOptionPane.showMessageDialog(this, "Las contraseñas nuevas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                usuarioEdicion.setPassword(pass);
            }
            
            usuarioEdicion.setUsername(user);
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");
        }
        
        dispose();
    }
}