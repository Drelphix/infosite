package info.infosite.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()

                .antMatchers("/*").authenticated() // только для зарегистрированных
                .antMatchers("/user/reg").permitAll()// общий доступ
                .antMatchers("/manage", "/xml/*", "/show", "/guide", "/guides", "/export", "/mode").hasRole("admin")
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/styles/**")
                .antMatchers("/images/**")
                .antMatchers("/pics/**")
                .antMatchers("/registration");
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select username, password, active from users where username=?")
                .authoritiesByUsernameQuery("select a.username, b.role from users a inner join dbo.role b ON a.role_id=b.id where a.username=?");
    }
}