package sofka.pruebatecnica.jpa.movement;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.common.ObjectMapperDomain;
import sofka.pruebatecnica.domain.common.ex.BusinessException;
import sofka.pruebatecnica.domain.entities.Movimientos;
import sofka.pruebatecnica.domain.movement.MovementSqlRepository;
import sofka.pruebatecnica.jpa.data.MovimientoData;
import sofka.pruebatecnica.reactive.repository.jpa.AdapterOperations;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class MovementSqlAdapter
        extends AdapterOperations<Movimientos, MovimientoData, Integer, MovementDataSqlRepository>
        implements MovementSqlRepository, ObjectMapperDomain {

    public static final String ADAPTER_MOVEMENT_SQL_ERROR = "Adapter Movement error:";

    @Autowired
    public MovementSqlAdapter(MovementDataSqlRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Movimientos.MovimientosBuilder.class)
                .build());
    }

    @Override
    public Mono<Movimientos> saveMovement(Movimientos movement, String date) {
        return Mono.just(repository.saveMovement(movement.getCuentaId(), date, movement.getTipo(),
                        movement.getValor(), movement.getSaldo()))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_MOVEMENT_SQL_ERROR + error.toString())))
                .map(idAcc -> movement);
    }

    @Override
    public Flux<Movimientos> findMovementRangeDate(String identification, String startDate, String endDate) {
        return Mono.just(repository.findMovementIdentificationRangeDate(identification, startDate, endDate))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_MOVEMENT_SQL_ERROR + error.toString())))
                .flatMapMany(movimientoDataList -> Objects.isNull(movimientoDataList)
                        ? Mono.error(new BusinessException(BusinessException.Type.NOT_FOUND_SQL, ""))
                        : Flux.fromIterable(toEntities(movimientoDataList)));
    }

    @Override
    public Flux<Movimientos> findMovementRangeDate(Long account, String startDate, String endDate) {
        return Mono.just(repository.findMovementAccountRangeDate(account, startDate, endDate))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_MOVEMENT_SQL_ERROR + error.toString())))
                .flatMapMany(movimientoDataList -> Objects.isNull(movimientoDataList)
                        ? Mono.error(new BusinessException(BusinessException.Type.NOT_FOUND_SQL, ""))
                        : Flux.fromIterable(toEntities(movimientoDataList)));
    }

    @Override
    public Flux<Movimientos> findMovementAccount(Long account) {
        return Mono.just(repository.findMovementAccount(account))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_MOVEMENT_SQL_ERROR + error.toString())))
                .flatMapMany(cuentaDataList -> Objects.isNull(cuentaDataList)
                        ? Mono.error(new BusinessException(BusinessException.Type.NOT_FOUND_SQL, ""))
                        : Flux.fromIterable(toEntities(cuentaDataList)));
    }

    @Override
    public Flux<Movimientos> findMovementIdentification(String identification) {
        return Mono.just(repository.findMovementIdentification(identification))
                .onErrorResume(error -> Mono.error(new RuntimeException(ADAPTER_MOVEMENT_SQL_ERROR + error.toString())))
                .flatMapMany(cuentaDataList -> Objects.isNull(cuentaDataList)
                        ? Mono.error(new BusinessException(BusinessException.Type.NOT_FOUND_SQL, ""))
                        : Flux.fromIterable(toEntities(cuentaDataList)));
    }

    private List<Movimientos> toEntities(List<MovimientoData> movimientoDataList) {
        return movimientoDataList.stream().map
                (this::toEntity
                ).collect(Collectors.toList());
    }

    protected Movimientos toEntity(MovimientoData movimientoData) {
        return Movimientos.builder()
                .movimientosId(movimientoData.getMovimientosId())
                .cuentaId(movimientoData.getCuentaId())
                .fecha(movimientoData.getFecha())
                .tipo(movimientoData.getTipo())
                .valor(movimientoData.getValor())
                .saldo(movimientoData.getSaldo())
                .build();
    }
}
