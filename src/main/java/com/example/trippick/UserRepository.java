package com.example.trippick;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // NullPointerException을 방지하기 위한 Optional 클래스를 가져옵니다.


// JpaRepository를 상속받아 User 엔티티와 Long 타입의 기본 키를 다룬다고 명시합니다.
// JpaRepository를 상속받으면 기본적인 DB CRUD(Create, Read, Update, Delete) 작업이 자동 생성됩니다.
public interface UserRepository extends JpaRepository<User, Long> {
    // 'findBy' 뒤에 컬럼 이름을 붙여주면 해당 컬럼으로 데이터를 찾는 메소드가 자동으로 만들어집니다.
    // 여기서는 username으로 사용자를 찾기 위해 findByUsername을 정의했습니다.
    Optional<User> findByUsername(String username);
}