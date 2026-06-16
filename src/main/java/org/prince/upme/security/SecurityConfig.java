package org.prince.upme.security;

import org.prince.upme.config.OAuth2LoginSuccessHandler;
import org.prince.upme.security.jwt.AuthEntryPointJwt;
import org.prince.upme.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Autowired private AuthEntryPointJwt unauthorizedHandler;

  @Lazy @Autowired private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

  @Bean
  public AuthTokenFilter authenticationTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Adding cors
    http.cors(Customizer.withDefaults());

    // Adding csrf
    http.csrf(
        csrf ->
            csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/auth/**")
                .ignoringRequestMatchers("/oauth2/**"));

    // Securing End points
    http.authorizeHttpRequests(
        req -> {
          req.requestMatchers("/api/csrf-tokens").permitAll();
          req.requestMatchers("/oauth2/**").permitAll();
          req.anyRequest().authenticated();
        });

    // TODO: OAuth 2
    http.oauth2Login(
        oauth2 -> {
          oauth2.successHandler(oAuth2LoginSuccessHandler);
        });

    // TODO: Exception Handler
    http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));

    // TODO: Adding our custom auth filter
    http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
