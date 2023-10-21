package com.luv2code.springboot.cruddemo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurity {

   private SecurityConfig securityConfig;
    @Autowired
    public DemoSecurity(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    //if you want to hardcode the values directly from the database, use the sql scripts in workbench.
/*@Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }*/

    //Thismethod is used to difine our own customized roles and details without using database

    @Bean
    public InMemoryUserDetailsManager UserDetails() {

        UserDetails Ramu = User.builder()
                .username("ramu")
                .password(securityConfig.passwordEncoder().encode("ramu123"))
                .roles("EMPLOYEE")
                .build();
        UserDetails Raghu = User.builder()
                .username("raghu")
                .password(securityConfig.passwordEncoder().encode("raghu123"))
                .roles("EMPLOYEE")
                .build();
        UserDetails Sitha = User.builder()
                .username("sitha")
                .password(securityConfig.passwordEncoder().encode("sitha123"))
                .roles("MANAGER").roles("EMPLOYEE")
                .build();
        UserDetails ravi = User.builder()
                .username("ravi")
                .password(securityConfig.passwordEncoder().encode("ravi123"))
                .roles("admin").roles("MANAGER").roles("EMPLOYEE")
                .build();

        return new InMemoryUserDetailsManager(Ramu, Raghu, Sitha, ravi);
    }

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests(configurer->
                configurer
                        .requestMatchers(HttpMethod.GET,"/api/employees").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET,"/api/employees/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST,"/api/employees").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT,"/api/employees").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasRole("ADMIN")
        );
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf->csrf.disable());
        return http.build();
}
}

