package com.hollykunge.config;

import com.hollykunge.constants.VoteConstants;
import com.hollykunge.filter.TaskLoginSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

/**
 *
 * @author lark
 */
@Configuration
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
                .antMatchers("/**","/home", "/registration", "/error", "/vote/**", "/vote/**", "/h2-console/**", VoteConstants.INVITECODE_RPC+"**").permitAll()
                .antMatchers("/newVote/**", "/voteVote/**", "/createTurn/**", "/turnForm/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/home")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                // Fix for H2 console
                .and().headers().frameOptions().disable();
        //用重写的Filter替换掉原有的UsernamePasswordAuthenticationFilter
        http.addFilterAt(taskAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // Database authentication
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder());

        // In memory authentication
        auth.inMemoryAuthentication()
                .withUser(adminUsername).password(adminPassword).roles("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    TaskLoginSecurityFilter taskAuthenticationFilter() throws Exception {
        TaskLoginSecurityFilter filter = new TaskLoginSecurityFilter();

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

}
