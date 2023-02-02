package sofka.pruebatecnica.reactive.web.movement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDto {
    private Integer movimientosId;
    private Integer cuentaId;
    private Date fecha;
    private String tipo;
    private Double valor;
    private Double saldo;
}
