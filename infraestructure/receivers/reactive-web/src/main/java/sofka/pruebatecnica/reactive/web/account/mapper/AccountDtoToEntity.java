package sofka.pruebatecnica.reactive.web.account.mapper;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sofka.pruebatecnica.domain.common.ObjectMapperDomain;
import sofka.pruebatecnica.domain.entities.Cuenta;
import sofka.pruebatecnica.reactive.web.account.dto.AccountDto;

@Component
public class AccountDtoToEntity implements ObjectMapperDomain {

    @Autowired
    private ObjectMapper mapper;

    public Cuenta dtoToEntity(AccountDto accountDto) {
        return Cuenta.builder()
                .cuentaId(accountDto.getId())
                .numero(accountDto.getNumero())
                .tipo(accountDto.getTipo())
                .saldoInicial(accountDto.getSaldoInicial())
                .estado(accountDto.getEstado())
                .build();
    }
}
