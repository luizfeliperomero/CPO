package ufsm.csi.cpo.modules.credentials;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufsm.csi.cpo.data.CpoData;
import ufsm.csi.cpo.security.JwtService;

@RestController
@RequestMapping("/2.2.1/credentials")
public class CredentialsController {
    private final CredentialsTokenService credentialsTokenService;
    private final CredentialsService credentialsService;

    public CredentialsController(JwtService jwtService, CredentialsTokenService credentialsTokenService, CredentialsService credentialsService) {
        this.credentialsTokenService = credentialsTokenService;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/get_token")
    public ResponseEntity<String> getToken() {
        String token = this.credentialsTokenService.generateToken();
        this.credentialsTokenService.validateToken(token);
        this.credentialsService.setTokenA(token);
        return ResponseEntity.ok(this.credentialsTokenService.encodeToken(token));
    }

    @SneakyThrows
    @PostMapping("/sender")
    public ResponseEntity<Credentials> exchangeCredentialsSender(@RequestBody Credentials credentials) {
        return ResponseEntity.ok(this.credentialsService.exchangeCredentialsAsSender(credentials));
    }

    @SneakyThrows
    @PostMapping("/receiver")
    public ResponseEntity<Credentials> exchangeCredentialsReceiver(@RequestBody Credentials credentials) {
        return ResponseEntity.ok(this.credentialsService.exchangeCredentialsAsReceiver(credentials));
    }
}
