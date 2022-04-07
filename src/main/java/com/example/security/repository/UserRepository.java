package com.example.security.repository;

import com.example.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository 생략 가능
public interface UserRepository extends JpaRepository<User,Integer> {
}
