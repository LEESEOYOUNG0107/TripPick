package com.example.trippick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class AIController {
    @Autowired private AIService aiService;
    @Autowired private FavoriteService favoriteService;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());

            // 찜한 장소 객체 리스트를 모델에 추가. 찜에서도 모달이 보이게 하기 위해.
            List<Favorite> favorites = favoriteService.getFavorites(principal.getName());
            model.addAttribute("favorites", favorites);

            Set<String> favoriteNames = favorites.stream()
                    .map(Favorite::getPlaceName).collect(Collectors.toSet());
            model.addAttribute("favoriteNames", favoriteNames);
        }else{
            // 로그인 안 한 사용자를 위해 빈 목록 추가
            model.addAttribute("favoriteNames", Collections.emptySet());
        }
        return "index";
    }

    @PostMapping("/askAI")
    public String askAI(@RequestParam("area") String area,
                        @RequestParam("mood") String mood,
                        Model model, Principal principal) {

        // 콘솔 출력 (디버깅용 나중에지워야됌!!!)
        System.out.println("사용자로부터 입력받은 값: " + area + ", " + mood);

        CompletableFuture<List<PlaceInfo>> future1 = CompletableFuture.supplyAsync(() -> {
            // 3번째 파라미터 '5'개 요청
            String json = aiService.getAIResponse(area, mood, 5);
            return parseJsonArray(json);
        });
        CompletableFuture<List<PlaceInfo>> future2 = CompletableFuture.supplyAsync(() -> {
            // 3번째 파라미터 '5'개 요청
            String json = aiService.getAIResponse(area, mood, 5);
            return parseJsonArray(json);
        });
        // 두 스레드가 모두 끝날 때까지 기다렸다가 합치기
        List<PlaceInfo> allPlaces = Stream.of(future1, future2)
                .map(CompletableFuture::join) // 기다림 (동기화)
                .flatMap(List::stream)        // 두 리스트를 하나로 합침
                .collect(Collectors.toList()); // 최종 10개 리스트 생성

        model.addAttribute("area", area);
        model.addAttribute("mood", mood);
        model.addAttribute("placeInfos",  allPlaces);

        if (principal != null) {
            model.addAttribute("username", principal.getName());

            List<Favorite> favorites = favoriteService.getFavorites(principal.getName());
            model.addAttribute("favorites", favorites);

            // 찜한 장소 이름 목록을 모델에 추가
            Set<String> favoriteNames = getFavoriteNames(principal.getName());
            model.addAttribute("favoriteNames", favoriteNames);
        } else {
            // 로그인 안 한 사용자를 위해 빈 목록 추가
            model.addAttribute("favorites", Collections.emptyList());
            model.addAttribute("favoriteNames", Collections.emptySet());
        }
        return "index";
    }

    // 찜한 장소 이름만 가져오는 헬퍼 메소드
    private Set<String> getFavoriteNames(String username) {
        List<Favorite> favorites = favoriteService.getFavorites(username);
        return favorites.stream()
                .map(Favorite::getPlaceName) // Favorite 객체에서 장소 이름만 뽑아서
                .collect(Collectors.toSet()); // Set(중복 없는 목록)으로 만듦
    }

    private static List<PlaceInfo> parseJsonArray(String json) {
        List<PlaceInfo> result = new ArrayList<>();
        // 배열의 각 객체를 `},` 기준으로 나눔
        String[] items = json.split("\\},");

        for (String item : items) {
            /********잇으니까 데이터 배열의 마지막값만 들어가서 일단은 없앰 왜 에러생겻는지모르는부분 추후해결문제
            if (!item.contains("{") || !item.contains("}")) {
                continue;
            }
            */

            String obj = item;
            // 끝에 ‘}’가 빠졌을 수 있으므로 추가
            if (!obj.trim().endsWith("}")) {
                obj = obj + "}";
            }
            System.out.println("obj##############"+ obj);
            System.out.println("!!!!!!!!!item##############"+ item);
            PlaceInfo placeInfo = new PlaceInfo();
            placeInfo.setName(extractString(obj, "\"name\""));
            placeInfo.setIntroduction(extractString(obj, "\"introduction\""));
            placeInfo.setKeywords(extractArray(obj, "\"keywords\""));
            placeInfo.setGoodPoints(extractArray(obj, "\"goodPoints\""));
            placeInfo.setVisitTip(extractString(obj, "\"visitTip\""));
            placeInfo.setImageUrl(extractString(obj, "\"imageUrl\""));
            System.out.println("placeInfo.imageUrl :: " + placeInfo.getImageUrl());
            result.add(placeInfo);
        }
        System.out.println("result: " + result.size());
        return result;
    }

    private static List<String> extractArray(String json, String key) {
        List<String> list = new ArrayList<>();
        int idx = json.indexOf(key);
        if (idx == -1) return list;
        int start = json.indexOf("[", idx) + 1;
        int end = json.indexOf("]", start);
        if (start == -1 || end == -1) return list;
        String inside = json.substring(start, end);
        String[] parts = inside.split(",");
        for (String p : parts) {
            p = p.trim();
            if (p.startsWith("\"")) p = p.substring(1);
            if (p.endsWith("\"")) p = p.substring(0, p.length()-1);
            list.add(p);
        }
        return list;
    }

    private static String extractString(String json, String key) {
        int idx = json.indexOf(key);
        if (idx == -1) return null;
        int start = json.indexOf(":", idx) + 1;
        // 첫 번째 쌍따옴표 시작
        start = json.indexOf("\"", start) + 1;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}
