package com.example.Taxi.item;

import com.example.Taxi.item.entity.Item;
import com.example.Taxi.item.entity.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ItemReqDto {
    private String location;
    private String name;
    private Status status;
    private String category;
    private LocalDate lostDate;

    public Item toEntity(String imgUrl) {
        return Item.builder()
                .atcId("not police api data")
                .location(location)
                .name(name)
                .status(status)
                .category(category)
                .lostTime(lostDate)
                .img(imgUrl)
                .build();
    }
}
