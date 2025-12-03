package visual;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import logico.SistemaGestion;
import logico.Usuario;

@SuppressWarnings("serial")
public class Login extends JFrame {

    private JPanel contentPane;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    private final Color COLOR_SIDEBAR_BG = new Color(0, 51, 102); 
    private final Color COLOR_BTN_TEXT   = new Color(10, 186, 181);

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	SistemaGestion.cargarDatos();
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 450);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(null);
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 2, 0, 0)); 

        JPanel pnlIzquierdo = new JPanel();
        pnlIzquierdo.setBackground(Color.WHITE); 
        pnlIzquierdo.setLayout(new GridLayout(1, 1)); 
        contentPane.add(pnlIzquierdo);
        
        JLabel lblLogoCompleto = new JLabel("");
        lblLogoCompleto.setHorizontalAlignment(SwingConstants.CENTER);

        String rutaA = "recursos/logo.jpeg";
        String rutaB = "logo proycto.jpeg";
        File archivoA = new File(rutaA);
        File archivoB = new File(rutaB);
        
        if(archivoA.exists()) {
            cargarImagen(lblLogoCompleto, rutaA);
        } else if (archivoB.exists()) {
            cargarImagen(lblLogoCompleto, rutaB);
        } else {
            lblLogoCompleto.setText("LOGO NO ENCONTRADO");
            lblLogoCompleto.setForeground(Color.RED);
        }
        pnlIzquierdo.add(lblLogoCompleto);

        JPanel pnlDerecho = new JPanel();
        pnlDerecho.setBackground(COLOR_SIDEBAR_BG);
        pnlDerecho.setLayout(null);
        contentPane.add(pnlDerecho);
        
        JLabel lblCerrar = new JLabel("X");
        lblCerrar.setHorizontalAlignment(SwingConstants.CENTER);
        lblCerrar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCerrar.setForeground(Color.WHITE);
        lblCerrar.setBounds(330, 10, 30, 30);
        lblCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblCerrar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { System.exit(0); }
            public void mouseEntered(MouseEvent e) { lblCerrar.setForeground(Color.RED); }
            public void mouseExited(MouseEvent e) { lblCerrar.setForeground(Color.WHITE); }
        });
        pnlDerecho.add(lblCerrar);
        
        JLabel lblLoginTitulo = new JLabel("INICIAR SESIÓN");
        lblLoginTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLoginTitulo.setForeground(Color.WHITE);
        lblLoginTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLoginTitulo.setBounds(0, 60, 375, 40);
        pnlDerecho.add(lblLoginTitulo);

        JLabel lblUser = new JLabel("Usuario");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(Color.WHITE); 
        lblUser.setBounds(50, 130, 200, 20);
        pnlDerecho.add(lblUser);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(50, 150, 275, 30);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setForeground(Color.BLACK);
        txtUsuario.setBackground(Color.WHITE); 
        txtUsuario.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        pnlDerecho.add(txtUsuario);
        
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(Color.WHITE);
        lblPass.setBounds(50, 200, 200, 20);
        pnlDerecho.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 220, 275, 30);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlDerecho.add(txtPassword);

        JButton btnEntrar = new JButton("INGRESAR");
        btnEntrar.setBounds(50, 290, 275, 40);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setForeground(COLOR_BTN_TEXT);
        btnEntrar.setBackground(Color.WHITE);  
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btnEntrar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                btnEntrar.setBackground(new Color(230, 230, 230)); 
            }
            public void mouseExited(MouseEvent e) { 
                btnEntrar.setBackground(Color.WHITE); 
            }
        });
        
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validarIngreso();
            }
        });
        pnlDerecho.add(btnEntrar);
        
        JLabel lblFooter = new JLabel("© 2025 Clínica System");
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblFooter.setForeground(new Color(220, 220, 220));
        lblFooter.setBounds(0, 420, 375, 20);
        pnlDerecho.add(lblFooter);
    }

    private void cargarImagen(JLabel lbl, String ruta) {
        ImageIcon iconOriginal = new ImageIcon(ruta);
        Image imgEscalada = iconOriginal.getImage().getScaledInstance(375, 450, Image.SCALE_SMOOTH);
        lbl.setIcon(new ImageIcon(imgEscalada));
    }
    
    private void validarIngreso() {
        String username = txtUsuario.getText();
        String password = new String(txtPassword.getPassword());
        
        Usuario userValid = SistemaGestion.getInstance().validarLogin(username, password);

        if (userValid != null) {
            String rol = userValid.getRol();
            
            if (rol.equalsIgnoreCase("ADMIN")) {
                PrincipalAdmin adminFrame = new PrincipalAdmin(userValid);
                adminFrame.setVisible(true);
            } 
            else if (rol.equalsIgnoreCase("SECRETARIA")) {
                PrincipalSecretaria secFrame = new PrincipalSecretaria(userValid);
                secFrame.setVisible(true);
            } 
            else if (rol.equalsIgnoreCase("MEDICO")) {
            	PrincipalMedico medicoFrame = new PrincipalMedico(userValid);
                medicoFrame.setVisible(true);            
            } 
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
}