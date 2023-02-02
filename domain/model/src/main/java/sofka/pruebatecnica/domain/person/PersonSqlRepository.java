package sofka.pruebatecnica.domain.person;

import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.entities.Cliente;

public interface PersonSqlRepository {
   Mono<Integer> savePerson(Cliente cliente);
   Mono<Cliente> updatePerson(Cliente cliente);
}
