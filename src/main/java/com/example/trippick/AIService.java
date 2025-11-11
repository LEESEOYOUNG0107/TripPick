package com.example.trippick;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service

public class AIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 프롬포트작성
     */
    public String getAIResponse(String area,String mood) {

        String prompt = String.format(
                "%s 지역에서 '%s'의 조건에 해당하는 장소를 10개 추천하고, 다음 JSON 형식에 맞춰 응답해줘." +
                        "다음 필드를 정확히 지켜서 출력해야 해:\n" +
                        "{\n" +
                        "  \"name\": \"장소 이름\",\n" +
                        "  \"introduction\": \"장소에 대한 간략한 소개 (한두 문장)\",\n" +
                        "  \"keywords\": [\"키워드1\", \"키워드2\", \"키워드3\"], (최대 3개)\n" +
                        "  \"goodPoints\": [\n" +
                        "    \"좋은 점 1 (문장)\",\n" +
                        "    \"좋은 점 2 (문장)\",\n" +
                        "    \"좋은 점 3 (문장)\",\n" +
                        "    \"좋은 점 4 (문장)\"\n" +
                        "  ],\n" +
                        "  \"visitTip\": \"방문 시 유용한 팁 (한두 문장)\",\n" +
                        "  \"imageUrl\": \"이 장소를 대표하는 이미지의 가상 URL (예: https://example.com/image.jpg)\"\n" +
                        "}", area, mood
        );
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        // 요청 바디 설정
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(Map.of("role", "user", "content", prompt))
        );

        // 요청 보내기
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    request,
                    Map.class
            );
            // 결과 파싱
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices != null && !choices.isEmpty()) {
                for (Map<String, Object> map : choices) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();

                        // value가 특정 타입이라면 타입캐스팅 가능

                        // 예: if ("someKey".equals(key)) { String strVal = (String) value; }
                    }
                }
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" + (String) message.get("content"));
                return (String) message.get("content");
            }
            return "AI의 응답을 가져오지 못했습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
    }

}
