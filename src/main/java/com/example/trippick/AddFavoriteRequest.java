package com.example.trippick;

// JS가 JSON으로 보낸 데이터를 담는 곳
public class AddFavoriteRequest {
    private String placeName;
    private String placeIntroduction;
    private String placeImageUrl;

    public AddFavoriteRequest() {}

    // JS가 JSON을 Java 객체로 바꿀 때 필요한 Getter
    public String getPlaceName() {
        return placeName;
    }
    public String getPlaceIntroduc() {
        return placeIntroduction;
    }
    public String getPlaceImageUrl() {
        return placeImageUrl;
    }
}
