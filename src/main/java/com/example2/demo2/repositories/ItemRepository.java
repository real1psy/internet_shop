package com.example2.demo2.repositories;

import com.example2.demo2.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item,Long> {

    /**
     * @param name
     * @return
     */



    @Query(value="SELECT * FROM Item it WHERE LOWER(it.item_name) LIKE LOWER(?1)  AND it.brand_brand_id=?2 ORDER BY it.item_price ASC",nativeQuery = true)
    List<Item> findAllByNameLikeAndBrandIdOrderByPriceAsc(String name,Long brandID);

    @Query(value="SELECT * FROM Item it WHERE LOWER(it.item_name) LIKE LOWER(?1)",nativeQuery = true)
    List<Item> findAllByNameLike(String name);

    @Query(value="SELECT * FROM Item it WHERE LOWER(it.item_name) LIKE LOWER(?1)  AND it.brand_brand_id=?2 ORDER BY it.item_price DESC",nativeQuery = true)
    List<Item> findAllByNameLikeAndBrandIdOrderByPriceDesc(String name,Long brandID);

    List<Item> findAllByInTopPage(boolean top);

    @Query(value="SELECT * FROM Item it WHERE LOWER(it.item_name) LIKE LOWER(?1) AND it.brand_brand_id=?2 AND it.item_price>=(?3) ORDER BY it.item_price DESC",nativeQuery = true)
    List<Item> findAllByNameLikeAndBrandIdAndPriceGreaterThanEqualOrderByPriceDesc(String name, Long brandID,double from);

    @Query(value="SELECT * FROM Item it WHERE LOWER(it.item_name) LIKE LOWER(?1) AND it.brand_brand_id=?2 AND it.item_price>=(?3) ORDER BY it.item_price ASC",nativeQuery = true)
    List<Item> findAllByNameLikeAndBrandIdAndPriceGreaterThanEqualOrderByPriceAsc(String name,Long brandID, double from);

    @Query(value="SELECT * FROM Item it WHERE LOWER(it.item_name) LIKE LOWER(?1) AND it.brand_brand_id=?2 AND it.item_price<=(?3) ORDER BY it.item_price DESC",nativeQuery = true)
    List<Item> findAllByNameLikeAndBrandIdAndPriceLessThanEqualOrderByPriceDesc(String name,Long brandID, double to);

    @Query(value="SELECT * FROM Item it WHERE LOWER(it.item_name) LIKE LOWER(?1) AND it.brand_brand_id=?2 AND it.item_price<=(?3) ORDER BY it.item_price ASC",nativeQuery = true)
    List<Item> findAllByNameLikeAndBrandIdAndPriceLessThanEqualOrderByPriceAsc(String name, Long brandID,double to);

    @Query(value="SELECT * FROM Item it WHERE LOWER(it.item_name) LIKE LOWER(?1)AND it.brand_brand_id=?2 AND it.item_price BETWEEN (?3) AND (?4) ORDER BY it.item_price ASC",nativeQuery = true)
    List<Item> findAllByNameLikeAndBrandIdAndPriceBetweenOrderByPriceAsc(String name,Long brandID, double from, double to);

    @Query(value="SELECT * FROM Item it WHERE LOWER(it.item_name) LIKE LOWER(?1) AND it.brand_brand_id=?2 AND it.item_price BETWEEN (?3) AND (?4)  ORDER BY it.item_price DESC",nativeQuery = true)
    List<Item> findAllByNameLikeAndBrandIdAndPriceBetweenOrderByPriceDesc(String name, Long brandID,double from, double to);

    List<Item> findAllByBrandIdAndPriceLessThanEqualOrderByPriceAsc( Long brandID,double to);
    List<Item> findAllByBrandIdAndPriceLessThanEqualOrderByPriceDesc( Long brandID,double to);
    List<Item> findAllByBrandIdAndPriceGreaterThanEqualOrderByPriceAsc( Long brandID,double from);
    List<Item> findAllByBrandIdAndPriceGreaterThanEqualOrderByPriceDesc( Long brandID,double from);
    List<Item> findAllByBrandIdAndPriceBetweenOrderByPriceAsc( Long brandID,double from,double to);
    List<Item> findAllByBrandIdAndPriceBetweenOrderByPriceDesc( Long brandID,double from,double to);


    List<Item> findAllByBrandIdOrderByPriceAsc(Long id);
    List<Item> findAllByBrandIdOrderByPriceDesc(Long id);
    List<Item> findAllByBrandId(Long id);

    List<Item> findAllByCategories(Category category);


    @Query("SELECT COUNT(it) FROM Item it WHERE it.id=?1")
    Long checkItem(Long id);
}
