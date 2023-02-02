package sofka.pruebatecnica.reactive.web.common.dto;

import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseDTOTest {

    private static final String TEST = "TEST";
    private static final Integer CODE_TEST= 1234;

    @Test
    public void correctExecution(){
        List<ErrorDTO> errors = Collections.singletonList(ErrorDTO.builder()
                .code(CODE_TEST)
                .message(TEST)
                .build());

        StatusDTO status= StatusDTO.builder()
                .status(TEST)
                .code(CODE_TEST)
                .message(TEST)
                .build();

        HeaderOutDTO headerOut = HeaderOutDTO.builder()
                .applicationId(TEST)
                .status(status)
                .errors(errors)
                .build();

        ResponseDTO responseDTO =new ResponseDTO();
        responseDTO.setData(TEST);
        responseDTO.setHeader(headerOut);

        assertThat(responseDTO.getData()).isEqualTo(TEST);
        assertThat(responseDTO.getHeader()).isEqualTo(headerOut);
    }

    @Test
    public void correctExecution_AllArgsConstructor(){
        List<ErrorDTO> errors = Collections.singletonList(ErrorDTO.builder()
                .code(CODE_TEST)
                .message(TEST)
                .build());

        StatusDTO status= StatusDTO.builder()
                .status(TEST)
                .code(CODE_TEST)
                .message(TEST)
                .build();

        HeaderOutDTO headerOut = HeaderOutDTO.builder()
                .applicationId(TEST)
                .status(status)
                .errors(errors)
                .build();

        ResponseDTO responseDTO =new ResponseDTO(headerOut, TEST);

        assertThat(responseDTO.getData()).isEqualTo(TEST);
        assertThat(responseDTO.getHeader()).isEqualTo(headerOut);
    }

    @Test
    public void correctExecution_Builder(){
        List<ErrorDTO> errors = Collections.singletonList(ErrorDTO.builder()
                .code(CODE_TEST)
                .message(TEST)
                .build());

        StatusDTO status= StatusDTO.builder()
                .status(TEST)
                .code(CODE_TEST)
                .message(TEST)
                .build();

        HeaderOutDTO headerOut = HeaderOutDTO.builder()
                .applicationId(TEST)
                .status(status)
                .errors(errors)
                .build();

        ResponseDTO responseDTO = ResponseDTO.builder()
                .header(headerOut)
                .data(TEST)
                .build();

        assertThat(responseDTO.getData()).isEqualTo(TEST);
        assertThat(responseDTO.getHeader()).isEqualTo(headerOut);
    }


}