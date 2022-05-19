package ch.uzh.ifi.hase.soprafs22.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;



    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .logout().logoutUrl("/logout")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT,"/users/*").hasAuthority("REGISTER")
                .antMatchers(HttpMethod.DELETE,"/users/*").hasAuthority("REGISTER")
                .antMatchers(HttpMethod.POST, "/debates/rooms").hasAuthority("REGISTER")
                .antMatchers(HttpMethod.POST, "/debates/topics").hasAuthority("REGISTER")
                .anyRequest().permitAll()
                .and()
                .httpBasic();
    }
}
