package com.example.Taxi.item;

import com.example.Taxi.item.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/item")
    public List<ItemResDto> findItemByLocation(
            @RequestParam String location,
            @RequestParam Status status ) {
        return itemService.findItemByLocation(location, status);
    }

}
