package sofka.pruebatecnica;

import lombok.extern.java.Log;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sofka.pruebatecnica.domain.account.AccountSqlRepository;
import sofka.pruebatecnica.domain.bank.AccountUseCase;
import sofka.pruebatecnica.domain.bank.ClientUseCase;
import sofka.pruebatecnica.domain.bank.MovementUseCase;
import sofka.pruebatecnica.domain.client.ClientSqlRepository;
import sofka.pruebatecnica.domain.movement.MovementSqlRepository;
import sofka.pruebatecnica.domain.person.PersonSqlRepository;

@Log
@Configuration
public class ControllerConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImp();
    }

    @Bean
    public ClientUseCase clientUseCase(ClientSqlRepository clientSqlRepository,
                                       PersonSqlRepository personSqlRepository) {

        return new ClientUseCase(clientSqlRepository, personSqlRepository);
    }

    @Bean
    public AccountUseCase accountUseCase(AccountSqlRepository accountSqlRepository,
                                         ClientSqlRepository clientSqlRepository) {

        return new AccountUseCase(accountSqlRepository, clientSqlRepository);
    }

    @Bean
    public MovementUseCase movementUseCase(MovementSqlRepository movementSqlRepository,
                                           AccountSqlRepository accountSqlRepository,
                                           ClientSqlRepository clientSqlRepository) {

        return new MovementUseCase(movementSqlRepository, accountSqlRepository, clientSqlRepository);
    }
}
