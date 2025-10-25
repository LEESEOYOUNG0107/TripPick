package com.example.trippick;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.trippick.exception.DuplicateUser;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

@Service // 이 클래스가 서비스 계층의 컴포넌트임을 나타냅니다.
public class UserService implements UserDetailsService {

    private final UserRepository userRepository; // UserRepository를 주입받아 사용
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 Encoder를 받습니다.

    // 생성자 (DB 저장소와 암호화 도구를 주입받음)
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 사용자를 찾습니다.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // '스프링 시큐리티'가 알아듣는 'UserDetails' 객체로 변환해서 반환합니다.
        // (이 객체 안에 DB의 '암호화된 비밀번호'가 들어있습니다)
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // 기본 권한
        );
    }

    // 회원가입 메서드
    public void registerUser(String username, String password) {
        if(username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("아이디와 비밀번호를 모두 입력해주세요.");
        }
        if(username.contains(" ") || password.contains(" ")) {
            throw new IllegalArgumentException("아이디 또는 비밀번호에 띄어쓰기를 사용할 수 없습니다.");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateUser("이미 존재하는 사용자입니다."); //직접 만든 예외 클래스 사용
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // 비밀번호를 암호화하여 저장합니다.
        userRepository.save(user); // 데이터베이스에 저장합니다.
        System.out.println("회원가입");
    }

    // 로그인 메서드
    public boolean loginUser(String username, String password) {
        // 사용자 이름으로 데이터베이스에서 사용자를 찾습니다.
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return false; // 사용자가 없으면 로그인 실패
        }
        // 입력받은 비밀번호와 암호화된 비밀번호가 일치하는지 확인합니다.
        return passwordEncoder.matches(password, user.getPassword());
    }
}