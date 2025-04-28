package net.eventhub.rest;

import java.util.Collection;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
//import javax.ws.rs.PUT;
//import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import net.eventhub.domain.Category;
import net.eventhub.domain.User;
import net.eventhub.rest.filter.Secured;
import net.eventhub.service.CategoryBusiness;
import net.eventhub.service.EventBusiness;
import net.eventhub.service.UserBusiness;
import net.eventhub.service.UserBusinessObject;
import net.eventhub.utils.NullAwareBeanUtilsBean;

@Component
@Path("/categories")
//@Secured
public class CategoryRestService {
    
    @Autowired
    @Qualifier(value="categoryBusiness")
    private CategoryBusiness categoryBusinessObj;
    
    // Basic CRUD operations for User service
    
    // http://localhost:8080/EventHub/rest/categories/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)   
    public Collection<Category> getAllCategories() {
        return categoryBusinessObj.getAllCategories();
    }
    
 // http://localhost:8080/EventHub/rest/categories/json/get/1
   
    @GET
    @Path("/json/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Category getById(@PathParam("id")int categoryId) {
        return categoryBusinessObj.getCategoryById(categoryId);
    }
    
	public CategoryBusiness getCategoryBusinessObj() {
		return categoryBusinessObj;
	}

	public void setCategoryBusinessObj(CategoryBusiness categoryBusinessObj) {
		this.categoryBusinessObj = categoryBusinessObj;
	}
    
    
}

