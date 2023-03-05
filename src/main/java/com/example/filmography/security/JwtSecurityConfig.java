package com.example.filmography.security;

import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class JwtSecurityConfig
        implements WebMvcConfigurer {

    private final JwtTokenFilter jwtTokenFilter;

    public JwtSecurityConfig(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui.html/**",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/css/**", "/img/**", "/js/**", "/encode/*", "/login"
            // other public endpoints of your API may be appended to this array
    };

    @Bean
    public SecurityFilterChain filterChainJwt(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                //включаем базовую авторизацию
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //Доступ для всех пользователей
                .antMatchers(AUTH_WHITELIST).permitAll()//.hasRole("APP_ADMIN")
                //.mvcMatchers("/css/**", "/img/**", "/js/**", "/encode/*", "/login").permitAll()
                .and()
                .exceptionHandling()
                //.accessDeniedPage()
                .authenticationEntryPoint((request, response, ex) -> {
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            ex.getMessage()
                    );
                })
                .and().authorizeRequests()
                //Доступ только для авторизованных пользователей
                .antMatchers("/rest/director/**").hasRole("USER") //руты которые доступны юзеру
                .anyRequest().permitAll()
                .and()
                //JWT Token VALID or NOT
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
