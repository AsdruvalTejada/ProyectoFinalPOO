package visual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelVigilancia extends JPanel {

    private static final long serialVersionUID = 1L;

    public PanelVigilancia() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(220, 53, 69));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setPreferredSize(new Dimension(200, 150)); 
        setMaximumSize(new Dimension(200, 200));
        
        JLabel lblTitulo = new JLabel("VIGILANCIA EPIDEMIOLÓGICA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        add(lblTitulo);
        
        add(new JLabel(" "));
    }

    public void actualizarDatos(String datosRaw) {
        this.removeAll();
        
        JLabel lblTitulo = new JLabel("ALERTA ACTIVA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(Color.YELLOW);
        lblTitulo.setAlignmentX(CENTER_ALIGNMENT);
        add(lblTitulo);
        add(new JLabel(" ")); 

        try {
            if (datosRaw.isEmpty()) {
                JLabel lblNada = new JLabel("Sin casos reportados");
                lblNada.setForeground(Color.WHITE);
                lblNada.setAlignmentX(CENTER_ALIGNMENT);
                add(lblNada);
            } else {
                String[] enfermedades = datosRaw.split(";");
                for (String item : enfermedades) {
                    if(item.contains(":")) {
                        String[] parts = item.split(":");
                        String nombre = parts[0];
                        String cantidad = parts[1];
                        
                        if (!cantidad.equals("0")) {
                            JLabel lblDato = new JLabel("• " + nombre + ": " + cantidad);
                            lblDato.setFont(new Font("Segoe UI", Font.BOLD, 12));
                            lblDato.setForeground(Color.WHITE);
                            lblDato.setAlignmentX(CENTER_ALIGNMENT);
                            add(lblDato);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error renderizando vigilancia");
        }
        
        revalidate();
        repaint();
    }
}