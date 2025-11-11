package com.example.trippick;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AIController {

    @Autowired
    private AIService aiService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/askAI")
    public String askAI(@RequestParam("area") String area,
                        @RequestParam("mood") String mood,
                        Model model) {

        // 콘솔 출력 (디버깅용 나중에지워야됌!!!)
        System.out.println("사용자로부터 입력받은 값: " + area + ", " + mood);

        // (원래 OpenAI API 호출 부분은 잠시 주석처리해도 된다고함 잘몰르니까 일단주석처리함)
        // String aiResponse = aiService.getAIResponse(userInput);
        // model.addAttribute("response", aiResponse);

        String content=aiService.getAIResponse(area,mood);
        List<PlaceInfo> placeInfos = parseJsonArray(content);
        System.out.println("########################## " + placeInfos.size());
        for (PlaceInfo placeInfo : placeInfos) {
            System.out.println("=== " + placeInfo.name + " ===");
            System.out.println("소개: " + placeInfo.introduction);
            System.out.println("키워드: " + placeInfo.keywords);
            System.out.println("좋은 점:");
            for (String gp : placeInfo.goodPoints) {
                System.out.println("  - " + gp);
            }
            System.out.println("방문 팁: " + placeInfo.visitTip);
            System.out.println("이미지 URL: " + placeInfo.imageUrl);
            System.out.println();
        }
        model.addAttribute("area", area);
        model.addAttribute("mood", mood);
        model.addAttribute("placeInfos",  placeInfos);
        return "index";
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
            placeInfo.name = extractString(obj, "\"name\"");
            placeInfo.introduction = extractString(obj, "\"introduction\"");
            placeInfo.keywords = extractArray(obj, "\"keywords\"");
            placeInfo.goodPoints = extractArray(obj, "\"goodPoints\"");
            placeInfo.visitTip = extractString(obj, "\"visitTip\"");
            placeInfo.imageUrl = extractString(obj, "\"imageUrl\"");
            System.out.println("placeInfo.imageUrl :: " + placeInfo.imageUrl);
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
