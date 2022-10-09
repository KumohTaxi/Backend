package com.example.Taxi.item;

import com.example.Taxi.PoliceApi;
import com.example.Taxi.item.entity.Item;
import com.example.Taxi.item.entity.Page;
import com.example.Taxi.item.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final JpaItemRepo jpaItemRepo;
    private final JpaPageRepo jpaPageRepo;
    private final PoliceApi policeApi;
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

    public List<Item> filterDuplicate(List<Item> items) {
        List<Item> newItems = new ArrayList<>();
        for (Item item : items) {
            if (!jpaItemRepo.existsByAtcId(item.getAtcId())) {
                newItems.add(item);
            }
        }
        return newItems;
    }
}
