package com.example.security.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

// ORM - Object Relation Mapping

/**
 * 구글에서 sub = 10945321354543(사용자 고유값) =>얘를 google_을 붙여서 우리에서 username = google_10945321354543으로
 *         name = 정다해,
 *         given name = 다해
 *         famailyname = 정
 *         picture = http:/~~~
 *         email=jdahae@gmail.com, => 얘를 우리에서 email로
 *         email_verified=true
 *         local=ko
 *
 *         + role = ROLE_USER를 기본 값으로.
 * 이렇게 토큰에 담아옴
*/
@Data
@Entity
public class User {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username; //goole에서 넘어온 primary id값을 넣을 것임
    private String password; //사용자가 입력한 비밀번호를 암호화 하여 넣을 것임.
    private String email; //구글 토큰으로 넘어온 이메일 정보를 넣을 것임

    private String role; //ROLE_USER, ROLE_ADMIN

    //일반 사용자와 외부사이트를 통한 회원가입자를 구분 하기 위함
    private String provider; //ex. google
    private String providerId;//google토크에서 받은 10945321354543이거 사용자 고유값

    @CreationTimestamp
    private Timestamp createDate;
}
