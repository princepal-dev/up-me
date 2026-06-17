package org.prince.upme.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.prince.upme.model.User;
import org.prince.upme.security.jwt.JwtUtils;
import org.prince.upme.security.service.UserDetailsImpl;
import org.prince.upme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private final JwtUtils jwtUtils;
    @Autowired
    private final UserService userService;

    private String username;
    private String idAttributeKey;

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Authentication authentication)
            throws ServletException, IOException {
        OAuth2AuthenticationToken oAuthToken = (OAuth2AuthenticationToken) authentication;

        if ("github".equals(oAuthToken.getAuthorizedClientRegistrationId())) {

            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String name = attributes.getOrDefault("name", "").toString();
            String email = attributes.getOrDefault("email", "").toString();

            if ("github".equals(oAuthToken.getAuthorizedClientRegistrationId())) {
                idAttributeKey = "id";
                username = attributes.getOrDefault("login", "").toString();
            } else {
                username = "";
                idAttributeKey = "id";
            }

            System.out.println("Hello OAuth: " + email + " : " + name + " : ");

            userService
                    .findByEmail(email)
                    .ifPresentOrElse(
                            user -> {
                                Authentication securityAuth = getAuthentication(user, attributes, oAuthToken);
                                SecurityContextHolder.getContext().setAuthentication(securityAuth);
                            },
                            () -> {
                                User newUser = new User();

                                newUser.setEmail(email);
                                newUser.setUserName(username);
                                newUser.setSignUpMethod(oAuthToken.getAuthorizedClientRegistrationId());
                                userService.registerUser(newUser);
                                Authentication securityAuth = getAuthentication(newUser, attributes, oAuthToken);
                                SecurityContextHolder.getContext().setAuthentication(securityAuth);
                            });
        }

        this.setAlwaysUseDefaultTargetUrl(true);

        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        String email = (String) attributes.get("email");

        UserDetailsImpl userDetails = new UserDetailsImpl(null, username, email, null);

        String jwtToken = jwtUtils.generateTokenFromUserName(userDetails);

        String targetUrl
                = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                        .queryParam("token", jwtToken)
                        .build()
                        .toUriString();
        this.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private @NonNull
    Authentication getAuthentication(
            User user, Map<String, Object> attributes, OAuth2AuthenticationToken oAuthToken) {

        DefaultOAuth2User oauthUser
                = new DefaultOAuth2User(
                        List.of(), // no authorities
                        attributes,
                        idAttributeKey);

        return new OAuth2AuthenticationToken(
                oauthUser,
                List.of(), // no roles/authorities
                oAuthToken.getAuthorizedClientRegistrationId());
    }
}
