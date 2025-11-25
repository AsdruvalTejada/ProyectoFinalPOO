package visual;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import logico.Enfermedad;
import logico.SistemaGestion;

@SuppressWarnings("serial")
public class PanelAdmin_Reportes extends JPanel {

    private Color colorAccent;

    public PanelAdmin_Reportes(Color accent) {
        this.colorAccent = accent;
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new GridLayout(1, 2, 20, 0)); 
        
        JPanel pnlEnfermedades = new JPanel();
        pnlEnfermedades.setBackground(new Color(245, 250, 255));
        pnlEnfermedades.setBorder(new TitledBorder(new LineBorder(colorAccent, 2), "Epidemiología", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        pnlEnfermedades.setLayout(null);
        add(pnlEnfermedades);

        JButton btnVerEnfermedades = new JButton("Ver Reporte Gráfico");
        btnVerEnfermedades.setBounds(50, 100, 200, 50);
        btnVerEnfermedades.setBackground(colorAccent);
        btnVerEnfermedades.setForeground(Color.WHITE);
        btnVerEnfermedades.setFocusPainted(false);
        btnVerEnfermedades.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HashMap<Enfermedad, Integer> datos = SistemaGestion.getInstance().getReporteEnfermedades();
                
                DialogVerReporte dialog = new DialogVerReporte(datos, "Reporte de Enfermedades", 1);
                dialog.setVisible(true);
            }
        });
        pnlEnfermedades.add(btnVerEnfermedades);
        JPanel pnlVacunas = new JPanel();
        pnlVacunas.setBackground(new Color(245, 255, 250));
        pnlVacunas.setBorder(new TitledBorder(new LineBorder(new Color(0, 168, 107), 2), "Control de Vacunación", TitledBorder.CENTER, TitledBorder.TOP, null, null));
        pnlVacunas.setLayout(null);
        add(pnlVacunas);

        JButton btnVerVacunas = new JButton("Ver Reporte Gráfico");
        btnVerVacunas.setBounds(50, 100, 200, 50);
        btnVerVacunas.setBackground(new Color(0, 168, 107));
        btnVerVacunas.setForeground(Color.WHITE);
        btnVerVacunas.setFocusPainted(false);
        btnVerVacunas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HashMap<String, Integer> datos = SistemaGestion.getInstance().getReporteVacunacion();
                DialogVerReporte dialog = new DialogVerReporte(datos, "Reporte de Vacunación", 2);
                dialog.setVisible(true);
            }
        });
        pnlVacunas.add(btnVerVacunas);//
    }
}