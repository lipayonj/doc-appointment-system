package org.dabs;

import org.dabs.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class FrontSpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	@Lazy
	private UserService userService;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/", "/register-patient", "/register-doctor", "/doctors/**", "/mm_pics/**",
                            "/bootstrap/**", "/jquery/**", "/tether/**", "/font-awesome/**", "/select2/**", "/css/**", 
                            "/connect/**").permitAll()
                    .antMatchers( "/appointment/doctor/**", "/doctor/edit", "/doctor/patients").hasRole("DOCTOR")
                    .antMatchers( "/patient/edit").hasRole("PATIENT")
                    .antMatchers("/appointment/**").hasAnyRole("PATIENT","DOCTOR")
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
                    .userDetailsService(userService)
                    .tokenValiditySeconds(100000)
                .and()
                    .logout().logoutSuccessUrl("/login?logout").permitAll()
                .and()
                    .exceptionHandling().accessDeniedPage("/unauthorized")
                .and()
                    .csrf()
                    .disable();
    }
}
