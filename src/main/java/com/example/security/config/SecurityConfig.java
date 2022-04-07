package com.example.security.config;

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
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{

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
                .antMatchers("/user/**").authenticated()

                // /manager로 들어오면 로그인 + ROLE_ADMIN과 ROLE_MANAGER의 권한을 받은 애들만 접속 가능하도록
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")

                //따라서 관리자 페이지 api를 작성 할 떄 /admin을 끼고 설계 할 것.
                // /admin로 들어오면 로그인 + ROLE_ADMIN 권한을 받은 애들만 접속 가능하도록
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")

                .anyRequest().permitAll() //위의 3개 이외의 주소로 들어오는 건 누구나 권한이 모두 허용 되도록

                .and()
                .formLogin()
                .loginPage("/loginForm"); //자체 로그인 페아지를 연결해준다.;
                //.loginProcessingUrl("/loginProc")
                //.defaultSuccessUrl("/");
    }
}
