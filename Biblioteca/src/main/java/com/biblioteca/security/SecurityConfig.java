package com.biblioteca.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
    	//método que cria e configura um filtro de segurança. 
    	MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
    	http
    	    .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests ->
            //inicia a autorização para solicitações
                authorizeRequests
                .requestMatchers(mvcMatcherBuilder.pattern("/authenticator")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/usuario")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/usuario/editar/3")).permitAll()
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

