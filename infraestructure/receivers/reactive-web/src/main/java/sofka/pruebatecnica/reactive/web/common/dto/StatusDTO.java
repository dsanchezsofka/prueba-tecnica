package sofka.pruebatecnica.reactive.web.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StatusDTO {

    private String status;
    private Integer code;
    private String message;
}
