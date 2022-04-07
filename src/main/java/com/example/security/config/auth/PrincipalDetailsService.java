package com.example.security.config.auth;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//SecurityConfig에서 .loginProcessingUrl("/login")코드를 통해
// login주소가 호출 되면 시큐리티가 낚아채서 대신 로그인 진행 하도록 하였는데
//이 때 login 주소로 들어오면 UserDetailsService 타입으로 IOC 되어 있는
// PrincipalDetailsService의 loadUserByUsername 메서드가 실행 된다. 이건 그냥 시큐리티 규칙임
//넘어올 때 넘어온 html폼의 username을 가지고 옴
@Service
public class PrincipalDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    //주의!!!! UserDetails에서 받는 파람 String usernamer과 html폼에서 넘어오는 input의 name이 같아야 함
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            return null;
        }else {
            return new PrincipalDetails(user); //user리포지토리에서 넘어온 user 값을 받고 값이 있으면 PrincipalDetails 타입으로 만들어서 시큐리티 세션에 넣어준다.
        }

    }

}
