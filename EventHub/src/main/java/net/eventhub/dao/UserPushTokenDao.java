package net.eventhub.dao;

import java.util.Collection;
import java.util.List;

import net.eventhub.domain.UserPushToken;
import net.eventhub.valueobject.UserPushTokenVO;

public interface UserPushTokenDao extends GenericDao<UserPushToken>{
	
	Collection<UserPushTokenVO> getUserPushTokens(Integer userId);
	
	UserPushToken getUserPushTokenByString(String tokenString);
	
	Collection<UserPushTokenVO> getListOfUsersPushTokens(Collection<Integer> userIds);

}
