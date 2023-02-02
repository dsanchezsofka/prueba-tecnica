package sofka.pruebatecnica.domain.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.client.ClientSqlRepository;
import sofka.pruebatecnica.domain.common.ex.BusinessException;
import sofka.pruebatecnica.domain.entities.Cliente;
import sofka.pruebatecnica.domain.person.PersonSqlRepository;

import java.util.List;

@Log
@RequiredArgsConstructor
public class ClientUseCase {

    private final ClientSqlRepository clientSqlRepository;
    private final PersonSqlRepository personSqlRepository;


    public Mono<Cliente> createClient(Cliente cliente) {
        return personSqlRepository.savePerson(cliente)
                .onErrorResume(error -> Mono.error(BusinessException.Type.NOT_FOUND_INSERT_SQL
                        .build("Error insertando la información de persona en la base de datos. "
                                .concat(error.getMessage()))))
                .flatMap(idPersona -> clientSqlRepository.saveClient(cliente, idPersona)
                        .onErrorResume(error -> Mono.error(BusinessException.Type.NOT_FOUND_INSERT_SQL
                                .build("Error insertando la información del cliente en la base de datos. "
                                        .concat(error.getMessage())))));
    }

    public Mono<List<Cliente>> findAllClients() {
        return clientSqlRepository.findAll()
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de clientes en la base de datos")))
                .collectList();
    }

    public Mono<Cliente> findClient(String identification) {
        return clientSqlRepository.findClient(identification)
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de clientes en la base de datos")));
    }

    public Mono<String> updateClient(Cliente cliente) {
        return personSqlRepository.updatePerson(cliente)
                .onErrorResume(error -> Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("Error actualizando la información de persona en la base de datos. ".concat(error.getMessage()))))
                .flatMap(client -> clientSqlRepository.updateClient(cliente)
                        .onErrorResume(error -> Mono.error(BusinessException.Type.NOT_FOUND_SQL
                                .build("Error actualizando la información de clientes en la base de datos. ".concat(error.getMessage())))));
    }

    public Mono<String> deleteClient(String identification) {
        return clientSqlRepository.findIdCliente(identification)
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de cliente en la Base de Datos para el identificador: ".concat(identification))))
                .flatMap(clientId -> clientSqlRepository.deleteClient(clientId))
                .onErrorResume(error -> Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("Error eliminando la información del cliente en la base de datos".concat(error.getMessage()))))
                .thenReturn(identification);
    }
}
