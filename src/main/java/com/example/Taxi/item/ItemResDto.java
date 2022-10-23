package com.example.Taxi.item;

import com.example.Taxi.item.entity.Item;
import com.example.Taxi.item.entity.Status;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ItemResDto {
    private String atcId;
    private String location;
    private String name;
    private Status status;
    private String category;
    private LocalDate lostDate;
    private String img;
    private Long identityNum;

    public ItemResDto(Item item) {
        this.atcId = item.getAtcId();
        this.location = item.getLocation();
        this.name = item.getName();
        this.status = item.getStatus();
        this.category = item.getCategory();
        this.lostDate = item.getLostTime();
        this.img = item.getImg();
        this.identityNum = item.getRegistrant().getIdentityNum();
    }
}
