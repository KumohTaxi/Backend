package com.example.Taxi.controller;

import com.example.Taxi.domain.Room;
import com.example.Taxi.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {
    
    private final RoomService roomService;

    @PostMapping("/room/new")
    public void createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        roomService.saveRoom(roomRequestDto.toEntity());
    }

    @GetMapping("/room")
    public List<RoomResponseDto> findRoom() {
        List<Room> rooms = roomService.findRooms();
        List<RoomResponseDto> roomResponseDtos = new ArrayList<>();
        for (Room room : rooms) {
            roomResponseDtos.add(new RoomResponseDto(room));
        }
        return roomResponseDtos;
    }
}