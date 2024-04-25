package ufsm.csi.cpo.modules.credentials;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufsm.csi.cpo.security.JwtService;

@RestController
@RequestMapping("/credentials")
public class CredentialsController {
    private final JwtService jwtService;

    public CredentialsController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping()
    public ResponseEntity<String> sendToken() {
        return ResponseEntity.ok(this.jwtService.generateToken());
    }
}
