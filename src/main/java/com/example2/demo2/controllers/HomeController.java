package com.example2.demo2.controllers;

import com.example2.demo2.entities.*;
import com.example2.demo2.services.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;


@Controller
public class HomeController {
    public static ArrayList<Basket> baskets=new ArrayList<>();

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession sess;



    @Value("${file.avatar.viewPath}")
    private String viewPath;

    @Value("${file.avatar.uploadPath}")
    private String uploadPath;

    @Value("${file.avatar.defaultPicture}")
    private String defaultPicture;

    @Value("${file.itemPictures.viewPath}")
    private String itemViewPath;

    @Value("${file.itemPictures.uploadPath}")
    private String itemUploadPath;

    @Value("${file.itemPictures.defaultPicture}")
    private String itemDefaultPicture;

    @GetMapping(value = "/")
    public String index(Model model) {
        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Category> categories = categoryService.listAllCategories();
        if (categories != null) {
            model.addAttribute("categoriesJ", categories);
        }

        List<Item> items = itemService.listOnlyInTopPage();
        if (items != null) {
            model.addAttribute("items", items);
        }
        return "home";
    }

    @GetMapping(value = "/allItems")
    public String allItems(Model model) {
        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Category> categories = categoryService.listAllCategories();
        if (categories != null) {
            model.addAttribute("categoriesJ", categories);
        }

        List<Item> items = itemService.listAllItems();
        if (items != null) {
            model.addAttribute("items", items);
        }
        return "home";
    }

    @GetMapping(value = "/sortByCategory/{id}")
    public String sortByCategory(Model model, @PathVariable(name="id") Long categoryID) {


        if(categoryID!=null && categoryService.CategoryExists(categoryID)==true){
            List<Item> items = itemService.findAllByCategory(categoryService.listCategory(categoryID));
            if (items != null) {
                System.out.println(items.size());
                model.addAttribute("items", items);
            }

            if(this.itemCountInBasket()>0){
                model.addAttribute("itemAmount",this.itemCountInBasket());
            }

            List<Brand> brands = brandService.listAllBrands();
            if (brands != null) {
                model.addAttribute("brands", brands);
            }

            List<Category> categories = categoryService.listAllCategories();
            if (categories != null) {
                model.addAttribute("categoriesJ", categories);
            }
            return "home";
        }
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        List<Category> categories = categoryService.listAllCategories();
        if (categories != null) {
            model.addAttribute("categoriesJ", categories);
        }
        return "login";
    }

    @GetMapping(value = "/detailed/{search}")
    public String detailedSearch(Model model) {
        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Category> categories = categoryService.listAllCategories();
        if (categories != null) {
            model.addAttribute("categoriesJ", categories);
        }

        List<Item> items = itemService.listAllItems();
        if (items != null) {
            model.addAttribute("items", items);
        }
        return "redirect:/";
    }

