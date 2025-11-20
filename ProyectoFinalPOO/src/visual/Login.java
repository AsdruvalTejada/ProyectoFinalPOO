package visual;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.SistemaGestion;
import logico.Usuario;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Login - Sistema de Clínica");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 220);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(50, 40, 80, 20);
		contentPane.add(lblUsuario);

		txtUsuario = new JTextField();
		txtUsuario.setBounds(140, 40, 200, 25);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);

		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(50, 80, 80, 20);
		contentPane.add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(140, 80, 200, 25);
		contentPane.add(txtPassword);

		JButton btnEntrar = new JButton("Entrar");

		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String username = txtUsuario.getText();
				String password = new String(txtPassword.getPassword()); 
				Usuario userValid = SistemaGestion.getInstance().validarLogin(username, password);

				if (userValid != null) {

					String rol = userValid.getRol();

					if (rol.equalsIgnoreCase("ADMIN")) {
						PrincipalAdmin adminFrame = new PrincipalAdmin(userValid);
						adminFrame.setVisible(true);

					} else if (rol.equalsIgnoreCase("MEDICO")) {
						//PrincipalMedico medicoFrame = new PrincipalMedico(userValid);
						//medicoFrame.setVisible(true);

					} else if (rol.equalsIgnoreCase("SECRETARIA")) {
						PrincipalSecretaria secFrame = new PrincipalSecretaria(userValid);
						secFrame.setVisible(true);

					} else {
						// Por si acaso hay un rol no reconocido
						JOptionPane.showMessageDialog(null, "Rol de usuario no reconocido: " + rol);
						return;
					}

					dispose(); 

				} else {
					JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error de Login", JOptionPane.ERROR_MESSAGE);
					txtPassword.setText("");
				}
			}
		});

		btnEntrar.setBounds(160, 130, 100, 30);
		contentPane.add(btnEntrar);
	}
}