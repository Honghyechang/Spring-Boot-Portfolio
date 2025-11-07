package com.example.Spring.Board.Project.config;

import com.example.Spring.Board.Project.model.Authority;
import com.example.Spring.Board.Project.model.Member;
import com.example.Spring.Board.Project.model.MemberUserDetails;
import com.example.Spring.Board.Project.repository.AuthorityRepository;
import com.example.Spring.Board.Project.repository.MemberRepository;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.swing.*;
import java.util.List;

@Configuration
public class SecurtiyConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth)->
                        {
                            auth.requestMatchers("/", "/signup", "/article/list", "/article/content").permitAll()
                                    .requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
                                    .anyRequest().authenticated();
                        }
                        )
                .formLogin((login)->
                        {
                            login.loginPage("/login")
                                    .defaultSuccessUrl("/")
                                    .permitAll();
                        }
                        )
                .logout((logout)->{
                    logout.logoutUrl("/logout")
                            .logoutSuccessUrl("/")
                            .permitAll();
                });
        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository, AuthorityRepository authorityRepository) {

        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

                Member member = memberRepository.findByEmail(username).orElseThrow();
                List<Authority> authorities=authorityRepository.findByMember(member);


                return new MemberUserDetails(member,authorities);
            }
        };

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                // 1. H2 콘솔 경로 무시
                .requestMatchers(PathRequest.toH2Console())

                // 2. CSS, JS, Images 등 Spring Boot의 일반적인 정적 리소스 경로 무시
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


}

//
//  http
//          .authorizeHttpRequests((auth)->
//        {
//        auth.requestMatchers("/", "/signup", "/article/list", "article/content").permitAll()
//                                    .requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
//                                    .anyRequest().authenticated();
//                        }
//                                )
//                                .formLogin((login)->
//        {
//        login.loginPage("/login")
//                                    .defaultSuccessUrl("/")
//                                    .permitAll();
//                        }
//                                )
//                                .logout((logout)->{
//        logout.logoutUrl("/logout")
//                            .logoutSuccessUrl("/")
//                            .permitAll();
//                });
//                        return http.build();
//
//
//Spring Security 설정 최종 정리
//1. 전역 접근 제어 규칙 (Authorization)
//authorizeHttpRequests 블록에서 설정된 규칙은 사용자의 인증 상태 및 권한에 따라 접근을 허용하거나 거부하는 가장 핵심적인 설정입니다.
//
//URL 패턴	요구사항	해석 (권한)
///member/**	hasAuthority("ROLE_ADMIN")	ROLE_ADMIN 권한을 가진 관리자만 접근 가능합니다.
// anyRequest() (나머지)	authenticated()	위에 명시되지 않은 모든 나머지 경로는 **로그인(인증)**이 필수입니다.
//
// Sheets로 내보내기
//
// 2. 비로그인 허용 경로 (permitAll())
// 사용자가 로그인을 하지 않았더라도 접근을 허용하는 경로들입니다.
//
// 경로	설정 위치	허용되는 HTTP 메서드
// /	auth.requestMatchers(...)	GET, POST 등 모두
// /signup	auth.requestMatchers(...)	GET, POST 등 모두
// /article/list	auth.requestMatchers(...)	GET, POST 등 모두
// /article/content	auth.requestMatchers(...)	GET, POST 등 모두
// /login	formLogin().permitAll()	GET (로그인 화면 요청), POST (로그인 처리 요청)
// /logout	logout().permitAll()	POST (로그아웃 처리 요청)
//
// Sheets로 내보내기
//
// 💡 로그인/로그아웃 관련 permitAll()의 역할 (매우 중요)
// 설정	기술적 대상	설명
// formLogin().permitAll()	/login (GET/POST)	로그인 기능 자체가 작동하도록 해당 URL에 대한 권한 검사를 면제합니다. GET은 화면을 보여주기 위해, POST는 인증 정보를 처리하기 위해 비로그인 상태에서 접근이 허용되어야 합니다.
// logout().permitAll()	/logout (POST)	로그아웃 기능 자체가 작동하도록 해당 URL에 대한 권한 검사를 면제합니다. 사용자는 로그아웃 처리 경로에 접근하여 인증 상태를 해제해야 하므로 permitAll()이 필수입니다.
//
// Sheets로 내보내기
//
// 3. 로그인 및 로그아웃 성공 후 이동 경로
// 이 설정들은 단순히 사용자를 리다이렉트 시키는 목적지를 정의하며, 이 자체로 경로의 권한을 permitAll()로 만들지는 않습니다.
//
// 기능	설정 값	의미	최종 도착지 권한 확인
// 로그인 성공 시	defaultSuccessUrl("/")	로그인 성공 후 루트 경로(/)로 이동시킵니다.	/ 경로는 위에서 permitAll()이 설정되었으므로 문제없이 접근 가능합니다.
// 로그아웃 성공 시	logoutSuccessUrl("/")	로그아웃 성공 후 루트 경로(/)로 이동시킵니다.	/ 경로는 위에서 permitAll()이 설정되었으므로 문제없이 접근 가능합니다.
//
// Sheets로 내보내기
//
// 최종 결론
// 고객님의 설정은 게시판 애플리케이션의 표준적인 요구사항을 완벽하게 반영하고 있습니다.
//
// 관리자 외 모든 경로는 로그인 필수.
//
// 게시글 조회 및 메인 페이지는 모두에게 공개.
//
// 로그인/로그아웃 기능의 URL 자체는 정상 작동을 위해 모두에게 공개.
//
// 로그인/로그아웃 성공 후 도착하는 곳(/) 역시 공개 경로이므로 사용자 경험에 문제가 없습니다.



