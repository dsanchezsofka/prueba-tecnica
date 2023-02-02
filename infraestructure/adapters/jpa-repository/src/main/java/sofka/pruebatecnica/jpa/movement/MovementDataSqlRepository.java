package sofka.pruebatecnica.jpa.movement;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import sofka.pruebatecnica.jpa.data.MovimientoData;

import javax.transaction.Transactional;
import java.util.List;

public interface MovementDataSqlRepository extends CrudRepository<MovimientoData, Integer>,
        QueryByExampleExecutor<MovimientoData> {

    @Transactional
    @Query(value = "INSERT INTO movimientos (cuentaId,fecha,tipo,valor,saldo) VALUES(?1,?2,?3,?4,?5) SELECT @@IDENTITY RETORNO", nativeQuery = true)
    Integer saveMovement(Integer cuentaId, String fecha, String tipo, Double valor, Double saldo);

    @Query(value = "SELECT * FROM movimientos INNER JOIN cuenta ON movimientos.cuentaId = cuenta.cuentaId INNER JOIN cliente ON cuenta.clienteId = cuenta.clienteId INNER JOIN persona ON cliente.personaId = persona.personaId WHERE persona.identificacion = ?1 AND movimientos.fecha BETWEEN ?2 AND ?3 ORDER BY numero, fecha DESC", nativeQuery = true)
    List<MovimientoData> findMovementIdentificationRangeDate(String identification, String startDate, String endDate);

    @Query(value = "SELECT * FROM movimientos INNER JOIN cuenta ON movimientos.cuentaId = cuenta.cuentaId WHERE cuenta.numero = ?1 AND movimientos.fecha BETWEEN ?2 AND ?3 ORDER BY fecha DESC", nativeQuery = true)
    List<MovimientoData> findMovementAccountRangeDate(Long account, String startDate, String endDate);

    @Query(value = "SELECT * FROM movimientos INNER JOIN cuenta ON movimientos.cuentaId = cuenta.cuentaId WHERE cuenta.numero = ?1 ORDER BY fecha DESC", nativeQuery = true)
    List<MovimientoData> findMovementAccount(Long account);

    @Query(value = "SELECT * FROM movimientos INNER JOIN cuenta ON movimientos.cuentaId = cuenta.cuentaId INNER JOIN cliente ON cuenta.clienteId = cuenta.clienteId INNER JOIN persona ON cliente.personaId = persona.personaId WHERE persona.identificacion = ?1 ORDER BY numero, fecha DESC", nativeQuery = true)
    List<MovimientoData> findMovementIdentification(String identification);
}
