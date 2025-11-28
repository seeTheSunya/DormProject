package com.example.sbb.naver;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class NaverShopDto {
    private int total;
    private int start;
    private int display;
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private String title;
        private String link;
        private String image;
        private String lprice; // 최저가
        private String mallName;
        private String productId;
        private String productType;
        private String brand;
        private String maker;
        private String category1;
        private String category2;
        private String category3;
        private String category4;
    }
}