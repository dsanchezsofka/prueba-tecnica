package sofka.pruebatecnica.domain.movement;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.entities.Movimientos;

public interface MovementSqlRepository {
   Mono<Movimientos> saveMovement(Movimientos movement, String date);
   Flux<Movimientos> findMovementRangeDate(String identification, String startDate, String endDate);
   Flux<Movimientos> findMovementRangeDate(Long account, String startDate, String endDate);
   Flux<Movimientos> findMovementAccount(Long account);
   Flux<Movimientos> findMovementIdentification(String identification);
}
