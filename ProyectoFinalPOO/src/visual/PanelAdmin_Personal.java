package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

public class PanelAdmin_Personal extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tablePersonal;
	private DefaultTableModel model;
	private Color colorAccent;

	/**
	 * Constructor
	 */
	public PanelAdmin_Personal(Color colorAccent) {
		this.colorAccent = colorAccent;
		setLayout(new BorderLayout(0, 0));
		setBackground(new Color(248, 249, 250));
		setBorder(new EmptyBorder(20, 20, 20, 20));

		// --- HEADER ---
		JPanel pnlHeader = new JPanel(new BorderLayout());
		pnlHeader.setBackground(new Color(248, 249, 250));
		add(pnlHeader, BorderLayout.NORTH);

		JLabel lblTitulo = new JLabel("Gestión de Personal (Médicos y Secretarias)");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitulo.setForeground(new Color(33, 37, 41));
		pnlHeader.add(lblTitulo, BorderLayout.WEST);

		// --- TABLA CENTRAL ---
		JPanel pnlTabla = new JPanel(new BorderLayout());
		pnlTabla.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		pnlTabla.setBackground(new Color(248, 249, 250));
		add(pnlTabla, BorderLayout.CENTER);

		String[] headers = {"ID", "Nombre Completo", "Rol", "Cédula/ID", "Detalle (Especialidad)"};
		model = new DefaultTableModel();
		model.setColumnIdentifiers(headers);

		tablePersonal = new JTable(model);
		tablePersonal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePersonal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tablePersonal.setRowHeight(30);
		tablePersonal.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		tablePersonal.getTableHeader().setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(tablePersonal);
		scrollPane.getViewport().setBackground(Color.WHITE);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
		pnlTabla.add(scrollPane, BorderLayout.CENTER);

		// --- BOTONES INFERIORES ---
		JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlBotones.setBackground(new Color(248, 249, 250));
		add(pnlBotones, BorderLayout.SOUTH);

		JButton btnNuevoMedico = new JButton("Registrar Médico");
		estilizarBoton(btnNuevoMedico);
		btnNuevoMedico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FormRegMedico reg = new FormRegMedico();
				reg.setModal(true);
				reg.setVisible(true);
				cargarPersonal();
			}
		});
		pnlBotones.add(btnNuevoMedico);

		JButton btnNuevaSecretaria = new JButton("Registrar Secretaria");
		estilizarBoton(btnNuevaSecretaria);
		btnNuevaSecretaria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Aquí se abrirá el FormRegSecretaria (Pendiente de crear)");
			}
		});
		pnlBotones.add(btnNuevaSecretaria);

		cargarPersonal();
	}

	private void estilizarBoton(JButton btn) {
		btn.setBackground(colorAccent);
		btn.setForeground(Color.WHITE);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
	}

	public void cargarPersonal() {
		model.setRowCount(0);

		ArrayList<Medico> medicos = SistemaGestion.getInstance().getListaMedicos();
		for (Medico m : medicos) {
			model.addRow(new Object[]{
					m.getId(), m.getNombreCompleto(), "Médico", m.getId(), m.getEspecialidad()});
		}

		ArrayList<Secretaria> secretarias = SistemaGestion.getInstance().getListaSecretarias();
		for (Secretaria s : secretarias) {
			model.addRow(new Object[]{ 
					s.getId(), s.getNombreCompleto(), "Secretaria", s.getId(), "N/A"
			});
		}
	}
}