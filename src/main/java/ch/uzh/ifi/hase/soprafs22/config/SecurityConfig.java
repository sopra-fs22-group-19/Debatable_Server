package ch.uzh.ifi.hase.soprafs22.config;

import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
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
