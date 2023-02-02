package sofka.pruebatecnica.jpa.person;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.common.ObjectMapperDomain;
import sofka.pruebatecnica.domain.common.ex.BusinessException;
import sofka.pruebatecnica.domain.entities.Cliente;
import sofka.pruebatecnica.domain.entities.Persona;
import sofka.pruebatecnica.domain.person.PersonSqlRepository;
import sofka.pruebatecnica.jpa.data.PersonaData;
import sofka.pruebatecnica.reactive.repository.jpa.AdapterOperations;

@Repository
public class PersonSqlAdapter
        extends AdapterOperations<Persona, PersonaData, Integer, PersonDataSqlRepository>
        implements PersonSqlRepository, ObjectMapperDomain {

    public static final String ADAPTER_PERSON_SQL_ERROR = "Adapter PersonSql error:";

    @Autowired
    public PersonSqlAdapter(PersonDataSqlRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Cliente.ClienteBuilder.class)
                .build());
    }

    @Override
    public Mono<Integer> savePerson(Cliente cliente) {
        return Mono.just(repository.savePerson(cliente.getNombre(), cliente.getGenero(), cliente.getEdad(), cliente.getIdentificacion(), cliente.getDireccion(), cliente.getTelefono()))
                .onErrorResume(error -> Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("Error insertando informacion la tabla persona en la base de datos".concat(error.getMessage()))));
    }

    public Mono<Cliente> updatePerson(Cliente cliente) {
        return Mono.just(repository.updatePerson(cliente.getNombre(), cliente.getGenero(), cliente.getEdad(), cliente.getDireccion(), cliente.getTelefono(), cliente.getIdentificacion()))
                .onErrorResume(error -> Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("Error actualizando la tabla persona en la base de datos".concat(error.getMessage()))))
                .thenReturn(cliente);
    }
}
