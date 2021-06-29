package com.example2.demo2.services;

import com.example2.demo2.entities.*;

import java.util.List;


// all tables must be here , in one service?
public interface ItemService {
    boolean saveItem(Item item);
    List<Item> listAllItems();
    List<Item> listOnlyInTopPage();
    Item listItem(Long id);
    void deleteItem(Long id);

    List<Item> listAllItemByName(String name);
    List<Item> listItemsByNameAscByPrice(String name,Long id);
    List<Item> listItemsByNameDescByPrice(String name,Long id);

    List<Item> listItemsByNameAndFromPriceAndDescByPrice(String name,Long brandID,double from);
    List<Item> listItemsByNameAndFromPriceAndAscByPrice(String name,Long brandID,double from);

    List<Item> listItemsByNameAndToPriceAndDescByPrice(String name,Long brandID,double to);
    List<Item> listItemsByNameAndToPriceAndAscByPrice(String name,Long brandID,double to);

    List<Item> listItemsByNameAndToAndFromPriceAndDescByPrice(String name,Long brandID,double from,double to);
    List<Item> listItemsByNameAndToAndFromPriceAndAscByPrice(String name,Long brandID,double from,double to);

    List<Item> findAllByBrandIdAndPriceLessThanEqualOrderByPriceAsc(double to, Long brandID);
    List<Item> findAllByBrandIdAndPriceLessThanEqualOrderByPriceDesc( Long brandID,double to);
    List<Item> findAllByBrandIdAndPriceGreaterThanEqualOrderByPriceAsc( Long brandID,double from);
    List<Item> findAllByBrandIdAndPriceGreaterThanEqualOrderByPriceDesc( Long brandID,double from);
    List<Item> findAllByBrandIdAndPriceBetweenOrderByPriceAsc( Long brandID,double from,double to);
    List<Item> findAllByBrandIdAndPriceBetweenOrderByPriceDesc( Long brandID,double from,double to);

    List<Item> findItemsBrandId(Long id);
    List<Item> findItemsBrandIdAsc(Long id);
    List<Item> findItemsBrandIdDesc(Long id);

    List<Picture> listAllPictures();
    Picture listPicture(Long id);
    void deletePicture(Long id);
    void savePicture(Picture picture);
    List<Picture>findPicturesByItemId(Long id);

    List<Item> findAllByCategory(Category category);

    boolean ItemExists(Long id);

    List<Item> sortAsc(String search,Long brandID,String from,String to);
    List<Item> sortDesc(String search,Long brandID,String from,String to);

    List<SoldItem> listAllSoldItems();
    SoldItem listSoldItem(Long id);
    void deleteSoldItem(Long id);
    void saveSoldItem(SoldItem soldItem);



}
