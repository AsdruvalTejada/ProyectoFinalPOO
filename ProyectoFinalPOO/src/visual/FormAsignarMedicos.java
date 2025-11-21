package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logico.Medico;
import logico.Secretaria;
import logico.SistemaGestion;

public class FormAsignarMedicos extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JPanel panelCheckboxes;
    private Secretaria secretariaActual;
    private ArrayList<JCheckBox> listaChecks;
    
    private final Color COLOR_FONDO = new Color(253, 247, 238);
    private final Color COLOR_TEAL = new Color(10, 186, 181);

    public FormAsignarMedicos(Secretaria secre) {
        this.secretariaActual = secre;
        this.listaChecks = new ArrayList<>();
        
        setTitle("Asignación de Médicos");
        setBounds(100, 100, 450, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        
        // HEADER
        JLabel lblTitulo = new JLabel("Asignar Médicos a " + secre.getName());
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(60, 60, 60));
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        contentPanel.add(lblTitulo, BorderLayout.NORTH);
        
        // LISTA DE MEDICOS
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        panelCheckboxes = new JPanel();
        panelCheckboxes.setBackground(Color.WHITE);
        panelCheckboxes.setLayout(new BoxLayout(panelCheckboxes, BoxLayout.Y_AXIS));
        scrollPane.setViewportView(panelCheckboxes);
        
        cargarMedicos();
        
        // INFO
        JLabel lblInfo = new JLabel("Nota: Máximo 3 médicos por secretaria.");
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBorder(new EmptyBorder(5, 0, 5, 0));
        contentPanel.add(lblInfo, BorderLayout.SOUTH);

        // BOTONES
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(COLOR_FONDO);
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton btnGuardar = new JButton("GUARDAR ASIGNACIÓN");
        btnGuardar.setBackground(COLOR_TEAL);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(e -> guardarAsignacion());
        buttonPane.add(btnGuardar);
        
        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> dispose());
        buttonPane.add(btnCancelar);
    }
    
    private void cargarMedicos() {
        ArrayList<Medico> todosLosMedicos = SistemaGestion.getInstance().getListaMedicos();
        ArrayList<Medico> yaAsignados = secretariaActual.getMedicosAsignados();
        
        if(todosLosMedicos.isEmpty()) {
            JLabel lbl = new JLabel("No hay médicos registrados en el sistema.");
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCheckboxes.add(lbl);
            return;
        }
        
        for(Medico m : todosLosMedicos) {
            JCheckBox chk = new JCheckBox(m.getId() + " - Dr. " + m.getApellido() + " (" + m.getEspecialidad() + ")");
            chk.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            chk.setBackground(Color.WHITE);
            chk.setName(m.getId()); // Guardamos el ID en el nombre del componente
            
            // Si ya está asignado, marcarlo
            if(yaAsignados.contains(m)) {
                chk.setSelected(true);
            }
            
            listaChecks.add(chk);
            panelCheckboxes.add(chk);
        }
    }
    
    private void guardarAsignacion() {
        int contadorSeleccionados = 0;
        for(JCheckBox chk : listaChecks) {
            if(chk.isSelected()) contadorSeleccionados++;
        }
        
        if(contadorSeleccionados > 3) {
            JOptionPane.showMessageDialog(this, 
                "No puede asignar más de 3 médicos a una secretaria.\nHa seleccionado: " + contadorSeleccionados, 
                "Regla de Negocio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Aplicar cambios
        // Primero limpiamos la lista actual (o creamos una nueva lógica de diff, pero limpiar y rellenar es más fácil)
        secretariaActual.getMedicosAsignados().clear();
        
        for(JCheckBox chk : listaChecks) {
            if(chk.isSelected()) {
                String idMedico = chk.getName();
                Medico m = SistemaGestion.getInstance().buscarMedicoPorId(idMedico);
                if(m != null) {
                    // Usamos el método de la clase Secretaria o agregamos directo
                    // secretariaActual.asignarMedico(m); -> este tiene validación interna, mejor usarlo
                    // Pero como ya limpiamos la lista, podemos agregar directo para evitar problemas con la lógica del if(size>=3)
                    // si es que lo hacemos uno por uno.
                    // Mejor:
                    secretariaActual.getMedicosAsignados().add(m);
                }
            }
        }
        
        JOptionPane.showMessageDialog(this, "Asignación actualizada correctamente.");
        dispose();
    }
}