package net.eventhub.dao.hibernate;


import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import net.eventhub.dao.UserDao;
import net.eventhub.domain.User;

@Repository("userDAO")
public class UserDAOImpl extends AbstractGenericDao<User> implements UserDao {

	@Override
	public User getUserByEmail(String email) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root).where(builder.equal(root.get("email"), email));
        
        User result = session.createQuery(query).uniqueResult();
        
		return result;
	}

	@Override
	public List<User> searchUserByKeyword(String keyword) {
		Session session = getSession();
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
        
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        
        Predicate clause1 = builder.like(builder.lower(root.get("email")), 
        		"%" + keyword.toLowerCase() + "%");
        
        Predicate clause2 = builder.like(builder.lower(root.get("displayName")), 
        		"%" + keyword.toLowerCase() + "%");
        
        
        query.multiselect(root.get("id"),  root.get("displayName"), root.get("email"))
        		.where(builder.or(clause1, clause2));
        
        List<User> results = session.createQuery(query).getResultList();
        
		return results;
	}
    
    
}
