package sofka.pruebatecnica.domain.common.ex;

import org.junit.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class BusinessExceptionTest {

    @Test
    public void testException() {
        final BusinessException ex = BusinessException.Type.ERROR_INPUT_DATA.build("");
        assertThat(ex).hasMessage("Error en los datos de entrada. ");
        assertThat(ex.getCode()).isEqualTo("ERROR_INPUT_DATA");
        assertThat(BusinessException.Type.ERROR_INPUT_DATA.getMessage())
                .isEqualTo("Error en los datos de entrada");
    }

    @Test
    public void testExceptionDefer() {
        final Supplier<Throwable> defer = BusinessException.Type.ERROR_INPUT_DATA.defer("");
        assertThat(defer.get()).hasMessage("Error en los datos de entrada. ");
        assertThat(((BusinessException)defer.get()).getCode()).isEqualTo("ERROR_INPUT_DATA");
    }
}