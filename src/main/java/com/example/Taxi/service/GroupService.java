package com.example.Taxi.service;

import com.example.Taxi.domain.Group;
import com.example.Taxi.repo.GroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepo groupRepo;

    @Transactional
    public void saveRoom(Group group) {
        groupRepo.save(group);
    }

    public List<Group> findRooms() {
        return groupRepo.findAll();
    }
}
