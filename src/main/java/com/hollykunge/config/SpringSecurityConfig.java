package com.hollykunge.config;

import com.hollykunge.constants.VoteConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.sql.DataSource;

/**
 *
 * @author lark
 */
@ConditionalOnProperty(prefix = "system.login",name = "enable",havingValue = "true")
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccessDeniedHandler accessDeniedHandler;

    final DataSource dataSource;

    @Value("${spring.admin.username}")
    private String adminUsername;

    @Value("${spring.admin.username}")
    private String adminPassword;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    private static final String ERRORLOGIN = "/login?error";

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public SpringSecurityConfig(AccessDeniedHandler accessDeniedHandler, DataSource dataSource) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.dataSource = dataSource;
    }

    /**
     * 安全配置项
     * - ADMIN  /admin/**
     * - USER  /user/** 和 /newVote/**
     * - 谁都可以 /, /home, /registration, /error, /vote/**, /vote/**, /h2-console/**
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/home",
                        "/registration",
                        "/error",
                        "/vote/**",
                        "/h2-console/**",
                        VoteConstants.INVITECODE_RPC+"**").permitAll()
                .antMatchers("/newVote/**", "/voteVote/**", "/createTurn/**", "/turnForm/**")
                .hasAnyRole("USER")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                  .loginPage("/login")
                //自定义的filter也得描述这个失败请求，这里就是统一的地方
                  .failureUrl(ERRORLOGIN)
                  .defaultSuccessUrl("/home")
                  .permitAll()
                  .and()
                .logout()
                  .logoutSuccessUrl("/login?logout")
                  .permitAll()
                  .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                // Fix for H2 console
                .and().headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**",
                "/webfonts/**",
                "/webjars/**",
                "/css/**",
                "/js/**",
                "/hero-illo.svg",
                "/erbu_logo.png");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // Database authentication
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);

        // In memory authentication
        auth.inMemoryAuthentication()
                .withUser(adminUsername).password(adminPassword).roles("ADMIN");
    }

}
