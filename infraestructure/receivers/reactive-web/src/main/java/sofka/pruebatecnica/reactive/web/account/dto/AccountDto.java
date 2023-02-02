package sofka.pruebatecnica.reactive.web.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AccountDto {
    private Integer id;
    private Long numero;
    private String tipo;
    private Double saldoInicial;
    private Boolean estado;
}
