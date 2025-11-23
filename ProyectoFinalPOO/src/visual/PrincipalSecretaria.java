package visual;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logico.Secretaria;
import logico.SistemaGestion;
import logico.Usuario;

@SuppressWarnings("serial")
public class PrincipalSecretaria extends JFrame {

    private JPanel contentPane;
    @SuppressWarnings("unused")
	private Usuario userLogueado;
    private Secretaria secretariaLogueada;

    public PrincipalSecretaria(Usuario user) {
        this.userLogueado = user;
        
        this.secretariaLogueada = SistemaGestion.getInstance().getSecretariaLogueada(user);
        
        if(this.secretariaLogueada == null) {
            JOptionPane.showMessageDialog(this, "Error: Usuario no vinculado a una Secretaria válida.");
        }

        setTitle("Panel de Secretaria - " + (secretariaLogueada != null ? secretariaLogueada.getName() : ""));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1280, 720);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        CalendarioSecretaria panelCalendario = new CalendarioSecretaria(secretariaLogueada);
        contentPane.add(panelCalendario, BorderLayout.CENTER);
    }
}