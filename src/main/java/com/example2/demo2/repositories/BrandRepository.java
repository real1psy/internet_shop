package com.example2.demo2.repositories;

import com.example2.demo2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
@Transactional
public interface BrandRepository extends JpaRepository<Brand,Long> {

    List<Brand> findAllByCountry_Id(Long id);

    @Query("SELECT COUNT(br) FROM Brand br WHERE br.id=?1")
    Long checkBrand(Long id);

}
