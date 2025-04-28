package net.eventhub.service;

import java.util.List;
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

import net.eventhub.dao.UserDao;
import net.eventhub.domain.User;

@Service("userBusiness")
@Transactional(readOnly = true)
public class UserBusinessObject implements UserBusiness {
    
    @Autowired
    private UserDao userDAO;
    
    
    @Override
	public User getUserByEmail(String email) {
		
		return userDAO.getUserByEmail(email);
	}
    
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
    
    
    
    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }
    
    @Transactional(readOnly = false)
    public Integer createUser(User newUser) {  	
    	 
    	Integer userId = (Integer)userDAO.save(newUser);
    		
        return userId;
    }
    
    @Transactional(readOnly = false)
    public void updateUser(User updatedUser) {       
    	userDAO.saveOrUpdate(updatedUser);            
    }
    
    @Transactional(readOnly = false)
    public void deleteUser(int userId) throws Exception {
    	User deletingUser = getUserById(userId);
        
        userDAO.delete(deletingUser);       
    }
    
    @Transactional(readOnly = false)
    public void deleteUser(User deletingUser) throws Exception {
    	userDAO.delete(deletingUser);       
    }

	@Override
	public List<User> searchUserByKeyword(String keyword) {
		// TODO Auto-generated method stub
		return userDAO.searchUserByKeyword(keyword);
	}



	
}

