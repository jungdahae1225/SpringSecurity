package com.example.security.repository;

import com.example.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository 생략 가능
public interface UserRepository extends JpaRepository<User,Integer> {
    //select * from user where username = 1? (물음표엔 넘어온 username이 들어감)
    public User findByUsername(String username);
}
