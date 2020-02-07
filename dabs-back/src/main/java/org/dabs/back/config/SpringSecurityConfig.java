package org.dabs.back.config;

import org.dabs.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
	@Lazy
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService).passwordEncoder(getBCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http.authorizeRequests()
                    .antMatchers("/", "/register-patient", "/register-doctor", "/doctors/**", "/mm_pics/**",
                            "/bootstrap/**", "/jquery/**", "/tether/**", "/font-awesome/**", "/select2/**", "/css/**", 
                            "/connect/**").permitAll()
                    .antMatchers( "/appointment/doctor/**", "/doctor/edit", "/doctor/patients").hasRole("DOCTOR")
                    .antMatchers( "/patient/edit").hasRole("PATIENT")
                    .antMatchers("/appointment/**","/api/v1/appointment/**").hasAnyRole("PATIENT","DOCTOR")
                    .anyRequest().authenticated()
                .and()
                    .formLogin().loginPage("/login").permitAll()
                    .usernameParameter("username")
                    .passwordParameter("password")
                .and()
                    .rememberMe()
                    .rememberMeCookieName("RememberMe")
                    .rememberMeParameter("rememberMe")
                    .key("SecretKey")
                    .tokenValiditySeconds(100000)
                .and()
                    .logout().logoutSuccessUrl("/login?logout").permitAll()
                .and()
                    .exceptionHandling().accessDeniedPage("/unauthorized")
                .and()
                    .csrf()
                    .disable();
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Bean
    public RequestHeaderAuthenticationFilter getRequestHeaderAuthenticationFilter(final AuthenticationManager authenticationManager) {
        RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilterImpl();
        requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager);

        return requestHeaderAuthenticationFilter;
    }*/
}
