package com.example2.demo2.services.impls;

import com.example2.demo2.entities.*;
import com.example2.demo2.repositories.*;
import com.example2.demo2.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private SoldItemRepository soldItemRepository;

    @Override
    public boolean saveItem(Item item) {
        if(itemRepository.save(item)!=null){
            return true;
        }
        return false;
    }

    @Override
    public List<Item> listAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> listOnlyInTopPage() {
        return itemRepository.findAllByInTopPage(true);
    }

    @Override
    public Item listItem(Long id) {
        return itemRepository.getOne(id);
    }

    @Override
    public void deleteItem(Long id) { itemRepository.deleteById(id); }

    @Override
    public List<Item> listAllItemByName(String name){
        return itemRepository.findAllByNameLike("%"+name+"%");
    }

    @Override
    public List<Item> listItemsByNameAscByPrice(String name,Long id){
        return itemRepository.findAllByNameLikeAndBrandIdOrderByPriceAsc("%"+name+"%",id);
    }

    @Override
    public List<Item> listItemsByNameDescByPrice(String name,Long id){
        return itemRepository.findAllByNameLikeAndBrandIdOrderByPriceDesc("%"+name+"%",id);
    }

    @Override
    public List<Item> listItemsByNameAndFromPriceAndAscByPrice(String name,Long brandID,double from){
        return itemRepository.findAllByNameLikeAndBrandIdAndPriceGreaterThanEqualOrderByPriceAsc("%"+name+"%",brandID,from);
    }
    @Override
    public List<Item> listItemsByNameAndFromPriceAndDescByPrice(String name,Long brandID,double from){
        return itemRepository.findAllByNameLikeAndBrandIdAndPriceGreaterThanEqualOrderByPriceDesc("%"+name+"%",brandID,from);
    }

    @Override
    public List<Item> listItemsByNameAndToPriceAndDescByPrice(String name,Long brandID,double to){
        return itemRepository.findAllByNameLikeAndBrandIdAndPriceLessThanEqualOrderByPriceDesc("%"+name+"%",brandID,to);
    }

    @Override
    public List<Item> listItemsByNameAndToPriceAndAscByPrice(String name,Long brandID,double to){
        return itemRepository.findAllByNameLikeAndBrandIdAndPriceLessThanEqualOrderByPriceAsc("%"+name+"%",brandID,to);
    }

    @Override
    public List<Item> listItemsByNameAndToAndFromPriceAndDescByPrice(String name,Long brandID,double from,double to){
        return itemRepository.findAllByNameLikeAndBrandIdAndPriceBetweenOrderByPriceDesc("%"+name+"%",brandID,from,to);
    }

    @Override
    public List<Item> listItemsByNameAndToAndFromPriceAndAscByPrice(String name,Long brandID,double from,double to){
        return itemRepository.findAllByNameLikeAndBrandIdAndPriceBetweenOrderByPriceAsc("%"+name+"%",brandID,from,to);
    }


    @Override
    public List<Item> findAllByBrandIdAndPriceLessThanEqualOrderByPriceAsc(double to, Long brandID){
        return itemRepository.findAllByBrandIdAndPriceLessThanEqualOrderByPriceAsc(brandID,to);
    }

    @Override
    public List<Item> findAllByBrandIdAndPriceLessThanEqualOrderByPriceDesc(Long brandID, double to) {
        return itemRepository.findAllByBrandIdAndPriceLessThanEqualOrderByPriceDesc(brandID,to);
    }

    @Override
    public List<Item> findAllByBrandIdAndPriceGreaterThanEqualOrderByPriceAsc(Long brandID, double from) {
        return itemRepository.findAllByBrandIdAndPriceGreaterThanEqualOrderByPriceAsc(brandID,from);
    }

    @Override
    public List<Item> findAllByBrandIdAndPriceGreaterThanEqualOrderByPriceDesc(Long brandID, double from) {
        return itemRepository.findAllByBrandIdAndPriceGreaterThanEqualOrderByPriceDesc(brandID,from);
    }

    @Override
    public List<Item> findAllByBrandIdAndPriceBetweenOrderByPriceAsc(Long brandID, double from, double to) {
        return itemRepository.findAllByBrandIdAndPriceBetweenOrderByPriceAsc(brandID,from,to);
    }

    @Override
    public List<Item> findAllByBrandIdAndPriceBetweenOrderByPriceDesc(Long brandID, double from, double to) {
        return itemRepository.findAllByBrandIdAndPriceBetweenOrderByPriceDesc(brandID,from,to);
    }

    @Override
    public List<Picture> listAllPictures() {
        return pictureRepository.findAll();
    }

    @Override
    public Picture listPicture(Long id) {
        return pictureRepository.getOne(id);
    }

    @Override
    public void deletePicture(Long id) {
        pictureRepository.deleteById(id);
    }

    @Override
    public void savePicture(Picture picture) {
        pictureRepository.save(picture);
    }

    @Override
    public List<Picture> findPicturesByItemId(Long id) {
        return pictureRepository.findAllByItem_Id(id);
    }

    @Override
    public List<Item> findAllByCategory(Category category) {
        return itemRepository.findAllByCategories(category);
    }

    @Override
    public List<Item> findItemsBrandId(Long id){
        return itemRepository.findAllByBrandId(id);
    }

    @Override
    public List<Item> findItemsBrandIdAsc(Long id) {
        return itemRepository.findAllByBrandIdOrderByPriceAsc(id);
    }

    @Override
    public List<Item> findItemsBrandIdDesc(Long id) {
        return itemRepository.findAllByBrandIdOrderByPriceDesc(id);
    }

    @Override
    public boolean ItemExists(Long id){
        if(itemRepository.checkItem(id)>0){
            return true;
        }
        return false;
    }

    @Override
    public List<Item> sortAsc(String search, Long brandID, String from, String to) {
        List<Item> items=new ArrayList<>();
        if (search != "" && from == "" && to == "") {
            items= listItemsByNameAscByPrice(search, brandID);
        }
        else if (search != "" && from != "" && to == "") {
            double fromPrice = Double.parseDouble(from);
            items= listItemsByNameAndFromPriceAndAscByPrice(search, brandID, fromPrice);
        }
        else if (search != "" && from == "" && to != "") {
            double toPrice = Double.parseDouble(to);
            items= listItemsByNameAndToPriceAndAscByPrice(search, brandID, toPrice);
        }
        else if (search != "" && from != "" && to != "") {
            double toPrice = Double.parseDouble(to);
            double fromPrice = Double.parseDouble(from);
            items= listItemsByNameAndToAndFromPriceAndDescByPrice(search, brandID, fromPrice, toPrice);
        }
        else if (search == "" && from == "" && to == "") {
            items = findItemsBrandIdAsc(brandID);
        }
        else if (search == "" && from != "" && to == "") {
            double fromPrice = Double.parseDouble(from);
            items = findAllByBrandIdAndPriceGreaterThanEqualOrderByPriceAsc(brandID, fromPrice);
        }
        else if (search == "" && from == "" && to != "") {
            double toPrice = Double.parseDouble(to);
            items = findAllByBrandIdAndPriceLessThanEqualOrderByPriceAsc(toPrice, brandID);
        }
        else if (search == "" && from != "" && to != "") {
            double toPrice = Double.parseDouble(to);
            double fromPrice = Double.parseDouble(from);
            items = findAllByBrandIdAndPriceBetweenOrderByPriceAsc(brandID, fromPrice, toPrice);
        }
        return items;
    }

    @Override
    public List<Item> sortDesc(String search, Long brandID, String from, String to) {
        List<Item> items=new ArrayList<>();
        if (search != "" && from == "" && to == "") {
            items=listItemsByNameDescByPrice(search, brandID);
        } else if (search != "" && from != "" && to == "") {
            double fromPrice = Double.parseDouble(from);
            items=listItemsByNameAndFromPriceAndDescByPrice(search, brandID, fromPrice);
        } else if (search != "" && from == "" && to != "") {
            double toPrice = Double.parseDouble(to);
            items=listItemsByNameAndToPriceAndDescByPrice(search, brandID, toPrice);
        } else if (search != "" && from != "" && to != "") {
            double toPrice = Double.parseDouble(to);
            double fromPrice = Double.parseDouble(from);
            items=listItemsByNameAndToAndFromPriceAndDescByPrice(search, brandID, fromPrice, toPrice);
        } else if (search == "" && from == "" && to == "") {
            items=findItemsBrandIdDesc(brandID);
        } else if (search == "" && from != "" && to == "") {
            double fromPrice = Double.parseDouble(from);
            items=findAllByBrandIdAndPriceGreaterThanEqualOrderByPriceDesc(brandID, fromPrice);
        } else if (search == "" && from == "" && to != "") {
            double toPrice = Double.parseDouble(to);
            items=findAllByBrandIdAndPriceLessThanEqualOrderByPriceDesc(brandID, toPrice);
        } else if (search == "" && from != "" && to != "") {
            double toPrice = Double.parseDouble(to);
            double fromPrice = Double.parseDouble(from);
            items=findAllByBrandIdAndPriceBetweenOrderByPriceDesc(brandID, fromPrice, toPrice);
        }
        return items;
    }

    @Override
    public List<SoldItem> listAllSoldItems() {
        return soldItemRepository.findAll();
    }

    @Override
    public SoldItem listSoldItem(Long id) {
        return soldItemRepository.getOne(id);
    }

    @Override
    public void deleteSoldItem(Long id) {
        soldItemRepository.deleteById(id);
    }

    @Override
    public void saveSoldItem(SoldItem soldItem) {
        soldItemRepository.save(soldItem);
    }

}
