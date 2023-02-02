package sofka.pruebatecnica.domain.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.entities.Cliente;

public interface ClientSqlRepository {
   Mono<Cliente> saveClient(Cliente cliente, Integer idPersona);
   Mono<Integer> findIdCliente(String identification);
   Flux<Cliente> findAll();
   Mono<Cliente> findClient(String identification);
   Mono<Cliente> findClient(Integer clientId);
   Mono<String> updateClient(Cliente cliente);
   Mono<Integer> deleteClient(Integer clientId);
}
