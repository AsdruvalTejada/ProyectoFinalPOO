package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

import logico.Secretaria;
import logico.SistemaGestion;

public class FormRegSecretaria extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtId;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JFormattedTextField txtTelefono;
	private JSpinner spnFechaNac;
	private JComboBox<String> cbxSexo;
	private Secretaria secreEdit = null;

	private final Color COLOR_FONDO = new Color(253, 247, 238);
	private final Color COLOR_VERDE_BOTON = new Color(0, 168, 107);
	private final Color COLOR_TEXTO = new Color(60, 60, 60);

	public FormRegSecretaria(Secretaria secre) {
		this.secreEdit = secre;

		setTitle(secre == null ? "Registrar Nueva Secretaria" : "Editar Secretaria");
		setBounds(100, 100, 620, 400);
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());

		contentPanel.setBackground(COLOR_FONDO);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panelHeader = new JPanel();
		panelHeader.setBounds(20, 20, 565, 50);
		panelHeader.setBackground(COLOR_FONDO);
		panelHeader.setBorder(new LineBorder(Color.GRAY, 1, true));
		panelHeader.setLayout(new BorderLayout(0, 0));
		contentPanel.add(panelHeader);

		JLabel lblTitulo = new JLabel(secre == null ? "Registro de Secretaria" : "Edición de Secretaria");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setForeground(COLOR_TEXTO);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
		panelHeader.add(lblTitulo, BorderLayout.CENTER);

		JPanel panelForm = new JPanel();
		panelForm.setBounds(20, 85, 565, 200);
		panelForm.setBackground(COLOR_FONDO);
		panelForm.setBorder(new LineBorder(Color.GRAY, 1, true));
		panelForm.setLayout(null);
		contentPanel.add(panelForm);

		crearLabel("ID Secre:", 30, 20, panelForm);
		txtId = new JTextField();
		txtId.setBounds(140, 20, 150, 25);
		txtId.setEditable(false);
		txtId.setBackground(new Color(220, 220, 220));
		configurarCampo(txtId);
		panelForm.add(txtId);

		crearLabel("Nombre:", 30, 60, panelForm);
		txtNombre = new JTextField();
		txtNombre.setBounds(140, 60, 150, 25);
		configurarCampo(txtNombre);
		panelForm.add(txtNombre);

		crearLabel("Apellido:", 300, 60, panelForm);
		txtApellido = new JTextField();
		txtApellido.setBounds(380, 60, 150, 25);
		configurarCampo(txtApellido);
		panelForm.add(txtApellido);

		crearLabel("Fecha Nac.:", 30, 100, panelForm);
		spnFechaNac = new JSpinner(new SpinnerDateModel());
		spnFechaNac.setEditor(new JSpinner.DateEditor(spnFechaNac, "dd/MM/yyyy"));
		spnFechaNac.setBounds(140, 100, 150, 25);
		panelForm.add(spnFechaNac);

		crearLabel("Sexo:", 300, 100, panelForm);
		cbxSexo = new JComboBox<>();
		cbxSexo.setModel(new DefaultComboBoxModel<>(new String[] {"Seleccione", "M", "F"}));
		cbxSexo.setBackground(Color.WHITE);
		cbxSexo.setBounds(380, 100, 100, 25);
		panelForm.add(cbxSexo);

		crearLabel("Teléfono:", 30, 140, panelForm);
		try {
			MaskFormatter mascaraTel = new MaskFormatter("###-###-####");
			mascaraTel.setPlaceholderCharacter('_');
			txtTelefono = new JFormattedTextField(mascaraTel);
		} catch (ParseException e) {
			e.printStackTrace();
			txtTelefono = new JFormattedTextField();
		}
		txtTelefono.setBounds(140, 140, 150, 25);
		txtTelefono.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelForm.add(txtTelefono);

		JPanel panelBotones = new JPanel();
		panelBotones.setBounds(20, 300, 565, 50);
		panelBotones.setBackground(COLOR_FONDO);
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		contentPanel.add(panelBotones);

		JButton btnGuardar = new JButton("GUARDAR");
		btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnGuardar.setBackground(COLOR_VERDE_BOTON);
		btnGuardar.setForeground(Color.WHITE);
		btnGuardar.setFocusPainted(false);
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardar();
			}
		});
		panelBotones.add(btnGuardar);

		JButton btnCancelar = new JButton("CANCELAR");
		btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnCancelar.setBackground(new Color(220, 53, 69));
		btnCancelar.setForeground(Color.WHITE);
		btnCancelar.setFocusPainted(false);
		btnCancelar.addActionListener(e -> dispose());
		panelBotones.add(btnCancelar);

		// Inicializar
		if (this.secreEdit == null) {
			generarNuevoId();
		} else {
			cargarDatosSiEsEdicion();
		}
	}
	
	private void generarNuevoId() {
		int siguienteId = SistemaGestion.getInstance().getListaSecretarias().size() + 1;
		String idGenerado = String.format("SEC-%03d", siguienteId);
		
		while(SistemaGestion.getInstance().buscarSecretariaPorId(idGenerado) != null) {
			siguienteId++;
			idGenerado = String.format("SEC-%03d", siguienteId);
		}
		txtId.setText(idGenerado);
	}

	private void crearLabel(String texto, int x, int y, JPanel panel) {
		JLabel lbl = new JLabel(texto);
		lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lbl.setForeground(COLOR_TEXTO);
		lbl.setBounds(x, y, 100, 25);
		panel.add(lbl);
	}

	private void configurarCampo(JTextField txt) {
		txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txt.setColumns(10);
	}

	private void cargarDatosSiEsEdicion() {
		if(secreEdit != null) {
			txtId.setText(secreEdit.getId());
			
			txtNombre.setText(secreEdit.getName());
			txtApellido.setText(secreEdit.getApellido());
			txtTelefono.setText(secreEdit.getContacto());
			cbxSexo.setSelectedItem(secreEdit.getSexo());

			LocalDate ld = secreEdit.getFechaNacimiento();
			Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
			spnFechaNac.setValue(date);
		}
	}

	private void guardar() {
		String telefono = txtTelefono.getText();
		
		if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || telefono.contains("_")) {
			JOptionPane.showMessageDialog(this, "Complete los campos obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String id = txtId.getText();
		String nombre = txtNombre.getText();
		String apellido = txtApellido.getText();
		String contacto = txtTelefono.getText();
		String sexo = (String) cbxSexo.getSelectedItem();
		Date date = (Date) spnFechaNac.getValue();
		LocalDate fechaNac = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if(secreEdit == null) {
			Secretaria nueva = new Secretaria(id, nombre, apellido, fechaNac, sexo, contacto);
			SistemaGestion.getInstance().registrarSecretaria(nueva);
			JOptionPane.showMessageDialog(this, "Registrada correctamente con ID: " + id);
		} else {
			secreEdit.setName(nombre);
			secreEdit.setApellido(apellido);
			secreEdit.setSexo(sexo);
			secreEdit.setContacto(contacto);
			secreEdit.setFechaNacimiento(fechaNac);
			JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
		}
		dispose();
	}
}