package pl.polsl.BicycleRental.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import pl.polsl.BicycleRental.Model.Service.AdminAccountServ;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class ScurityConf {
    private AdminAccountServ adminAccountServ;
    private DataSource dataSource;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public ScurityConf(AdminAccountServ adminAccountService, DataSource dataSource, PasswordEncoder passwordEncoder){
        this.adminAccountServ = adminAccountService;
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher adminRequestMatcher = new AntPathRequestMatcher("/admin/**");
        http.formLogin(withDefaults());
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(adminRequestMatcher).hasRole("ADMIN")
                                .anyRequest().permitAll()
                )
                .httpBasic(withDefaults());
        http
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
        return http.build();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT LOGIN, PASSWORD, 'true' as ENABLED FROM ADMIN_ACCOUNT WHERE LOGIN=?")
                .authoritiesByUsernameQuery("SELECT LOGIN, 'ROLE_ADMIN' as AUTHORITY FROM ADMIN_ACCOUNT WHERE LOGIN=?")
                .passwordEncoder(passwordEncoder);
    }
}
