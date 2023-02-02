package sofka.pruebatecnica.domain.bank;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import sofka.pruebatecnica.domain.client.ClientSqlRepository;
import sofka.pruebatecnica.domain.common.ex.BusinessException;
import sofka.pruebatecnica.domain.entities.Cliente;
import sofka.pruebatecnica.domain.entities.Persona;
import sofka.pruebatecnica.domain.person.PersonSqlRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientUseCaseTest {

    @InjectMocks
    private ClientUseCase useCase;

    @Mock
    private ClientSqlRepository clientSqlRepository;

    @Mock
    private PersonSqlRepository personSqlRepository;

    private Cliente cliente;
    private Cliente cliente2;

    @Before
    public void init() {
        cliente = Cliente.builder()
                .contrasena("123")
                .estado(Boolean.TRUE)
                .personaId(1)
                .nombre("Daniel Sanchez")
                .genero("M")
                .edad(30)
                .identificacion("20650252")
                .direccion("Fortaleza, Brasil")
                .telefono("123456789")
                .build();

        cliente2 = Cliente.builder()
                .contrasena("321")
                .estado(Boolean.FALSE)
                .personaId(2)
                .nombre("Jose Vegas")
                .genero("M")
                .edad(26)
                .identificacion("25205602")
                .direccion("Caracas, Venezuela")
                .telefono("987654321")
                .build();
    }

    @Test
    public void createClientTest() {
        when(personSqlRepository.savePerson(any(Cliente.class))).thenReturn(Mono.just(1));

        when(clientSqlRepository.saveClient(any(Cliente.class), anyInt()))
                .thenReturn(Mono.just(cliente));

        StepVerifier.create(useCase.createClient(cliente))
                .expectNext(cliente)
                .verifyComplete();

        verify(personSqlRepository, times(1)).savePerson(any(Cliente.class));
        verify(clientSqlRepository, times(1)).saveClient(any(Cliente.class), anyInt());
    }

    @Test
    public void businessExceptionSavePerson() {
        when(personSqlRepository.savePerson(any(Cliente.class)))
                .thenReturn(Mono.error(new NullPointerException("")));

        StepVerifier.create(useCase.createClient(cliente))
                .verifyErrorSatisfies(throwable -> assertThat(throwable)
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("Error guardando en SQL. Error insertando la información de persona en la base de datos. "));

        verify(personSqlRepository, times(1)).savePerson(any(Cliente.class));
    }

    @Test
    public void businessExceptionSaveClient() {
        when(personSqlRepository.savePerson(any(Cliente.class))).thenReturn(Mono.just(1));

        when(clientSqlRepository.saveClient(any(Cliente.class), anyInt()))
                .thenReturn(Mono.error(new NullPointerException("")));

        StepVerifier.create(useCase.createClient(cliente))
                .verifyErrorSatisfies(throwable -> assertThat(throwable)
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("Error guardando en SQL. Error insertando la información del cliente en la base de datos. "));

        verify(personSqlRepository, times(1)).savePerson(any(Cliente.class));
    }

    @Test
    public void findAllClientsTest() {
        List<Cliente> clienteList = new ArrayList<>();
        clienteList.add(cliente);
        clienteList.add(cliente2);

        when(clientSqlRepository.findAll())
                .thenReturn(Flux.just(cliente, cliente2));

        StepVerifier.create(useCase.findAllClients())
                .expectNext(clienteList)
                .verifyComplete();

        verify(clientSqlRepository, times(1)).findAll();
    }

    @Test
    public void businessExceptionFindAllClients() {
        when(clientSqlRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(useCase.findAllClients())
                .verifyErrorSatisfies(throwable -> assertThat(throwable)
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("Documento no encontrado en SQL. No se encontró información de clientes en la base de datos"));


        verify(clientSqlRepository, times(1)).findAll();
    }

    @Test
    public void updateClientTest() {
        when(personSqlRepository.updatePerson(any(Cliente.class))).thenReturn(Mono.just(cliente));
        when(clientSqlRepository.updateClient(any(Cliente.class))).thenReturn(Mono.just(cliente.getIdentificacion()));

        StepVerifier.create(useCase.updateClient(cliente))
                .expectNext(cliente.getIdentificacion())
                .verifyComplete();

        verify(personSqlRepository, times(1)).updatePerson(any(Cliente.class));
        verify(clientSqlRepository, times(1)).updateClient(any(Cliente.class));
    }

    @Test
    public void businessExceptionUpdatePerson() {
        when(personSqlRepository.updatePerson(any(Cliente.class))).thenReturn(Mono.error(new NullPointerException("")));

        StepVerifier.create(useCase.updateClient(cliente))
                .verifyErrorSatisfies(throwable -> assertThat(throwable)
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("Documento no encontrado en SQL. Error actualizando la información de persona en la base de datos. "));

        verify(personSqlRepository, times(1)).updatePerson(any(Cliente.class));
    }

    @Test
    public void businessExceptionUpdateClient() {
        when(personSqlRepository.updatePerson(any(Cliente.class))).thenReturn(Mono.just(cliente));
        when(clientSqlRepository.updateClient(any(Cliente.class))).thenReturn(Mono.error(new NullPointerException("")));

        StepVerifier.create(useCase.updateClient(cliente))
                .verifyErrorSatisfies(throwable -> assertThat(throwable)
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("Documento no encontrado en SQL. Error actualizando la información de clientes en la base de datos. "));

        verify(personSqlRepository, times(1)).updatePerson(any(Cliente.class));
        verify(clientSqlRepository, times(1)).updateClient(any(Cliente.class));
    }

    @Test
    public void deleteClientTest() {
        when(clientSqlRepository.findIdCliente(anyString())).thenReturn(Mono.just(1));
        when(clientSqlRepository.deleteClient(anyInt())).thenReturn(Mono.just(1));

        StepVerifier.create(useCase.deleteClient(cliente.getIdentificacion()))
                .expectNext(cliente.getIdentificacion())
                .verifyComplete();

        verify(clientSqlRepository, times(1)).findIdCliente(anyString());
        verify(clientSqlRepository, times(1)).deleteClient(anyInt());
    }
}
