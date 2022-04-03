package com.example.Taxi.service;

import com.example.Taxi.domain.Room;
import com.example.Taxi.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepo roomRepo;

    public void saveRoom(Room room) {
        roomRepo.save(room);
    }

    public List<Room> findRooms() {
        return roomRepo.findAll();
    }
}
