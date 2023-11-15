package org.aptech.metube.personal.controller;

import org.aptech.metube.personal.config.Translator;
import org.aptech.metube.personal.constant.EntityStatusCode;
import org.aptech.metube.personal.entity.User;
import org.aptech.metube.personal.exception.NotFoundException;
import org.aptech.metube.personal.exception.RequestValidException;
import org.aptech.metube.personal.repository.EmailVerificationTokenRepository;
import org.aptech.metube.personal.repository.RoleRepository;
import org.aptech.metube.personal.repository.UserRepository;
import org.aptech.metube.personal.security.payload.request.LoginRequest;
import org.aptech.metube.personal.security.payload.request.SignupRequest;
import org.aptech.metube.personal.security.payload.response.JwtResponse;
import org.aptech.metube.personal.security.payload.response.MessageResponse;
import org.aptech.metube.personal.service.impl.EmailVerificationService;
import org.aptech.metube.personal.service.impl.UserDetailsImpl;
import org.aptech.metube.personal.entity.EmailVerificationToken;
import org.aptech.metube.personal.entity.Role;
import org.aptech.metube.personal.security.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j(topic = "LOGIN-CONTROLLER")
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws NotFoundException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new NotFoundException(Translator.toLocale("user.not.found")));

        if (!userDetails.isEmailVerified()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is not verified!"));
        }

        if (!user.getStatusCode().equals(EntityStatusCode.ACTIVE)){
            throw new RequestValidException(Translator.toLocale("user.not.active"));
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest){
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        String hashedPassword = encoder.encode(signUpRequest.getPassword());

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), hashedPassword);

        user.setPhone(signUpRequest.getTel());
        user.setFullName(signUpRequest.getFullName());

        List<Role> roles = new ArrayList<>();

        Role userRole = roleRepository.findByName(signUpRequest.getRoleName())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setRoles((List<Role>) roles);
        user.setIsVerified(true);
        user.setStatusCode(EntityStatusCode.ACTIVE);
        user.setCreatedDate(LocalDateTime.now());
        userRepository.save(user);
//        emailVerificationService.sendVerificationEmail(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid verification token"));
        }

        User user = verificationToken.getUser();
        user.setIsVerified(true); // Đánh dấu email là đã xác nhận
        userRepository.save(user);

        emailVerificationTokenRepository.delete(verificationToken);

        return ResponseEntity.ok(new MessageResponse("Email verified successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String token = extractAuthToken(request);
        if (token != null) {
            jwtUtils.expireJwtToken(token);
        }
        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }

    private String extractAuthToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }
}
