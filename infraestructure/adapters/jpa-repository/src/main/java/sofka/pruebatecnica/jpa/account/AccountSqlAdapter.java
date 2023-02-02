package sofka.pruebatecnica.jpa.account;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.account.AccountSqlRepository;
import sofka.pruebatecnica.domain.common.ObjectMapperDomain;
import sofka.pruebatecnica.domain.common.ex.BusinessException;
import sofka.pruebatecnica.domain.entities.Cuenta;
import sofka.pruebatecnica.jpa.data.CuentaData;
import sofka.pruebatecnica.reactive.repository.jpa.AdapterOperations;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class AccountSqlAdapter
        extends AdapterOperations<Cuenta, CuentaData, Integer, AccountDataSqlRepository>
        implements AccountSqlRepository, ObjectMapperDomain {

    public static final String ADAPTER_ACCOUNT_SQL_ERROR = "Adapter AccountSqlAdapter error:";

    @Autowired
    public AccountSqlAdapter(AccountDataSqlRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Cuenta.CuentaBuilder.class)
                .build());
    }

    @Override
    public Mono<Cuenta> saveAccount(Cuenta cuenta, Integer clientId) {
        return Mono.just(repository.saveAccount(clientId, cuenta.getNumero(), cuenta.getTipo(), cuenta.getSaldoInicial(), Boolean.compare(cuenta.getEstado(), false)))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_ACCOUNT_SQL_ERROR + error.toString())))
                .map(idAcc -> cuenta);
    }

    @Override
    public Mono<Cuenta> findAccount(Long account) {
        return Mono.just(repository.findAccount(account))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_ACCOUNT_SQL_ERROR + error.toString())))
                .flatMap(cuentaData -> Objects.isNull(cuentaData) ? Mono.error(new BusinessException(BusinessException.Type.NOT_FOUND_SQL, ""))
                        : Mono.just(toEntity(cuentaData)));
    }

    @Override
    public Flux<Cuenta> findAccounts(Integer clientId) {
        return Mono.just(repository.findAccounts(clientId))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_ACCOUNT_SQL_ERROR + error.toString())))
                .flatMapMany(cuentaDataList -> Objects.isNull(cuentaDataList) ? Mono.error(new BusinessException(BusinessException.Type.NOT_FOUND_SQL, ""))
                        : Flux.fromIterable(toEntities(cuentaDataList)));
    }

    @Override
    public Flux<Cuenta> findAccountAll() {
        return Mono.just(repository.findAccounts())
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_ACCOUNT_SQL_ERROR + error.toString())))
                .flatMapMany(cuentaDataList -> Objects.isNull(cuentaDataList) ? Mono.error(new BusinessException(BusinessException.Type.NOT_FOUND_SQL, ""))
                        : Flux.fromIterable(toEntities(cuentaDataList)));
    }

    @Override
    public Mono<Cuenta> updateAccount(Cuenta cuenta) {
        return Mono.just(repository.updateAccount(cuenta.getNumero(), cuenta.getTipo(), cuenta.getSaldoInicial(), Boolean.compare(cuenta.getEstado(), false)))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_ACCOUNT_SQL_ERROR + error.toString())))
                .thenReturn(cuenta);
    }

    @Override
    public Mono<Long> deleteAccount(Long account) {
        return Mono.just(repository.disableAccount(account))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_ACCOUNT_SQL_ERROR + error.toString())))
                .thenReturn(account);
    }

    private List<Cuenta> toEntities(List<CuentaData> cuentaDataList) {
        return cuentaDataList.stream().map
                (this::toEntity
                ).collect(Collectors.toList());
    }

    protected Cuenta toEntity(CuentaData cuentaData) {
        return Cuenta.builder()
                .cuentaId(cuentaData.getCuentaId())
                .clienteId(cuentaData.getClienteId())
                .numero(cuentaData.getNumero())
                .tipo(cuentaData.getTipo())
                .saldoInicial(cuentaData.getSaldoInicial())
                .estado(cuentaData.getEstado())
                .build();
    }
}
