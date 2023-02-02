package sofka.pruebatecnica.jpa.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movimientos")
@Builder(toBuilder = true)
public class MovimientoData {
    @Id
    private Integer movimientosId;
    private Integer cuentaId;
    private Date fecha;
    private String tipo;
    private Double valor;
    private Double saldo;
}
