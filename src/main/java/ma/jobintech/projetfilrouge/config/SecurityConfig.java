package ma.jobintech.projetfilrouge.config;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableMethodSecurity  
public class SecurityConfig {

//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//	    http
//	        .cors(cors -> {})   
//	        .csrf(csrf -> csrf.disable())
//	        .authorizeHttpRequests(auth -> auth
//	            .requestMatchers("/api/auth/**").permitAll()
//	            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//	            .anyRequest().authenticated()
//	        );
//
//	    return http.build();
//	}
	
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	    return http
//	    		.cors(cors -> {})
//	            .csrf(csrf -> csrf.disable())  
//	            .authorizeHttpRequests(auth -> auth
//	                    .requestMatchers("/api/users").authenticated()
//	                    .anyRequest().permitAll()
//	            )
//	            .exceptionHandling(ex -> ex
//	                    .authenticationEntryPoint(
//	                        (request, response, authException) ->
//	                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
//	                    )
//	            )
//	            .build();
//	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	    return http
	            .cors(cors -> {})
	            .csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(auth -> auth
	                    .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
	                    .requestMatchers("/api/auth/**").permitAll()
	                    .requestMatchers("/api/users/**").authenticated()
	                    .anyRequest().permitAll()
	            )
	            .exceptionHandling(ex -> ex
	                    .authenticationEntryPoint(
	                            (request, response, authException) ->
	                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
	                    )
	            )
	            .build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

	    CorsConfiguration configuration = new CorsConfiguration();

	    configuration.setAllowedOrigins(List.of("http://localhost:4200"));
	    configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
	    configuration.setAllowedHeaders(List.of("*"));
	    configuration.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source =
	            new UrlBasedCorsConfigurationSource();

	    source.registerCorsConfiguration("/**", configuration);

	    return source;
	}
    
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = org.springframework.security.core.userdetails.User
                .withUsername("admin")
                .password(encoder.encode("12345678"))
                .roles("ADMIN")
                .build();

        UserDetails etudiant = org.springframework.security.core.userdetails.User
                .withUsername("etudiant")
                .password(encoder.encode("12345678"))
                .roles("ETUDIANT")
                .build();

        return new InMemoryUserDetailsManager(admin, etudiant);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

//package ma.jobintech.projetfilrouge.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//  @Bean
//  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//      http
//          .csrf(csrf -> csrf.disable()) 
//          .cors(Customizer.withDefaults()) 
//          .authorizeHttpRequests(auth -> auth
//              .anyRequest().permitAll() 
//          );
//
//      return http.build();
//  }
//}

