package com.example.Taxi.item;

import com.example.Taxi.item.entity.Item;
import com.example.Taxi.item.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaItemRepo extends JpaRepository<Item,Long> {

    boolean existsByAtcId(String actId);

    List<Item> findByLocationContainsAndStatus(String location, Status status);
}
