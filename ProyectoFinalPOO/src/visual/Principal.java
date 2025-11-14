package visual;
//
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import logico.Usuario;

public class Principal extends JFrame {

	private JPanel contentPane;
	private Usuario userLogueado;

	/**
	 * Create the frame.
	 */
	public Principal(Usuario user) {
		this.userLogueado = user;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		visualPorRol();
	}

	private void visualPorRol() {
		String rol = userLogueado.getRol();
		setTitle("Clínica - " + "Usuario: " + userLogueado.getUsername() + " (" + rol + ")");
		
		if(rol.equalsIgnoreCase("ADMIN")) {
			//visual para el admin
		}else if(rol.equalsIgnoreCase("SECRETARIA")) {
			//visual para la secretaria
		}else if(rol.equalsIgnoreCase("MEDICO")){
			//visual para médico
		}
	}
	
}
