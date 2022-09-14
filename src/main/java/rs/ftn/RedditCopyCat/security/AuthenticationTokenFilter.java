package rs.ftn.RedditCopyCat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private  UserDetailsService userDetailsService;

    @Autowired
    private TokenUtils tokenUtils;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Authorization");

               if(token != null){
            if(token.startsWith("Bearer ")){
                token = token.substring(7);
            }
        }

        String username = tokenUtils.getUsernameFromToken(token);

//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//            if (tokenUtils.isTokenValid(token, userDetails)) {
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities()
//                );
//
//                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }
//----------------------------------------------------------------------------
	if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails= this.userDetailsService.loadUserByUsername(username);
            if (tokenUtils.isTokenValid(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                String authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining());
                System.out.println("\n\n---------------------\nAuthorities granted : " + authorities);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                System.out.println("\n\n---------------------\nNot Valid Token");
            }

        }
//        else {
//            System.out.println("\n\n---------------------\nNo Token");
//        }
//-------------------------------------------------------------------------

        chain.doFilter(request, response);
    }
}
