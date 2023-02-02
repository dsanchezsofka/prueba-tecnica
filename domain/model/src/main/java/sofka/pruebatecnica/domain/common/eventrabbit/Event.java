package sofka.pruebatecnica.domain.common.eventrabbit;

public interface Event {
    String name();
    Object getData();
}
