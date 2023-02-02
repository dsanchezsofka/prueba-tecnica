package sofka.pruebatecnica.domain.entities;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Cliente extends Persona{
    private Integer clienteid;
    private String contrasena;
    private Boolean estado;

    @Builder(toBuilder = true)
    public Cliente(Integer personaId, String nombre, String genero, Integer edad, String identificacion, String direccion, String telefono, Integer clienteid, String contrasena, Boolean estado) {
        super(personaId, nombre, genero, edad, identificacion, direccion, telefono);
        this.clienteid = clienteid;
        this.contrasena = contrasena;
        this.estado = estado;
    }
}
