package com.shankar.reddit.Service;

import com.shankar.reddit.Security.JwtProvider;
import com.shankar.reddit.dto.AuthenticationResponse;
import com.shankar.reddit.dto.LoginRequest;
import com.shankar.reddit.dto.RefreshTokenRequest;
import com.shankar.reddit.dto.RegisterRequest;
import com.shankar.reddit.entity.NotificationEmail;
import com.shankar.reddit.entity.RedditUser;
import com.shankar.reddit.entity.VerificationToken;
import com.shankar.reddit.exception.SpringRedditException;
import com.shankar.reddit.repo.ReddituserRepository;
import com.shankar.reddit.repo.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final ReddituserRepository userRepo;

    private final VerificationTokenRepository verificationTokenRepo;

    private final MailService mailService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    @Transactional
    public RedditUser signup(RegisterRequest registerRequest) {
    log.info("Auth Service - sign up");
        RedditUser user = new RedditUser();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword( passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(false);
        user.setCreatedDate(Instant.now());

        RedditUser savedUser = userRepo.save(user);
        String token = generateVerificationToken(savedUser);
        mailService.sendEmail(new NotificationEmail("Please Activate ", user.getEmail(),
                "Thanks for signing up to Spring Reddit, "+
                        "Please click on the below url to activate : "+
                        "http://localhost:9368/api/auth/accountVerification/"+token));
        return savedUser;

    }

    private String generateVerificationToken(RedditUser savedUser) {

        String generatedToken = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(generatedToken);
        verificationToken.setUser(savedUser);

        verificationTokenRepo.save(verificationToken);

        return generatedToken;

    }

    public String verifyToken(String userToken) {

        VerificationToken verificationToken = verificationTokenRepo.findByToken(userToken).orElseThrow(() -> new SpringRedditException("Invalid verification Token"));
        boolean status = fetchUserAndEnable(verificationToken);

        return status ? "User Verified " : "User not verified";
    }

    private boolean fetchUserAndEnable(VerificationToken verificationToken) {
        RedditUser user = verificationToken.getUser();
        user.setEnabled(true);
        RedditUser savedUser = userRepo.save(user);
        return savedUser.getUsername() != null;
    }

    public AuthenticationResponse userLogin(LoginRequest loginRequest) {
    log.info("Auth Service login ");

    try {
        Authentication authenticated = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        log.info("Authenticate object "+authenticated.isAuthenticated());

        SecurityContextHolder.getContext().setAuthentication(authenticated);
        String token = jwtProvider.generateToken(authenticated);
        AuthenticationResponse build = AuthenticationResponse.builder()
                .authenticationToken(token)
                .username(loginRequest.getUsername())
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationinMills()))
                .build();
        return build;
    } catch (Exception e){
        throw new SpringRedditException(e.getLocalizedMessage());
    }




    }

    public RedditUser getCurrentUser() {

        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByUsername(principal.getSubject()).orElseThrow(()-> new UsernameNotFoundException("User "+principal.getSubject()+" not found"));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());

        return  AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationinMills()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}
