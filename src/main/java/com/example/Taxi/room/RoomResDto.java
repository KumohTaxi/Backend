package com.example.Taxi.room;

import com.example.Taxi.item.entity.Item;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoomResDto {
    private Long roomId;
    private LocalDateTime requestTime;
    private ItemDto item;
    private Long requesterId;
    private Status status;

    @Builder
    public RoomResDto(Room room) {
        this.roomId = room.getId();
        this.requestTime = room.getRequestTime();
        this.item = new ItemDto(room.getItem());
        this.requesterId = room.getRequester().getIdentityNum();
        this.status = room.getStatus();
    }

    private class ItemDto {
        private String name;
        private String img;
        private Long registrantId;

        public ItemDto(Item item) {
            this.name = item.getName();
            this.img = item.getImg();
            this.registrantId = item.getRegistrant().getIdentityNum();
        }

    }

}

