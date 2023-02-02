package sofka.pruebatecnica.reactive.web.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class HeaderOutDTO {

    private String applicationId;
    private StatusDTO status;
    private List<ErrorDTO> errors;
}
