package sofka.pruebatecnica.jpa.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cliente")
@Builder(toBuilder = true)
public class ClienteData{

    @Id
    private Integer clienteId;
    @OneToOne
    @JoinColumn(name = "personaId")
    private PersonaData persona;
    private String contrasena;
    private Boolean estado;
}
