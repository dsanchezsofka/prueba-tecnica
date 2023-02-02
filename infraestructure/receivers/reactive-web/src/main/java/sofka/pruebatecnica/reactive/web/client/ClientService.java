package sofka.pruebatecnica.reactive.web.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.bank.ClientUseCase;
import sofka.pruebatecnica.reactive.web.client.dto.ClienteDto;
import sofka.pruebatecnica.reactive.web.client.mapper.ClientDtoToEntity;
import sofka.pruebatecnica.reactive.web.common.dto.Response;
import sofka.pruebatecnica.reactive.web.common.dto.ResponseDTO;

import java.util.Objects;

@Log
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@SuppressWarnings("unchecked")
public class ClientService {

    private final ClientUseCase clientUseCase;
    private final ClientDtoToEntity toEntity;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "clientes")
    public Mono<ResponseDTO> createClient(@RequestBody ClienteDto clienteDto) {
        return clientUseCase.createClient(toEntity.dtoToEntity(clienteDto))
                .map(Response.successResponse(HttpStatus.OK, "Cliente creado satisfactoriamente", "applicationId"))
                .onErrorResume(error -> Mono.just("")
                        .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "clientes")
    public Mono<ResponseDTO> getClient(@RequestHeader(value = "identification", required = false) String identification) {
        if (Objects.isNull(identification) || identification.isEmpty()) {
            return clientUseCase.findAllClients()
                    .map(Response.successResponse(HttpStatus.OK, "", "applicationId"))
                    .onErrorResume(error -> Mono.just("")
                            .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
        } else {
            return clientUseCase.findClient(identification)
                    .map(Response.successResponse(HttpStatus.OK, "", "applicationId"))
                    .onErrorResume(error -> Mono.just("")
                            .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "clientes")
    public Mono<ResponseDTO> updateClient(@RequestBody ClienteDto clienteDto) {
        return clientUseCase.updateClient(toEntity.dtoToEntity(clienteDto))
                .map(Response.successResponse(HttpStatus.OK, "Cliente actualizado correctamente", "applicationId"))
                .onErrorResume(error -> Mono.just("")
                        .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "clientes")
    public Mono<ResponseDTO> deleteClient(@RequestHeader(value = "identification") String identification) {
        return clientUseCase.deleteClient(identification)
                .map(Response.successResponse(HttpStatus.OK, "Cliente eliminado satisfactoriamente", "applicationId"))
                .onErrorResume(error -> Mono.just("")
                        .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
    }
}
