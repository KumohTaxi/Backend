package com.example.Taxi.room;

import com.example.Taxi.token.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/room")
    public List<RoomResDto> findMyRoom(@RequestBody TokenDto token) {
        return roomService.findMyRoom(token);
    }
}
