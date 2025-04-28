package net.eventhub.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.eventhub.dao.UserDao;
import net.eventhub.dao.UserPushTokenDao;
import net.eventhub.domain.UserPushToken;
import net.eventhub.utils.VOEntityConverter;
import net.eventhub.valueobject.UserPushTokenVO;

@Service("userPushTokenBusiness")
@Transactional(readOnly = true)
public class UserPushTokenBusinessImpl implements UserPushTokenBusiness {

	@Autowired
    private UserPushTokenDao userPushTokenDao;
	
	@Override
	@Transactional(readOnly = false)
	public UserPushTokenVO addPushToken(UserPushTokenVO token) {
		UserPushToken existingToken = userPushTokenDao.getUserPushTokenByString(token.getPushToken());
		if ( existingToken == null )
		{
			Integer tokenId = (Integer)userPushTokenDao.save(VOEntityConverter.toUserPushToken(token));
			UserPushToken newToken = userPushTokenDao.findById(tokenId);
			return VOEntityConverter.fromUserPushToken(newToken);
		}
		
		return null;
		
	}

	@Override
	@Transactional(readOnly = false)
	public Boolean deletePushToken(String tokenString) {
		UserPushToken existingToken = userPushTokenDao.getUserPushTokenByString(tokenString);
		
		if ( existingToken != null )
		{
			userPushTokenDao.delete(existingToken);
			
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public UserPushTokenVO updatePushToken(UserPushTokenVO token) {
		UserPushToken existingToken = userPushTokenDao.getUserPushTokenByString(token.getPushToken());
		if ( existingToken != null )
		{
			existingToken.setEnabled(token.getEnabled());
			userPushTokenDao.saveOrUpdate(existingToken);
			
			return VOEntityConverter.fromUserPushToken(existingToken);
		}
		return null;
	}

	@Override
	public Collection<UserPushTokenVO> getUserPushTokens(Integer userId) {
		Collection<UserPushTokenVO> tokens = userPushTokenDao.getUserPushTokens(userId);
		return tokens;
	}

	@Override
	public Collection<UserPushTokenVO> getListOfUsersPushTokens(Collection<Integer> userIds) {
		
		return userPushTokenDao.getListOfUsersPushTokens(userIds);
	}

	public UserPushTokenDao getUserPushTokenDao() {
		return userPushTokenDao;
	}

	public void setUserPushTokenDao(UserPushTokenDao userPushTokenDap) {
		this.userPushTokenDao = userPushTokenDap;
	}
	
	

}
