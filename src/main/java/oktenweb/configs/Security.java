package oktenweb.configs;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by user on 16.01.19.
 */
public class Security extends WebSecurityConfigurerAdapter{

     @Override
         protected void configure(AuthenticationManagerBuilder auth) throws Exception {

            auth.inMemoryAuthentication().withUser("asd").password("{noop}asd").roles("ADMIN");
             auth.inMemoryAuthentication().withUser("qwe").password("{noop}qwe").roles("USER");
         }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .and()
                .addFilterBefore(new RequestProcessingJWTFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilter(/*"/login", authenticationManager()*/), UsernamePasswordAuthenticationFilter.class)
                ;

    }


}
