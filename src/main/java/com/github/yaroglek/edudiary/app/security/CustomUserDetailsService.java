package com.github.yaroglek.edudiary.app.security;

import com.github.yaroglek.edudiary.app.repository.UserRepository;
import com.github.yaroglek.edudiary.domain.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User appUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(appUser.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name()))
                .build();
    }
}
