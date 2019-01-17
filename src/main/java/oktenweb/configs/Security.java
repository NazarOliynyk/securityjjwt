package oktenweb.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
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
                .antMatchers(HttpMethod.GET, "/admin/data").hasRole("ADMIN")
                .and()
                .addFilterBefore(new RequestProcessingJWTFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new LoginFilter("/loginx", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                ;

        // succesion: RequestProcessingJWTFilter works first (each time we get a JWT),
        // then  LoginFilter(works ony once when a user tries to LOG IN)
        // and only then UsernamePasswordAuthenticationFilter
        // RequestProcessingJWTFilter works with all kind of requests
        // LoginFilter only with "/loginx"
        // The creation of a user happens in LoginFilter! See the class

    }


}
