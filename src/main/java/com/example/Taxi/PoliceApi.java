package com.example.Taxi;

import com.example.Taxi.item.entity.Item;
import com.example.Taxi.item.entity.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PoliceApi {

    @Value("${key.police}")
    private String key;

    public List<Item> checkItemInfo(int page, int rows, String url) {

        List<Item> items = new ArrayList<>();
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(url + "?serviceKey=" + key + "&START_YMD=20210101&numOfRows="+rows+"&pageNo=" + page);
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("item");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                Element element = (Element) node;
                Item item = Item.builder()
                        .atcId(getTagValue("atcId",element))
                        .lostTime(LocalDate.parse(getTagValue("fdYmd",element)))
                        .category(getTagValue("prdtClNm",element))
                        .location(getTagValue("addr",element))
                        .status(Status.ACQUIRE)
                        .name(getTagValue("fdPrdtNm",element))
                        .foundOrder(Integer.getInteger(getTagValue("fdSn",element)))
                        .build();
                items.add(item);
            }
            System.out.println("page: "+page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    public int checkItemCount(String url) {

        int totalCount = -1;
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(url + "?serviceKey=" + key + "&START_YMD=20210101&numOfRows=1&pageNo=1");
            document.getDocumentElement().normalize();
            totalCount = Integer.parseInt(
                    document.getDocumentElement().getElementsByTagName("totalCount").item(0).getTextContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    public String getTagValue(String tag, Element eElement) {

        String result = "";
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        result = nlList.item(0).getTextContent();
        return result;
    }
}
