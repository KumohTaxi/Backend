package com.example.Taxi.item;

import com.example.Taxi.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaItemRepo extends JpaRepository<Item,Long> {

    boolean existsByAtcId(String actId);
}
