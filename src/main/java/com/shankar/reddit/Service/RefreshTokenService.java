package com.shankar.reddit.Service;

import com.shankar.reddit.entity.RefreshToken;
import com.shankar.reddit.exception.SpringRedditException;
import com.shankar.reddit.repo.ReddituserRepository;
import com.shankar.reddit.repo.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepo;
    public void validateRefreshToken(String refreshToken) {
        refreshTokenRepo.findByToken(refreshToken).orElseThrow(() -> new SpringRedditException("Invalid token"));
    }

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepo.save(refreshToken);
    }

    public void deleteRefreshToken(String token){
        refreshTokenRepo.deleteByToken(token);
    }
}
