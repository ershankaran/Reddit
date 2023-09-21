package com.shankar.reddit.Controller;

import com.shankar.reddit.Service.AuthService;
import com.shankar.reddit.Service.RefreshTokenService;
import com.shankar.reddit.dto.AuthenticationResponse;
import com.shankar.reddit.dto.LoginRequest;
import com.shankar.reddit.dto.RefreshTokenRequest;
import com.shankar.reddit.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest){
        log.info("Auth Controller - Signup");
        return  ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(registerRequest));
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity verifyUser(@PathVariable("token") String userToken){
       return ResponseEntity.status(HttpStatus.OK).body(authService.verifyToken(userToken));
    }

    @PostMapping("/login")
    public ResponseEntity userLogin(@RequestBody LoginRequest loginRequest){
    log.info("Controller login "+loginRequest.getUsername()+" "+loginRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(authService.userLogin(loginRequest));
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Refresh Token is deleted");
    }

}
