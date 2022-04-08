package com.example.security.config;

import com.example.security.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 스프링 시큐리티 필터 클래스
 */
@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 해당 클래스(스프링 시큐리티 필터)가 스프링 필터 체인에 등록됨
// securedEnabled == 특정 주소 접근시 권한 및 인증을 위한 어노테이션(@secured) 활성화
//prePostEnabled == @preAuthorize 와 @postAuthorize 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    //해당 메서드의 리턴 오프젝트를 IOC로 등록
    //=>따라서 이 메서드 빈 등록 후 어디에서나 해당 메서드 사용 가능
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable(); //이건 비활성화
        http.authorizeRequests()

                // /user로 들어오면 로그인이 필요하도록
                .antMatchers("/user/**").authenticated() //인증만 되면 들어갈 수 있따는 것

                // /manager로 들어오면 로그인 + ROLE_ADMIN과 ROLE_MANAGER의 권한을 받은 애들만 접속 가능하도록
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")

                //따라서 관리자 페이지 api를 작성 할 떄 /admin을 끼고 설계 할 것.
                // /admin로 들어오면 로그인 + ROLE_ADMIN 권한을 받은 애들만 접속 가능하도록
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")

                .anyRequest().permitAll() //위의 3개 이외의 주소로 들어오는 건 누구나 권한이 모두 허용 되도록

                .and()
                .formLogin()
                .loginPage("/loginForm") //자체 로그인 페아지를 연결해준다.;
                .loginProcessingUrl("/login") //login주소가 호출 되면 시큐리티가 낚아채서 대신 로그인 진행
                .defaultSuccessUrl("/")//로그인이 완료 되면 main페이지로 가게 한다. + /loginForm으로 와서 login하면 /로. 다른 페이지로 갔다가 막혀서 로그인하러 왓으면 원래 접근하려했던 페이지로 다시 가게 도와줌
                .and()
                .oauth2Login()
                .loginPage("/loginForm")//여기까지 하면 구글 로그인 접근까지 가능.
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

    }
}
