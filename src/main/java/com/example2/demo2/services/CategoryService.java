package com.example2.demo2.services;

import com.example2.demo2.entities.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    List<Category> listAllCategories();
    Category listCategory(Long id);
    void deleteCategory(Long id);
    void saveCategory(Category category);

    boolean CategoryExists(Long id);

}
