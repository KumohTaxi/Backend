package com.example.Taxi.controller.room;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RoomController {

    @PostMapping("/room/new")
    public ResponseEntity<Object> createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        return new ResponseEntity(roomRequestDto, HttpStatus.OK);
    }
}
