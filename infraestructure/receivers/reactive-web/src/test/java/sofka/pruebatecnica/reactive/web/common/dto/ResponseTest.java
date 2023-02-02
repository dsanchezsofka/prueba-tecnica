package sofka.pruebatecnica.reactive.web.common.dto;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseTest {

    private static final String TEST = "TEST";

    @Test
    public void ResponseSuccessful(){
        HttpStatus httpStatus = HttpStatus.OK;

        Function<Object, ResponseDTO> functionResponse = Response.successResponse(httpStatus, "message", TEST);

        assertThat(functionResponse).isNotNull();

        ResponseDTO responseDTO = functionResponse.apply(TEST);

        assertThat(responseDTO.getHeader().getApplicationId()).isEqualTo(TEST);
        assertThat(responseDTO.getHeader().getStatus().getMessage()).isEqualTo("message");
        assertThat(responseDTO.getHeader().getStatus().getStatus()).isEqualTo("SUCCESS");
        Assertions.assertThat(responseDTO.getHeader().getErrors()).isNull();
        assertThat(responseDTO.getData()).isEqualTo(TEST);
    }

    @Test
    public void ResponseError(){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Function<Object, ResponseDTO> functionResponse = Response.errorResponse(httpStatus, TEST, TEST);

        assertThat(functionResponse).isNotNull();

        ResponseDTO responseDTO = functionResponse.apply(TEST);

        assertThat(responseDTO.getHeader().getApplicationId()).isEqualTo(TEST);
        assertThat(responseDTO.getHeader().getStatus().getStatus()).isEqualTo(httpStatus.toString());
        Assertions.assertThat(responseDTO.getHeader().getErrors()).isNull();
        assertThat(responseDTO.getData()).isEqualTo(TEST);
    }

}