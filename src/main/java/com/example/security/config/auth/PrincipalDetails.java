package com.example.security.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.example.security.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

//시큐리티가 /login으로 요청이 들어오면 낚아채서 로그인을 진행시킨다.
//로그인 진행이 완료가 되면 session을 만들어준다.("시큐리티의 session") (Security ContextHolder에 세션 정보를 저장한다.)
//이 곳에 들어갈 수 있는 정보는 Authentication 타입의 객체이다. 이외의 객체는 안됨.
//Authentication 타입의 객체는 User 정보가 있어야 하고
//다시 User 오브젝트타입은 UserDetails 타입 객체여야 한다.
//즉!! => 시큐리티의 session 속 Authentication 객체 속 UserDetails 타입을 상속 받는 PrincipalDetails 클래스(지금 현재 클래스).
//해당 클래스애서 만들어진 UserDetails 타입을 상속 받는 PrincipalDetails를 시큐리티 세션에 넣기 위한 Authentication 객체로 만들어 주는 클래스가
// UserDetailsService를 상속받는 PrincipalDetailsService

@Data
// Authentication 객체에 저장할 수 있는 유일한 타입
public class PrincipalDetails implements UserDetails{

    private User user;

    // 일반 시큐리티 로그인시 사용
    public PrincipalDetails(User user) {
        this.user = user;
    }

    /*
    // OAuth2.0 로그인시 사용
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }*/

    /*public User getUser() {
        return user;
    }*/

    //이하 메소드 UserDetails Override

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * 이하 4개 메소드는 몇 년이 지나면 계정 비활성화 등의 로직을 구현할 때 사용하면 됨
     * 구현 할 게 아니라면 return true;
     */

    //계정이 만료가 됐니
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겼는가
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정이 오래 됐는가
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화 되었는가
    @Override
    public boolean isEnabled() {
        return true;
    }

    //해당 user의 권한을 리턴하는 곳
    //현재 우리는 User 모델에 권한 리턴 값을 String으로 해놓았고, 오버라이딩 메소드의 반환 타입인 Collection<?와
    //맞추어 주기 위한 코드가 아래 2줄 코드.
    //이해 안되면 스프링 부트 시큐리티 4강-시큐리티 로그인 다시 듣기
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
        collect.add(()->{ return user.getRole();});
        return collect;
    }

    /*
    // 리소스 서버로 부터 받는 회원정보
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // User의 PrimaryKey
    @Override
    public String getName() {
        return user.getId()+"";
    }*/

}
