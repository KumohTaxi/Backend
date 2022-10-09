package com.example.Taxi.item;

import com.example.Taxi.item.entity.Page;
import com.example.Taxi.item.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPageRepo extends JpaRepository<Page, Long> {

    Page findByStatus(Status status);
}
