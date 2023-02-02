package sofka.pruebatecnica.jpa.client;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import sofka.pruebatecnica.jpa.data.ClienteData;

import javax.transaction.Transactional;
import java.util.List;

public interface ClientDataSqlRepository extends CrudRepository<ClienteData, Integer>,
        QueryByExampleExecutor<ClienteData> {

    @Transactional
    @Query(value = "INSERT INTO cliente (personaId,contrasena,estado) VALUES(?1,?2,?3) SELECT @@IDENTITY RETORNO", nativeQuery = true)
    Integer saveClient(Integer idPersona, String contrasena, Integer estado);

    @Query(value = "SELECT cliente.clienteId FROM cliente INNER JOIN persona ON cliente.personaId = persona.personaId WHERE persona.identificacion = ?1", nativeQuery = true)
    Integer findIdCliente(String identification);

    @Query(value = "SELECT * FROM cliente INNER JOIN persona ON cliente.personaId = persona.personaId", nativeQuery = true)
    List<ClienteData> findAll();

    @Query(value = "SELECT * FROM cliente INNER JOIN persona ON cliente.personaId = persona.personaId WHERE persona.identificacion = ?1", nativeQuery = true)
    ClienteData findClient(String identification);

    @Query(value = "SELECT * FROM cliente INNER JOIN persona ON cliente.personaId = persona.personaId WHERE cliente.clienteId = ?1", nativeQuery = true)
    ClienteData findClient(Integer clientId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cliente SET cliente.contrasena = ?1, cliente.estado = ?2 FROM cliente INNER JOIN persona ON cliente.personaId = persona.personaId WHERE persona.identificacion = ?3", nativeQuery = true)
    Integer updateClient(String contrasena, Integer estado, String identificacion);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cliente SET cliente.estado = 0 FROM cliente WHERE cliente.clienteId = ?1 SELECT @@IDENTITY RETORNO", nativeQuery = true)
    Integer disableClient(Integer clientId);
}
