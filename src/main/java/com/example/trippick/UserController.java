package com.example.trippick;
import java.util.List;
import com.example.trippick.exception.DuplicateUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

@Controller // 이 클래스가 Spring MVC의 컨트롤러임을 나타냅니다.
public class UserController {

    private final UserService userService; // UserService를 사용
    private final FavoriteService favoriteService;
    public UserController(UserService userService, FavoriteService favoriteService) {
        this.userService = userService;
        this.favoriteService = favoriteService;
    }

    // '/signup' 경로로 GET 요청이 오면 이 메서드가 실행.
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signUp"; // 'signUp.html' 파일을 렌더링하여 반환
    }

    // '/signup' 경로로 POST 요청(회원가입 폼 제출)이 오면 이 메서드가 실행.
    @PostMapping("/signup")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        try{
            userService.registerUser(username, password); // 회원가입 로직을 호출
            return "redirect:/login"; // 회원가입 성공 후 '/login' 경로로 재요청을 보냅니다.
        }catch (DuplicateUser | IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "signUp";  // 에러 메시지를 담아서 다시 회원가입 페이지로
        }
    }
    // '/login' 경로로 GET 요청이 오면 이 메서드가 실행
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // 'login.html' 파일을 렌더링하여 반환
    }

    @GetMapping("/favorites")
    public String showFavorites(Model model, Principal principal) {
        String loggedinUsername = principal.getName();
        model.addAttribute("username", loggedinUsername);

        // 현재 로그인한 유저의 찜 목록(List<Favorite>)을 가져옵니다.
        List<Favorite> favorites = favoriteService.getFavorites(loggedinUsername);

        // favorites.html이 사용할 수 있도록 "favoriteList"라는 이름으로 Model에 담습니다.
        model.addAttribute("favoriteList", favorites);
        return "favorites";
    }

    @PostMapping("/favorites/add")
    @ResponseBody                               // JSON이 보낸 데이터       누가 찜 했는지 확인
    public String addFavorite(@RequestBody AddFavoriteRequest request, Principal principal) {
        try{
            favoriteService.addFavorite(principal.getName(), request);
            return "{\"status\":\"success\"}";
        }catch (Exception e){
            return "{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}";
        }
    }

    @PostMapping("/favorites/remove")
    @ResponseBody
    public String removeFavorite(@RequestBody AddFavoriteRequest request, Principal principal){
        try{
            favoriteService.removeFavorite(principal.getName(), request);
            return "{\"status\":\"success\"}";
        } catch (Exception e){
            return "{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}";
        }
    }
}