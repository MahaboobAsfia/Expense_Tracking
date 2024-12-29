package com.expensetracker.service;

import com.expensetracker.exceptions.CategoryExistsException;
import com.expensetracker.exceptions.CategoryNotFoundException;
import com.expensetracker.model.Category;
import com.expensetracker.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public void addNewCategory(Category category) throws CategoryExistsException {
        Optional<Category> _category = categoryRepository.findCategoryByName(category.getName());

        if (_category.isPresent()) {
            log.error("category " + category.getName() + " already exists...");
            throw new CategoryExistsException("Category exists...");
        }
        categoryRepository.save(category);
    }

    public void deleteCategoryByName(String categoryName) throws CategoryNotFoundException {
        Optional<Category> _category = categoryRepository.findCategoryByName(categoryName);

        if (!_category.isPresent()) {
            log.error("category " + categoryName + " does not exist...");
            throw new CategoryNotFoundException("Category does not exist...");
        }
        System.out.println("Deleting " + categoryName);
        categoryRepository.deleteById(_category.get().getId());
        return;
    }

    public void deleteCategoryById(Long categoryId) throws CategoryNotFoundException {
        Optional<Category> _category = categoryRepository.findById(categoryId);

        if (_category.isPresent()) {
            categoryRepository.deleteById(categoryId);
            return;
        }

        log.error("category id " + categoryId + " does not exist...");
        throw new CategoryNotFoundException("Category does not exist...");
    }

    @Transactional
    public ResponseEntity<Category> updateCategory(String oldCategoryName, Category category) throws CategoryNotFoundException {
        Optional<Category> _category = categoryRepository.findCategoryByName(oldCategoryName);

        if (!_category.isPresent()) {
            log.error("category " + oldCategoryName + " does not exist...");
            throw new CategoryNotFoundException("Category does not exist...");
        }

        _category.get().setName(category.getName());
        _category.get().setDescription(category.getDescription());
        Category updatedCategory = categoryRepository.save(_category.get());
        return ResponseEntity.ok(updatedCategory);
    }

    public Category getCategoryById(Long categoryId) throws CategoryNotFoundException {
        Optional<Category> _category = categoryRepository.findById(categoryId);

        if (!_category.isPresent()) {
            log.error("category id " + categoryId + " does not exist...");
            throw new CategoryNotFoundException("Category does not exist...");
        }

        return _category.get();
    }

    public Category getCategoryByName(String categoryName) throws CategoryNotFoundException {
        Optional<Category> _category = categoryRepository.findCategoryByName(categoryName);

        if (!_category.isPresent()) {
            log.error("category " + categoryName + " does not exist...");
            throw new CategoryNotFoundException("Category does not exist...");
        }

        return _category.get();
    }
}
