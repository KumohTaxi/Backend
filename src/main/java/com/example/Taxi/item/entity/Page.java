package com.example.Taxi.item.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Page {
    @Id @GeneratedValue
    private Long id;
    private Status status;
    private Integer lastPage;
    private LocalDateTime updateTime;

    public Page(Status status,Integer lastPage, LocalDateTime updateTime) {
        this.status = status;
        this.lastPage = lastPage;
        this.updateTime = updateTime;
    }

    public void updatePage(Integer lastPage) {
        this.lastPage = lastPage;
        this.updateTime = LocalDateTime.now();
    }
}
