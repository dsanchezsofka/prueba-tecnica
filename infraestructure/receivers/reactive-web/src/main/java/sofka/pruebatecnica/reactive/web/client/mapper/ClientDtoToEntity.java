package sofka.pruebatecnica.reactive.web.client.mapper;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sofka.pruebatecnica.domain.common.ObjectMapperDomain;
import sofka.pruebatecnica.domain.entities.Cliente;
import sofka.pruebatecnica.reactive.web.client.dto.ClienteDto;

@Component
public class ClientDtoToEntity implements ObjectMapperDomain {

    @Autowired
    private ObjectMapper mapper;

    public Cliente dtoToEntity(ClienteDto clienteDto) {
        return Cliente.builder()
                .personaId(clienteDto.getPersonaId())
                .clienteid(clienteDto.getClienteid())
                .contrasena(clienteDto.getContrasena())
                .estado(clienteDto.getEstado())
                .nombre(clienteDto.getNombre())
                .genero(clienteDto.getGenero())
                .edad(clienteDto.getEdad())
                .identificacion(clienteDto.getIdentificacion())
                .direccion(clienteDto.getDireccion())
                .telefono(clienteDto.getTelefono())
                .build();
    }
}
