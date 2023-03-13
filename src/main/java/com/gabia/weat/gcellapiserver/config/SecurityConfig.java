package com.gabia.weat.gcellapiserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gabia.weat.gcellapiserver.domain.type.RoleType;
import com.gabia.weat.gcellapiserver.jwt.JwtAccessDeniedHandler;
import com.gabia.weat.gcellapiserver.jwt.JwtAuthenticationEntryPoint;
import com.gabia.weat.gcellapiserver.jwt.JwtAuthenticationFilter;
import com.gabia.weat.gcellapiserver.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAccessDeniedHandler accessDeniedHandler;
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	private final JwtTokenProvider jwtTokenProvider;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			.and()
			.authorizeHttpRequests()
			.requestMatchers("/oauth2/authorization/hiworks").permitAll()
			.requestMatchers("/login/oauth2/code/hiworks").permitAll()
			.requestMatchers("/auth/exception/**").permitAll()
			.requestMatchers(HttpMethod.GET,
				"/excels",
				"/excels/{id}",
				"/connect",
				"/connect/file/{excel-info-id}").hasAnyRole(RoleType.MEMBER.name(), RoleType.ADMIN.name())
			.requestMatchers(HttpMethod.POST, "/excels").hasAnyRole(RoleType.MEMBER.name(), RoleType.ADMIN.name())
			.requestMatchers(HttpMethod.PATCH, "/excels/{id}").hasAnyRole(RoleType.MEMBER.name(), RoleType.ADMIN.name())
			.requestMatchers(HttpMethod.DELETE, "/excels/{id}").hasAnyRole(RoleType.MEMBER.name(), RoleType.ADMIN.name())
			.requestMatchers(HttpMethod.POST, "/dataset").hasRole(RoleType.ADMIN.name())
			.anyRequest().authenticated()

			.and()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler)
			.authenticationEntryPoint(authenticationEntryPoint)

			.and()
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
