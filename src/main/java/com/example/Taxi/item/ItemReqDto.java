package com.example.Taxi.item;

import com.example.Taxi.item.entity.Item;
import com.example.Taxi.item.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ItemReqDto {
    private String location;
    private String name;
    @Schema(description = "분실물인지 습득물인지",example = "ACQUIRE | LOST")
    private Status status;
    @Schema(example = "가방 > 에코백")
    private String category;
    private LocalDate lostDate;
    private double lat;
    private double lng;
    private String comment;

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
