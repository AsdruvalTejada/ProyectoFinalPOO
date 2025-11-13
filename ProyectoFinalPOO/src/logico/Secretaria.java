package logico;

import java.time.LocalDate;

public class Secretaria extends Persona {

    public Secretaria(String id, String name, String apellido, LocalDate fechaNacimiento, String sexo, String contacto) {
        super(id, name, apellido, fechaNacimiento, sexo, contacto);
    }
    
}