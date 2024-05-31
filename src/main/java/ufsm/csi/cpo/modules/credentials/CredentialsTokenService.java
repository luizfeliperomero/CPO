package ufsm.csi.cpo.modules.credentials;

import org.springframework.stereotype.Service;
import ufsm.csi.cpo.data.CpoData;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Service
public class CredentialsTokenService {
    private final CpoData cpoData;

    public CredentialsTokenService() {
        this.cpoData = CpoData.getInstance();
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

    public boolean isTokenValid(String token) {
        return this.cpoData.getValidCredentialsTokens().stream()
                .anyMatch(ct -> ct.equals(token));
    }

    public boolean validateToken(String token) {
        return this.cpoData.getValidCredentialsTokens().add(token);
    }

    public boolean invalidateToken(String token) {
       return this.cpoData.getValidCredentialsTokens().remove(token);
    }
}
