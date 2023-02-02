package sofka.pruebatecnica.domain.common.ex;

import java.util.function.Supplier;

public class BusinessException extends ApplicationException {

    public enum Type {
        NOT_FOUND_SQL("Documento no encontrado en SQL"),
        NOT_FOUND_INSERT_SQL("Error guardando en SQL"),
        ERROR_INPUT_DATA("Error en los datos de entrada"),
        ERROR_OPERATION_INVALID("Transaccion invalida");

        private final String message;

        public String getMessage() {
            return message;
        }

        public BusinessException build(String additionalInfo) {
            return new BusinessException(this, additionalInfo);
        }

        public Supplier<Throwable> defer(String additionalInfo) {
            return () -> new BusinessException(this, additionalInfo);
        }

        Type(String message) {
            this.message = message;
        }

    }

    private final Type type;

    public BusinessException(Type type, String additionalInfo) {
        super(type.message + ". " + additionalInfo);
        this.type = type;
    }

    @Override
    public String getCode() {
        return type.name();
    }


}
