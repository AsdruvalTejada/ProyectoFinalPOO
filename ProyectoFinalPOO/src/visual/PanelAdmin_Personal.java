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
import logico.Medico;
import logico.Secretaria;
import logico.SistemaGestion;
import logico.Usuario;

@SuppressWarnings("serial")
public class PanelAdmin_Personal extends JPanel {
	private JTable tablePersonal;
	private DefaultTableModel model;

	public PanelAdmin_Personal(Color colorAccent) {
		setLayout(new BorderLayout(0, 0));
		setBackground(new Color(248, 249, 250));
		setBorder(new EmptyBorder(20, 20, 20, 20));

		JPanel pnlHeader = new JPanel(new BorderLayout());
		pnlHeader.setBackground(new Color(248, 249, 250));
		add(pnlHeader, BorderLayout.NORTH);

		JLabel lblTitulo = new JLabel("Gestión de Personal");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitulo.setForeground(new Color(33, 37, 41));
		pnlHeader.add(lblTitulo, BorderLayout.WEST);

		JPanel pnlTabla = new JPanel(new BorderLayout());
		pnlTabla.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		pnlTabla.setBackground(new Color(248, 249, 250));
		add(pnlTabla, BorderLayout.CENTER);

		String[] headers = {"ID", "Nombre Completo", "Rol", "Teléfono", "Detalle"};
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		model.setColumnIdentifiers(headers);
		tablePersonal = new JTable(model);
		tablePersonal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePersonal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tablePersonal.setRowHeight(30);
		tablePersonal.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		tablePersonal.getTableHeader().setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(tablePersonal);
		pnlTabla.add(scrollPane, BorderLayout.CENTER);

		JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlBotones.setBackground(new Color(248, 249, 250));
		add(pnlBotones, BorderLayout.SOUTH);

		JButton btnNuevoMedico = new JButton("Nuevo Médico");
		estilizarBoton(btnNuevoMedico, colorAccent);
		btnNuevoMedico.addActionListener(e -> {
			FormRegMedico reg = new FormRegMedico(null);
			reg.setVisible(true);
			cargarPersonal();
		});
		pnlBotones.add(btnNuevoMedico);

		JButton btnNuevaSecretaria = new JButton("Nueva Secretaria");
		estilizarBoton(btnNuevaSecretaria, colorAccent);
		btnNuevaSecretaria.addActionListener(e -> {
			FormRegSecretaria reg = new FormRegSecretaria(null);
			reg.setVisible(true);
			cargarPersonal();
		});
		pnlBotones.add(btnNuevaSecretaria);

		JLabel lblSep = new JLabel(" | ");
		pnlBotones.add(lblSep);

		JButton btnEditar = new JButton("Modificar");
		estilizarBoton(btnEditar, new Color(86, 223, 207));
		btnEditar.setForeground(Color.WHITE);
		btnEditar.addActionListener(e -> modificarSeleccionado());
		pnlBotones.add(btnEditar);

		JButton btnAsignar = new JButton("Asignar Médicos");
		estilizarBoton(btnAsignar, new Color(100, 100, 100));
		btnAsignar.addActionListener(e -> addAsignacion());
		pnlBotones.add(btnAsignar);

		JButton btnEliminar = new JButton("Eliminar");
		estilizarBoton(btnEliminar, new Color(86, 223, 207));
		btnEliminar.addActionListener(e -> eliminarPersonal());
		pnlBotones.add(btnEliminar);

		cargarPersonal();
	}

	private void estilizarBoton(JButton btn, Color bg) {
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
	}

	public void cargarPersonal() {
		model.setRowCount(0);
		for (Medico m : SistemaGestion.getInstance().getListaMedicos()) {
			model.addRow(new Object[]{ m.getId(), m.getNombreCompleto(), "Médico", m.getContacto(), m.getEspecialidad() });
		}
		for (Secretaria s : SistemaGestion.getInstance().getListaSecretarias()) {
			model.addRow(new Object[]{ s.getId(), s.getNombreCompleto(), "Secretaria", s.getContacto(), "N/A" });
		}
	}

	private void modificarSeleccionado() {
		int row = tablePersonal.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione una persona para modificar.");
			return;
		}

		String id = (String) model.getValueAt(row, 0);
		String rol = (String) model.getValueAt(row, 2);

		if (rol.equalsIgnoreCase("Médico")) {
			Medico med = SistemaGestion.getInstance().buscarMedicoPorId(id);
			if (med != null) {
				FormRegMedico form = new FormRegMedico(med); 
				form.setVisible(true);
			}
		} else if (rol.equalsIgnoreCase("Secretaria")) {
			Secretaria sec = SistemaGestion.getInstance().buscarSecretariaPorId(id);
			if (sec != null) {
				FormRegSecretaria form = new FormRegSecretaria(sec);
				form.setVisible(true);
			}
		}
		cargarPersonal();
	}

	private void eliminarPersonal() {
		int row = tablePersonal.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione una persona para eliminar.");
			return;
		}

		String id = (String) model.getValueAt(row, 0);
		String rol = (String) model.getValueAt(row, 2);
		String nombre = (String) model.getValueAt(row, 1);

		int confirm = JOptionPane.showConfirmDialog(this, 
				"¿Está seguro de eliminar a " + nombre + "?\nEsta acción borrará también su acceso al sistema.", 
				"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			SistemaGestion sys = SistemaGestion.getInstance();

			if (rol.equalsIgnoreCase("Médico")) {
				Medico med = sys.buscarMedicoPorId(id);

				boolean tieneCitasPendientes = false;
				for(Cita c : sys.getListaCitas()) {
					if(c.getMedico().getId().equals(id) && 
							(c.getEstado().equalsIgnoreCase("pendiente") || c.getEstado().equalsIgnoreCase("activa"))) {
						tieneCitasPendientes = true;
						break;
					}
				}

				if(tieneCitasPendientes) {
					JOptionPane.showMessageDialog(this, "No se puede eliminar: El médico tiene citas pendientes.", "Acción Bloqueada", JOptionPane.ERROR_MESSAGE);
					return;
				}

				Usuario usuarioABorrar = null;
				for(Usuario u : sys.getListaUsuarios()) {
					if(u.getIdPersonaVinculada().equals(id)) {
						usuarioABorrar = u;
						break;
					}
				}
				if(usuarioABorrar != null) sys.getListaUsuarios().remove(usuarioABorrar);

				sys.getListaMedicos().remove(med);

			} else {
				Secretaria sec = sys.buscarSecretariaPorId(id);

				Usuario usuarioABorrar = null;
				for(Usuario u : sys.getListaUsuarios()) {
					if(u.getIdPersonaVinculada().equals(id)) {
						usuarioABorrar = u;
						break;
					}
				}
				if(usuarioABorrar != null) sys.getListaUsuarios().remove(usuarioABorrar);

				sys.getListaSecretarias().remove(sec);
			}

			cargarPersonal();
			JOptionPane.showMessageDialog(this, "Personal eliminado correctamente.");
		}
	}

	private void addAsignacion() {
		int row = tablePersonal.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione una persona.");
			return;
		}

		String id = (String) model.getValueAt(row, 0);
		String rol = (String) model.getValueAt(row, 2);

		if (rol.equalsIgnoreCase("Secretaria")) {
			Secretaria sec = SistemaGestion.getInstance().buscarSecretariaPorId(id);
			if (sec != null) {
				FormAsignarMedicos form = new FormAsignarMedicos(sec);
				form.setVisible(true);
			}
		} else {
			JOptionPane.showMessageDialog(this, "La asignación de médicos solo aplica para Secretarias.", "Acción no válida", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}