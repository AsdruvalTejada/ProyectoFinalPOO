package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logico.Cita;
import logico.SistemaGestion;

@SuppressWarnings("serial")
public class PanelAdmin_Citas extends JPanel {

    private JTable tableCitas;
    private DefaultTableModel model;
    @SuppressWarnings("unused")
	private Color colorAccent;

	public PanelAdmin_Citas(Color colorAccent) {
        this.colorAccent = colorAccent;
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(248, 249, 250));
        add(pnlHeader, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("Gestión Global de Citas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(33, 37, 41));
        pnlHeader.add(lblTitulo, BorderLayout.WEST);

        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        pnlTabla.setBackground(new Color(248, 249, 250));
        add(pnlTabla, BorderLayout.CENTER);

        String[] headers = {"ID Cita", "Fecha/Hora", "Médico", "Paciente/Visitante", "Estado"};
        model = new DefaultTableModel(null, headers) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tableCitas = new JTable(model);
        tableCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCitas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableCitas.setRowHeight(30);
        tableCitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableCitas.getTableHeader().setBackground(Color.WHITE);
        
        pnlTabla.add(new JScrollPane(tableCitas), BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBotones.setBackground(new Color(248, 249, 250));
        add(pnlBotones, BorderLayout.SOUTH);
        
        JButton btnRefrescar = new JButton("Refrescar Lista");
        estilizarBoton(btnRefrescar, colorAccent);
        btnRefrescar.addActionListener(e -> cargarCitas());
        pnlBotones.add(btnRefrescar);

        JButton btnCancelar = new JButton("Cancelar cita seleccionada");
        estilizarBoton(btnCancelar, new Color(220, 53, 69));
        btnCancelar.addActionListener(e -> cancelarCita());
        pnlBotones.add(btnCancelar);
        
        cargarCitas();
    }

    private void estilizarBoton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
    }

    private void cargarCitas() {
        model.setRowCount(0);
        for (Cita c : SistemaGestion.getInstance().getListaCitas()) {
            String fechaHora = c.getFechaCitada().toLocalDate().toString() + " " + 
                               c.getFechaCitada().toLocalTime().toString();
            
            model.addRow(new Object[]{
                c.getId(),
                fechaHora,
                c.getMedico().getNombreCompleto(),
                c.getNameVisitante(),
                c.getEstado().toUpperCase()
            });
        }
    }
    
    private void cancelarCita() {
        int row = tableCitas.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita.");
            return;
        }
        
        String estado = (String) model.getValueAt(row, 4);
        if(estado.equalsIgnoreCase("CANCELADA") || estado.equalsIgnoreCase("COMPLETADA")) {
            JOptionPane.showMessageDialog(this, "No se puede cancelar una cita que ya está " + estado.toLowerCase() + ".");
            return;
        }
        
        String id = (String) model.getValueAt(row, 0);
        int opt = JOptionPane.showConfirmDialog(this, "¿Seguro que desea cancelar la cita " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if(opt == JOptionPane.YES_OPTION) {
            SistemaGestion.getInstance().cancelarCita(id);
            cargarCitas();
            JOptionPane.showMessageDialog(this, "Cita cancelada.");
        }
    }
}