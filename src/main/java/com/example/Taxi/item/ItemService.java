package com.example.Taxi.item;

import com.example.Taxi.PoliceApi;
import com.example.Taxi.image.FileProcessService;
import com.example.Taxi.item.entity.Item;
import com.example.Taxi.item.entity.Page;
import com.example.Taxi.item.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final JpaItemRepo jpaItemRepo;
    private final JpaPageRepo jpaPageRepo;
    private final PoliceApi policeApi;
    private final FileProcessService fileService;
    private String acqUrl = "http://apis.data.go.kr/1320000/LosfundInfoInqireService/getLosfundInfoAccToLc";

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void saveAcqItem() {
        Page page = jpaPageRepo.findByStatus(Status.ACQUIRE);
        int totalCount = policeApi.checkItemCount(acqUrl);
        int endPage = (totalCount / 1000) + 1;
        List<Item> filterItems = filterDuplicate(policeApi.checkItemInfo(page.getLastPage(), 1000, acqUrl));
        jpaItemRepo.saveAll(filterItems);
        for (int p = page.getLastPage()+1; p <= endPage; p++) {
            List<Item> items = policeApi.checkItemInfo(p, 1000, acqUrl);
            jpaItemRepo.saveAll(items);
        }
        page.updatePage(endPage);
    }

    @Transactional
    public void enroll(MultipartFile img, ItemReqDto itemReqDto) {
        String url = fileService.uploadImage(img);
        Item item = itemReqDto.toEntity(url);
        jpaItemRepo.save(item);
    }

    public List<Item> filterDuplicate(List<Item> items) {
        List<Item> newItems = new ArrayList<>();
        for (Item item : items) {
            if (!jpaItemRepo.existsByAtcId(item.getAtcId())) {
                newItems.add(item);
            }
        }
        return newItems;
    }

    public List<ItemResDto> findItemByLocation(String location, Status status) {
        return jpaItemRepo.findByLocationContainsAndStatus(location, status)
                .stream().map(item -> new ItemResDto(item)).collect(Collectors.toList());
    }
}
