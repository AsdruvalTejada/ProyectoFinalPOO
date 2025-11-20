package visual;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import logico.Usuario; 

@SuppressWarnings("serial")
public class PrincipalAdmin extends JFrame {

    private JPanel contentPane;
    private JPanel pnlSidebar;
    private JPanel pnlMainContent;
    private CardLayout cardLayout;
    
    private Color COLOR_SIDEBAR = new Color(10, 186, 181);
    private Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private Color COLOR_CONTENT_BG = Color.WHITE;
    private Color COLOR_TEXT_LIGHT = Color.WHITE;
    private Color COLOR_ACCENT = new Color(86, 223, 207);
    private Color COLOR_LOGOUT = new Color(220, 53, 69);

    public PrincipalAdmin(Usuario user) {
        
        setTitle("Panel de Administración - Clínica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1024, 768);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(COLOR_BACKGROUND);
        setContentPane(contentPane);

        pnlSidebar = new JPanel();
        pnlSidebar.setBackground(COLOR_SIDEBAR);
        pnlSidebar.setLayout(new GridLayout(12, 1, 0, 0)); 
        contentPane.add(pnlSidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        pnlMainContent = new JPanel(cardLayout);
        pnlMainContent.setBackground(COLOR_BACKGROUND);
        pnlMainContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.add(pnlMainContent, BorderLayout.CENTER);

        JButton btnCitas = createSidebarButton("Gestión de Citas");
        JButton btnPacientes = createSidebarButton("Gestión de Pacientes");
        JButton btnPersonal = createSidebarButton("Gestión de Personal");
        JButton btnUsuarios = createSidebarButton("Gestión de Usuarios");
        JButton btnReportes = createSidebarButton("Reportes");
        JButton btnCatalogos = createSidebarButton("Catálogos");
        
        pnlSidebar.add(btnCitas);
        pnlSidebar.add(btnPacientes);
        pnlSidebar.add(btnPersonal);
        pnlSidebar.add(btnUsuarios);
        pnlSidebar.add(btnReportes);
        pnlSidebar.add(btnCatalogos);
        
        pnlSidebar.add(new JLabel("")); 
        pnlSidebar.add(new JLabel("")); 
        pnlSidebar.add(new JLabel("")); 
        pnlSidebar.add(new JLabel("")); 
        
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogout.setForeground(COLOR_LOGOUT);
        btnLogout.setBackground(COLOR_SIDEBAR);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> cerrarSesion());
        pnlSidebar.add(btnLogout);

        // --- PANELES ---
        // JPanel panelCitas = new PanelAdmin_Citas(COLOR_ACCENT); 
        // JPanel panelPacientes = new PanelAdmin_Pacientes(COLOR_ACCENT);
        JPanel panelPersonal = new PanelAdmin_Personal(COLOR_ACCENT);
        // JPanel panelUsuarios = new PanelAdmin_Usuarios(COLOR_ACCENT);
        JPanel panelReportes = new PanelAdmin_Reportes(COLOR_ACCENT);
        // JPanel panelCatalogos = new PanelAdmin_Catalogos(COLOR_ACCENT); 
        
        // Placeholder panels
        pnlMainContent.add(createPlaceholderPanel("Gestión de Citas"), "citas");
        pnlMainContent.add(createPlaceholderPanel("Gestión de Pacientes"), "pacientes");
        pnlMainContent.add(panelPersonal, "personal");
        pnlMainContent.add(createPlaceholderPanel("Gestión de Usuarios"), "usuarios");
        pnlMainContent.add(panelReportes, "reportes");
        pnlMainContent.add(createPlaceholderPanel("Catálogos"), "catalogos");

        // Eventos
        btnCitas.addActionListener(e -> cardLayout.show(pnlMainContent, "citas"));
        btnPacientes.addActionListener(e -> cardLayout.show(pnlMainContent, "pacientes"));
        btnPersonal.addActionListener(e -> cardLayout.show(pnlMainContent, "personal"));
        btnUsuarios.addActionListener(e -> cardLayout.show(pnlMainContent, "usuarios"));
        btnReportes.addActionListener(e -> cardLayout.show(pnlMainContent, "reportes"));
        btnCatalogos.addActionListener(e -> cardLayout.show(pnlMainContent, "catalogos"));
        
        // Vista inicial
        cardLayout.show(pnlMainContent, "personal"); // Mostramos personal por ahora para probar
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea cerrar sesión y volver al login?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            dispose();
            Login login = new Login();
            login.setVisible(true);
        }
    }
    
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(COLOR_TEXT_LIGHT);
        button.setBackground(COLOR_SIDEBAR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(COLOR_ACCENT.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(COLOR_SIDEBAR);
            }
        });
        return button;
    }
    
    private JPanel createPlaceholderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_CONTENT_BG);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(lblTitle, BorderLayout.NORTH);
        return panel;
    }
}