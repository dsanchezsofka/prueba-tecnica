package sofka.pruebatecnica.jpa.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;
import sofka.pruebatecnica.domain.entities.Cliente;
import sofka.pruebatecnica.jpa.TestApp;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApp.class)
@TestPropertySource("classpath:application_test.properties")
public class ClientSqlAdapterTest {

    @Autowired
    private ClientSqlAdapter adapter;

    private final Cliente entity = Cliente.builder().contrasena("123").estado(Boolean.TRUE).build();
    private final Cliente entity2 = Cliente.builder().contrasena("123").estado(Boolean.TRUE).build();
    private final Cliente entity3 = Cliente.builder()
            .personaId(1).nombre("Daniel Sanchez").genero("M").edad(30).identificacion("20650252")
            .direccion("Fortaleza, Brasil").telefono("123456789").clienteid(1).contrasena("123")
            .estado(Boolean.TRUE).build();
    private final Cliente entity4 = Cliente.builder()
            .personaId(2).nombre("Jose Vegas").genero("M").edad(26).identificacion("25205602")
            .direccion("Caracas, Venezuela").telefono("987654321").clienteid(2).contrasena("321")
            .estado(Boolean.FALSE).build();
    private final Cliente entity5 = Cliente.builder().contrasena("123").estado(Boolean.TRUE)
            .identificacion("20650252").build();

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:client/insert.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:client/delete.sql")
    public void saveClientTest() {
        StepVerifier.create(adapter.saveClient(entity,1))
                .expectNext(entity2)
                .verifyComplete();
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:client/insertPersonClient.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:client/delete.sql")
    public void findIdClientTest() {
        StepVerifier.create(adapter.findIdCliente("20650252"))
                .assertNext(clientId -> assertThat(clientId).isNotNull().isPositive())
                .verifyComplete();
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:client/insertPersonClient.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:client/delete.sql")
    public void findClientIdentificationTest() {
        StepVerifier.create(adapter.findClient("20650252"))
                .expectNext(entity3)
                .verifyComplete();
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:client/insertPersonClient.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:client/delete.sql")
    public void findClientIdTest() {
        StepVerifier.create(adapter.findClient(1))
                .expectNext(entity3)
                .verifyComplete();
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:client/insertAll.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:client/delete.sql")
    public void findAllTest() {
        StepVerifier.create(adapter.findAll())
                .expectNext(entity3)
                .expectNext(entity4)
                .verifyComplete();
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:client/insert.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:client/delete.sql")
    public void updateClientTest() {
        StepVerifier.create(adapter.updateClient(entity5))
                .expectNext(entity5.getIdentificacion())
                .verifyComplete();

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:client/insertPersonClient.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:client/delete.sql")
    public void deleteClientTest() {
        StepVerifier.create(adapter.deleteClient(1))
                .assertNext(clientId -> assertThat(clientId).isNotNull().isPositive())
                .verifyComplete();
    }
}
