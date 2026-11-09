package riichi.mahjong_tracker;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
    .csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(authorize -> authorize
      .requestMatchers(
        "/", 
        "/paikka/lista",
        "/peli/lista",
        "/peli/*",
        "/pelaaja/lista",
        "/tulos/lista",
        "/tilasto",
        "/css/**"
      ).permitAll()
      //API: get-pyynnöt ilman kirjautumista
      .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
      //muut vaati kirjautumista
      .requestMatchers("/api/**").authenticated()
      .anyRequest().authenticated()
    )
    .httpBasic(withDefaults())
    .formLogin(formlogin -> formlogin
      .defaultSuccessUrl("/peli/lista", true)
      .permitAll()
    )
    .logout(logout -> logout
    .logoutSuccessHandler((request, response, authentication) -> {

      String referer = request.getHeader(HttpHeaders.REFERER);

      if (referer != null) {
          response.sendRedirect(referer);
      } else {
          response.sendRedirect("/");
      }
    })
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
