package sofka.pruebatecnica.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Movimientos {
    private Integer movimientosId;
    private Integer cuentaId;
    private Date fecha;
    private String tipo;
    private Double valor;
    private Double saldo;
    private Double saldoDisponible;
}
