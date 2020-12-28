package com.brizaldi.expensetracker.repositories;

import com.brizaldi.expensetracker.domain.Category;
import com.brizaldi.expensetracker.exceptions.EtBadRequestException;
import com.brizaldi.expensetracker.exceptions.EtResourceNotFoundException;

import java.util.List;

public interface CategoryRepository {

    List<Category> findAll(Integer userId) throws EtResourceNotFoundException;

    Category findById(Integer userId, Integer categoryId) throws EtResourceNotFoundException;

    Integer create(Integer userId, String title, String description) throws EtBadRequestException;

    void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException;

    void removeById(Integer userId, Integer categoryId);
}
