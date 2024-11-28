package ufsm.csi.cpo.modules.credentials;

import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufsm.csi.cpo.modules.types.DateTime;
import ufsm.csi.cpo.modules.types.Response;
import ufsm.csi.cpo.security.JwtService;

import java.sql.Timestamp;
import java.util.Date;

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
    public Response<Credentials> getCredentials(@RequestHeader(value = "Authorization") String token) {
        return new Response<>(this.credentialsService.getCredentials(this.credentialsTokenService.getTokenFromAuthorizationHeader(token)),
                1000,
                new Date());
    }

    @GetMapping("/get_token")
    public Response<String> getToken() {
        var token = this.credentialsTokenService.generateToken();
        this.credentialsTokenService.validateToken(token);
        this.credentialsService.setTokenA(token);
        return new Response<>(this.credentialsTokenService.encodeToken(token),
                1000,
                new Date());
    }

    @SneakyThrows
    @PostMapping("/sender")
    public Response<Credentials> registerAsSender(@RequestHeader(value = "Authorization") String tokenB, @RequestBody Credentials credentials) {
        return new Response<Credentials>(this.credentialsService.registerAsSender(credentials, this.credentialsTokenService.getTokenFromAuthorizationHeader(tokenB)),
                1000,
                new Date()
                );
    }

    @SneakyThrows
    @PostMapping("/receiver")
    public Response<Credentials> registerAsReceiver(@RequestHeader(value = "Authorization") String token, @RequestBody Credentials credentials) {
        return new Response<Credentials>(this.credentialsService.registerAsReceiver(credentials, this.credentialsTokenService.getTokenFromAuthorizationHeader(token)),
                1000,
                new Date()
                );
    }

    @SneakyThrows
    @PutMapping("/sender")
    public void updateAsSender(@RequestHeader(value = "Authorization") String token) {
       this.credentialsService.updateVersionAsSender(this.credentialsTokenService.getTokenFromAuthorizationHeader(token));
    }

    @SneakyThrows
    @PutMapping("/receiver")
    public Response<Credentials> updateAsReceiver(@RequestHeader(value = "Authorization") String token) {
        return new Response<>(this.credentialsService.updateVersionAsReceiver(this.credentialsTokenService.getTokenFromAuthorizationHeader(token)),
                1000,
                new Date()
                );
    }

    @SneakyThrows
    @DeleteMapping
    public Response<String> unregister(@RequestHeader(value = "Authorization") String token) {
        this.credentialsService.unregisterPlatform(this.credentialsTokenService.getTokenFromAuthorizationHeader(token));
        return new Response<>(1000, "Platform successfully unregistered", new Date());
    }
}
