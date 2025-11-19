package com.example.trippick;
import java.util.List;

public class PlaceInfo {
    String name;
    String introduction;
    List<String> keywords;
    List<String> goodPoints;
    String visitTip;
    String imageUrl;

    public PlaceInfo() {
    }

    public PlaceInfo(String name, String introduction, List<String> keywords, List<String> goodPoints, String visitTip, String imageUrl) {
        this.name = name;
        this.introduction = introduction;
        this.keywords = keywords;
        this.goodPoints = goodPoints;
        this.visitTip = visitTip;
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
    public void setGoodPoints(List<String> goodPoints) { this.goodPoints = goodPoints; }
    public void setVisitTip(String visitTip) { this.visitTip = visitTip; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getName() {
        return name;
    }
    public String getIntroduction() { return introduction; }
    public List<String> getKeywords() { return keywords; }
    public List<String> getGoodPoints() { return goodPoints; }
    public String getVisitTip() { return visitTip; }
    public String getImageUrl() { return imageUrl; }
}
