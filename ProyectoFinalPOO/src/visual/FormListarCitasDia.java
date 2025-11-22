package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logico.Cita;
import logico.SistemaGestion;

@SuppressWarnings("serial")
public class FormListarCitasDia extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable tableCitas;
    private DefaultTableModel model;
    private LocalDate fechaConsulta;
    private CalendarioSecretaria padre;

    private final Color COLOR_FONDO = new Color(254, 251, 246);
    private final Color COLOR_ROJO = new Color(255, 100, 100);
    private final Color COLOR_TEAL = new Color(10, 186, 181);

    public FormListarCitasDia(LocalDate fecha, CalendarioSecretaria padre) {
        this.fechaConsulta = fecha;
        this.padre = padre;

        setTitle("Gestión de Citas - " + fecha.toString());
        setBounds(100, 100, 700, 450);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 10));

        JLabel lblTitulo = new JLabel("Citas programadas para: " + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(50, 50, 50));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_TEAL));
        contentPanel.add(lblTitulo, BorderLayout.NORTH);

        String[] headers = {"ID", "Hora", "Médico", "Paciente", "Estado"};
        model = new DefaultTableModel(null, headers) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tableCitas = new JTable(model);
        tableCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCitas.setRowHeight(25);
        tableCitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableCitas.getTableHeader().setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tableCitas);
        scrollPane.getViewport().setBackground(Color.WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(COLOR_FONDO);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnCancelarCita = new JButton("Cancelar Cita Seleccionada");
        btnCancelarCita.setBackground(COLOR_ROJO);
        btnCancelarCita.setForeground(Color.WHITE);
        btnCancelarCita.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelarCita.setFocusPainted(false);
        btnCancelarCita.addActionListener(e -> cancelarCitaSeleccionada());
        buttonPane.add(btnCancelarCita);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        cargarCitas();
    }

    private void cargarCitas() {
        model.setRowCount(0);
        ArrayList<Cita> lista = SistemaGestion.getInstance().getListaCitas();
        
        for (Cita c : lista) {
            if (c.getFechaCitada().toLocalDate().equals(fechaConsulta)) {
                model.addRow(new Object[]{
                    c.getId(),
                    c.getFechaCitada().toLocalTime().toString(),
                    c.getMedico().getApellido(),
                    c.getNameVisitante(),
                    c.getEstado().toUpperCase()
                });
            }
        }
    }

    private void cancelarCitaSeleccionada() {
        int row = tableCitas.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String estado = (String) model.getValueAt(row, 4);
        if (!estado.equalsIgnoreCase("PENDIENTE")) {
            JOptionPane.showMessageDialog(this, "Solo se pueden cancelar citas pendientes.", "Acción no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idCita = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de cancelar la cita " + idCita + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            SistemaGestion.getInstance().cancelarCita(idCita);
            cargarCitas();
            padre.refrescarCalendario();
            JOptionPane.showMessageDialog(this, "Cita cancelada correctamente.");
        }
    }
}