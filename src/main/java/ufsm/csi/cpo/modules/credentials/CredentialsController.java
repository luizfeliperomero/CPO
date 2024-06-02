package ufsm.csi.cpo.modules.credentials;

import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @SneakyThrows
    @GetMapping()
    public Credentials getCredentials(@RequestHeader(value = "Authorization") String token) {
       return this.credentialsService.getCredentials(this.credentialsTokenService.getTokenFromAuthorizationHeader(token));
    }

    @GetMapping("/get_token")
    public ResponseEntity<String> getToken() {
        var token = this.credentialsTokenService.generateToken();
        this.credentialsTokenService.validateToken(token);
        this.credentialsService.setTokenA(token);
        return ResponseEntity.ok(this.credentialsTokenService.encodeToken(token));
    }

    @SneakyThrows
    @PostMapping("/sender")
    public Credentials registerAsSender(@RequestHeader(value = "Authorization") String tokenB, @RequestBody Credentials credentials) {
        return this.credentialsService.registerAsSender(credentials, this.credentialsTokenService.getTokenFromAuthorizationHeader(tokenB));
    }

    @SneakyThrows
    @PostMapping("/receiver")
    public Credentials registerAsReceiver(@RequestHeader(value = "Authorization") String token, @RequestBody Credentials credentials) {
        return this.credentialsService.registerAsReceiver(credentials, this.credentialsTokenService.getTokenFromAuthorizationHeader(token));
    }

    @SneakyThrows
    @PutMapping
    public void updateAsSender(@RequestHeader(value = "Authorization") String token) {
       this.credentialsService.updateVersionAsSender(this.credentialsTokenService.getTokenFromAuthorizationHeader(token));
    }

    @SneakyThrows
    @PutMapping
    public void updateAsReceiver(@RequestHeader(value = "Authorization") String token) {
        this.credentialsService.updateVersionAsReceiver(this.credentialsTokenService.getTokenFromAuthorizationHeader(token));
    }

    @SneakyThrows
    @DeleteMapping
    public ResponseEntity unregister(@RequestHeader(value = "Authorization") String token) {
        this.credentialsService.unregisterPlatform(this.credentialsTokenService.getTokenFromAuthorizationHeader(token));
        return ResponseEntity.ok("");
    }
}
