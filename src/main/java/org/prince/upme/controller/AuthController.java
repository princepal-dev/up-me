package org.prince.upme.controller;

import org.prince.upme.model.User;
import org.prince.upme.repository.UserRepository;
import org.prince.upme.security.jwt.JwtUtils;
import org.prince.upme.security.request.SignInRequest;
import org.prince.upme.security.request.SignUpRequest;
import org.prince.upme.security.response.LoginResponse;
import org.prince.upme.security.response.MessageResponse;
import org.prince.upme.security.service.UserDetailsImpl;
import org.prince.upme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired private JwtUtils jwtUtils;
  @Autowired private UserService userService;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private AuthenticationManager authenticationManager;

  @PostMapping("/public/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody SignInRequest signInRequest) {
    Authentication authentication;

    try {
      authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  signInRequest.getUserName(), signInRequest.getPassword()));
    } catch (AuthenticationException e) {
      Map<String, Object> map = new HashMap<>();
      map.put("status", false);
      map.put("message", "Bad credentials");
      return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    if (userDetails == null) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error : in logging in!"));
    }

    String jwtToken = jwtUtils.generateTokenFromUserName(userDetails);

    return new ResponseEntity<>(
        new LoginResponse(jwtToken, signInRequest.getUserName()), HttpStatus.OK);
  }

  @PostMapping("/public/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: email is already registered"));
    }

    if (userRepository.existsByUserName(signUpRequest.getUserName())) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: username is already registered"));
    }

    User user =
        new User(
            signUpRequest.getEmail(),
            signUpRequest.getUserName(),
            "Email",
            passwordEncoder.encode(signUpRequest.getPassword()));

    userRepository.save(user);
    return ResponseEntity.ok().body(new MessageResponse("User registered successfully!"));
  }
}
