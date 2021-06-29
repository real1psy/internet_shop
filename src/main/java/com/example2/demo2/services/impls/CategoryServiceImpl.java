package com.example2.demo2.services.impls;

import com.example2.demo2.entities.Category;
import com.example2.demo2.repositories.CategoryRepository;
import com.example2.demo2.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public boolean CategoryExists(Long id) {
        if(categoryRepository.checkCategory(id)>0){
            return true;
        }
        return false;
    }

    @Override
    public List<Category> listAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category listCategory(Long id) {
        return categoryRepository.getOne(id);
    }

    @Override
    public void deleteCategory(Long id) { categoryRepository.deleteById(id); }
}
