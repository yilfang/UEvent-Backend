package net.eventhub.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.eventhub.dao.CategoryDao;
import net.eventhub.dao.UserDao;
import net.eventhub.domain.Category;
import net.eventhub.domain.User;

@Service("categoryBusiness")
@Transactional(readOnly = true)
public class CategoryBusinessObject implements CategoryBusiness {
    
    @Autowired
    private CategoryDao categoryDAO;
    
       
    public Collection<Category> getAllCategories() {
    	List<Category> categories = categoryDAO.findAll();
    	List<Category> activeCategories = categories.stream().filter( cat -> cat.getActive() == 1 ).collect(Collectors.toList());
        return activeCategories;
    }
    
    public Category getCategoryById(int categoryId) {
        return categoryDAO.findById(categoryId);
    }
    
}

