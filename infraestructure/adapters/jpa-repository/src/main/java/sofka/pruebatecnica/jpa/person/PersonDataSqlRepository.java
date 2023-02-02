package sofka.pruebatecnica.jpa.person;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import sofka.pruebatecnica.jpa.data.PersonaData;

import javax.transaction.Transactional;

public interface PersonDataSqlRepository extends CrudRepository<PersonaData, Integer>,
        QueryByExampleExecutor<PersonaData> {

    @Transactional
    @Query(value = "INSERT INTO persona (nombre,genero,edad,identificacion,direccion,telefono) VALUES(?1,?2,?3,?4,?5,?6) SELECT @@IDENTITY RETORNO", nativeQuery = true)
    Integer savePerson(String nombre, String genero, Integer edad, String identificacion, String direccion, String telefono);

    @Modifying
    @Transactional
    @Query(value = "UPDATE persona SET persona.nombre = ?1, persona.genero = ?2, persona.edad = ?3, persona.direccion = ?4, persona.telefono = ?5 FROM persona WHERE persona.identificacion = ?6 SELECT @@IDENTITY RETORNO", nativeQuery = true)
    Integer updatePerson(String nombre, String genero, Integer edad, String direccion, String telefono, String identification);
}
