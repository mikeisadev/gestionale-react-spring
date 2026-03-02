package com.progetto.gestionale.config;

import java.util.ArrayList;

import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter configurazioneCors() {

        CorsConfiguration config = new CorsConfiguration();

        /**
         * Serve a dare il consenso al frontend di usare
         * delle credenziali di accesso.
         * 
         * Questa voce è molto utile per il login dal frontend
         */
        config.setAllowCredentials(true);

        /**
         * A questo punto, creo un ArrayList per 
         * aggiungere una lista di indirizzi abilitati
         * ad accedere al backend.
         */
        ArrayList<String> urls = new ArrayList<String>();
        urls.add("http://localhost:5173");

        config.setAllowedOrigins(urls);

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter(source);
    }

}
