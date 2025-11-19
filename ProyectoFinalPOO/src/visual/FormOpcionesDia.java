package visual;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Cita;
import logico.SistemaGestion;

import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FormOpcionesDia extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private CalendarioSecretaria panelPadre; 

    public FormOpcionesDia(LocalDate fechaSeleccionada, boolean tieneCitaPrevia, CalendarioSecretaria padre) {
        this.panelPadre = padre;
        
        setTitle("Opciones: " + fechaSeleccionada.toString());
        setBounds(100, 100, 300, 250);
        setLocationRelativeTo(null);
        setModal(true);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(0, 1, 10, 10));

        JLabel lblTitulo = new JLabel("Acciones para el día " + fechaSeleccionada.getDayOfMonth());
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        contentPane.add(lblTitulo);

        JButton btnAgregar = new JButton("1. Agregar Cita");
        btnAgregar.setBackground(new Color(0, 200, 151));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
                
                FormAgendarCita form = new FormAgendarCita(fechaSeleccionada, null);
                form.setVisible(true);
                
                panelPadre.refrescarCalendario();
            }
        });
        contentPane.add(btnAgregar);

        if (tieneCitaPrevia) {
            
            JButton btnBuscar = new JButton("2. Ver Citas del Día");
            btnBuscar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Citas para el ").append(fechaSeleccionada).append(":\n");
                    
                    for(Cita c : SistemaGestion.getInstance().getListaCitas()) {
                        if(c.getFechaCitada().toLocalDate().equals(fechaSeleccionada) && !c.getEstado().equalsIgnoreCase("Cancelada")) {
                            sb.append("- ").append(c.getId()).append(": ").append(c.getNameVisitante())
                              .append(" (").append(c.getFechaCitada().toLocalTime()).append(")\n");
                        }
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());
                }
            });
            contentPane.add(btnBuscar);

            JButton btnCancelar = new JButton("3. Cancelar Cita");
            btnCancelar.setBackground(new Color(255, 100, 100));
            btnCancelar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String idACancelar = JOptionPane.showInputDialog("Ingrese el ID de la cita a cancelar (Ej: C-001):");
                    if(idACancelar != null && !idACancelar.isEmpty()) {
                        SistemaGestion.getInstance().cancelarCita(idACancelar);
                        JOptionPane.showMessageDialog(null, "Proceso de cancelación finalizado.");
                        dispose();
                        panelPadre.refrescarCalendario();
                    }
                }
            });
            contentPane.add(btnCancelar);
        }
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        contentPane.add(btnCerrar);
    }
}