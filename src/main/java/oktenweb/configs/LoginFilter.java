package oktenweb.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import oktenweb.models.ClientDataAuthModel;
import oktenweb.models.AccountCredentials;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// the regular look:
//public class LoginFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        filterChain.doFilter(servletRequest, servletResponse);
//        System.out.println("LOGIN FILTER WORKS");
//    }
//
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}




public class LoginFilter extends AbstractAuthenticationProcessingFilter{

    // this is just to try if it works - see the realization below
//        @Override
//    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
//        System.out.println("attemptAuthentication in LoginFilter works");
//        ClientDataAuthModel user =
//                new ObjectMapper().readValue(httpServletRequest.getInputStream(), ClientDataAuthModel.class);
//
//          // if we send a JSON {"username": "asd" , "password": "asd"} from Postman
//            // we will get ClientDataAuthModel(username=asd, password=asd) in console
//            System.out.println(user);
//
//        return null;
//    }

    public LoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url)); // defines the url this filter reacts on
        setAuthenticationManager(authManager);
    }

    // During the authen. attempt, which is being made by  the attemptAuthentication method,
    // we get the username and password from the request httpServletRequest.
    // As soon as they are retrieved, we use the AuthenticationManager to verify that these details
    // match with an existing user. If it does, we enter the successfulAuntefication method.
    // In this method we fetch the nam from the authenticated user and pass it on to
    // TokenAuthenticationService which will then add a JWT to the response

    private ClientDataAuthModel creds;
    // this AccountCredentials == ClientDataAuthModel


    @Override
    public Authentication attemptAuthentication
            (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {
        // this method reacts only on /login and gets a body from the request
        // then maps it to the model AccountCredential
        creds = new ObjectMapper()
                .readValue(httpServletRequest.getInputStream(), ClientDataAuthModel.class);
        System.out.println(creds);

          List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
//  authorityList // in case if the rore is not kept in a JSON object
        // works with {"username": "qwe" , "password": "{noop}qwe"}
        // providing that "ROLE_USER"
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getUsername(),
                        creds.getPassword(),
                        //Collections.emptyList()
                        authorityList
                )
        );
    }

    @Override
    protected void successfulAuthentication
            (HttpServletRequest req, // body, param...
             HttpServletResponse res, // response to client
             FilterChain chain,
             Authentication auth) throws IOException, ServletException {

        String jwtoken = Jwts.builder()
                .setSubject(auth.getName())
                .signWith(SignatureAlgorithm.HS512, "yes".getBytes())
                //.setExpiration(new Date(System.currentTimeMillis()+200000))
                .compact();

        System.out.println("jwtoken:  "+jwtoken);
        String fullHeaderValue = "Bearer "+jwtoken;
        res.addHeader("Authorization", fullHeaderValue);

    }
}

