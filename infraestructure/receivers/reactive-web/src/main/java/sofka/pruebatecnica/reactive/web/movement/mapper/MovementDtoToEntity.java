package sofka.pruebatecnica.reactive.web.movement.mapper;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sofka.pruebatecnica.domain.common.ObjectMapperDomain;
import sofka.pruebatecnica.domain.entities.Movimientos;
import sofka.pruebatecnica.reactive.web.movement.dto.MovimientoDto;

@Component
public class MovementDtoToEntity implements ObjectMapperDomain {

    @Autowired
    private ObjectMapper mapper;

    public Movimientos dtoToEntity(MovimientoDto movimientoDto) {
        return Movimientos.builder()
                .cuentaId(movimientoDto.getCuentaId())
                .fecha(movimientoDto.getFecha())
                .tipo(movimientoDto.getTipo())
                .valor(movimientoDto.getValor())
                .saldo(movimientoDto.getSaldo())
                .build();
    }
}
