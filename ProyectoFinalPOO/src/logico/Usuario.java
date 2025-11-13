package logico;

public class Usuario {
    private String username;
    private String password;
    private String rol;
    private String idUser; 

    public Usuario(String username, String password, String rol, String idUser) {
        super();
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getIdPersonaVinculada() {
        return idUser;
    }

    public void setIdPersonaVinculada(String idUser) {
        this.idUser = idUser;
    }
    
    public boolean VerifPass(String passwordIntento) {
        return this.password.equals(passwordIntento);
    }
}