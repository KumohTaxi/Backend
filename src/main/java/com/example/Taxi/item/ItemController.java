package com.example.Taxi.item;

import com.example.Taxi.item.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/item")
    public List<ItemResDto> findItemByLocation(
            @RequestParam String location,
            @RequestParam Status status) {
        return itemService.findItemByLocation(location, status);
    }

    /**
     * formdate로 받는 경우에는 @RequestBody를 사용하지 않는다.
     * 해당 어노테이션은 json 요청값을 받을때 사용하는 것이다.
     * 보통 파일은 @RequestParam을 이용해서 받는다. 하지만 다른 값들은 @RequestBody로 받는다.
     * 따라서 두가지를 혼용할 수 있도록 @RequestPart를 사용했다.
     */
    @PostMapping("/item")
    public void enroll(
            @RequestPart(required = false) MultipartFile img,
            @RequestPart ItemReqDto itemReqDto) {
        itemService.enroll(img,itemReqDto);
    }

    @Scheduled(cron = "0 15 15 * * *")
    public void updateItem() {
        itemService.triggerSchedule();
    }
}
