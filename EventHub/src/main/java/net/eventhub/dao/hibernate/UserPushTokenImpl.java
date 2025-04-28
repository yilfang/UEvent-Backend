package net.eventhub.dao.hibernate;


import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import net.eventhub.dao.UserPushTokenDao;
import net.eventhub.domain.EventInvitee;
import net.eventhub.domain.UserPushToken;
import net.eventhub.valueobject.UserPushTokenVO;

@Repository("userPushTokenDAO")
public class UserPushTokenImpl extends AbstractGenericDao<UserPushToken> implements UserPushTokenDao {

	@Override
	public Collection<UserPushTokenVO> getUserPushTokens(Integer userId) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<UserPushToken> query = builder.createQuery(UserPushToken.class);
     // write the Root, Path elements as usual
        Root<UserPushToken> root = query.from(UserPushToken.class);
        query.select(root);  //using metamodel
        
        query.where(builder.equal(root.get("user").get("id"), userId)).distinct(true);
        List<UserPushToken> search = session.createQuery(query).getResultList();
		return DetachedEntityConverter.convertTo(search);
	}

	@Override
	public UserPushToken getUserPushTokenByString(String tokenString) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<UserPushToken> query = builder.createQuery(UserPushToken.class);
     // write the Root, Path elements as usual
        Root<UserPushToken> root = query.from(UserPushToken.class);
        query.select(root);  //using metamodel
        
        query.where(builder.equal(root.get("pushToken"), tokenString)).distinct(true);
        List<UserPushToken> search = session.createQuery(query).getResultList();
        
        UserPushToken token = null;
        if ( search != null && search.size() > 0 )
        {
        	token = search.get(0);
        }
		return token;
	}

	@Override
	public Collection<UserPushTokenVO> getListOfUsersPushTokens(Collection<Integer> userIds) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<UserPushToken> query = builder.createQuery(UserPushToken.class);
     // write the Root, Path elements as usual
        Root<UserPushToken> root = query.from(UserPushToken.class);
        query.select(root);  //using metamodel
        
        query.where(root.get("user").get("id").in(userIds)).distinct(true);
        List<UserPushToken> search = session.createQuery(query).getResultList();
		return DetachedEntityConverter.convertTo(search);
	}

	    
    
}
