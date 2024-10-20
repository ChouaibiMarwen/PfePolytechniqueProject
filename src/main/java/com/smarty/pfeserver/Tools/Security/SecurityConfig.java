package com.smarty.pfeserver.Tools.Security;

import com.smarty.pfeserver.Tools.Configuration.OAuth2AuthenticationFailureHandler;
import com.smarty.pfeserver.Tools.Configuration.OAuth2AuthenticationSuccessHandler;
import com.smarty.pfeserver.Tools.Oathloginservice.CustomOAuth2UserService;
import com.smarty.pfeserver.Tools.Oathloginservice.CustomUserDetailsService;
import com.smarty.pfeserver.Tools.Util.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final String[] PUBLIC_ENDPOINTS = {
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger.json",
            "/favicon.ico/**",
            "/swagger-ui/**",
            "/api/v1/auth/**",
            "/api/v1/**",
            "/api/v1/public/**",
            "/api/v1/country/**",
            "/WebContent/**",
            "/api/v1/add_missing_social_data",
            "/asset/**",
            "/ws",
            "/STATIC/**",
            "/socket",
            "/apple",
            "/apple/**",
            "/socket/**",
            "/auth/**",
            "/oauth2/**",
            "/api/auth_otp/**",
            "/api/v1/oauth2/redirect",
            "/**",
            "/dashboard/v1/**",
            "/app/**",
            "/oauth2/redirect",
            "/api/v1/filter/**",
            "/images/**",
            "/user/**"
    };


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }


    @Bean
    AuthFilter authFilter() {
        return new AuthFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated()          .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
