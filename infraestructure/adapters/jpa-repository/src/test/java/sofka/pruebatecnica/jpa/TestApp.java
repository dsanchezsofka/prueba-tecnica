package sofka.pruebatecnica.jpa;

import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@TestPropertySource("classpath:application_test.properties")
public class TestApp {

    public static void main(String[] args) {
        run(TestApp.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImp();
    }
}
