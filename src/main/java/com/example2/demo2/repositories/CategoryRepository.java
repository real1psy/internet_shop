package com.example2.demo2.repositories;

import com.example2.demo2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CategoryRepository  extends JpaRepository<Category,Long> {

    @Query("SELECT COUNT(cat) FROM Category cat WHERE cat.id=?1")
    Long checkCategory(Long id);

}
