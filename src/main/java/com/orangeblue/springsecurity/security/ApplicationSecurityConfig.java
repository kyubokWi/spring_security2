package com.orangeblue.springsecurity.security;

import static com.orangeblue.springsecurity.security.ApplicationUserPermission.*;
import static com.orangeblue.springsecurity.security.ApplicationUserRoles.*;

import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import com.orangeblue.springsecurity.auth.ApplicationUserService;
import com.orangeblue.springsecurity.jwt.JwtConfig;
import com.orangeblue.springsecurity.jwt.JwtTokenVerifier;
import com.orangeblue.springsecurity.jwt.JwtUsernameAndPasswordAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;


    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService, SecretKey secretKey, JwtConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }


    


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) ,cross site request forgery
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(),jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(secretKey,jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests() // ????????? ??? ????????? ??????
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll() // ?????? ????????? ?????? ?????? ????????? ??? ??? . ?????? ????????? ?????? ??????.
                .antMatchers("/api/**").hasRole(STUDENT.name()) // STUDENT ROLES??? /api/~~ ??? ???????????? url??? ?????? ??????
                // .antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name()) // antMatchers??? ????????? ?????????.
                // .antMatchers(HttpMethod.DELETE , "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                // .antMatchers(HttpMethod.POST , "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                // .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                .anyRequest().authenticated(); // ?????? ???????????? ????????? ????????????
                
                // .httpBasic(); // httpBasic????????? ???????????? ??????

                // .formLogin().loginPage("/login").permitAll() // custom login form??? ?????? ??? ??????
                //     .defaultSuccessUrl("/courses", true) // ???????????? ????????? ??? ????????? ?????????
                //     .passwordParameter("password") //login.html??? password name property
                //     .usernameParameter("username")
                
                // .and()
                // .rememberMe()
                //     .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)) // defaults to 2 weeks
                //     .key("somethingverysecured").rememberMeParameter("remember-me")
                    
                // .and()
                // .logout()
                //     .logoutUrl("/logout") // logout??? ??? ????????? ??????, ????????? ???????????????.
                //     .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) //csrf.disable??? ??? 
                //     .clearAuthentication(true).invalidateHttpSession(true).deleteCookies("JSESSIONID", "remember-me")
                //     .logoutSuccessUrl("/login");

    }
            

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    



    // @Override
    // @Bean
    // protected UserDetailsService userDetailsService() {
    //         UserDetails annaSmithUser = User.builder()
    //                                         .username("annasmith")
    //                                         .password(passwordEncoder.encode("password1")) // password??? encode??? ?????? ?????????????????????.
    //                                         // .roles(STUDENT.name()) // ROLE_STUDENT
    //                                         .authorities(STUDENT.getGrantedAuthorities())
    //                                         .build();
                                            
                    
    //         UserDetails lindaUser = User.builder()
    //                                     .username("linda")
    //                                     .password(passwordEncoder.encode("password2"))
    //                                     // .roles(ADMIN.name()) // ROLES_ADMIN
    //                                     .authorities(ADMIN.getGrantedAuthorities())
    //                                     .build();

    //         UserDetails tomUser = User.builder()
    //                                     .username("tom")
    //                                     .password(passwordEncoder.encode("password3"))
    //                                     // .roles(ADMINTRAINEE.name()) // ROLES_ADMIN
    //                                     .authorities(ADMINTRAINEE.getGrantedAuthorities())
    //                                     .build();
        
        
    //         return new InMemoryUserDetailsManager(
    //         annaSmithUser,
    //         lindaUser,
    //         tomUser
    //     );
    // }
    
    
    
}
