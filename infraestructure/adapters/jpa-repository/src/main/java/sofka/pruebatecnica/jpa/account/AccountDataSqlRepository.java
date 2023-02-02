package sofka.pruebatecnica.jpa.account;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import sofka.pruebatecnica.jpa.data.CuentaData;

import javax.transaction.Transactional;
import java.util.List;

public interface AccountDataSqlRepository extends CrudRepository<CuentaData, Integer>,
        QueryByExampleExecutor<CuentaData> {

    @Transactional
    @Query(value = "INSERT INTO cuenta (clienteId,numero,tipo,saldoInicial,estado) VALUES(?1,?2,?3,?4,?5) SELECT @@IDENTITY RETORNO", nativeQuery = true)
    Integer  saveAccount(Integer clienteId, Long numero,String tipo,Double saldoInicial, Integer estado);

    @Query(value = "SELECT * FROM cuenta WHERE numero = ?1", nativeQuery = true)
    CuentaData findAccount(Long account);

    @Query(value = "SELECT * FROM cuenta WHERE cuenta.clienteId = ?1", nativeQuery = true)
    List<CuentaData> findAccounts(Integer clientId);

    @Query(value = "SELECT * FROM cuenta", nativeQuery = true)
    List<CuentaData> findAccounts();

    @Modifying
    @Transactional
    @Query(value = "UPDATE cuenta SET cuenta.numero = ?1, cuenta.tipo = ?2, cuenta.saldoInicial = ?3, cuenta.estado = ?4 FROM cuenta WHERE cuenta.numero = ?1 SELECT @@IDENTITY RETORNO", nativeQuery = true)
    Integer updateAccount(Long numero, String tipo, Double saldoInicial, Integer estado);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cuenta SET cuenta.estado = 0 FROM cuenta WHERE cuenta.numero = ?1 SELECT @@IDENTITY RETORNO", nativeQuery = true)
    Integer disableAccount(Long account);
}
