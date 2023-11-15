package org.aptech.metube.videoservice.config;

import org.aptech.metube.videoservice.security.filter.AuTokenFilter;
import org.aptech.metube.videoservice.security.filter.AuthEntryPoint;
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
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private AuthEntryPoint unauthorizedHandler;

    @Bean
    public AuTokenFilter authenticationJwtTokenFilter() {
        return new AuTokenFilter();
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
                .antMatchers("/api/v1/video/list-video").permitAll()
                .antMatchers("/api/v1/video/testRedis").hasAuthority("ROLE_USER")
                .antMatchers("/api/v1/video/upload").hasAuthority("USER")
                .antMatchers("/api/v1/video/user/latest/**").permitAll()
                .antMatchers("/api/v1/video/list-video/category/**").permitAll()
                .antMatchers("/api/v1/category/list-categories").permitAll()
                .antMatchers("/api/v1/video/get").permitAll()
                .antMatchers("/api/v1/video/stream/**").hasAnyAuthority("USER", "SUPER_ADMIN")
                .antMatchers("/api/v1/comment/get").hasAuthority("ROLE_USER")
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}