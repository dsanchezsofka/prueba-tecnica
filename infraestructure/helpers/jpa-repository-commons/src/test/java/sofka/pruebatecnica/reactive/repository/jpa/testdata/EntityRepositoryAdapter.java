package sofka.pruebatecnica.reactive.repository.jpa.testdata;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import sofka.pruebatecnica.reactive.repository.jpa.AdapterOperations;

@Repository
public class EntityRepositoryAdapter extends AdapterOperations<DomainEntity, DataEntity, String, DataRepository> {

    public EntityRepositoryAdapter(DataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, DomainEntity.DomainEntityBuilder.class).build());
    }
}
