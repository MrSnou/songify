package com.songify.infrastructure.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;

import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.key.public}")
    private Resource publicKeyResource;
    @Value("${jwt.key.private}")
    private Resource privateKeyResource;


    @Bean
    public RSAPublicKey rsaPublicKey() throws IOException {
        InputStream inputStream = publicKeyResource.getInputStream();
        return RsaKeyConverters.x509().convert(inputStream);
    }

    @Bean
    public RSAPrivateKey rsaPrivateKey() throws IOException {
        InputStream inputStream = privateKeyResource.getInputStream();
        return RsaKeyConverters.pkcs8().convert(inputStream);
    }
}