    @PostMapping(value = "/addItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String addItem(Model model,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "description") String description,
                          @RequestParam(name = "price") double price,
                          @RequestParam(name = "rating") int rating,
                          @RequestParam(name="count") int count,
                          @RequestParam(name = "small_picture") String smallPicture,
                          @RequestParam(name = "large_picture") String largePicture,
                          @RequestParam(name = "in_top") String inTop,
                          @RequestParam(name = "brand_id") Long brandID) {

        Date date = new Date(Calendar.getInstance().getTime().getTime());
        boolean inTopPage = false;

        if (inTop.equals("yes")) {
            inTopPage = true;
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Item item = new Item();
        if (brandID == -1) {
            item = new Item(null, name, description, price, rating,count, smallPicture, largePicture, date, inTopPage, null, null);
        } else {
            Brand brand = brandService.listBrand(brandID);
            if (brand != null) {
                item = new Item(null, name, description, price, rating, count,smallPicture, largePicture, date, inTopPage, brand, null);
            }
        }
        boolean added = itemService.saveItem(item);

        if (added == true) {
            return "redirect:/adminPanel?added=successfull";
        }
        return "redirect:/adminPanel?added=error";
    }

    @GetMapping(value = "/itemDetails/{item_id}")
    public String taskDetails(Model model, @PathVariable(name = "item_id", required = true) Long id) {

        if ( id!=null && itemService.ItemExists(id)==true) {
            Item item = itemService.listItem(id);
            List<Comment> itemComments=commentService.findAllByItemOOrderByAddedDateAsc(id);
            if(itemComments!=null){
                model.addAttribute("comments",itemComments);
            }

            if(this.itemCountInBasket()>0){
                model.addAttribute("itemAmount",this.itemCountInBasket());
            }

            model.addAttribute("item", item);

            List<Brand> brands = brandService.listAllBrands();
            if (brands != null) {
                model.addAttribute("brands", brands);
            }

            List<Category> categories = categoryService.listAllCategories();
            if (categories != null) {
                model.addAttribute("categoriesJ", categories);
            }

            if(this.getCurrentUser()!=null){
                model.addAttribute("currentUser",this.getCurrentUser());
            }

            List<Picture> pictures=itemService.findPicturesByItemId(id);
            if(pictures!=null){
                model.addAttribute("pictures",pictures);
            }
            return "itemDetails";
        }
        return "403";
    }

    @GetMapping(value = "/sortByBrand/{id}")
    public String sortByBrand(Model model, @PathVariable(name = "id") Long id) {

        if (id != null && brandService.BrandExists(id)==true) {
            Brand brand = brandService.listBrand(id);

            model.addAttribute("brandS", brand);
            List<Item> items = itemService.findItemsBrandId(id);
            if (items != null) {
                model.addAttribute("items", items);
            }
            List<Brand> brands = brandService.listAllBrands();

            if (brands != null) {
                model.addAttribute("brands", brands);
            }

            List<Category> categories = categoryService.listAllCategories();
            if (categories != null) {
                model.addAttribute("categoriesJ", categories);
            }

            if(this.itemCountInBasket()>0){
                model.addAttribute("itemAmount",this.itemCountInBasket());
            }
            return "detailedSearch";
        }
        return "403";
    }

    @PostMapping(value = "/editItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String editItem(Model model,
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "added_date") Date date,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "description") String description,
                           @RequestParam(name = "price") double price,
                           @RequestParam(name = "rating") int rating,
                           @RequestParam(name = "count") int count,
                           @RequestParam(name = "small_picture") String smallPicture,
                           @RequestParam(name = "large_picture") String largePicture,
                           @RequestParam(name = "in_top") String inTop,
                           @RequestParam(name = "brand_id") Long brandID) {

        boolean inTopPage = false;

        if (inTop.equals("yes")) {
            inTopPage = true;
        }
        Item item = new Item();
        if (brandID == -1) {
            item = new Item(id, name, description, price, rating,count, smallPicture, largePicture, date, inTopPage, null, null);
        } else {
            Brand brand = brandService.listBrand(brandID);
            if (brand != null) {
                item = new Item(id, name, description, price, rating,count, smallPicture, largePicture, date, inTopPage, brand, null);
            }
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        boolean edited = itemService.saveItem(item);

        if (edited == true) {
            return "redirect:/itemDetail/" + item.getId();
        }


        return "redirect:/adminPanel?edited=error";
    }

    @PostMapping(value = "/deleteItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deleteItem(Model model, @RequestParam(name = "id") Long id) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (itemService.listItem(id) != null) {
            itemService.deleteItem(id);
            return "redirect:/adminPanel?deleted=successful";
        }
        return "redirect:/adminPanel?deleted=error";
    }

    @GetMapping(value = "/detailedSearch")
    public String toDetailedSearch(Model model, @RequestParam(name = "search", required = false) String search) {

        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Category> categories = categoryService.listAllCategories();
        if (categories != null) {
            model.addAttribute("categoriesJ", categories);
        }

        System.out.println(itemService.listAllItemByName(search));
        model.addAttribute("items", itemService.listAllItemByName(search));

        model.addAttribute("searchRes", search);
        return "detailedSearch";
    }

    @GetMapping(value = "/detSearch")
    public String inDetailedSearch(Model model,
                                   @RequestParam(name = "search",required = false) String search,
                                   @RequestParam(name = "price_from",required = false) String from,
                                   @RequestParam(name = "price_to",required = false) String to,
                                   @RequestParam(name = "options",required = false) String by,
                                   @RequestParam(name = "brand_id",required = false) Long brandID) {

        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Category> categories = categoryService.listAllCategories();
        if (categories != null) {
            model.addAttribute("categoriesJ", categories);
        }


        if (brandID != null) {
            Brand brand = brandService.listBrand(brandID);
            model.addAttribute("brandS", brand);
        }

        if (by!=null && by.equals("asc")) {
            model.addAttribute("items", itemService.sortAsc(search,brandID,from,to));
        }
        else if (by!=null && by.equals("desc")) {
            model.addAttribute("items", itemService.sortDesc(search,brandID,from,to));
        }
        if(search!="") {
            model.addAttribute("searchRes", search);
        }
        else{
            model.addAttribute("searchRes", "");
        }

        return "detailedSearch";
    }

    @GetMapping(value = "/adminPanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String adminPanel(Model model) {

        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Item> items = itemService.listAllItems();
        if (items != null) {
            model.addAttribute("items", items);
        }

        return "adminPanel";
    }

    @GetMapping(value = "/allBrands")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String allBrands(Model model) {

        List<Country> countries = countryService.listAllCountries();
        if (countries != null) {
            model.addAttribute("countries", countries);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }
        return "adminAllBrands";
    }

    @GetMapping(value = "/allCountries")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String allCountries(Model model) {

        List<Country> countries = countryService.listAllCountries();
        if (countries != null) {
            model.addAttribute("countries", countries);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        return "adminAllCountries";
    }

    @GetMapping(value = "/allCategories")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String allCategories(Model model) {

        List<Category> categories = categoryService.listAllCategories();
        if (categories != null) {
            model.addAttribute("categories", categories);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        return "adminAllCategories";
    }

    @PostMapping(value = "/addCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCategory(Model model,
                              @RequestParam(name = "name") String name,
                              @RequestParam(name = "logo") String logoURL) {

        categoryService.saveCategory(new Category(null, name, logoURL));

        return "redirect:/allCategories?added=successfull";
    }

    @PostMapping(value = "/assignCategory")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String assignCategory(Model model,
                                 @RequestParam(name = "category_id") Long categoryID,
                                 @RequestParam(name = "item_id") Long itemID) {
        Category ct = categoryService.listCategory(categoryID);

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (ct != null) {
            Item it = itemService.listItem(itemID);
            if (it != null) {
                List<Category> categories = it.getCategories();
                if (categories == null) {
                    categories = new ArrayList<>();
                }
                int count = 0;
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getId() == categoryID) {
                        count++;
                    }
                }
                if (count == 0) {
                    categories.add(ct);
                    itemService.saveItem(it);
                    return "redirect:/itemDetail/" + itemID;
                }
            }
        }

        return "redirect:/adminPanel?assigned=error";
    }

    @PostMapping(value = "/deleteItemCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteItemCategory(Model model,
                                     @RequestParam(name = "category_id") Long categoryID,
                                     @RequestParam(name = "item_id") Long itemID) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Category ct = categoryService.listCategory(categoryID);

        if (ct != null) {
            Item it = itemService.listItem(itemID);
            if (it != null) {
                int index = it.getCategories().indexOf(ct);
                it.getCategories().remove(index);
                itemService.saveItem(it);

            }
        }
        return "redirect:/itemDetail/" + itemID;
    }

    @GetMapping(value = "/categoryDetail/{category_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String categoryDetails(Model model, @PathVariable(name = "category_id", required = true) Long id) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (id != null && categoryService.CategoryExists(id)==true) {
            Category category = categoryService.listCategory(id);

            model.addAttribute("category", category);
            if (itemService.listAllItems() != null) {
                model.addAttribute("items", itemService.listAllItems());

            }
            return "adminCategoryDetails";
        }
        return "403";
    }

    @GetMapping(value = "/itemDetail/{item_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String itemDetails(Model model, @PathVariable(name = "item_id", required = true) Long id) {

        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (itemService.ItemExists(id)==true && id!=null) {
            Item item = itemService.listItem(id);

            if(itemService.findPicturesByItemId(item.getId())!=null){
                System.out.println(itemService.findPicturesByItemId(item.getId()));
                model.addAttribute("pictures",itemService.findPicturesByItemId(item.getId()));
            }
            if (itemService.listAllItems() != null) {
                model.addAttribute("items", itemService.listAllItems());
            }
            if (item.getCategories() != null) {
                model.addAttribute("itemCategories", item.getCategories());
            }
            model.addAttribute("item", item);

            List<Category> categories = categoryService.listAllCategories();
            if (categories != null) {
                int c = 0;
                List<Category> onlyNotExist = new ArrayList<>();
                for (int i = 0; i < categories.size(); i++) {
                    for (int j = 0; j < item.getCategories().size(); j++) {
                        if (categories.get(i).getId() != item.getCategories().get(j).getId()) {
                            c++;
                        }
                    }
                    if (c == item.getCategories().size()) {
                        onlyNotExist.add(categories.get(i));
                    }
                    c = 0;
                }
                model.addAttribute("categories", onlyNotExist);
            }
            return "adminItemDetails";
        }
        return "403";
    }

    @PostMapping(value = "/addBrand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editItem(Model model,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "country_id") Long countryID) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Brand brand = new Brand();
        if (countryID == -1) {
            brand = new Brand(null, name, null);
        }
        else {
            Country cnt = countryService.listCountry(countryID);
            if (cnt != null) {
                brand = new Brand(null, name, cnt);
            }
        }
        brandService.saveBrand(brand);

        return "redirect:/allBrands?added=successfull";
    }

    @PostMapping(value = "/editBrand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editItem(Model model,
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "country_id") Long countryID) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Brand brand = new Brand();
        if (countryID == -1) {
            brand = new Brand(id, name, null);
        }
        else {
            Country cnt = countryService.listCountry(countryID);
            if (cnt != null) {
                brand = new Brand(id, name, cnt);
            }
        }
        brandService.saveBrand(brand);

        return "redirect:/allBrands?edited=successfull";
    }

    @GetMapping(value = "/brandDetail/{brand_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String brandDetails(Model model, @PathVariable(name = "brand_id", required = true) Long id) {

        List<Country> countries = countryService.listAllCountries();
        if (countries != null) {
            model.addAttribute("countries", countries);
        }

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (id != null && brandService.BrandExists(id)==true) {
            Brand brand = brandService.listBrand(id);
            model.addAttribute("brand", brand);
            List<Item> items = itemService.findItemsBrandId(id);
            if (items != null) {
                model.addAttribute("itemsCount", items.size());
            }
            return "adminBrandDetails";
        }
        return "403";
    }

    @PostMapping(value = "/deleteBrand")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteBrand(Model model, @RequestParam(name = "id") Long id) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        brandService.deleteBrand(id);
        return "redirect:/allBrands?deleted=successful";
    }

    @PostMapping(value = "/addCountry")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCountry(Model model,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "code") String code) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Country cnt = new Country(null, name, code);

        countryService.saveCountry(cnt);

        return "redirect:/allCountries?added=successfull";
    }

    @PostMapping(value = "/editCountry")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editItem(Model model,
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "code") String code) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Country cnt = new Country();
        cnt = new Country(id, name, code);

        countryService.saveCountry(cnt);

        return "redirect:/countryDetail/" + cnt.getId();
    }

    @GetMapping(value = "/countryDetail/{country_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String countryDetails(Model model, @PathVariable(name = "country_id", required = true) Long id) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (id != null && countryService.CountryExists(id)==true) {
            Country country = countryService.listCountry(id);
            model.addAttribute("country", country);
            List<Brand> brands = brandService.findBrandsCountryId(id);
            if (brands != null) {
                model.addAttribute("brandsCount", brands.size());
            }
            return "adminCountryDetails";
        }
        return "403";
    }

    @PostMapping(value = "/deleteCountry")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCountry(Model model, @RequestParam(name = "id") Long id) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        countryService.deleteCountry(id);
        return "redirect:/allCountries?deleted=successful";
    }


    @PostMapping(value = "/editCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editCategory(Model model,
                               @RequestParam(name = "category_id") Long id,
                               @RequestParam(name = "name") String name,
                               @RequestParam(name = "logo") String logo) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (categoryService.listCategory(id) != null) {
            Category ct = categoryService.listCategory(id);
            ct.setName(name);
            ct.setLogoURL(logo);
            categoryService.saveCategory(ct);
            return "redirect:/allCategories?edited=successfull";
        }
        return "redirect:/allCategories?edited=error";
    }

    @PostMapping(value = "/deleteCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteCategory(Model model, @RequestParam(name = "category_id") Long id) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (categoryService.listCategory(id) != null) {
            List<Item> items = itemService.listAllItems();
            int count = 0;

            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getCategories() != null) {
                        List<Category> categories = items.get(i).getCategories();
                        for (int j = 0; j < categories.size(); j++) {
                            if (categories.get(j).getId() == id) {
                                count++;
                            }
                        }
                    }
                }
            }
            if (count == 0) {
                categoryService.deleteCategory(id);
                return "redirect:/allCategories?deleted=successful";
            }
        }
        return "redirect:/allCategories?deleted=error";
    }

    @GetMapping(value = "/403")
    public String errorPage(Model model) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        return "403";
    }

    @GetMapping(value = "/allUsers")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String allUsers(Model model) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Users> users = userService.listAllUsers();
        if (users != null) {
            model.addAttribute("users", users);
        }
        return "adminAllUsers";
    }

    @GetMapping(value = "/allRoles")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String allRoles(Model model) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Role> roles = userService.listAllRoles();
        if (roles != null) {
            model.addAttribute("roles", roles);
        }
        return "adminAllRoles";
    }

    @GetMapping(value = "/roleDetail/{role_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String roleDetails(Model model, @PathVariable(name = "role_id", required = true) Long id) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (id != null && userService.RoleExists(id)==true) {
            Role role = userService.listRole(id);

            model.addAttribute("role", role);
            List<Users> users = userService.findRolesUserId(id);
            if (users != null) {
                model.addAttribute("usersCount", users.size());
            }
            return "adminRoleDetails";
        }
        return "403";
    }

    @PostMapping(value = "/addUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUser(Model model,
                          @RequestParam(name = "name") String name,
                          @RequestParam(name = "email") String email,
                          @RequestParam(name = "password") String password) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Users u = userService.getUserByEmail(email);
        if (u == null) {
            Users user = new Users(null, email, passwordEncoder.encode(password), name,null, null);
            userService.saveUser(user);
            return "redirect:/allUsers?added=successfull";
        }
        return "redirect:/allUsers?added=error";
    }

    @GetMapping(value = "/registration")
    public String registrationPage(Model model) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        List<Category> categories = categoryService.listAllCategories();
        if (categories != null) {
            model.addAttribute("categoriesJ", categories);
        }

        return "signUp";
    }

    @PostMapping(value = "/registration")
    public String registration(Model model,
                               @RequestParam(name = "full_name") String name,
                               @RequestParam(name = "email") String email,
                               @RequestParam(name = "password") String password) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Users u = userService.getUserByEmail(email);
        if (u == null) {

            List<Role> roles = new ArrayList<>();
            roles.add(userService.findRoleByNameF("ROLE_USER"));

            Users user = new Users(null, email, passwordEncoder.encode(password), name, null,roles);

            userService.saveUser(user);

            return "redirect:/login?added=successfull";
        }
        return "redirect:/registration?added=email_error";
    }


    @PostMapping(value = "/addRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addRole(Model model,
                          @RequestParam(name = "name") String name) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Role role = new Role(null, name);
        userService.saveRole(role);

        return "redirect:/allRoles?added=successfull";
    }

    @PostMapping(value = "/editRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editRole(Model model,
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "name") String name) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Role role = userService.listRole(id);
        if (role != null) {
            role.setName(name);
            userService.saveRole(role);
        }
        return "redirect:/roleDetail/" + role.getId();
    }

    @PostMapping(value = "/editUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editUser(Model model,
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "password") String password) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Users u = userService.listUser(id);
        if (u != null) {
            u.setFullName(name);

            if ( password!="" && !passwordEncoder.matches(password, u.getPassword())) {
                u.setPassword(passwordEncoder.encode(password));
            }
            userService.saveUser(u);
        }
        return "redirect:/userDetails/" + u.getId();
    }

    @PostMapping(value = "/deleteRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteRole(Model model, @RequestParam(name = "id") Long id) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        userService.deleteRole(id);
        return "redirect:/allRoles?deleted=successful";
    }

    @PostMapping(value = "/deleteUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(Model model, @RequestParam(name = "id") Long id) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (userService.listUser(id) != null) {
            userService.deleteUser(id);
            return "redirect:/allUsers?deleted=successful";
        }
        return "redirect:/allUsers?deleted=error";

    }

    @GetMapping(value = "/myProfile")
    @PreAuthorize("isAuthenticated()")
    public String myProfile(Model model) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (this.getCurrentUser() != null) {
            model.addAttribute("currentUser", this.getCurrentUser());
        }

        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        List<Category> categories = categoryService.listAllCategories();
        if (categories != null) {
            model.addAttribute("categoriesJ", categories);
        }
        return "userProfile";
    }

    @PostMapping(value = "/updateProfile")
    @PreAuthorize("isAuthenticated()")
    public String updateUserProfile(Model model, @RequestParam(name = "full_name") String name) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (this.getCurrentUser() != null) {
            Users current = this.getCurrentUser();
            current.setFullName(name);
            userService.saveUser(current);
            return "redirect:/myProfile?updated=successful";
        }
        return "redirect:/myProfile?updated=error";
    }

    @PostMapping(value = "/updatePassword")
    @PreAuthorize("isAuthenticated()")
    public String updatePassword(Model model,
                                 @RequestParam(name = "password") String oldPassword,
                                 @RequestParam(name = "new_password") String newPassword,
                                 @RequestParam(name = "re_new_password") String reNewPassword) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (this.getCurrentUser() != null) {
            Users current = this.getCurrentUser();

            if (!passwordEncoder.matches(oldPassword, current.getPassword()) && !oldPassword.equals(newPassword)) {
                return "redirect:/myProfile?change=old_password_error";
            }
            current.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(current);
            return "redirect:/myProfile?change=successful";
        }
        return "redirect:/myProfile?change=error";
    }

    @GetMapping(value = "/userDetails/{user_id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String usersDetails(Model model, @PathVariable(name = "user_id") Long id) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        if (id != null && userService.UserExists(id)==true) {
            Users u = userService.listUser(id);
            List<Role> roles = userService.listAllRoles();

            if (roles != null) {
                roles.removeAll(u.getRoles());
                model.addAttribute("roles", roles);
            }
            model.addAttribute("currentUser",this.getCurrentUser());
            model.addAttribute("user", u);

            return "adminUserDetails";
        }
        return "403";
    }

    @PostMapping(value = "/assignRoles")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String assignRoles(Model model,
                              @RequestParam(name = "role_id") Long roleID,
                              @RequestParam(name = "user_id") Long userID) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Role role = userService.listRole(roleID);

        if (role != null) {
            Users u = userService.listUser(userID);
            if (u != null) {
                List<Role> roles = u.getRoles();
                if (roles == null) {
                    roles = new ArrayList<>();
                }
                int count = 0;
                for (int i = 0; i < roles.size(); i++) {
                    if (roles.get(i).getId() == roleID) {
                        count++;
                    }
                }
                if (count == 0) {
                    roles.add(role);
                    userService.saveUser(u);
                }
            }
        }
        return "redirect:/userDetails/" + userID;
    }

    @PostMapping(value = "/deleteUserRole")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUserRole(Model model,
                                     @RequestParam(name = "role_id") Long roleID,
                                     @RequestParam(name = "user_id") Long userID) {

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        Role role = userService.listRole(roleID);

        if (role != null) {
            Users u = userService.listUser(userID);
            if (u != null) {
                int index = u.getRoles().indexOf(role);
                u.getRoles().remove(index);
                userService.saveUser(u);

            }
        }
        return "redirect:/userDetails/" + userID;
    }

    @PostMapping(value = "/uploadAvatar")
    @PreAuthorize("isAuthenticated()")
    public String uploadAvatar(@RequestParam(name="file") MultipartFile file){



        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") ) {

            try {
                Users u = this.getCurrentUser();

                String pictureName =  DigestUtils.sha1Hex("avatar_"+u.getId()+"_pic");

                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath+pictureName + ".jpg");
                Files.write(path, bytes);
                u.setPictureURL(pictureName);

                userService.saveUser(u);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/myProfile";
    }

    @PostMapping(value = "/deleteAvatar")
    @PreAuthorize("isAuthenticated()")
    public String deleteAvatar(Model model,@RequestParam(name="id") Long id ){

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }
        Users u=this.getCurrentUser();
        if(u.getId()==id){
            u.setPictureURL(null);
            userService.saveUser(u);
            return "redirect:/myProfile?avatar_deleted=success";
        }
        return "redirect:/myProfile?avatar_deleted=error";
    }

    @GetMapping(value="/viewPhoto/{url}",produces={MediaType.IMAGE_JPEG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name="url") String url) throws IOException {


        String picture=viewPath+defaultPicture;
        if(url!=null && !url.equals("null")){
            picture=viewPath+url+".jpg";
        }
        InputStream in;
        try {
            ClassPathResource resource=new ClassPathResource(picture);
            in=resource.getInputStream();
        }
        catch (Exception e){
            ClassPathResource resource=new ClassPathResource(viewPath+defaultPicture);
            in=resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/assignPictures")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String assignPictures(
                              @RequestParam(name="file") MultipartFile file,
                             @RequestParam(name = "item_id") Long itemID){



        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png") ) {
            try {
                if(itemService.listItem(itemID)!=null) {
                    Date date = new Date(Calendar.getInstance().getTime().getTime());

                    itemService.savePicture(new Picture(null,null,date,itemService.listItem(itemID)));

                    List<Picture> itemPic=itemService.findPicturesByItemId(itemID);
                    Picture added=itemPic.get(itemPic.size()-1);

                    String pictureName = DigestUtils.sha1Hex("item_" + itemID + "_pic"+added.getId());

                    added.setUrl(pictureName);
                    itemService.savePicture(added);

                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(itemUploadPath + pictureName + ".jpg");
                    System.out.println(pictureName);
                    Files.write(path, bytes);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/itemDetail/" + itemID;
    }


    @GetMapping(value="/viewItemPicture/{url}",produces={MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] viewItemPicture(@PathVariable(name="url") String url) throws IOException {



        String picture=itemViewPath+itemDefaultPicture;
        if(url!=null && !url.equals("null")){
            picture=itemViewPath+url+".jpg";
        }
        InputStream in;
        try {
            ClassPathResource resource=new ClassPathResource(picture);
            in=resource.getInputStream();
        }
        catch (Exception e){
            ClassPathResource resource=new ClassPathResource(itemViewPath+itemDefaultPicture);
            in=resource.getInputStream();
            e.printStackTrace();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/deletePicture")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String deleteItemPicture(@RequestParam(name="picture_id") Long id ,
                                    @RequestParam(name="item_id" ) Long itemID){



        if(itemService.listPicture(id)!=null){
            itemService.deletePicture(id);
        }
        return "redirect:/itemDetail/" + itemID;
    }

    @PostMapping(value = "/addComment")
    @PreAuthorize("isAuthenticated()")
    public String addCommentToItem(@RequestParam(name="item_id" ) Long itemID,
                                   @RequestParam(name="comment") String text){

        if(this.getCurrentUser()!=null){
            Item item=new Item();
            item=itemService.listItem(itemID);
            if(item!=null){
                Date date = new Date(Calendar.getInstance().getTime().getTime());
                commentService.saveComment(new Comment(null,text,item,date,getCurrentUser()));
                return "redirect:/itemDetails/" + itemID;
            }
        }
        return "403";
    }

    @PostMapping(value = "/deleteComment")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@RequestParam(name="comment_id") Long commentID){

        if(this.getCurrentUser()!=null){
            Item item=new Item();
            if(commentID!=null && commentService.listComment(commentID)!=null) {
                Comment comment=commentService.listComment(commentID);
                item = itemService.listItem(comment.getItem().getId());
                commentService.deleteComment(commentID);
                return "redirect:/itemDetails/" + item.getId();
            }
        }
        return "403";

    }

    @PostMapping(value = "/editComment")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String editComment(@RequestParam(name="comment_id") Long commentID,
                              @RequestParam(name="comment_text") String text){

        if(this.getCurrentUser()!=null){
            if(commentID!=null && commentService.listComment(commentID)!=null) {
                Comment comment=commentService.listComment(commentID);
                comment.setComment(text);
                commentService.saveComment(comment);
                return "redirect:/itemDetails/" + comment.getItem().getId();
            }
        }
        return "403";
    }

    @GetMapping(value="/basket")
    public String basket(Model model,HttpSession session){

        if(this.itemCountInBasket()>0){
            model.addAttribute("itemAmount",this.itemCountInBasket());
        }

        ArrayList<Basket> its= (ArrayList<Basket>) session.getAttribute("basketItems");

        if(its!=null){
             model.addAttribute("basket",its);
             double totalAmount=0;
             for(Basket bas:its){
                 totalAmount+=bas.getCount()*bas.getItem().getPrice();
             }
             model.addAttribute("totalAmount",totalAmount);
        }
        List<Brand> brands = brandService.listAllBrands();
        if (brands != null) {
            model.addAttribute("brands", brands);
        }

        List<Category> categories = categoryService.listAllCategories();
        if (categories != null) {
            model.addAttribute("categoriesJ", categories);
        }
        return "basket";
    }

    @PostMapping(value = "/addCountToItem")
    public String addCountToItemInBasket(HttpSession session,@RequestParam(name="id") Long itemID){

        if(itemID!=null && itemService.ItemExists(itemID)==true){

            Item item=itemService.listItem(itemID);
            ArrayList<Basket> basketItems= (ArrayList<Basket>) session.getAttribute("basketItems");

            for(Basket basket:basketItems){
                int count=basket.getCount()+1;

                if(itemID==basket.getItem().getId() && item.getCount()>basket.getCount() ){
                    basket.setCount(count);

                    return "redirect:/basket";
                }
            }
            session.setAttribute("basketItems",basketItems);

            return "redirect:/basket?out_of_range_error";
        }
        return "redirect:/basket";
    }

    @PostMapping(value = "/makePayment")
    public String makePayment(HttpSession session){

            Date date = new Date(Calendar.getInstance().getTime().getTime());

            ArrayList<Basket> basketItems= (ArrayList<Basket>) session.getAttribute("basketItems");

            for(Basket basket:basketItems){

                Item item=itemService.listItem(basket.getItem().getId());
                int count=item.getCount()-basket.getCount();
                item.setCount(count);
                itemService.saveItem(item);
                itemService.saveSoldItem(new SoldItem(null,basket.getCount(),date,basket.getItem()));
            }
            session.removeAttribute("basketItems");

            return "redirect:/basket";

    }

    @PostMapping(value = "/decreaseCountOfItem")
    public String decreaseCountOfItemFromBasket(HttpSession session,@RequestParam(name="id") Long itemID){

        if(itemID!=null && itemService.ItemExists(itemID)==true){

            Item item=itemService.listItem(itemID);
            ArrayList<Basket> basketItems= (ArrayList<Basket>) session.getAttribute("basketItems");

            for(Basket basket:basketItems){

                if(item.getId()==basket.getItem().getId() ){
                    int count=basket.getCount()-1;

                    if(count>0){
                        basket.setCount(count);
                    }
                    else if(count==0){
                        Basket b=basket;
                        basketItems.remove(b);
                    }
                    break;
                }
            }
            session.setAttribute("basketItems",basketItems);
        }
        return "redirect:/basket";
    }

    @PostMapping(value = "/clearBasket")
    public String clearBasket(HttpSession session){

        session.removeAttribute("basketItems");

        return "redirect:/basket";
    }

    @PostMapping(value="/addToBasket")

    public String addToBasket(HttpSession session,
                              @RequestParam(name="item_id_b") Long id){
        if(id!=null && itemService.listItem(id)!=null){

            ArrayList<Basket> its= (ArrayList<Basket>) session.getAttribute("basketItems");

            if(its==null ){
                its=new ArrayList<>();
            }

            int cnt=0;
            for(Basket bas:its){
                if(bas.getItem().getId()==itemService.listItem(id).getId()){

                    int count=bas.getCount()+1;
                    if(count<=itemService.listItem(id).getCount()) {
                        bas.setCount(count);
                    }
                    break;
                }
                else{
                    cnt++;
                }
            }

            if(cnt==its.size() || its.size()==0){
                its.add(new Basket(itemService.listItem(id),1));
            }

            session.setAttribute("basketItems",its);

            for(Basket iy : its){
                System.out.println("id: "+iy.getItem().getName());
            }
        }
        return "redirect:/itemDetails/"+id;
    }

    @GetMapping(value = "/allSoldItems")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
    public String allSoldItems(Model model) {

        if(itemService.listAllSoldItems()!=null){
            model.addAttribute("soldItems",itemService.listAllSoldItems());
        }
        return "adminAllSoldItems";
    }

    private int itemCountInBasket(){
        ArrayList<Basket> its= (ArrayList<Basket>) sess.getAttribute("basketItems");
        int count=0;
        if(its!=null){
            for(Basket bas:its){
                count+=bas.getCount();
            }
        }
        return count;
    }
    private Users getCurrentUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser=(User)authentication.getPrincipal();
            Users current=userService.getUserByEmail(secUser.getUsername());
            return current;
        }
        return null;
    }
}
