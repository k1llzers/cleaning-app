package com.ukma.cleaning.utils.security;

import com.ukma.cleaning.user.dto.UserRegistrationDto;
import com.ukma.cleaning.utils.exceptions.CantRefreshTokenException;
import com.ukma.cleaning.utils.security.dto.AuthRequest;
import com.ukma.cleaning.utils.security.dto.JwtResponse;
import com.ukma.cleaning.utils.security.refresh.tokens.RefreshTokenEntity;
import com.ukma.cleaning.utils.security.refresh.tokens.RefreshTokenRepository;
import com.ukma.cleaning.utils.security.refresh.tokens.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthenticationController {
    private final CustomUserDetails service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/registration")
    public Boolean addNewUser(@RequestBody UserRegistrationDto dto) {
        return service.register(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (Throwable e) {
            return new ResponseEntity<>("Invalid username or password!", HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(new JwtResponse(jwtService.generateToken(authRequest.getUsername()), refreshTokenService.create(authRequest.getUsername())));
    }

    @PostMapping("/refreshToken")
    public JwtResponse refreshToken(@RequestBody String refreshToken) {
        RefreshTokenEntity token = refreshTokenService.findByToken(refreshToken).orElseThrow(
                () -> new CantRefreshTokenException("Can`t find refresh token")
        );
        refreshTokenService.verify(token);
        return new JwtResponse(jwtService.generateToken(token.getUser().getEmail()), refreshToken);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }
}
