package net.eventhub.service;

import java.util.Collection;

import net.eventhub.domain.UserPushToken;
import net.eventhub.valueobject.UserPushTokenVO;

public interface UserPushTokenBusiness {
	
	UserPushTokenVO addPushToken(UserPushTokenVO token);
	
	Boolean deletePushToken(String token);
	
	UserPushTokenVO updatePushToken(UserPushTokenVO userPushToken);
	
	Collection<UserPushTokenVO> getUserPushTokens(Integer userId);
	
	Collection<UserPushTokenVO> getListOfUsersPushTokens(Collection<Integer> userIds);

}
