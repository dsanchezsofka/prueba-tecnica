package sofka.pruebatecnica.reactive.web.movement;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.bank.MovementUseCase;
import sofka.pruebatecnica.reactive.web.common.dto.Response;
import sofka.pruebatecnica.reactive.web.common.dto.ResponseDTO;
import sofka.pruebatecnica.reactive.web.movement.mapper.MovementDtoToEntity;

import java.util.Objects;

@Log
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@SuppressWarnings("unchecked")
public class MovementService {

    private final MovementUseCase movementUseCase;
    private final MovementDtoToEntity toEntity;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "movimientos")
    public Mono<ResponseDTO> createMovement(@RequestHeader(value = "value") Double value,
                                           @RequestHeader(value = "account") Long account) {
        return movementUseCase.createMovement(value, account)
                .map(Response.successResponse(HttpStatus.OK, "Transaccion exitosa", "applicationId"))
                .onErrorResume(error -> Mono.just("")
                        .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "movimientos")
    public Mono<ResponseDTO> getMovement(@RequestHeader(value = "account") Long account,
                                         @RequestHeader(value = "identification") String identification,
                                         @RequestParam(value = "startDate", required = false) String startDate,
                                         @RequestParam(value = "endDate", required = false) String endDate) {
        if ((!Objects.isNull(account) && account != 0) || (!Objects.isNull(identification) && !identification.isEmpty())) {
            return movementUseCase.getMovement(account, identification, startDate, endDate)
                    .map(Response.successResponse(HttpStatus.OK, "Listado de movimientos obtenidos satisfactoriamente", "applicationId"))
                    .onErrorResume(error -> Mono.just("")
                            .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
        } else {
            return Mono.just("debe indicar un numero de cuenta o numero de identificacion")
                    .map(Response.errorResponse(HttpStatus.NOT_FOUND, "Error en los datos de entrada.","applicationId"));
        }
    }
}
