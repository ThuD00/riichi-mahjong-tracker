package riichi.mahjong_tracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration 
@EnableMethodSecurity
public class WebSecurityConfig {

  @Bean 
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http
    .authorizeHttpRequests(authorize -> authorize
      .requestMatchers(
        "/", //etusivu
        "/paikkalista",
        "/pelilista",
        "/pelaajalista",
        "/tilastot",
        "/css/**"
      ).permitAll()
      .anyRequest().authenticated()
    )
    .formLogin(formlogin -> formlogin
      .defaultSuccessUrl("/paikkalista", true)
      .permitAll()
    )
    .logout(logout -> logout
      .permitAll()
    );
    return http.build();
  }

  @Bean 
  public UserDetailsService userDetailsService() {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    UserDetails admin = User
        		.withUsername("admin")
        		.password(passwordEncoder.encode("admin"))
        		.roles("ADMIN")
        		.build();

    return new InMemoryUserDetailsManager(admin);
  }
}
