package visual;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Secretaria;

import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class FormOpcionesDia extends JDialog {

    private JPanel contentPane;
    private CalendarioSecretaria panelPadre;
    private Secretaria secretariaLogueada;

    public FormOpcionesDia(LocalDate fechaSeleccionada, boolean tieneCitaPrevia, CalendarioSecretaria padre, Secretaria secre) {
        this.panelPadre = padre;
        this.secretariaLogueada = secre;
        
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
                FormAgendarCita form = new FormAgendarCita(fechaSeleccionada, null, secretariaLogueada);
                form.setVisible(true);
                panelPadre.refrescarCalendario();
            }
        });
        contentPane.add(btnAgregar);

        if (tieneCitaPrevia) {
            
            JButton btnBuscar = new JButton("2. Gestionar Citas del Día");
            btnBuscar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    
                    FormListarCitasDia formLista = new FormListarCitasDia(fechaSeleccionada, panelPadre);
                    formLista.setVisible(true);
                }
            });
            contentPane.add(btnBuscar);
        }
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        contentPane.add(btnCerrar);
    }
}