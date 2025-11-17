package com.example.trippick;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    // User의 모든 찜 목록(List<Favorite>)을 찾아줘, JPA가 이름만 보고 자동으로 SQL을 만들어 줍니다!
    List<Favorite> findByUser(User user);

    // User가 placeName을 찜했는지 확인
    boolean existsByUserAndPlaceName(User user, String placeName);

    // User와 placeName을 기준으로 찜을 삭제합니다.
    void deleteByUserAndPlaceName(User user, String placeName);
}
