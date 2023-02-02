package sofka.pruebatecnica.domain.account;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.entities.Cuenta;

public interface AccountSqlRepository {
   Mono<Cuenta> saveAccount(Cuenta cuenta, Integer idPersona);
   Mono<Cuenta> findAccount(Long account);
   Flux<Cuenta> findAccounts(Integer clientId);
   Flux<Cuenta> findAccountAll();
   Mono<Cuenta> updateAccount(Cuenta cuenta);
   Mono<Long> deleteAccount(Long account);
}
