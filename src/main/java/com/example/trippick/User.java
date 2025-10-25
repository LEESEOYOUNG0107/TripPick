package com.example.trippick;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // 이 클래스가 DB 테이블임을 선언
@Table(name = "user") // DB에 "user"라는 이름의 테이블을 만듦
public class User {

    @Id // 이 필드가 '주요 키(ID)'임을 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID를 DB가 자동으로 생성
    private Long userkey;

    private String username; // 실제 아이디
    private String password; // 암호화된 비밀번호

    // --- (Getter/Setter: JPA가 필요로 함) ---
    public Long getUserkey() {
        return userkey;
    }
    public void setUserkey(Long userkey) {
        this.userkey = userkey;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}