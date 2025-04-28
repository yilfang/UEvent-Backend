package net.eventhub.service;

import java.util.Collection;
import java.util.List;

import net.eventhub.domain.Category;


public interface CategoryBusiness {
	
	Collection<Category> getAllCategories();
    
    Category getCategoryById(int categoryId);

}
