package sofka.pruebatecnica.domain.bank;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
import sofka.pruebatecnica.domain.account.AccountSqlRepository;
import sofka.pruebatecnica.domain.client.ClientSqlRepository;
import sofka.pruebatecnica.domain.common.ex.BusinessException;
import sofka.pruebatecnica.domain.entities.*;
import sofka.pruebatecnica.domain.movement.MovementSqlRepository;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Log
@RequiredArgsConstructor
public class MovementUseCase {

    private final MovementSqlRepository movementSqlRepository;
    private final AccountSqlRepository accountSqlRepository;
    private final ClientSqlRepository clientSqlRepository;

    private static final Predicate<String> NULL_OR_EMPTY_PREDICATE = date -> Objects.isNull(date) || date.isEmpty();

    public Mono<Movimientos> createMovement(Double value, Long account) {
        if (!Objects.isNull(value) && value != 0) {
            return accountSqlRepository.findAccount(account)
                    .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                            .build("No se encontró información de la cuenta en la base de datos")))
                    .flatMap(cuenta -> validateMovement(cuenta, value)
                            .flatMap(movement -> saveMovement(movement))
                            .flatMap(movement -> updateAccount(cuenta, movement)));
        } else {
            return Mono.error(BusinessException.Type.ERROR_OPERATION_INVALID
                            .build("Debe ingresar una cifra diferente de null y diferente de cero."));
        }
    }

    private Mono<Movimientos> validateMovement(Cuenta cuenta, Double value) {
        return value > 0 ? creditOperation(cuenta, value)
                        : debitOperation(cuenta, value);
    }

    private Mono<Movimientos> saveMovement(Movimientos movement) {
        return movementSqlRepository.saveMovement(movement, convertDateToTimestamp(new Date()));
    }

    private Mono<Movimientos> creditOperation(Cuenta cuenta, Double value) {
        return Mono.just(Movimientos.builder()
                .cuentaId(cuenta.getCuentaId())
                .fecha(new Date())
                .tipo("Credito")
                .valor(value)
                .saldo(cuenta.getSaldoInicial())
                .build());
    }

    private Mono<Movimientos> debitOperation(Cuenta cuenta, Double value) {
        if ((cuenta.getSaldoInicial() + value) >= 0) {
            return Mono.just(Movimientos.builder()
                    .cuentaId(cuenta.getCuentaId())
                    .fecha(new Date())
                    .tipo("Debito")
                    .valor(value)
                    .saldo(cuenta.getSaldoInicial())
                    .build());
        } else {
            return Mono.error(BusinessException.Type.ERROR_OPERATION_INVALID
                    .build("Saldo no disponible"));
        }
    }

    private Mono<Movimientos> updateAccount(Cuenta cuenta, Movimientos movement) {
        Double value;
        if (movement.getTipo().equalsIgnoreCase("Credito")) {
            value = cuenta.getSaldoInicial() + movement.getValor();
        } else {
            value = cuenta.getSaldoInicial() + movement.getValor();
        }
        return accountSqlRepository.updateAccount(cuenta.toBuilder().saldoInicial(value).build())
                .thenReturn(movement);
    }

    public Mono<Report> getMovement(Long account, String identification, String startDate, String endDate) {
        if (!NULL_OR_EMPTY_PREDICATE.test(startDate)) {
            if (!NULL_OR_EMPTY_PREDICATE.test(endDate)) {
                return findMovementRangeDate(account, identification, startDate, endDate);
            } else {
                return findMovementSpecificDate(account, identification, startDate);
            }
        } else {
            return findMovement(account, identification);
        }
    }

    public Mono<Report> findMovementRangeDate(Long account, String identification, String startDate, String endDate) {
        if (!Objects.isNull(account)) {
            return findAccount(account)
                    .flatMap(cuenta -> findClient(cuenta.getClienteId())
                            .flatMap(cliente -> findMovementAccount(account, startDate, endDate)
                                    .flatMap(movimients -> createReport(cliente, cuenta, movimients))));
        } else {
            return findClient(identification)
                    .flatMap(cliente -> findAccounts(cliente.getClienteid())
                            .flatMap(accountList -> findMovementIdentification(identification, startDate, endDate)
                                    .flatMap(movimients -> createReport(cliente, accountList, movimients))));
        }
    }

    public Mono<Report> findMovementSpecificDate(Long account, String identification, String startDate) {
        if (!Objects.isNull(account)) {
            return findAccount(account)
                    .flatMap(cuenta -> findClient(cuenta.getClienteId())
                            .flatMap(cliente -> findMovementAccount(account, startDate)
                                    .flatMap(movimients -> createReport(cliente, cuenta, movimients))));
        } else {
            return findClient(identification)
                    .flatMap(cliente -> findAccounts(cliente.getClienteid())
                            .flatMap(accountList -> findMovementIdentification(identification, startDate)
                                    .flatMap(movimients -> createReport(cliente, accountList, movimients))));
        }
    }

    public Mono<Report> findMovement(Long account, String identification) {
        if (!Objects.isNull(account)) {
            return findAccount(account)
                    .flatMap(cuenta -> findClient(cuenta.getClienteId())
                            .flatMap(cliente -> findMovementAccount(account)
                                    .flatMap(movimients -> createReport(cliente, cuenta, movimients))));
        } else {
            return findClient(identification)
                    .flatMap(cliente -> findAccounts(cliente.getClienteid())
                            .flatMap(accountList -> findMovementIdentification(identification)
                                    .flatMap(movimients -> createReport(cliente, accountList, movimients))));
        }
    }

    public Mono<List<Movimientos>> findMovementAccount(Long account) {
        return movementSqlRepository.findMovementAccount(account)
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de los movimientos en la base de datos")))
                .collectList();
    }

    public Mono<List<Movimientos>> findMovementAccount(Long account, String startDate) {
        return movementSqlRepository.findMovementRangeDate(account, convertDateToTimestamp(startDate,
                        " 00:00:00.000"), convertDateToTimestamp(startDate, " 24:59:59.999"))
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de los movimientos en la base de datos")))
                .collectList();
    }

    public Mono<List<Movimientos>> findMovementAccount(Long account, String startDate, String endDate) {
        return movementSqlRepository.findMovementRangeDate(account, convertDateToTimestamp(startDate,
                        " 00:00:00.000"), convertDateToTimestamp(endDate, " 24:59:59.999"))
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de los movimientos en la base de datos")))
                .collectList();
    }

    public Mono<List<Movimientos>> findMovementIdentification(String identification) {
        return movementSqlRepository.findMovementIdentification(identification)
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de los movimientos en la base de datos")))
                .collectList();
    }

    public Mono<List<Movimientos>> findMovementIdentification(String identification, String startDate) {
        return movementSqlRepository.findMovementRangeDate(identification, convertDateToTimestamp(startDate,
                        " 00:00:00.000"), convertDateToTimestamp(startDate, " 24:59:59.999"))
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de los movimientos en la base de datos")))
                .collectList();
    }

    public Mono<List<Movimientos>> findMovementIdentification(String identification, String startDate, String endDate) {
        return movementSqlRepository.findMovementRangeDate(identification, convertDateToTimestamp(startDate,
                        " 00:00:00.000"), convertDateToTimestamp(endDate, " 24:59:59.999"))
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de los movimientos en la base de datos")))
                .collectList();
    }

    public Mono<Cliente> findClient(Integer clientId) {
        return clientSqlRepository.findClient(clientId)
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información del cliente en la base de datos")));
    }

    public Mono<Cliente> findClient(String identification) {
        return clientSqlRepository.findClient(identification)
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información del cliente en la base de datos")));
    }

    public Mono<Cuenta> findAccount(Long account) {
        return accountSqlRepository.findAccount(account)
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de la cuenta en la base de datos, valide el " +
                                "número de cuenta y vuelva a intentarlo")));
    }

    public Mono<List<Cuenta>> findAccounts(Integer clientId) {
        return accountSqlRepository.findAccounts(clientId)
                .switchIfEmpty(Mono.error(BusinessException.Type.NOT_FOUND_SQL
                        .build("No se encontró información de la cuenta en la base de datos, valide el " +
                                "número de cuenta y vuelva a intentarlo")))
                .collectList();
    }

    public Mono<Report> createReport(Cliente client, Cuenta account, List<Movimientos> movements) {
        return Mono.just(Report.builder()
                .cliente(client.getNombre())
                .cuentaList(Collections.singletonList(AccountReport.builder()
                        .cuenta(account.getNumero())
                        .tipo(account.getTipo())
                        .estado(account.getEstado())
                        .movimientosList(createMovementReport(movements))
                        .build()))
                .build());
    }

    public Mono<Report> createReport(Cliente client, List<Cuenta> accounts, List<Movimientos> movements) {
        return Mono.just(Report.builder()
                .cliente(client.getNombre())
                .cuentaList(createAccountReport(accounts, movements))
                .build());
    }

    public List<Movimientos> createMovementReport(List<Movimientos> movements) {
        List<Movimientos> movimientosList = new ArrayList<>();
        movements.forEach(movimient ->
                movimientosList.add(movimient.toBuilder()
                        .saldoDisponible(movimient.getSaldo() + movimient.getValor())
                        .build()));
        return movimientosList;
    }

    public List<AccountReport> createAccountReport(List<Cuenta> accounts, List<Movimientos> movimients) {
        List<AccountReport> cuentaList = new ArrayList<>();
        accounts.forEach(account ->
                cuentaList.add(AccountReport.builder()
                        .cuenta(account.getNumero())
                        .tipo(account.getTipo())
                        .estado(account.getEstado())
                        .movimientosList(createMovementReport(movimients.stream()
                                .filter(movimient -> movimient.getCuentaId() == account.getCuentaId())
                                .collect(Collectors.toList())))
                        .build()));
        return cuentaList;
    }

    public static Timestamp convertStringToTimestamp(String strDate) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(strDate);
            Timestamp timeStampDate = new Timestamp(date.getTime());

            return timeStampDate;
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
    }

    public static String convertDateToTimestamp(Date date) {
        Timestamp timeStampDate = new Timestamp(date.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        return timeStampDate.toString();
    }

    public static String convertDateToTimestamp(String date, String complement) {
        SimpleDateFormat inSDF = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");

        Date dateConvert = null;
        try {
            dateConvert = inSDF.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outSDF.format(dateConvert).concat(complement);
    }
}
