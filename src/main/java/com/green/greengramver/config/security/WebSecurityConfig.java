package com.green.greengramver.config.security;
// Spring Security 세팅

import com.green.greengramver.config.jwt.JwtAuthenticationEntryPoint;
import com.green.greengramver.config.jwt.TokenAuthenticationFilter;
import com.green.greengramver.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration // 빈등록 가능, 메소드 빈등록이 있어야 의미가 있다.
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    // Spring Security 기능 비활성화(Spring Security 가 관여하지 않았으면 하는 부분)
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring()
//                         .requestMatchers(new AntPathRequestMatcher("/static/**"));
//    }
    @Bean // Spring이 메소드 호출을 하고 리턴한 객체의 주소값을 관리한다.(빈 등록)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // session을 security가 사용하지 않는다
                .httpBasic(h -> h.disable()) // SSR(Server Side Rendering)이 아니다. 화면을 만들지 않을 것이기 때문에 비활성화 시킨다. 시큐리티 로그인 창이 나타나지 않을 것이다.
                .formLogin(form -> form.disable()) // SSR(Server Side Rendering)이 아니다. 폼로그인 기능 자체를 비활성화
                .csrf(csrf -> csrf.disable()) // SSR(Server Side Rendering)이 아니다. 보안 관련 SSR이 아니면 보안 이슈가 없기 때문에 기능을 끈다.
                .authorizeHttpRequests(req -> req.requestMatchers("/api/feed","/api/feed/**").authenticated()// 로그인이 되어 있어야만 사용 가능
                                                 .requestMatchers(HttpMethod.GET,"/api/user").authenticated()
                                                 .requestMatchers(HttpMethod.PATCH,"/api/user/pic").authenticated()
                                                 .anyRequest().permitAll())// 나머지 요청은 모두 허용
                .exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
