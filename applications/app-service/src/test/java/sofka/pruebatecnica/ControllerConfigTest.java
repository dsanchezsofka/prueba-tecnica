package sofka.pruebatecnica;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivecommons.utils.ObjectMapperImp;
import sofka.pruebatecnica.domain.account.AccountSqlRepository;
import sofka.pruebatecnica.domain.bank.AccountUseCase;
import sofka.pruebatecnica.domain.bank.ClientUseCase;
import sofka.pruebatecnica.domain.bank.MovementUseCase;
import sofka.pruebatecnica.domain.client.ClientSqlRepository;
import sofka.pruebatecnica.domain.movement.MovementSqlRepository;
import sofka.pruebatecnica.domain.person.PersonSqlRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ControllerConfigTest {

    @InjectMocks
    private ControllerConfig controllerConfig;

    @Mock
    private ClientSqlRepository clientSqlRepository;

    @Mock
    private AccountSqlRepository accountSqlRepository;

    @Mock
    private PersonSqlRepository personSqlRepository;

    @Mock
    private MovementSqlRepository movementSqlRepository;

    @Test
    public void objectMapper() {
        assertThat(controllerConfig.objectMapper())
                .isInstanceOf(ObjectMapperImp.class);
    }

    @Test
    public void controllerConfigTest() {
        assertThat(controllerConfig
                .clientUseCase(clientSqlRepository, personSqlRepository))
                .isInstanceOf(ClientUseCase.class);
    }

    @Test
    public void clientUseCaseTest() {
        assertThat(controllerConfig
                .clientUseCase(clientSqlRepository, personSqlRepository))
                .isInstanceOf(ClientUseCase.class);
    }

    @Test
    public void accountUseCaseTest() {
        assertThat(controllerConfig
                .accountUseCase(accountSqlRepository, clientSqlRepository))
                .isInstanceOf(AccountUseCase.class);
    }

    @Test
    public void movementUseCaseTest() {
        assertThat(controllerConfig
                .movementUseCase(movementSqlRepository, accountSqlRepository, clientSqlRepository))
                .isInstanceOf(MovementUseCase.class);
    }
}