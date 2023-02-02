package sofka.pruebatecnica.jpa.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cuenta")
@Builder(toBuilder = true)
public class CuentaData {
    @Id
    private Integer cuentaId;
    private Integer clienteId;
    private Long numero;
    private String tipo;
    private Double saldoInicial;
    private Boolean estado;
}
