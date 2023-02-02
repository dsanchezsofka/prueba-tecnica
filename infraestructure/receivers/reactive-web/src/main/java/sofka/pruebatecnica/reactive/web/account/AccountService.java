package sofka.pruebatecnica.reactive.web.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.bank.AccountUseCase;
import sofka.pruebatecnica.reactive.web.account.dto.AccountDto;
import sofka.pruebatecnica.reactive.web.account.mapper.AccountDtoToEntity;
import sofka.pruebatecnica.reactive.web.common.dto.Response;
import sofka.pruebatecnica.reactive.web.common.dto.ResponseDTO;

import java.util.Objects;

@Log
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@SuppressWarnings("unchecked")
public class AccountService {

    private final AccountUseCase accountUseCase;
    private final AccountDtoToEntity toEntity;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "cuentas")
    public Mono<ResponseDTO> createAccount(@RequestBody AccountDto accountDto,
                                          @RequestHeader(value = "identification") String identification) {
        return accountUseCase.createAccount(toEntity.dtoToEntity(accountDto), identification)
                .map(Response.successResponse(HttpStatus.OK, "Cuenta creada satisfactoriamente", "applicationId"))
                .onErrorResume(error -> Mono.just("")
                        .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "cuentas")
    public Mono<ResponseDTO> getAccount(@RequestHeader(value = "account") Long account,
                                        @RequestHeader(value = "identification") String identification) {
        if (!Objects.isNull(account) && account != 0) {
            return accountUseCase.findAccount(account)
                    .map(Response.successResponse(HttpStatus.OK, "", "applicationId"))
                    .onErrorResume(error -> Mono.just("")
                            .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
        } else if (!Objects.isNull(identification) && !identification.isEmpty()) {
            return accountUseCase.findAllAccounts(identification)
                    .map(Response.successResponse(HttpStatus.OK, "", "applicationId"))
                    .onErrorResume(error -> Mono.just("")
                            .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
        } else {
            return Mono.just("debe ingresar un numero de cuenta o un numero de identificacion valido")
                            .map(Response.errorResponse(HttpStatus.NOT_FOUND, "Error en los datos de entrada", "applicationId"));
        }
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "cuentas/all")
    public Mono<ResponseDTO> findAccountAll() {
        return accountUseCase.findAccountAll()
                    .map(Response.successResponse(HttpStatus.OK, "", "applicationId"))
                    .onErrorResume(error -> Mono.just("")
                            .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "cuentas")
    public Mono<ResponseDTO> updateAccount(@RequestBody AccountDto accountDto) {
        return accountUseCase.updateAccount(toEntity.dtoToEntity(accountDto))
                .map(Response.successResponse(HttpStatus.OK, "Cuenta actualizada satisfactoriamente", "applicationId"))
                .onErrorResume(error -> Mono.just("")
                        .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "cuentas")
    public Mono<ResponseDTO> deleteAccount(@RequestHeader(value = "account") Long account) {
        return accountUseCase.deleteAccount(account)
                .map(Response.successResponse(HttpStatus.OK, "Cuenta eliminada satisfactoriamente", "applicationId"))
                .onErrorResume(error -> Mono.just("")
                        .map(Response.errorResponse(HttpStatus.NOT_FOUND, error.getMessage(), "applicationId")));
    }
}
