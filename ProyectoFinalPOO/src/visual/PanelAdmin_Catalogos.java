package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

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
    @SuppressWarnings("unused")
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
        
        // Botones Enfermedades
        JPanel pnlBtnEnf = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnEnf.setBackground(Color.WHITE);
        
        JButton btnAddEnfermedad = new JButton("Nueva");
        estilizarBoton(btnAddEnfermedad, colorAccent);
        btnAddEnfermedad.addActionListener(e -> agregarEnfermedad());
        
        JButton btnEditEnfermedad = new JButton("Editar");
        estilizarBoton(btnEditEnfermedad, colorAccent);
        btnEditEnfermedad.setForeground(Color.BLACK);
        btnEditEnfermedad.addActionListener(e -> editarEnfermedad());
        
        JButton btnDelEnfermedad = new JButton("Eliminar");
        estilizarBoton(btnDelEnfermedad, new Color(220, 53, 69));
        btnDelEnfermedad.addActionListener(e -> eliminarEnfermedad());
        
        pnlBtnEnf.add(btnAddEnfermedad);
        pnlBtnEnf.add(btnEditEnfermedad);
        pnlBtnEnf.add(btnDelEnfermedad);
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
        
        JPanel pnlBtnVac = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBtnVac.setBackground(Color.WHITE);
        
        JButton btnAddVacuna = new JButton("Nueva");
        estilizarBoton(btnAddVacuna, colorAccent);
        btnAddVacuna.addActionListener(e -> agregarVacuna());
        
        JButton btnEditVacuna = new JButton("Editar");
        estilizarBoton(btnEditVacuna, colorAccent);
        btnEditVacuna.setForeground(Color.WHITE);
        btnEditVacuna.addActionListener(e -> editarVacuna());
        
        JButton btnDelVacuna = new JButton("Eliminar");
        estilizarBoton(btnDelVacuna, new Color(220, 53, 69));
        btnDelVacuna.addActionListener(e -> eliminarVacuna());
        
        pnlBtnVac.add(btnAddVacuna);
        pnlBtnVac.add(btnEditVacuna);
        pnlBtnVac.add(btnDelVacuna);
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
        Object[] inputs = { "Nombre:", txtNombre, chkVigilancia };
        
        int op = JOptionPane.showConfirmDialog(this, inputs, "Nueva Enfermedad", JOptionPane.OK_CANCEL_OPTION);
        if (op == JOptionPane.OK_OPTION && !txtNombre.getText().trim().isEmpty()) {
            String id = "ENF-" + (SistemaGestion.getInstance().getCatalogoEnfermedades().size() + 100); // +100 para evitar conflictos con seeds
            Enfermedad nueva = new Enfermedad(id, txtNombre.getText().trim(), chkVigilancia.isSelected());
            SistemaGestion.getInstance().agregarEnfermedadCatalogo(nueva);
            cargarDatos();
        }
    }
    
    private void editarEnfermedad() {
        int row = tableEnfermedades.getSelectedRow();
        if(row == -1) { JOptionPane.showMessageDialog(this, "Seleccione una enfermedad."); return; }
        
        String id = (String) modelEnfermedades.getValueAt(row, 0);
        Enfermedad enf = null;
        for(Enfermedad e : SistemaGestion.getInstance().getCatalogoEnfermedades()) {
            if(e.getId().equals(id)) { enf = e; break; }
        }
        
        if(enf != null) {
            JTextField txtNombre = new JTextField(enf.getNombre());
            JCheckBox chkVigilancia = new JCheckBox("¿Bajo Vigilancia Epidemiológica?", enf.getEstaBajoVigilancia());
            Object[] inputs = { "Nombre:", txtNombre, chkVigilancia };
            
            int op = JOptionPane.showConfirmDialog(this, inputs, "Editar Enfermedad", JOptionPane.OK_CANCEL_OPTION);
            if (op == JOptionPane.OK_OPTION && !txtNombre.getText().trim().isEmpty()) {
                enf.setNombre(txtNombre.getText().trim());
                enf.setEstaBajoVigilancia(chkVigilancia.isSelected());
                SistemaGestion.getInstance().guardarDatos();
                cargarDatos();
            }
        }
    }
    
    private void eliminarEnfermedad() {
        int row = tableEnfermedades.getSelectedRow();
        if(row == -1) { JOptionPane.showMessageDialog(this, "Seleccione una enfermedad."); return; }
        
        String nombre = (String) modelEnfermedades.getValueAt(row, 1);
        String id = (String) modelEnfermedades.getValueAt(row, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar '" + nombre + "' del catálogo?\nNota: Esto no borrará el historial de consultas pasadas.", "Confirmar", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            ArrayList<Enfermedad> lista = SistemaGestion.getInstance().getCatalogoEnfermedades();
            lista.removeIf(e -> e.getId().equals(id));
            SistemaGestion.getInstance().guardarDatos();
            cargarDatos();
        }
    }
    
    private void agregarVacuna() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la Vacuna:", "Registrar Vacuna", JOptionPane.QUESTION_MESSAGE);
        if (nombre != null && !nombre.trim().isEmpty()) {
            String id = "VAC-" + (SistemaGestion.getInstance().getCatalogoVacunas().size() + 100);
            Vacuna nueva = new Vacuna(id, nombre.trim());
            SistemaGestion.getInstance().agregarVacuna(nueva);
            cargarDatos();
        }
    }
    
    private void editarVacuna() {
        int row = tableVacunas.getSelectedRow();
        if(row == -1) { JOptionPane.showMessageDialog(this, "Seleccione una vacuna."); return; }
        
        String id = (String) modelVacunas.getValueAt(row, 0);
        Vacuna vac = null;
        for(Vacuna v : SistemaGestion.getInstance().getCatalogoVacunas()) {
            if(v.getId().equals(id)) { vac = v; break; }
        }
        
        if(vac != null) {
            String nuevoNombre = JOptionPane.showInputDialog(this, "Editar nombre:", vac.getNombre());
            if(nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                vac.setNombre(nuevoNombre.trim());
                SistemaGestion.getInstance().guardarDatos();
                cargarDatos();
            }
        }
    }
    
    private void eliminarVacuna() {
        int row = tableVacunas.getSelectedRow();
        if(row == -1) { JOptionPane.showMessageDialog(this, "Seleccione una vacuna."); return; }
        
        String nombre = (String) modelVacunas.getValueAt(row, 1);
        String id = (String) modelVacunas.getValueAt(row, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar '" + nombre + "' del catálogo?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            ArrayList<Vacuna> lista = SistemaGestion.getInstance().getCatalogoVacunas();
            lista.removeIf(v -> v.getId().equals(id));
            SistemaGestion.getInstance().guardarDatos();
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
    
    private void estilizarBoton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
    }
}