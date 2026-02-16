package com.example.testit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDecisionVoter<?> hierarchyVoter() {
        RoleHierarchy hierarchy = new RoleHierarchyImpl();
        ((RoleHierarchyImpl) hierarchy).setHierarchy(
                "ROLE_ADMIN > ROLE_MANAGER\n" +
                        "ROLE_MANAGER > ROLE_USER"
        );
        return new RoleHierarchyVoter(hierarchy);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
                http.authorizeHttpRequests((authz)->
                authz
                        .requestMatchers(HttpMethod.GET, "/tasks/**").authenticated() // Tous les utilisateurs peuvent lire les tâches
                        .requestMatchers(HttpMethod.POST, "/tasks").hasAuthority("ROLE_MANAGER") // assigner les tâches
                        .requestMatchers(HttpMethod.POST, "/tasks/*/start").hasAuthority("ROLE_USER") // démarrer les tâches
                        .requestMatchers(HttpMethod.POST, "/tasks/*/finish").hasAuthority("ROLE_MANAGER")  // arreter les tâches
                        .requestMatchers(HttpMethod.PUT, "/tasks/**").hasAuthority("ROLE_MANAGER")  // droit de mise à jour
                        .requestMatchers(HttpMethod.DELETE, "/tasks/**").hasAuthority("ROLE_ADMIN") // admin a le droit de delete
                        .anyRequest().authenticated()) // Chaque reqête doit être authentifiées
                .csrf().disable()
                .httpBasic(Customizer.withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //On rend les session stateless

        return http.build();
    }
}
