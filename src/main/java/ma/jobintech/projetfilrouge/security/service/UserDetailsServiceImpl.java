package ma.jobintech.projetfilrouge.security.service;

import lombok.RequiredArgsConstructor;
import ma.jobintech.projetfilrouge.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .map(user -> User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .disabled(!user.getActif())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .build()
            )
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
    }
}