package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import logico.Paciente;
import logico.SistemaGestion;

@SuppressWarnings("serial")
public class PanelAdmin_Pacientes extends JPanel {

    private JTable tablePacientes;
    private DefaultTableModel model;
    @SuppressWarnings("unused")
    private Color colorAccent;

    public PanelAdmin_Pacientes(Color colorAccent) {
        this.colorAccent = colorAccent;
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(new Color(248, 249, 250));
        add(pnlHeader, BorderLayout.NORTH);

        JLabel lblTitulo = new JLabel("Directorio de Pacientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(33, 37, 41));
        pnlHeader.add(lblTitulo, BorderLayout.WEST);

        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        pnlTabla.setBackground(new Color(248, 249, 250));
        add(pnlTabla, BorderLayout.CENTER);

        String[] headers = {"Cédula", "Nombre", "Apellido", "Sangre", "Teléfono", "Edad"};
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(headers);
        
        tablePacientes = new JTable(model);
        tablePacientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePacientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablePacientes.setRowHeight(30);
        tablePacientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablePacientes.getTableHeader().setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tablePacientes);
        pnlTabla.add(scrollPane, BorderLayout.CENTER);

        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBotones.setBackground(new Color(248, 249, 250));
        add(pnlBotones, BorderLayout.SOUTH);

        JButton btnNuevo = new JButton("Añadir Paciente"); 
        estilizarBoton(btnNuevo, new Color(86, 223, 207)); 
        btnNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 FormRegPaciente form = new FormRegPaciente(null);
                 form.setVisible(true);
                 cargarPacientes();
            }
        });
        pnlBotones.add(btnNuevo);
        
        JLabel lblSep = new JLabel(" | ");
        pnlBotones.add(lblSep);
        
        JButton btnEditar = new JButton("Modificar Datos");
        estilizarBoton(btnEditar, new Color(86, 223, 207));
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarPaciente();
            }
        });
        pnlBotones.add(btnEditar);
        
        JButton btnEliminar = new JButton("Eliminar");
        estilizarBoton(btnEliminar, new Color(220, 53, 69));
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarPaciente();
            }
        });
        pnlBotones.add(btnEliminar);
        
        cargarPacientes();
    }

    private void estilizarBoton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
    }

    public void cargarPacientes() {
        model.setRowCount(0);
        for (Paciente p : SistemaGestion.getInstance().getListaPacientes()) {
            model.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getApellido(),
                p.getTipoSangre(),
                p.getContacto(),
                p.getEdad()
            });
        }
    }

    private void editarPaciente() {
        int row = tablePacientes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para corregir sus datos.");
            return;
        }
        
        String id = (String) model.getValueAt(row, 0);
        Paciente p = SistemaGestion.getInstance().buscarPacientePorId(id);
        
        if(p != null) {
            FormRegPaciente form = new FormRegPaciente(p);
            form.setVisible(true);
            cargarPacientes();
        }
    }

    private void eliminarPaciente() {
        int row = tablePacientes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente.");
            return;
        }
        
        String id = (String) model.getValueAt(row, 0);
        String nombre = (String) model.getValueAt(row, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar al paciente " + nombre + "?\nSe perderá todo su historial médico.", 
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Paciente p = SistemaGestion.getInstance().buscarPacientePorId(id);
            if(p != null) {
                SistemaGestion.getInstance().getListaPacientes().remove(p);
                cargarPacientes();
                JOptionPane.showMessageDialog(this, "Paciente eliminado.");
            }
        }
    }
}