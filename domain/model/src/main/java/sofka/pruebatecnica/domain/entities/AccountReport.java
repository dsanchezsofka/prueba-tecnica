package sofka.pruebatecnica.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AccountReport {
    private Long cuenta;
    private String tipo;
    private Boolean estado;
    private List<Movimientos> movimientosList;
}
