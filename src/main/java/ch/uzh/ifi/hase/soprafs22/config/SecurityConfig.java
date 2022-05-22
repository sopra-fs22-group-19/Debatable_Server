package ch.uzh.ifi.hase.soprafs22.config;

import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .logout().logoutUrl("/logout")
                .and()
                .authorizeRequests()
                //same function by remove the banner access from frontend
                /*
                .antMatchers(HttpMethod.PUT,"/users/*").hasAuthority("REGISTER")
                .antMatchers(HttpMethod.DELETE,"/users/*").hasAuthority("REGISTER")
                .antMatchers(HttpMethod.POST, "/debates/rooms").hasAuthority("REGISTER")
                .antMatchers(HttpMethod.POST, "/debates/topics").hasAuthority("REGISTER")
                */
                //.antMatchers(HttpMethod.GET, "/debates/*/rooms").hasAuthority("REGISTER")
                .anyRequest().permitAll()
                .and()
                .httpBasic();
    }
}
