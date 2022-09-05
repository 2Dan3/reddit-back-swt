package rs.ftn.RedditCopyCat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuthentication(
            AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {

        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean()
            throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter();
        authenticationTokenFilter
                .setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        // disable auth check on Preflight requests
//        TODO* uncomment
//        httpSecurity.cors().and();
        // A note to browser not to cache data received from headers
        httpSecurity.headers().cacheControl().disable();

        // h2 console to app communication configuration
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/reddit/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/reddit/users/").permitAll()
//                .antMatchers(HttpMethod.GET, "/reddit/users/all").permitAll()
                .antMatchers(HttpMethod.PUT, "/reddit/users/ban").access("@webSecurity.moderatesCommunity(#communityId, principal)")
                .antMatchers(HttpMethod.PUT, "/reddit/users/unban").access("@webSecurity.moderatesCommunity(#communityId, principal)")
                /*.antMatchers(HttpMethod.POST, "reddit/users/logout").access("@webSecurity.isUserLogged(principal)")*/
                .antMatchers(HttpMethod.GET, "/reddit/communities").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/communities/{id}").permitAll()
                .antMatchers(HttpMethod.PUT, "/reddit/communities").access("@webSecurity.moderatesCommunity(#communityDTO, principal)")
                .antMatchers(HttpMethod.DELETE, "/reddit/communities/{communityId}").access("@webSecurity.moderatesCommunity(#communityId, principal)")
                .antMatchers(HttpMethod.GET, "/reddit/communities/{communityId}/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/communities/{communityId}/posts/{postId}").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/posts/{postId}").permitAll()
                .antMatchers(HttpMethod.POST, "/reddit/communities/{communityId}/posts").access("@webSecurity.canUserParttake(#postId, principal)")
                .antMatchers(HttpMethod.PUT, "/reddit/communities/{communityId}/posts/{postId}").access("@webSecurity.canChangePost(#postId, principal)")
                .antMatchers(HttpMethod.DELETE, "/reddit/communities/{communityId}/posts/{postId}").access("@webSecurity.canChangePost(#postId, principal)")
                .antMatchers(HttpMethod.GET, "/reddit/communities/{communityId}/flairs").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/communities/{communityId}/posts/{postId}/flair").permitAll()
                .antMatchers(HttpMethod.PUT, "/reddit/communities/{communityId}/posts/{postId}/flair").access("@webSecurity.canChangePost(#postId, principal)")
                .antMatchers(HttpMethod.GET, "/reddit/communities/{communityId}/flairs/{flair}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/reddit/communities/{communityId}/flairs/{flair}").access("@webSecurity.moderatesCommunity(#communityId, principal)")
                .antMatchers(HttpMethod.GET, "/reddit/posts/{postId}/comments").permitAll()
                .antMatchers(HttpMethod.GET, "/reddit/posts/{postId}/comments/{commentId}").permitAll()
                .antMatchers(HttpMethod.POST, "/reddit/posts/{postId}/comments/{parentId}").access("@webSecurity.canUserParttake(#postId, principal)")
                .antMatchers(HttpMethod.PUT, "/reddit/posts/{postId}/comments/{commentId}").access("@webSecurity.canChangeComment(#postId, #commentId, principal)")
                .antMatchers(HttpMethod.DELETE, "/reddit/posts/{postId}/comments/{commentId}").access("@webSecurity.canChangeComment(#postId, #commentId, principal)")
                .antMatchers(HttpMethod.PUT, "/reddit/reactions/{reactionId}").access("@webSecurity.canChangeReaction(#reactionId, principal)")
                .antMatchers(HttpMethod.POST, "/reddit/posts/{postId}/reactions").access("@webSecurity.canReactToPost(principal, #postId)")
                .antMatchers(HttpMethod.POST, "/reddit/comments/{commentId}/reactions").access("@webSecurity.canReactToComment(principal, #commentId)")
                .antMatchers(HttpMethod.POST, "/reddit/communities/{communityId}/rules").access("@webSecurity.moderatesCommunity(#communityId, principal)")
                .antMatchers(HttpMethod.DELETE, "/reddit/communities/{communityId}/rules/{ruleId}").access("@webSecurity.moderatesCommunity(#communityId, principal)")
                .antMatchers(HttpMethod.POST, "/reddit/posts/{postId}/reports").access("@webSecurity.canReportPost(principal, #postId)")
                .antMatchers(HttpMethod.POST, "/reddit/comments/{commentId}/reports").access("@webSecurity.canReportComment(principal, #commentId)")
                .antMatchers(HttpMethod.PUT, "/reddit/reports/{reportId}").access("@webSecurity.canChangeReport(principal, #reportId)")
                .antMatchers(HttpMethod.PUT, "/reddit/reports/{reportId}/accept").access("@webSecurity.canChangeReport(principal, #reportId)")

                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}
