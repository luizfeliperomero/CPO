package ufsm.csi.cpo.modules.credentials;

import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufsm.csi.cpo.modules.types.CiString;
import ufsm.csi.cpo.modules.types.Role;
import ufsm.csi.cpo.security.JwtService;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/2.2.1/credentials")
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

    @SneakyThrows
    @PostMapping()
    public ResponseEntity<String> exchangeCredentials(@RequestBody Credentials credentials) {
        /*CredentialsRole credentialsRole = CredentialsRole.builder()
                .role(Role.EMSP)
                .countryCode(new CiString("br"))
                .partyId(new CiString("eneg"))
                .build();

        Credentials credentials = Credentials.builder()
                .token("eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJjcG8iLCJleHAiOjE3MTY5MzIwOTYsImlhdCI6MTcxNjg0NTY5Nn0.YlgxGWUjWxmrK8YXKTUTZlSpVyJmQVMM9uQkC16UPQW73JZPwmSI5punalbUiOiXWH4MyiAhhf97EBMjP4iRHDXy75MmYUlIgR87MEGopEEovuH2kcx5NNwBloRv_Iv-2wVit1n3bYsmazbp1bpinYm76rpp8aUkE-6ph_MpFaoDZm1WbN79mL1zB_AcBUGO35T1zP4hJgYga54DC_SmtDQ1Xx9Bjb81TV94dr93_MX-vD0KpW539B2sdQW9TGp3D6fx3y6c2wQlFvRofR5ypQTzZfQrfXjBBWTjiH3kRvuIt3fgSbc3w192c0U0AWhV-8lWReqv80oL69ND-Qn6EQ")
                .url(new URL("http://localhost:8080/ocpi/cpo/versions"))
                .roles(Arrays.asList(credentialsRole))
                .build();*/

        this.credentialsService.exchangeCredentials(credentials);
        return ResponseEntity.ok("");
    }
}
