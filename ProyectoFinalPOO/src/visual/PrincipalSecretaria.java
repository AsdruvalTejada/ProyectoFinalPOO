package visual;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import logico.Usuario;

@SuppressWarnings("serial")
public class PrincipalSecretaria extends JFrame {

    private JPanel contentPane;
    @SuppressWarnings("unused") // Guardaré el usuario por si se necesita para lógica futura
    private Usuario userLogueado;

    /**
     * Constructor del Frame Principal de la Secretaria
     */
    public PrincipalSecretaria(Usuario user) {
        this.userLogueado = user;
        
        setTitle("Panel de Secretaria - Gestión de Citas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1280, 720); 
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        CalendarioSecretaria panelCalendario = new CalendarioSecretaria();
        contentPane.add(panelCalendario, BorderLayout.CENTER);
    }
}