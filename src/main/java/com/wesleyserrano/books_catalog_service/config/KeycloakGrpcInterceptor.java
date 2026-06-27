package com.wesleyserrano.books_catalog_service.config;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class KeycloakGrpcInterceptor implements ServerInterceptor {
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    private final JwtDecoder jwtDecoder;

    // Standard gRPC metadata key definition
    private static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    public KeycloakGrpcInterceptor(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    //@Bean
    public JwtDecoder jwtDecoder() {
        // 1. Point to the internal Docker network URL to fetch the public keys
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();

        // 2. Instruct Spring to expect the external 'localhost' issuer URL inside the token payload
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(this.issuerUri);
        jwtDecoder.setJwtValidator(withIssuer);

        return jwtDecoder;
    }

    //@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {}) // Still intercepts and populates the SecurityContext if a token exists
                );

        return httpSecurity.build();
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String authHeader = metadata.get(AUTHORIZATION_METADATA_KEY);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            serverCall.close(Status.UNAUTHENTICATED.withDescription("Missing or invalid Authorization token"), metadata);
            return new ServerCall.Listener<ReqT>() {};
        }

        try {
            // Extract and decode the token
            String token = authHeader.substring(7);
            Jwt jwt = jwtDecoder.decode(token);

            // Create Spring Security Authentication object
            // Note: You can pass custom granted authorities/roles extracted from JWT claims here
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt, null);

            // Set context for the current thread/call execution block
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            serverCall.close(Status.UNAUTHENTICATED.withDescription("Token validation failed: " + e.getMessage()), metadata);
            return new ServerCall.Listener<ReqT>() {};
        }

        // Forward execution downstream
        return serverCallHandler.startCall(serverCall, metadata);
    }
}
