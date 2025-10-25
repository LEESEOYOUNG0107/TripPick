package com.example.trippick;

import com.example.trippick.exception.DuplicateUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import org.springframework.ui.Model;

@Controller // 이 클래스가 Spring MVC의 컨트롤러임을 나타냅니다.
public class UserController {

    private final UserService userService; // UserService를 주입받아 사용합니다.

    // 생성자를 통해 의존성을 주입받습니다.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // '/signup' 경로로 GET 요청이 오면 이 메서드가 실행됩니다.
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signUp"; // 'signUp.html' 파일을 렌더링하여 반환합니다.
    }

    // '/signup' 경로로 POST 요청(회원가입 폼 제출)이 오면 이 메서드가 실행됩니다.
    @PostMapping("/signup")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        try{
            userService.registerUser(username, password); // 회원가입 로직을 호출합니다.
            return "redirect:/login"; // 회원가입 성공 후 '/login' 경로로 재요청을 보냅니다.
        }catch (DuplicateUser | IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "signUp";  // 에러 메시지를 담아서 다시 회원가입 페이지로
        }
    }
    // '/login' 경로로 GET 요청이 오면 이 메서드가 실행됩니다.
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // 'login.html' 파일을 렌더링하여 반환합니다.
    }

    @GetMapping("/favorites")
    public String showFavorites(Model model, Principal principal) {
        String loggedinUsername = principal.getName();
        model.addAttribute("username", loggedinUsername);
        return "favorites";
    }

    // '/login' 경로로 POST 요청(로그인 폼 제출)이 오면 이 메서드가 실행됩니다.
//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
//        if (userService.loginUser(username, password)) { // 로그인 로직을 호출합니다.
//            session.setAttribute("loggedInUser", username); // 로그인 성공 시 세션에 사용자 ID를 저장합니다.
//            return "redirect:/favorites.html"; // 로그인 성공 후 'favorites.html' 페이지로 이동합니다.
//        }
//        return "redirect:/login?error=true"; // 로그인 실패 시 에러 파라미터를 추가하여 로그인 페이지로 재이동합니다.
//    }
}