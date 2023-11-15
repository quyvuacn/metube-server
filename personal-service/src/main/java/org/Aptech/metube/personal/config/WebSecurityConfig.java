package org.aptech.metube.personal.config;

import org.aptech.metube.personal.security.filter.AuTokenFilter;
import org.aptech.metube.personal.security.filter.AuthEntryPoint;
import org.aptech.metube.personal.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPoint unauthorizedHandler;

    @Bean
    public AuTokenFilter authenticationJwtTokenFilter() {
        return new AuTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/login", "/api/v1/auth/signup", "/api/v1/auth/verify-email**").permitAll()
                .antMatchers("/api/v1/users/follows/**").hasAnyAuthority("USER", "USER_PREMIUM", "MEMBER")
                .antMatchers("/api/v1/users/list-all**").hasAuthority("SUPER_ADMIN")
                // .antMatchers("/api/v1/users/get-me").hasAnyAuthority("USER")
                .antMatchers("/api/v1/users/get/**").permitAll()
                .antMatchers("/api/v1/roles/**").hasAnyAuthority("SUPER_ADMIN")
                .antMatchers("/api/v1/users/update-user").hasAnyAuthority("SUPER_ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/roles/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN_VIEW")
                .antMatchers(HttpMethod.PUT, "/api/v1/roles/**").hasAnyAuthority("SUPER_ADMIN", "ADMIN_MODERATE")
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}