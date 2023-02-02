package sofka.pruebatecnica.jpa.client;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.client.ClientSqlRepository;
import sofka.pruebatecnica.domain.common.ObjectMapperDomain;
import sofka.pruebatecnica.domain.common.ex.BusinessException;
import sofka.pruebatecnica.domain.entities.Cliente;
import sofka.pruebatecnica.jpa.data.ClienteData;
import sofka.pruebatecnica.reactive.repository.jpa.AdapterOperations;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class ClientSqlAdapter
        extends AdapterOperations<Cliente, ClienteData, Integer, ClientDataSqlRepository>
        implements ClientSqlRepository, ObjectMapperDomain {

    public static final String ADAPTER_CLIENT_SQL_ERROR = "Adapter ClientSql error:";

    @Autowired
    public ClientSqlAdapter(ClientDataSqlRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Cliente.ClienteBuilder.class)
                .build());
    }

    @Override
    public Mono<Cliente> saveClient(Cliente cliente, Integer idPersona) {
        return Mono.just(repository.saveClient(idPersona, cliente.getContrasena(), Boolean.compare(cliente.getEstado(), false)))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_CLIENT_SQL_ERROR + error.toString())))
                .map(idPers -> cliente);
    }

    @Override
    public Mono<Integer> findIdCliente(String identification) {
        return Mono.just(repository.findIdCliente(identification))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_CLIENT_SQL_ERROR + error.toString())));
    }

    @Override
    public Mono<Cliente> findClient(String identification) {
        return Mono.just(repository.findClient(identification))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_CLIENT_SQL_ERROR + error.toString())))
                .flatMap(clienteData -> Objects.isNull(clienteData) ? Mono.error(new BusinessException(BusinessException.Type.NOT_FOUND_SQL, ""))
                        : Mono.just(toEntity(clienteData)));
    }

    @Override
    public Mono<Cliente> findClient(Integer clientId) {
        return Mono.just(repository.findClient(clientId))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_CLIENT_SQL_ERROR + error.toString())))
                .flatMap(clienteData -> Objects.isNull(clienteData) ? Mono.error(new BusinessException(BusinessException.Type.NOT_FOUND_SQL, ""))
                        : Mono.just(toEntity(clienteData)));
    }

    @Override
    public Flux<Cliente> findAll() {
        return Mono.just(repository.findAll())
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_CLIENT_SQL_ERROR + error.toString())))
                .flatMapMany(clienteDataList -> clienteDataList.isEmpty() ? Mono.error(new BusinessException(BusinessException.Type.NOT_FOUND_SQL, ""))
                        : Flux.fromIterable(toEntities(clienteDataList)));
    }

    @Override
    public Mono<String> updateClient(Cliente cliente) {
        return Mono.just(repository.updateClient(cliente.getContrasena(), Boolean.compare(cliente.getEstado(), false), cliente.getIdentificacion()))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_CLIENT_SQL_ERROR + error.toString())))
                .thenReturn(cliente.getIdentificacion());
    }

    public Mono<Integer> deleteClient(Integer clientId) {
        return Mono.just(repository.disableClient(clientId))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_CLIENT_SQL_ERROR + error.toString())));
    }

    private List<Cliente> toEntities(List<ClienteData> clientDataList) {
        return clientDataList.stream().map
                (this::toEntity
                ).collect(Collectors.toList());
    }

    protected Cliente toEntity(ClienteData clientData) {
        return Cliente.builder().clienteid(clientData.getClienteId())
                    .personaId(clientData.getPersona().getPersonaId())
                    .nombre(clientData.getPersona().getNombre())
                    .genero(clientData.getPersona().getGenero())
                    .edad(clientData.getPersona().getEdad())
                    .identificacion(clientData.getPersona().getIdentificacion())
                    .direccion(clientData.getPersona().getDireccion())
                    .telefono(clientData.getPersona().getTelefono())
                    .clienteid(clientData.getClienteId())
                    .contrasena(clientData.getContrasena())
                    .estado(clientData.getEstado())
                    .build();
    }
/*
    protected ClienteData toData(Cliente cliente) {
        return ClienteData.builder()
                .clienteId(cliente.getClienteid())
                .contrasena(cliente.getContrasena())
                .estado(cliente.getEstado())
                .persona(PersonaData.builder()
                        .nombre(cliente.)
                        .genero()
                        .edad()
                        .identificacion()
                        .direccion()
                        .telefono()
                        .build())
                .build();
    }*/
}
