package com.example.Taxi.item;

import com.example.Taxi.PoliceApi;
import com.example.Taxi.config.exception.CustomException;
import com.example.Taxi.config.exception.CustomExceptionStatus;
import com.example.Taxi.image.FileProcessService;
import com.example.Taxi.item.entity.Item;
import com.example.Taxi.item.entity.Page;
import com.example.Taxi.item.entity.Status;
import com.example.Taxi.member.Member;
import com.example.Taxi.member.MemberRepo;
import com.example.Taxi.token.TokenRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final JpaItemRepo jpaItemRepo;
    private final JpaPageRepo jpaPageRepo;
    private final PoliceApi policeApi;
    private final FileProcessService fileService;
    private final TokenRepo tokenRepo;
    private final MemberRepo memberRepo;
    private String acqUrl = "http://apis.data.go.kr/1320000/LosfundInfoInqireService/getLosfundInfoAccToLc";

    public void saveAcqItem(int p) {
        List<Item> items = policeApi.checkItemInfo(p, 1000, acqUrl);
        jpaItemRepo.saveAll(items);
    }

    @Transactional
    public void triggerSchedule() {
        Page page = jpaPageRepo.findByStatus(Status.ACQUIRE);
        int totalCount = policeApi.checkItemCount(acqUrl);
        int endPage = (totalCount / 1000) + 1;
        filterDuplicate(policeApi.checkItemInfo(page.getLastPage(), 1000, acqUrl));
        for (int p = page.getLastPage()+1; p <= endPage+1; p++) {
            log.info("page start:" + p);
            saveAcqItem(p);
            log.info("page end:" + p);
        }
    }

    @Transactional
    public void enroll(MultipartFile img, ItemReqDto itemReqDto) {
        Member member = findMemberByAccessToken(itemReqDto.getAccessToken());
        String url = fileService.uploadImage(img);
        Item item = itemReqDto.toEntity(url);
        item.enrollRegistrant(member);
        jpaItemRepo.save(item);
    }

    public void filterDuplicate(List<Item> items) {
        List<Item> newItems = new ArrayList<>();
        for (Item item : items) {
            if (!jpaItemRepo.existsByAtcId(item.getAtcId())) {
                newItems.add(item);
            }
        }
        jpaItemRepo.saveAll(newItems);
    }

    public List<ItemResDto> findItemByLocation(String location, Status status) {
        return jpaItemRepo.findByLocationContainsAndStatus(location, status)
                .stream().map(item -> new ItemResDto(item)).collect(Collectors.toList());
    }

    private Member findMemberByAccessToken(String accessToken) {
        Long identityNum = tokenRepo.findIdentityNumByAccessToken(accessToken)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_TOKEN));
        Member member = memberRepo.findMemberByIdentityNum(identityNum)
                .orElseThrow(()->new CustomException(CustomExceptionStatus.INVALID_IDENTITY_NUM));
        return member;
    }
}
