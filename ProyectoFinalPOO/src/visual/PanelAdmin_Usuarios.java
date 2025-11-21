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

import logico.Medico;
import logico.Secretaria;
import logico.SistemaGestion;
import logico.Usuario;

@SuppressWarnings("serial")
public class PanelAdmin_Usuarios extends JPanel {

	private JTable tableUsuarios;
	private DefaultTableModel model;
	@SuppressWarnings("unused")
	private Color colorAccent;

	/**
	 * Create the panel.
	 */
	public PanelAdmin_Usuarios(Color colorAccent) {
		this.colorAccent = colorAccent;
		setLayout(new BorderLayout(0, 0));
		setBackground(new Color(248, 249, 250));
		setBorder(new EmptyBorder(20, 20, 20, 20));

		JPanel pnlHeader = new JPanel(new BorderLayout());
		pnlHeader.setBackground(new Color(248, 249, 250));
		add(pnlHeader, BorderLayout.NORTH);

		JLabel lblTitulo = new JLabel("Gestión de Usuarios y Accesos");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitulo.setForeground(new Color(33, 37, 41));
		pnlHeader.add(lblTitulo, BorderLayout.WEST);

		JPanel pnlTabla = new JPanel(new BorderLayout());
		pnlTabla.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		pnlTabla.setBackground(new Color(248, 249, 250));
		add(pnlTabla, BorderLayout.CENTER);

		String[] headers = {"Username", "Rol", "Vinculado a (Nombre)", "ID Personal"};
		model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		model.setColumnIdentifiers(headers);

		tableUsuarios = new JTable(model);
		tableUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tableUsuarios.setRowHeight(30);
		tableUsuarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		tableUsuarios.getTableHeader().setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(tableUsuarios);
		pnlTabla.add(scrollPane, BorderLayout.CENTER);

		JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlBotones.setBackground(new Color(248, 249, 250));
		add(pnlBotones, BorderLayout.SOUTH);

		JButton btnNuevo = new JButton("Crear Usuario");
		estilizarBoton(btnNuevo, colorAccent);
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FormRegUsuario form = new FormRegUsuario(null);
				form.setVisible(true);
				cargarUsuarios();       
			}
		});
		pnlBotones.add(btnNuevo);

		JLabel lblSep = new JLabel(" | ");
		pnlBotones.add(lblSep);

		JButton btnEditar = new JButton("Modificar");
		estilizarBoton(btnEditar, colorAccent);
		btnEditar.setForeground(Color.WHITE);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editarUsuario();
			}
		});
		pnlBotones.add(btnEditar);

		JButton btnEliminar = new JButton("Eliminar Acceso");
		estilizarBoton(btnEliminar, new Color(220, 53, 69));
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminarUsuario();
			}
		});
		pnlBotones.add(btnEliminar);

		cargarUsuarios();
	}

	private void estilizarBoton(JButton btn, Color bg) {
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
	}

	public void cargarUsuarios() {
		model.setRowCount(0);
		for (Usuario u : SistemaGestion.getInstance().getListaUsuarios()) {

			String nombreVinculado = "---";

			if (u.getRol().equalsIgnoreCase("MEDICO")) {
				Medico m = SistemaGestion.getInstance().buscarMedicoPorId(u.getIdPersonaVinculada());
				if (m != null) nombreVinculado = m.getNombreCompleto();

			} else if (u.getRol().equalsIgnoreCase("SECRETARIA")) {
				Secretaria s = SistemaGestion.getInstance().buscarSecretariaPorId(u.getIdPersonaVinculada());
				if (s != null) nombreVinculado = s.getNombreCompleto();

			} else if (u.getRol().equalsIgnoreCase("ADMIN")) {
				nombreVinculado = "Administrador del Sistema";
			}

			model.addRow(new Object[]{
					u.getUsername(),
					u.getRol(),
					nombreVinculado,
					u.getIdPersonaVinculada()
			});
		}
	}

	private void eliminarUsuario() {
		int row = tableUsuarios.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
			return;
		}

		String username = (String) model.getValueAt(row, 0);
		String rol = (String) model.getValueAt(row, 1);

		if(rol.equalsIgnoreCase("ADMIN") && username.equalsIgnoreCase("admin")) {
			JOptionPane.showMessageDialog(this, "No puede eliminar al Administrador Principal.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea revocar el acceso a " + username + "?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			Usuario aBorrar = null;
			for(Usuario u : SistemaGestion.getInstance().getListaUsuarios()) {
				if(u.getUsername().equals(username)) {
					aBorrar = u;
					break;
				}
			}

			if(aBorrar != null) {
				SistemaGestion.getInstance().getListaUsuarios().remove(aBorrar);
				cargarUsuarios();
				JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
			}
		}
	}

	private void editarUsuario() {
		int row = tableUsuarios.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione un usuario para modificar.");
			return;
		}

		String username = (String) model.getValueAt(row, 0);

		Usuario userEdit = null;
		for(Usuario u : SistemaGestion.getInstance().getListaUsuarios()) {
			if(u.getUsername().equals(username)) {
				userEdit = u;
				break;
			}
		}

		if(userEdit != null) {
			FormRegUsuario form = new FormRegUsuario(userEdit);
			form.setVisible(true);
			cargarUsuarios();
		}
	}
}