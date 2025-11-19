package visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Usuario;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class Principal extends JFrame {

    private JPanel contentPane;
    private Usuario userLogueado;

    private CalendarioSecretaria panelCalendario;

    /**
     * Create the frame.
     */
    public Principal(Usuario user) {
        this.userLogueado = user;
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(100, 100, Math.min(1200, screenSize.width - 50), Math.min(800, screenSize.height - 50));
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarSesion();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnSistema = new JMenu("Opciones");
        menuBar.add(mnSistema);

        JMenuItem mntmCerrarSesion = new JMenuItem("Cerrar Sesión");
        mntmCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
        mnSistema.add(mntmCerrarSesion);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        visualPorRol();
    }

    private void visualPorRol() {
        String rol = userLogueado.getRol();
        setTitle("Clínica - " + "Usuario: " + userLogueado.getUsername() + " (" + rol + ")");
        
        if(rol.equalsIgnoreCase("ADMIN")) {
            JOptionPane.showMessageDialog(this, "Bienvenido Admin.");
            
        } else if(rol.equalsIgnoreCase("SECRETARIA")) {
            panelCalendario = new CalendarioSecretaria();
            contentPane.add(panelCalendario, BorderLayout.CENTER);
            contentPane.revalidate();
            contentPane.repaint();
            
        } else if(rol.equalsIgnoreCase("MEDICO")){
            JOptionPane.showMessageDialog(this, "Bienvenido Doctor.");
        }
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            Login login = new Login();
            login.setVisible(true);
        }
    }
}