package ru.kolpakov.Market.SecurityForApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.kolpakov.Market.SecurityForApp.services.PersonDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()//Пускать всех на эти страницы
                .anyRequest().hasAnyRole("USER", "ADMIN")//На все другие страницы пускать только пользователей с данными ролями
                .and()
                .formLogin().loginPage("/auth/login")// Настраиваем форму для аутентификации
                .loginProcessingUrl("/process_login")// Сюда будут приходить данные с формы аутентификации
                .defaultSuccessUrl("/market", true) //При успешно  аутентификации перенаправление на данную страницу
                .failureUrl("/auth/login?error")// Перенаправление при неудачной аутентификации
                .and()
                .logout().logoutUrl("/logout")//Настраиваем выход аутентифицированного пользователя(Удаление сессии,
                // кукис и разлогирование пользователя)
                .logoutSuccessUrl("/auth/login");

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(personDetailsService);
        authenticationProvider.setPasswordEncoder(getPasswordEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {// Шифрование пароля
        return new BCryptPasswordEncoder();
    }
}
