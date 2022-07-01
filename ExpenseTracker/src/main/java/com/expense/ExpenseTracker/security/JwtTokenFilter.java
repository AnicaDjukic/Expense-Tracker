package com.expense.ExpenseTracker.security;

import com.expense.ExpenseTracker.repository.UserRepository;
import org.springframework.http.HttpHeaders;
<<<<<<< HEAD
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
=======
>>>>>>> b24a87f46c2cd0570f7a2fa2b83babe4d96afb9b
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> b24a87f46c2cd0570f7a2fa2b83babe4d96afb9b

import static ch.qos.logback.core.util.OptionHelper.isEmpty;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {

<<<<<<< HEAD
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
=======
    //private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public JwtTokenFilter(UserRepository userRepository) {
>>>>>>> b24a87f46c2cd0570f7a2fa2b83babe4d96afb9b
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
<<<<<<< HEAD
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on the spring security context
        UserDetails userDetails = userRepository
                .findByUsername(jwtTokenUtil.getUsernameFromToken(token))
                .orElse(null);

        UsernamePasswordAuthenticationToken
=======
            //return;
        }

        // Get jwt token and validate
        /*final String token = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validate(token)) {
            chain.doFilter(request, response);
            return;
        }*/

        // Get user identity and set it on the spring security context
        /*UserDetails userDetails = userRepository
                .findByUsername(jwtTokenUtil.getUsername(token))
                .orElse(null);*/

        /*UsernamePasswordAuthenticationToken
>>>>>>> b24a87f46c2cd0570f7a2fa2b83babe4d96afb9b
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        List.of() : userDetails.getAuthorities()
<<<<<<< HEAD
        );

        authentication.setDetails(
=======
        );*/

        /*authentication.setDetails(
>>>>>>> b24a87f46c2cd0570f7a2fa2b83babe4d96afb9b
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
<<<<<<< HEAD
        filterChain.doFilter(request, response);
=======
        chain.doFilter(request, response);*/
>>>>>>> b24a87f46c2cd0570f7a2fa2b83babe4d96afb9b
    }
}
