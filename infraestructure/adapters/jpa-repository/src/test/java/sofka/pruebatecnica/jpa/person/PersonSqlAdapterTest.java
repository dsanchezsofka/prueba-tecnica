package sofka.pruebatecnica.jpa.person;

import org.junit.Before;
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
public class PersonSqlAdapterTest {

    @Autowired
    private PersonSqlAdapter adapter;

    private Cliente entity;
    private Cliente entity2;

    @Before
    public void init() {
        entity = Cliente.builder()
                .personaId(1).nombre("Jose Vegas").genero("M").edad(30).identificacion("25205602")
                .direccion("Caracas, Venezuela").telefono("123456789").clienteid(1).contrasena("123")
                .estado(Boolean.TRUE).build();

        entity2 = Cliente.builder()
                .personaId(1).nombre("Daniel Sanchez").genero("M").edad(30).identificacion("20650252")
                .direccion("Fortaleza, Brasil").telefono("85996526557").clienteid(1).contrasena("123")
                .estado(Boolean.TRUE).build();
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:person/delete.sql")
    public void savePersonTest() {
        StepVerifier.create(adapter.savePerson(entity))
                .assertNext(personId -> assertThat(personId).isNotNull().isPositive())
                .verifyComplete();
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:person/insert.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:person/delete.sql")
    public void updatePersonTest() {
        StepVerifier.create(adapter.updatePerson(entity2))
                .assertNext(cliente -> assertThat(cliente).isNotNull())
                .verifyComplete();
    }
}
