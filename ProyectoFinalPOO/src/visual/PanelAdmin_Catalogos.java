package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import logico.Enfermedad;
import logico.SistemaGestion;
import logico.Vacuna;

@SuppressWarnings("serial")
public class PanelAdmin_Catalogos extends JPanel {

    private JTable tableEnfermedades;
    private DefaultTableModel modelEnfermedades;
    private JTable tableVacunas;
    private DefaultTableModel modelVacunas;
    private Color colorAccent;

	public PanelAdmin_Catalogos(Color colorAccent) {
        this.colorAccent = colorAccent;
        setLayout(new GridLayout(1, 2, 20, 0)); 
        setBackground(new Color(248, 249, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel pnlEnfermedades = crearPanelBase("Catálogo de Enfermedades");
        add(pnlEnfermedades);
        
        String[] headersEnf = {"ID", "Nombre", "Vigilancia"};
        modelEnfermedades = new DefaultTableModel(null, headersEnf) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableEnfermedades = crearTabla(modelEnfermedades);
        pnlEnfermedades.add(new JScrollPane(tableEnfermedades), BorderLayout.CENTER);
        
        JButton btnAddEnfermedad = new JButton("Nueva Enfermedad");
        estilizarBoton(btnAddEnfermedad);
        btnAddEnfermedad.addActionListener(e -> agregarEnfermedad());
        
        JPanel pnlBtnEnf = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnEnf.setBackground(Color.WHITE);
        pnlBtnEnf.add(btnAddEnfermedad);
        pnlEnfermedades.add(pnlBtnEnf, BorderLayout.SOUTH);


        JPanel pnlVacunas = crearPanelBase("Catálogo de Vacunas");
        add(pnlVacunas);
        
        String[] headersVac = {"ID", "Nombre Vacuna"};
        modelVacunas = new DefaultTableModel(null, headersVac) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableVacunas = crearTabla(modelVacunas);
        pnlVacunas.add(new JScrollPane(tableVacunas), BorderLayout.CENTER);
        
        JButton btnAddVacuna = new JButton("Nueva Vacuna");
        estilizarBoton(btnAddVacuna);
        btnAddVacuna.addActionListener(e -> agregarVacuna());
        
        JPanel pnlBtnVac = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnVac.setBackground(Color.WHITE);
        pnlBtnVac.add(btnAddVacuna);
        pnlVacunas.add(pnlBtnVac, BorderLayout.SOUTH);
        
        cargarDatos();
    }
    
    private void cargarDatos() {
        modelEnfermedades.setRowCount(0);
        for(Enfermedad e : SistemaGestion.getInstance().getCatalogoEnfermedades()) {
            modelEnfermedades.addRow(new Object[] { e.getId(), e.getNombre(), e.getEstaBajoVigilancia() ? "SÍ" : "NO" });
        }
        
        modelVacunas.setRowCount(0);
        for(Vacuna v : SistemaGestion.getInstance().getCatalogoVacunas()) {
            modelVacunas.addRow(new Object[] { v.getId(), v.getNombre() });
        }
    }
    
    private void agregarEnfermedad() {
        JTextField txtNombre = new JTextField();
        JCheckBox chkVigilancia = new JCheckBox("¿Bajo Vigilancia Epidemiológica?");
        Object[] inputs = {
            "Nombre de la Enfermedad:", txtNombre,
            chkVigilancia
        };
        
        int op = JOptionPane.showConfirmDialog(this, inputs, "Registrar Enfermedad", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION && !txtNombre.getText().isEmpty()) {
            String id = "ENF-" + (SistemaGestion.getInstance().getCatalogoEnfermedades().size() + 1);
            Enfermedad nueva = new Enfermedad(id, txtNombre.getText(), chkVigilancia.isSelected());
            SistemaGestion.getInstance().agregarEnfermedadCatalogo(nueva);
            cargarDatos();
        }
    }
    
    private void agregarVacuna() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la Vacuna:", "Registrar Vacuna", JOptionPane.QUESTION_MESSAGE);
        if (nombre != null && !nombre.isEmpty()) {
            String id = "VAC-" + (SistemaGestion.getInstance().getCatalogoVacunas().size() + 1);
            Vacuna nueva = new Vacuna(id, nombre);
            SistemaGestion.getInstance().agregarVacuna(nueva);
            cargarDatos();
        }
    }

    private JPanel crearPanelBase(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true), 
                new EmptyBorder(15, 15, 15, 15)
        ));
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(33, 37, 41));
        panel.add(lbl, BorderLayout.NORTH);
        return panel;
    }
    
    private JTable crearTabla(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        return table;
    }
    
    private void estilizarBoton(JButton btn) {
        btn.setBackground(colorAccent);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
    }
}