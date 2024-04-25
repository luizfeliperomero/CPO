package ufsm.csi.cpo.security;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
    private final JwtEncoder encoder;

    public JwtService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateToken() {
        Instant now = Instant.now();
        long expiry = 3600L;
        var claims = JwtClaimsSet.builder()
                .issuer("cpo")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
