package com.example2.demo2.repositories;

import com.example2.demo2.entities.SoldItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SoldItemRepository extends JpaRepository<SoldItem,Long> {
}
