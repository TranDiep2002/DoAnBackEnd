package com.example.doan2.security;
import com.example.doan2.jwt.JwtAuthenticationFillter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class ConfigSecurity {
    @Autowired
    private JwtAuthenticationFillter jwtAuthenticationFillter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws  Exception{
        httpSecurity.cors();
        httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests().requestMatchers("/ws/**").permitAll();
        httpSecurity.authorizeRequests().requestMatchers("/login","/giangVien").permitAll();
        httpSecurity.authorizeRequests().requestMatchers("/admin/**").hasAnyRole("ADMIN","MANAGER"); // ROLE_ADMIN : đaay là cách viết đầy đủ
        httpSecurity.authorizeRequests().anyRequest().authenticated();
//        httpSecurity.httpBasic();
        httpSecurity.addFilterBefore(jwtAuthenticationFillter, UsernamePasswordAuthenticationFilter.class);
        return  httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

//        corsConfig.addAllowedOrigin("http://localhost:3000");
//        corsConfig.setAllowCredentials(true);
//        corsConfig.addAllowedMethod("*");
//        corsConfig.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
