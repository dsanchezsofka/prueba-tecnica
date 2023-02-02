package sofka.pruebatecnica.reactive.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClienteDto extends PersonaDto {
    private Integer clienteid;
    private String contrasena;
    private Boolean estado;
}
