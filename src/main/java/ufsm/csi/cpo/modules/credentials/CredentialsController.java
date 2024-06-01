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
    @GetMapping("/{partyId}")
    public Credentials getCredentials(@PathVariable String partyId) {
       return this.credentialsService.getCredentials(new CiString(partyId));
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
    public Credentials registerAsSender(@RequestBody Credentials credentials) {
        return this.credentialsService.registerAsSender(credentials);
    }

    @SneakyThrows
    @PostMapping("/receiver")
    public Credentials registerAsReceiver(@RequestBody Credentials credentials) {
        return this.credentialsService.registerAsReceiver(credentials);
    }
}
