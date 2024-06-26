package ufsm.csi.cpo.modules.credentials;

import org.springframework.stereotype.Service;
import ufsm.csi.cpo.data.PlatformData;

import java.util.Base64;
import java.util.UUID;

@Service
public class CredentialsTokenService {
    private final PlatformData platformData;

    public CredentialsTokenService() {
        this.platformData = PlatformData.getInstance();
    }

    public String generateToken() {
        return UUID.randomUUID().toString();
    }
    public String encodeToken(String token) {
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

    public String decodeToken(String token) {
        return new String(Base64.getDecoder().decode(token));
    }

    public String getTokenFromAuthorizationHeader(String authorizationHeaderValue) {
        return decodeToken(authorizationHeaderValue.substring(6));
    }

    public boolean isTokenValid(String token) {
        return this.platformData.getValidCredentialsTokens().stream()
                .anyMatch(ct -> ct.equals(token));
    }

    public boolean validateToken(String token) {
        return this.platformData.getValidCredentialsTokens().add(token);
    }

    public boolean invalidateToken(String token) {
       return this.platformData.getValidCredentialsTokens().remove(token);
    }
}
