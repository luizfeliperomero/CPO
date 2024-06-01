package ufsm.csi.cpo.modules.credentials;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufsm.csi.cpo.data.CpoData;
import ufsm.csi.cpo.exceptions.NoMutualVersion;
import ufsm.csi.cpo.exceptions.PlatformAlreadyRegistered;
import ufsm.csi.cpo.modules.types.CiString;
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
        String token = this.credentialsTokenService.generateToken();
        this.credentialsTokenService.validateToken(token);
        this.credentialsService.setTokenA(token);
        return ResponseEntity.ok(this.credentialsTokenService.encodeToken(token));
    }

    @SneakyThrows
    @PostMapping("/sender")
    public Credentials registerAsSender(@RequestHeader(value = "Authorization") String token, @RequestBody Credentials credentials) {
        return this.credentialsService.registerAsSender(credentials, this.credentialsTokenService.getTokenFromAuthorizationHeader(token));
    }

    @SneakyThrows
    @PostMapping("/receiver")
    public Credentials registerAsReceiver(@RequestHeader(value = "Authorization") String token, @RequestBody Credentials credentials) {
        return this.credentialsService.registerAsReceiver(credentials, this.credentialsTokenService.getTokenFromAuthorizationHeader(token));
    }

    @SneakyThrows
    @DeleteMapping
    public ResponseEntity unregister(@RequestHeader(value = "Authorization") String token, Credentials credentials) {
        this.credentialsService.unregisterPlatform(credentials, this.credentialsTokenService.getTokenFromAuthorizationHeader(token));
        return ResponseEntity.ok("");
    }
}
