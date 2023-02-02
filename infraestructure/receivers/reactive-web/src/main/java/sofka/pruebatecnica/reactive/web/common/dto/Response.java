package sofka.pruebatecnica.reactive.web.common.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Response {

    public static Function<Object, ResponseDTO> successResponse(HttpStatus httpStatus, String message, String applicationId) {
        return response(httpStatus, message, "SUCCESS", applicationId);
    }

    public static Function<Object, ResponseDTO> errorResponse(HttpStatus httpStatus, String message, String applicationId) {
        return response(httpStatus, message, httpStatus.value() >= 500 ? "ERROR" : httpStatus.toString(), applicationId);
    }

    private static Function<Object, ResponseDTO> response(HttpStatus httpStatus, String message, String statusIn, String applicationId) {
        return data -> ResponseDTO.builder()
                .header(HeaderOutDTO.builder()
                        .applicationId(applicationId)
                        .status(StatusDTO.builder()
                                .status(statusIn)
                                .code(httpStatus.value())
                                .message(message).build()).build())
                .data(data).build();
    }

}