package com.biblioteca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	//método que cria e configura um filtro de segurança. 
    	http
            .authorizeRequests(authorizeRequests ->
            //inicia a autorização para solicitações
                authorizeRequests
                    .antMatchers("/authenticator").permitAll()
                    //permite acesso público ao endpoint /authenticator
                    .anyRequest().authenticated()
                    //qualquer outra endpoint precisa de autorização
            )
            .httpBasic();

        return http.build();
    }
}


/*
 * @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
		return http.csrf().disable()
				.authorizeHttpRequests()
				.requestMatchers("/authenticator").permitAll()
				.and()
				.authorizeHttpRequests().requestMatchers("/products/***")
				.authenticated().and()
				.sessionCreationPoilicy(SessionCreationPolicy.STATELESS)
				.and()
				.authenticationProvider(AuthenticationProvider())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
 * 
 * 
 * 
 *
 *
    @Bean
    //definindo o SecurityFilterChain como bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        
    	http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .antMatchers("/authenticator").permitAll() // Endpoint de autenticação público
                    .anyRequest().authenticated() // Outros endpoints requerem autenticação
            )
            .httpBasic(); // Utilizar autenticação básica

        return http.build();
    }
}
*/

