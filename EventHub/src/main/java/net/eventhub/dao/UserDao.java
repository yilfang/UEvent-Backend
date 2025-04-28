package net.eventhub.dao;

import java.util.List;

import net.eventhub.domain.User;

public interface UserDao extends GenericDao<User>{
	
	User getUserByEmail(String email);
	
	List<User> searchUserByKeyword(String keyword);

}
