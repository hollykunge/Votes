package com.hollykunge.config;

import com.hollykunge.constants.VoteConstants;
import com.hollykunge.filter.TaskLoginSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

/**
 *
 * @author lark
 */
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
                  .failureUrl("/login?error")
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
        //用重写的Filter替换掉原有的UsernamePasswordAuthenticationFilter
        http.addFilterAt(taskAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**",
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

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，
        // 不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        //这个也是很关键，这个失败的处理器需要自己实现，
        // 也就是相当于自定义失败后返回到哪个路径下springsecurity交给你自己去处理这个路径
        //底层下描述的是，通过一层层的认证异常捕获，将request请求转发重定向到自定义的路径下。
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"));
        //成功的处理器，也可以自定义路径，这里就不需要了，使用系统默认的就行
        return filter;
    }

}
