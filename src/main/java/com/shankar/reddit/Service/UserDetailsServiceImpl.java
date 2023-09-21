package com.shankar.reddit.Service;

import com.shankar.reddit.entity.RedditUser;
import com.shankar.reddit.repo.ReddituserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final ReddituserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RedditUser redditUser = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user " + username + " found"));
        return new User(redditUser.getUsername(),redditUser.getPassword(),redditUser.getEnabled(),true,true,true,GetAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> GetAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
