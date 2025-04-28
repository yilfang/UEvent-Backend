package net.eventhub.service;

import java.util.List;

import net.eventhub.domain.User;


public interface UserBusiness {
	
	List<User> searchUserByKeyword(String keyword);
	
	User getUserByEmail(String email);
	
	List<User> getAllUsers();
    
    User getUserById(int userId);
    
    Integer createUser(User newUser) ;
    

    void updateUser(User updatedUser);
    

    void deleteUser(int userId) throws Exception;
    
    
    void deleteUser(User deletingUser) throws Exception ;


}
