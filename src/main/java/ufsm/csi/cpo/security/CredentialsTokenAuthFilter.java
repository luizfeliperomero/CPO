package ufsm.csi.cpo.security;

import ch.qos.logback.core.net.ObjectWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ufsm.csi.cpo.modules.credentials.Credentials;
import ufsm.csi.cpo.modules.credentials.CredentialsTokenService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CredentialsTokenAuthFilter extends OncePerRequestFilter {
    private final CredentialsTokenService credentialsTokenService;
    private final List<String> allowedUris;

    public CredentialsTokenAuthFilter(CredentialsTokenService credentialsTokenService) {
        this.credentialsTokenService = credentialsTokenService;
        this.allowedUris = List.of("/2.2.1/credentials/get_token");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String uri = request.getRequestURI();

        if(!allowedUris.contains(uri)) {
            if(!("POST".equalsIgnoreCase(request.getMethod()) && uri.equals("/2.2.1/credentials/sender"))) {
                if(authorizationHeader != null && authorizationHeader.startsWith("Token ")) {
                    String base64Token = authorizationHeader.substring(6);
                    String token = credentialsTokenService.decodeToken(base64Token);
                    if (!credentialsTokenService.isTokenValid(token)) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
