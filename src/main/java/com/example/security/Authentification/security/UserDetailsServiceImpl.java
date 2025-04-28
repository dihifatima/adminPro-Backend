package com.example.security.Authentification.security;

import com.example.security.Authentification.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
        return userRepository.findByEmail(useremail)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
