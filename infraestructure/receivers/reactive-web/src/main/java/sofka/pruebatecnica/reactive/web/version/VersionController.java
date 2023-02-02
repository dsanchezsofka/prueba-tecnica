package sofka.pruebatecnica.reactive.web.version;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version")
public class VersionController {
    @GetMapping(produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> version() {
        return new ResponseEntity<>("app-name v#{Build.buildNumber}#", HttpStatus.OK);
    }
}
