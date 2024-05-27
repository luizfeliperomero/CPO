package ufsm.csi.cpo.modules.credentials;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.security.JwtService;

import java.util.Map;

@RestController
@RequestMapping("ocpi/cpo/credentials")
public class CredentialsController {
    private final JwtService jwtService;
    private final CredentialsService credentialsService;

    public CredentialsController(JwtService jwtService, CredentialsService credentialsService) {
        this.jwtService = jwtService;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/get_token")
    public ResponseEntity<String> getToken() {
        return ResponseEntity.ok(this.jwtService.generateToken());
    }

    @PostMapping()
    public ResponseEntity<String> exchangeCredentials(@RequestBody Credentials credentials) {
        return ResponseEntity.ok(this.credentialsService.exchangeCredentials(credentials));
    }
}
