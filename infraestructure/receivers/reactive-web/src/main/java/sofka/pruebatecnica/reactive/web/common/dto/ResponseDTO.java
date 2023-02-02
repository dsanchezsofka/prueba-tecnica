package sofka.pruebatecnica.reactive.web.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ResponseDTO<T> {

    private HeaderOutDTO header;
    private T data;
}
