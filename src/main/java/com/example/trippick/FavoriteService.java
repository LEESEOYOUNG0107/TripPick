package com.example.trippick;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository; //찜 관리
    private final UserRepository userRepository; //회원 관리

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
    }

    //username의 모든 찜 목록 찾아오기
    public List<Favorite> getFavorites(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        return favoriteRepository.findByUser(user);
    }

    @Transactional
    public void addFavorite(String username, AddFavoriteRequest request) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        boolean alreadyExists = favoriteRepository.existsByUserAndPlaceName(user,request.getPlaceName());

        if (alreadyExists) {
            System.out.println("이미 찜한 장소입니다: " + request.getPlaceName());
            return;
        }

        Favorite newFavorite = new Favorite(
                user,
                request.getPlaceName(),
                request.getPlaceIntroduc(),
                request.getPlaceImageUrl()
        );
        favoriteRepository.save(newFavorite); // 찜 관리자(Repository)에게 저장하라고 명령
    }

    @Transactional
    public void removeFavorite(String username, AddFavoriteRequest request) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        favoriteRepository.deleteByUserAndPlaceName(user, request.getPlaceName());
    }
}
