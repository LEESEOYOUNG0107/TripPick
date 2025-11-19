package com.example.trippick;

// JS가 JSON으로 보낸 데이터를 담는 곳
public class AddFavoriteRequest {
    private String placeName;
    private String placeIntroduction;
    private String placeImageUrl;

    public AddFavoriteRequest() {}

    // JS가 JSON을 Java 객체로 바꿀 때 필요한 Getter
    public String getPlaceName() { return placeName; }
    public String getPlaceIntroduc() { return placeIntroduction; }
    public String getPlaceImageUrl() {
        return placeImageUrl;
    }

    public void setPlaceName(String placeName) { this.placeName = placeName; }
    // JS가 intro나 introduction 둘 중 뭘로 보내든 다 받도록 2개 만듦
    public void setPlaceIntroduction(String placeIntroduction) { this.placeIntroduction = placeIntroduction; }
    public void setPlaceIntroduc(String placeIntroduction) { this.placeIntroduction = placeIntroduction; }
    public void setPlaceImageUrl(String placeImageUrl) { this.placeImageUrl = placeImageUrl; }
}
