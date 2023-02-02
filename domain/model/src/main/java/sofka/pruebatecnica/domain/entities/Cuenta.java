package sofka.pruebatecnica.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Cuenta {
    private Integer cuentaId;
    private Integer clienteId;
    private Long numero;
    private String tipo;
    private Double saldoInicial;
    private Boolean estado;
}
