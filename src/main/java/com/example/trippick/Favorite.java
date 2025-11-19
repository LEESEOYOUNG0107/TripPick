package com.example.trippick;
import jakarta.persistence.*;

@Entity
public class Favorite extends BaseEntity{
    @ManyToOne //회원 1명이 찜을 여러개 할 수 있다
    @JoinColumn(name = "user_userkey") //user테이블에 userkey와 연결
    private User user;

    private String placeName; //장소 이름
    private String placeIntroduc; //장소 소개
    private String placeImageUrl; //장소 이미지 url

    protected Favorite() {}

    // 찜을 생성하는 생성자
    public Favorite(User user, String placeName, String placeIntroduc, String placeImageUrl) {
        this.user = user;
        this.placeName = placeName;
        this.placeIntroduc = placeIntroduc;
        this.placeImageUrl = placeImageUrl;
    }

    //찜 목록 읽을 때 필요
    public User getUser() {
        return user;
    }
    public String getPlaceName() {
        return placeName;
    }
    public String getPlaceIntroduc() { return placeIntroduc; }
    public String getPlaceImageUrl() {
        return placeImageUrl;
    }
}
