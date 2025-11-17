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
//    @Value("${openai.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    /**
     * 프롬포트작성
     */
    public String getAIResponse(String area,String mood) {
        String prompt = String.format(
                "%s 지역에서 '%s'의 조건에 해당하는 실제로 존재하는 장소를 정확히 10개 추천하고, 다음 JSON 형식에 맞춰 응답해줘. " +
                        "응답은 반드시 JSON 배열(각 원소는 아래 객체 형식)로만 출력해야 하고, 형식과 필드 이름은 원본과 정확히 일치해야 해. " +
                        "각 장소 정보는 블로그, 후기, 공식 홈페이지, 지도 등 여러 실제 출처(후기 종합)를 확인해 종합한 사실 기반 정보여야 하고, 장소와 이미지는 실제로 존재해 프로그램에서 활용 가능한 공개 URL을 포함해야 해. " +
                        "다음 필드를 정확히 지켜서 출력해야 해:\n" +
                        "{\n" +
                        "  \"name\": \"장소 이름\",\n" +
                        "  \"introduction\": \"장소에 대한 간략한 소개 (한두 문장)\",\n" +
                        "  \"keywords\": [\"키워드1\", \"키워드2\", \"키워드3\"],\n" +
                        "  \"goodPoints\": [\n" +
                        "    \"좋은 점 1 (문장)\",\n" +
                        "    \"좋은 점 2 (문장)\",\n" +
                        "    \"좋은 점 3 (문장)\",\n" +
                        "    \"좋은 점 4 (문장)\"\n" +
                        "  ],\n" +
                        "  \"visitTip\": \"방문 시 유용한 팁 (한두 문장)\",\n" +
                        "  \"imageUrl\": \"이 장소를 대표하는 이미지의 실제 공개 URL (예: https://example.com/image.jpg)\"\n" +
                        "}\n" +
                        "추가 요구사항:\n" +
                        "- 추천 장소는 반드시 실제 존재하는 장소여야 하며, 지역(예: 서울, 서울시 강서구 등촌동, 강원도 정선군 등)과 조건(예: '추운 날 들어가기 좋은 감성 카페', '연인과 즐기기 좋은 액티비티', '강아지와 함께 들어갈 수 있는 카페' 등)을 무조건 만족해야 함.\n" +
                        "- 각 장소의 imageUrl은 장소를 대표하는 실제 공개 이미지의 URL을 반드시 제공할 것(장소 공식사이트, 지도 서비스, 또는 퍼블릭하게 접근 가능한 후기 이미지 등 사용 가능).\n" +
                        "- 각 항목은 실제 후기, 블로그 글, 공식 안내 등을 종합해 작성하고, 사실 기반으로 신뢰할 수 있는 세부 정보를 담을 것.\n" +
                        "- keywords는 최대 3개로 제한하고, 관련성이 높은 키워드만 포함할 것.\n" +
                        "- 출력은 순수 JSON 배열만 허용(설명 문장, 추가 텍스트, 주석, 따옴표 밖의 아무 텍스트도 포함 금지). " +
                        "예: [{...}, {...}, ...]\n" +
                        "- 지역 및 조건은 String.format에 전달된 첫 번째와 두 번째 인자에 따라 적용될 것.\n" +
                        "- 장소가 존재함을 확인한 출처(블로그/후기/공식 페이지/지도 등)의 링크는 내부적으로 참조해 내용을 종합하되, 출력 JSON에는 링크를 포함하지 않아도 됨.\n" +
                        "이제 위 규칙을 엄격히 지켜서 JSON 형식으로 10개 장소를 추천해줘.", area, mood);

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
