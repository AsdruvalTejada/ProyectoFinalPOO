package visual;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.SistemaGestion;
import logico.Usuario; 

@SuppressWarnings("serial")
public class PrincipalAdmin extends JFrame {

    private JPanel contentPane;
    private JPanel pnlSidebar;
    private JPanel pnlMainContent;
    private CardLayout cardLayout;
    
    private PanelVigilancia panelVigilancia;
    
    private Color COLOR_SIDEBAR = new Color(0, 51, 102);
    private Color COLOR_BACKGROUND = new Color(248, 249, 250);
    private Color COLOR_TEXT_LIGHT = Color.WHITE;
    private Color COLOR_ACCENT = new Color(86, 223, 207);
    private Color colorito = new Color(0, 52, 102);

    public PrincipalAdmin(Usuario user) {
        
        setTitle("Panel de Administración - Clínica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1024, 768);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(COLOR_BACKGROUND);
        setContentPane(contentPane);

        pnlSidebar = new JPanel();
        pnlSidebar.setBackground(COLOR_SIDEBAR);
        pnlSidebar.setPreferredSize(new Dimension(220, 0));
        pnlSidebar.setLayout(new BorderLayout());
        contentPane.add(pnlSidebar, BorderLayout.WEST);

        JPanel pnlMenuNorte = new JPanel(new GridLayout(0, 1, 0, 5));
        pnlMenuNorte.setBackground(COLOR_SIDEBAR);
        pnlMenuNorte.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JButton btnCitas = createSidebarButton("Gestión de Citas");
        JButton btnPacientes = createSidebarButton("Gestión de Pacientes");
        JButton btnPersonal = createSidebarButton("Gestión de Personal");
        JButton btnUsuarios = createSidebarButton("Gestión de Usuarios");
        JButton btnReportes = createSidebarButton("Reportes");
        JButton btnCatalogos = createSidebarButton("Catálogos");
        
        pnlMenuNorte.add(btnCitas);
        pnlMenuNorte.add(btnPacientes);
        pnlMenuNorte.add(btnPersonal);
        pnlMenuNorte.add(btnUsuarios);
        pnlMenuNorte.add(btnReportes);
        pnlMenuNorte.add(btnCatalogos);
        
        pnlSidebar.add(pnlMenuNorte, BorderLayout.NORTH);

        JPanel pnlMenuCentro = new JPanel();
        pnlMenuCentro.setLayout(new BoxLayout(pnlMenuCentro, BoxLayout.Y_AXIS));
        pnlMenuCentro.setBackground(COLOR_SIDEBAR);
        pnlMenuCentro.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        panelVigilancia = new PanelVigilancia();
        panelVigilancia.actualizarDatos(SistemaGestion.getInstance().getDatosVigilanciaSocket());
        panelVigilancia.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        
        pnlMenuCentro.add(Box.createVerticalGlue());
        pnlMenuCentro.add(panelVigilancia);
        pnlMenuCentro.add(Box.createVerticalGlue());
        
        pnlSidebar.add(pnlMenuCentro, BorderLayout.CENTER);

        JPanel pnlMenuSur = new JPanel(new GridLayout(0, 1, 0, 5));
        pnlMenuSur.setBackground(COLOR_SIDEBAR);
        pnlMenuSur.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JButton btnBackup = createSidebarButton("Crear Backup");
        btnBackup.addActionListener(e -> {
            new Thread(() -> {
                boolean exito = SistemaGestion.getInstance().respaldoRemoto();
                javax.swing.SwingUtilities.invokeLater(() -> {
                    if (exito) {
                        JOptionPane.showMessageDialog(this, "Copia de seguridad enviada correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al conectar con el servidor de respaldo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        });
        
        JButton btnLogout = createSidebarButton("Cerrar Sesión");
        btnLogout.addActionListener(e -> cerrarSesion());
        
        pnlMenuSur.add(btnBackup);
        pnlMenuSur.add(btnLogout);
        
        pnlSidebar.add(pnlMenuSur, BorderLayout.SOUTH);

        cardLayout = new CardLayout();
        pnlMainContent = new JPanel(cardLayout);
        pnlMainContent.setBackground(COLOR_BACKGROUND);
        pnlMainContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.add(pnlMainContent, BorderLayout.CENTER);

        JPanel panelCitas = new PanelAdmin_Citas(colorito); 
        JPanel panelPacientes = new PanelAdmin_Pacientes(colorito);
        JPanel panelPersonal = new PanelAdmin_Personal(colorito);
        JPanel panelUsuarios = new PanelAdmin_Usuarios(colorito);
        JPanel panelReportes = new PanelAdmin_Reportes(colorito);
        JPanel panelCatalogos = new PanelAdmin_Catalogos(colorito); 
        
        pnlMainContent.add(panelCitas, "citas");
        pnlMainContent.add(panelPacientes, "pacientes");
        pnlMainContent.add(panelPersonal, "personal");
        pnlMainContent.add(panelUsuarios, "usuarios");
        pnlMainContent.add(panelReportes, "reportes");
        pnlMainContent.add(panelCatalogos, "catalogos");

        btnCitas.addActionListener(e -> cardLayout.show(pnlMainContent, "citas"));
        btnPacientes.addActionListener(e -> cardLayout.show(pnlMainContent, "pacientes"));
        btnPersonal.addActionListener(e -> cardLayout.show(pnlMainContent, "personal"));
        btnUsuarios.addActionListener(e -> cardLayout.show(pnlMainContent, "usuarios"));
        btnReportes.addActionListener(e -> cardLayout.show(pnlMainContent, "reportes"));
        btnCatalogos.addActionListener(e -> cardLayout.show(pnlMainContent, "catalogos"));
        
        cardLayout.show(pnlMainContent, "pacientes");
        
        iniciarEscuchaVigilancia();
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
        button.setPreferredSize(new Dimension(220, 50));
        
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
    
    private void iniciarEscuchaVigilancia() {
        new Thread(() -> {
            try {
                @SuppressWarnings("resource")
                java.net.Socket s = new java.net.Socket("127.0.0.1", 7000);
                java.io.DataInputStream in = new java.io.DataInputStream(s.getInputStream());
                java.io.DataOutputStream out = new java.io.DataOutputStream(s.getOutputStream());
                
                out.writeUTF("LOGIN:ADMIN_MONITOR"); 
                
                while(true) {
                    String msg = in.readUTF();
                    
                    if (msg.startsWith("VIGILANCIA;")) {
                        String datos = msg.split(";", 2)[1];
                        
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            panelVigilancia.actualizarDatos(datos);
                        });
                    }
                }
            } catch (Exception e) {
                System.out.println("Monitor desconectado del servidor.");
            }
        }).start();
    }
}