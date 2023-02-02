package sofka.pruebatecnica.domain.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.account.AccountSqlRepository;
import sofka.pruebatecnica.domain.client.ClientSqlRepository;
import sofka.pruebatecnica.domain.common.ex.BusinessException;
import sofka.pruebatecnica.domain.entities.Cuenta;

import java.util.List;

@Log
@RequiredArgsConstructor
public class AccountUseCase {

    private final AccountSqlRepository accountSqlRepository;
    private final ClientSqlRepository clientSqlRepository;

    public Mono<Cuenta> createAccount(Cuenta cuenta, String identification) {
        return validateClient(identification)
                .flatMap(clientId -> accountSqlRepository.saveAccount(cuenta, clientId))
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de las cuentas en la base de datos")));
    }

    public Mono<Integer> validateClient(String identification) {
        return clientSqlRepository.findIdCliente(identification)
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información del cliente en la base de datos, clienteId: ".concat(identification))));
    }

    public Mono<List<Cuenta>> findAllAccounts(String identification) {
        return validateClient(identification)
                .flatMapMany(clientId -> accountSqlRepository.findAccounts(clientId))
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de las cuentas en la base de datos")))
                .collectList();
    }

    public Mono<Cuenta> findAccount(Long account) {
        return accountSqlRepository.findAccount(account)
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de la cuenta en la base de datos")));
    }

    public Mono<List<Cuenta>> findAccountAll() {
        return accountSqlRepository.findAccountAll()
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de la cuenta en la base de datos")))
                .collectList();
    }

    public Mono<Cuenta> updateAccount(Cuenta cuenta) {
        return accountSqlRepository.updateAccount(cuenta)
                .onErrorResume(error -> Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("Error actualizando la información de la cuenta en la base de datos".concat(error.getMessage()))));
    }

    public Mono<Long> deleteAccount(Long account) {
        return accountSqlRepository.deleteAccount(account)
                .onErrorResume(error -> Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("Error eliminando la información de la cuenta en la base de datos".concat(error.getMessage()))));
    }
}
