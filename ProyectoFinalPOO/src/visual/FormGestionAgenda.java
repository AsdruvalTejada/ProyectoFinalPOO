package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logico.BloqueoAgenda;
import logico.Medico;
import logico.TurnoJornada;

@SuppressWarnings("serial")
public class FormGestionAgenda extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private Medico medico;
    private JTable tableJornada;
    private DefaultTableModel modelJornada;
    private JTable tableBloqueos;
    private DefaultTableModel modelBloqueos;

    private JSpinner spnFechaBloqueo;
    private JComboBox<String> cbxHoraInicio;
    private JComboBox<String> cbxHoraFin;
    private JTextField txtMotivo;

    private final Color COLOR_FONDO = new Color(254, 251, 246);
    private final Color COLOR_VERDE = new Color(0, 200, 151);

    public FormGestionAgenda(Medico medico) {
        this.medico = medico;
        setTitle("Configuración de Agenda - Dr. " + medico.getApellido());
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(COLOR_FONDO);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel panelJornada = new JPanel();
        panelJornada.setLayout(new BorderLayout(0, 10));
        panelJornada.setBackground(Color.WHITE);
        tabbedPane.addTab("Horario Habitual", null, panelJornada, null);

        String[] headersJornada = {"Día", "Trabaja (Si/No)", "Hora Entrada", "Hora Salida"};
        modelJornada = new DefaultTableModel(null, headersJornada) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 1) return Boolean.class;
                return String.class;
            }
        };
        tableJornada = new JTable(modelJornada);
        tableJornada.setRowHeight(35);
        tableJornada.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JScrollPane scrollJornada = new JScrollPane(tableJornada);
        panelJornada.add(scrollJornada, BorderLayout.CENTER);
        
        JButton btnGuardarJornada = new JButton("Guardar Cambios de Horario");
        btnGuardarJornada.setBackground(COLOR_VERDE);
        btnGuardarJornada.setForeground(Color.WHITE);
        btnGuardarJornada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardarJornada.addActionListener(e -> guardarCambiosJornada());
        
        JPanel pnlBtn1 = new JPanel();
        pnlBtn1.setBackground(Color.WHITE);
        pnlBtn1.add(btnGuardarJornada);
        panelJornada.add(pnlBtn1, BorderLayout.SOUTH);

        JPanel panelBloqueos = new JPanel();
        panelBloqueos.setLayout(new BorderLayout(0, 10));
        panelBloqueos.setBackground(Color.WHITE);
        tabbedPane.addTab("Bloqueos y Permisos", null, panelBloqueos, null);

        JPanel panelFormBloqueo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFormBloqueo.setBackground(new Color(240, 240, 240));
        panelBloqueos.add(panelFormBloqueo, BorderLayout.NORTH);
        
        panelFormBloqueo.add(new JLabel("Fecha:"));
        spnFechaBloqueo = new JSpinner(new SpinnerDateModel());
        spnFechaBloqueo.setEditor(new JSpinner.DateEditor(spnFechaBloqueo, "dd/MM/yyyy"));
        panelFormBloqueo.add(spnFechaBloqueo);
        
        panelFormBloqueo.add(new JLabel("De:"));
        cbxHoraInicio = new JComboBox<>();
        llenarComboHoras(cbxHoraInicio);
        panelFormBloqueo.add(cbxHoraInicio);
        
        panelFormBloqueo.add(new JLabel("A:"));
        cbxHoraFin = new JComboBox<>();
        llenarComboHoras(cbxHoraFin);
        panelFormBloqueo.add(cbxHoraFin);
        
        panelFormBloqueo.add(new JLabel("Motivo:"));
        txtMotivo = new JTextField(15);
        panelFormBloqueo.add(txtMotivo);
        
        JButton btnAddBloqueo = new JButton("Agregar Bloqueo");
        btnAddBloqueo.setBackground(new Color(255, 100, 100));
        btnAddBloqueo.setForeground(Color.WHITE);
        btnAddBloqueo.addActionListener(e -> agregarBloqueo());
        panelFormBloqueo.add(btnAddBloqueo);

        String[] headersBloqueo = {"Fecha Inicio", "Fecha Fin", "Motivo"};
        modelBloqueos = new DefaultTableModel(null, headersBloqueo);
        tableBloqueos = new JTable(modelBloqueos);
        tableBloqueos.setRowHeight(25);
        
        JScrollPane scrollBloqueos = new JScrollPane(tableBloqueos);
        panelBloqueos.add(scrollBloqueos, BorderLayout.CENTER);
        
        JButton btnEliminarBloqueo = new JButton("Eliminar Bloqueo Seleccionado");
        btnEliminarBloqueo.addActionListener(e -> eliminarBloqueo());
        
        JPanel pnlBtn2 = new JPanel();
        pnlBtn2.setBackground(Color.WHITE);
        pnlBtn2.add(btnEliminarBloqueo);
        panelBloqueos.add(pnlBtn2, BorderLayout.SOUTH);
        
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(COLOR_FONDO);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);
        
        cargarDatos();
    }
    
    private void llenarComboHoras(JComboBox<String> cbx) {
        for(int h=6; h<=22; h++) {
            cbx.addItem(String.format("%02d:00", h));
            cbx.addItem(String.format("%02d:30", h));
        }
    }

    private void cargarDatos() {
        modelJornada.setRowCount(0);
        for(TurnoJornada t : medico.getHorarioFijo()) {
            modelJornada.addRow(new Object[] {
                t.getDiaSemana(),
                t.isActivo(),
                t.getHoraInicio().toString(),
                t.getHoraFin().toString()
            });
        }
        modelBloqueos.setRowCount(0);
        for(BloqueoAgenda b : medico.getExceptHorario()) {
            modelBloqueos.addRow(new Object[] {
                b.getFechaHoraInicio().toString().replace("T", " "),
                b.getFechaHoraFin().toString().replace("T", " "),
                b.getMotivo()
            });
        }
    }
    
    private void guardarCambiosJornada() {
        for(int i=0; i<tableJornada.getRowCount(); i++) {
            boolean activo = (boolean) modelJornada.getValueAt(i, 1);
            String iniStr = (String) modelJornada.getValueAt(i, 2);
            String finStr = (String) modelJornada.getValueAt(i, 3);
            
            try {
                LocalTime ini = LocalTime.parse(iniStr);
                LocalTime fin = LocalTime.parse(finStr);
                
                TurnoJornada turno = medico.getHorarioFijo().get(i);
                turno.setActivo(activo);
                turno.setHoraInicio(ini);
                turno.setHoraFin(fin);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en formato de hora fila " + (i+1) + " (Use HH:mm)");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Horario actualizado correctamente.");
    }
    
    private void agregarBloqueo() {
        try {
            Date date = (Date) spnFechaBloqueo.getValue();
            LocalDate fecha = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            LocalTime horaIni = LocalTime.parse((String) cbxHoraInicio.getSelectedItem());
            LocalTime horaFin = LocalTime.parse((String) cbxHoraFin.getSelectedItem());
            
            if(!horaFin.isAfter(horaIni)) {
                JOptionPane.showMessageDialog(this, "La hora fin debe ser mayor a la inicio.");
                return;
            }
            
            LocalDateTime ldtInicio = LocalDateTime.of(fecha, horaIni);
            LocalDateTime ldtFin = LocalDateTime.of(fecha, horaFin);
            String motivo = txtMotivo.getText();
            
            if(motivo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Escriba un motivo.");
                return;
            }
            
            BloqueoAgenda bloqueo = new BloqueoAgenda(ldtInicio, ldtFin, motivo);
            medico.agregarBloqueo(bloqueo);
            
            cargarDatos();
            txtMotivo.setText("");
            
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear bloqueo: " + ex.getMessage());
        }
    }
    
    private void eliminarBloqueo() {
        int row = tableBloqueos.getSelectedRow();
        if(row != -1) {
            medico.getExceptHorario().remove(row);
            cargarDatos();
        }
    }
}