//@Bean
//public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//}
//
//@Bean
//public UserDetailsService userDetailsService(MemberRepository memberRepository, AuthorityRepository authorityRepository) {
//
//    return new UserDetailsService() {
//        @Override
//        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//            Member member = memberRepository.findByEmail(username).orElseThrow();
//            List<Authority> authorities=authorityRepository.fingByMember(member);
//
//
//            return new MemberUserDetails(member,authorities);
//        }
//    };
//
//}
//
//@Bean
//public WebSecurityCustomizer webSecurityCustomizer() {
//    return (web) -> web.ignoring()
//            // 1. H2 콘솔 경로 무시
//            .requestMatchers(PathRequest.toH2Console())
//
//            // 2. CSS, JS, Images 등 Spring Boot의 일반적인 정적 리소스 경로 무시
//            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//}
//
//위 3개설정 설명
//제공해 주신 Spring Security 설정 빈(Bean)들을 바탕으로 그 역할과 구현 내용을 명확하게 정리해 드리겠습니다. 이 설정은 사용자 정의 인증을 위한 핵심 컴포넌트들을 등록하고, 성능 최적화를 위해 특정 경로를 필터 체인에서 제외시키는 역할을 합니다.
//
//        🔐 Spring Security 설정 빈(Bean) 정리
//1. 비밀번호 인코더 등록 (PasswordEncoder)
//Java
//
//@Bean
//public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//}
//역할: 비밀번호를 안전하게 암호화하고 검증하는 데 사용되는 객체를 스프링 컨테이너에 등록합니다.
//
//구현: **BCryptPasswordEncoder**를 사용하고 있습니다. 이는 현재 가장 널리 사용되고 권장되는 단방향 해시(Hash) 함수로, 비밀번호 저장 시 반드시 사용되어야 합니다.
//
//사용처:
//
//회원가입/정보수정 시: 사용자가 입력한 평문 비밀번호를 데이터베이스에 저장하기 전에 암호화합니다.
//
//로그인 시: 사용자가 입력한 비밀번호와 DB에 저장된 암호화된 비밀번호가 일치하는지 비교(검증)할 때 사용됩니다.
//
//        2. 사용자 상세 정보 서비스 구현 (UserDetailsService)
//Java
//
//@Bean
//public UserDetailsService userDetailsService(MemberRepository memberRepository, AuthorityRepository authorityRepository) {
//    // ... 순수 구현 ...
//    return new UserDetailsService() {
//        @Override
//        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//            Member member = memberRepository.findByEmail(username).orElseThrow();
//            List<Authority> authorities = authorityRepository.fingByMember(member);
//            return new MemberUserDetails(member, authorities);
//        }
//    };
//}
//역할: 사용자가 로그인을 시도할 때, Spring Security에게 인증에 필요한 사용자 정보를 제공하는 핵심 인터페이스의 구현체입니다.
//
//구현 상세:
//
//사용자 조회: 로그인 ID로 사용되는 username (여기서는 이메일로 가정)을 받아 MemberRepository를 통해 데이터베이스에서 Member 객체를 조회합니다. (없으면 예외 발생)
//
//권한 조회: 해당 Member 객체에 연결된 Authority(권한 목록)를 AuthorityRepository를 통해 조회합니다.
//
//UserDetails 객체 생성: 조회한 Member 정보와 Authority 목록을 사용하여 직접 구현한 MemberUserDetails 객체를 생성하여 반환합니다.
//
//결론: 이 설정으로 Spring Security의 인증 필터는 DB에서 가져온 사용자 정의 UserDetails 객체(MemberUserDetails)를 기반으로 인증을 진행하게 됩니다.
//
//        3. 웹 보안 커스터마이저 (WebSecurityCustomizer)
//Java
//
//@Bean
//public WebSecurityCustomizer webSecurityCustomizer() {
//    return (web) -> web.ignoring()
//            .requestMatchers(PathRequest.toH2Console())
//            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//}
//역할: 보안 필터 체인 적용 대상에서 특정 경로를 제외시켜, 해당 리소스에 대한 접근 시 Spring Security가 아무런 검사도 하지 않고 통과하도록 합니다. 주로 성능 최적화 목적으로 사용됩니다.
//
//제외 대상:
//
//H2 콘솔 경로: 개발 환경에서 사용하는 인메모리 데이터베이스 콘솔 (/h2-console/**).
//
// 정적 리소스: 웹 페이지 디자인과 동작에 필수적인 파일들 (/css/**, /js/**, /images/** 등).
//
// 이점: 정적 리소스에 대해서 매번 세션 인증, CSRF 검사 등의 무거운 보안 필터 체인을 실행하지 않기 때문에 애플리케이션의 응답 속도가 빨라집니다.